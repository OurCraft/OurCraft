package org.craft.modding.events;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;

import org.objectweb.asm.*;
import org.objectweb.asm.Type;
import org.spongepowered.api.event.*;

public class ASMEventListener implements IEventListener, Opcodes
{

    private static int                             IDs               = 0;
    private static final HashMap<Method, Class<?>> cache             = Maps.newHashMap();
    private static final String                    HANDLER_FUNC_DESC = Type.getMethodDescriptor(IEventListener.class.getDeclaredMethods()[0]);

    private final Object                           handler;
    private String                                 readable;

    public ASMEventListener(Object target, Method method) throws Exception
    {
        handler = createWrapper(method).getConstructor(Object.class).newInstance(target);
        readable = "ASM: " + target + " " + method.getName() + Type.getMethodDescriptor(method);
    }

    @Override
    public void invoke(Object event)
    {
        if(handler != null)
        {
            boolean cancelled = false;
            if(event instanceof Cancellable)
            {
                cancelled = ((Cancellable) event).isCancelled();
            }
            boolean cancellable = false;
            if(event instanceof Event)
            {
                cancellable = ((Event) event).isCancellable();
            }
            if(event instanceof ModEvent)
            {
                cancellable = ((ModEvent) event).isCancellable();
            }
            if(!cancellable || !cancelled)
            {
                try
                {
                    handler.getClass().getDeclaredMethod("invoke", Object.class).invoke(handler, event);
                }
                catch(Exception e)
                {
                    ;
                }
            }
        }
    }

    public Class<?> createWrapper(Method callback)
    {
        if(cache.containsKey(callback))
        {
            return cache.get(callback);
        }

        try
        {
            ClassWriter cw = new ClassWriter(0);
            MethodVisitor mv;
            String name = getUniqueName(callback);
            String desc = name.replace('.', '/');
            String instType = Type.getInternalName(callback.getDeclaringClass());
            String eventType = Type.getInternalName(callback.getParameterTypes()[0]);

            cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER, desc, null, "java/lang/Object", new String[]
            {
                    IEventListener.class.getName().replace(".", "/")
            });

            cw.visitSource(".dynamic", null);
            {
                cw.visitField(ACC_PUBLIC, "instance", "Ljava/lang/Object;", null, null).visitEnd();
            }
            {
                mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Object;)V", null, null);
                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitFieldInsn(PUTFIELD, desc, "instance", "Ljava/lang/Object;");
                mv.visitInsn(RETURN);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
            {
                mv = cw.visitMethod(ACC_PUBLIC, "invoke", HANDLER_FUNC_DESC, null, null);
                mv.visitCode();

                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, desc, "instance", "Ljava/lang/Object;");
                mv.visitTypeInsn(CHECKCAST, instType);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitTypeInsn(CHECKCAST, eventType);
                mv.visitMethodInsn(INVOKEVIRTUAL, instType, callback.getName(), Type.getMethodDescriptor(callback), false);
                mv.visitInsn(RETURN);
                mv.visitMaxs(2, 2);

                mv.visitEnd();
            }
            cw.visitEnd();
            byte[] bytes = cw.toByteArray();
            Object o = ClassLoader.getSystemClassLoader().getClass().getMethod("define", String.class, byte[].class, Integer.TYPE, Integer.TYPE).invoke(ClassLoader.getSystemClassLoader(),
                    name, bytes, 0, bytes.length);
            Class<?> ret = (Class<?>) o;
            cache.put(callback, ret);
            return ret;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String getUniqueName(Method callback)
    {
        return String.format("%s_%d_%s_%s_%s", getClass().getName(), IDs++ ,
                callback.getDeclaringClass().getSimpleName(),
                callback.getName(),
                callback.getParameterTypes()[0].getSimpleName());
    }

    public String toString()
    {
        return readable;
    }

}
