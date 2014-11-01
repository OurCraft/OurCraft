package org.craft.modding.events;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.*;
import org.craft.modding.*;
import org.objectweb.asm.*;
import org.objectweb.asm.Type;
import org.spongepowered.api.event.*;

public class ASMEventListener implements IEventListener, Opcodes
{

    private static int                             IDs               = 0;
    private static final String                    HANDLER_DESC      = Type.getInternalName(IEventListener.class);
    private static final String                    HANDLER_FUNC_DESC = Type.getMethodDescriptor(IEventListener.class.getDeclaredMethods()[0]);
    private static final ASMClassLoader            LOADER            = new ASMClassLoader();
    private static final HashMap<Method, Class<?>> cache             = Maps.newHashMap();
    private static final boolean                   GETCONTEXT        = Boolean.parseBoolean(System.getProperty("fml.LogContext", "false"));

    private final IEventListener                   handler;
    private ModContainer                           owner;
    private String                                 readable;

    public ASMEventListener(Object target, Method method) throws Exception
    {
        handler = (IEventListener) createWrapper(method).getConstructor(Object.class).newInstance(target);
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
                    handler.invoke(event);
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

        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        String name = getUniqueName(callback);
        String desc = name.replace('.', '/');
        String instType = Type.getInternalName(callback.getDeclaringClass());
        String eventType = Type.getInternalName(callback.getParameterTypes()[0]);

        /*
        System.out.println("Name:     " + name);
        System.out.println("Desc:     " + desc);
        System.out.println("InstType: " + instType);
        System.out.println("Callback: " + callback.getName() + Type.getMethodDescriptor(callback));
        System.out.println("Event:    " + eventType);
        */

        cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER, desc, null, "java/lang/Object", new String[]
        {
                Type.getInternalName(IEventListener.class)
        });

        cw.visitSource(".dynamic", null);
        {
            cw.visitField(ACC_PUBLIC, "instance", "Ljava/lang/Object;", null, null).visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
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
            mv.visitMethodInsn(INVOKEVIRTUAL, instType, callback.getName(), Type.getMethodDescriptor(callback));
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();
        Class<?> ret = OurClassLoader.instance.define(name, bytes, 0, bytes.length);
        cache.put(callback, ret);
        return ret;
    }

    private String getUniqueName(Method callback)
    {
        return String.format("%s_%d_%s_%s_%s", getClass().getName(), IDs++ ,
                callback.getDeclaringClass().getSimpleName(),
                callback.getName(),
                callback.getParameterTypes()[0].getSimpleName());
    }

    private static class ASMClassLoader extends ClassLoader
    {
        private ASMClassLoader()
        {
            super(ASMClassLoader.class.getClassLoader());
        }

        public Class<?> define(String name, byte[] data)
        {
            return defineClass(name, data, 0, data.length);
        }
    }

    public String toString()
    {
        return readable;
    }

}
