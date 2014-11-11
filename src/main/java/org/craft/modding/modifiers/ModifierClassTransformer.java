package org.craft.modding.modifiers;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.*;
import org.craft.utils.*;
import org.craft.utils.asm.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/**
 * Class transformer used to modify bytecode at runtime. Inspired to work like <code>@Mixin</code> from Sponge:
 * <br/><a href="https://github.com/SpongePowered/Sponge/blob/master/src/main/java/org/spongepowered/mod/asm/transformers/MixinTransformer.java">MixinTransformer.java from Sponge's code</a>
 */
public class ModifierClassTransformer implements IClassTransformer, Opcodes
{

    private HashMap<String, String> toModify;
    private OurClassLoader          classloader;

    public static boolean           debug;

    public ModifierClassTransformer()
    {
        toModify = Maps.newHashMap();
    }

    public void addModifier(Class<?> modifier)
    {
        if(!modifier.isAnnotationPresent(BytecodeModifier.class))
            Log.fatal("Class " + modifier.getName() + " does not have a @BytecodeModifier annotation but was intented to be used as a modifier anyway");
        else
        {
            String toModifyClass = modifier.getAnnotation(BytecodeModifier.class).value();
            toModify.put(toModifyClass, Type.getInternalName(modifier));
            try
            {
                classloader.findClass(toModifyClass);
            }
            catch(ClassNotFoundException e)
            {
                ;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] apply(String untransformedName, String transformedName, byte[] classBytes)
    {
        if(toModify.containsKey(transformedName))
        {
            try
            {
                ClassReader originalReader = new ClassReader(classBytes);
                ClassReader modifierReader = new ClassReader(toModify.get(transformedName));
                ClassNode modifierNode = new ClassNode();
                ClassNode originalNode = new ClassNode();
                modifierReader.accept(modifierNode, 0);
                originalReader.accept(originalNode, 0);
                Log.message("Started to modify " + originalNode.name + " using " + modifierNode.name);

                if(!modifierNode.superName.equals(originalNode.superName))
                {
                    Log.fatal(modifierNode.name + " bytecode modifier does not have the same superclass that original class " + originalNode.name + "(found: " + originalNode.superName + " vs. " + modifierNode.superName + ")");
                }
                originalNode.interfaces.addAll(modifierNode.interfaces);

                applyFields(originalReader, modifierReader, originalNode, modifierNode);
                applyMethods(originalReader, modifierReader, originalNode, modifierNode);

                byte[] bytes = writeClass(originalNode);
                if(debug)
                {
                    ClassReader debugReader = new ClassReader(bytes);
                    ClassNode debugNode = new ClassNode();
                    debugReader.accept(debugNode, 0);

                    List<MethodNode> debugMethods = debugNode.methods;
                    for(MethodNode mNode : debugMethods)
                    {
                        Log.message(">> Method at end: " + mNode.access + " " + mNode.desc + " " + mNode.name + " signature: " + mNode.signature);
                    }
                }

                Log.message("Succesfully modified " + originalNode.name + " using " + modifierNode.name);
                return bytes;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return classBytes;
    }

    @SuppressWarnings("unchecked")
    private void applyFields(ClassReader originalReader, ClassReader modifierReader, ClassNode originalNode, ClassNode modifierNode)
    {
        List<FieldNode> fieldNodes = modifierNode.fields;
        for(FieldNode node : fieldNodes)
        {
            if(!ASMUtils.hasAnnotation(node, Shadow.class))
            {
                if(ASMUtils.hasAnnotation(node, Overwrite.class))
                {
                    List<FieldNode> originalFields = originalNode.fields;
                    for(FieldNode fieldNode : originalFields)
                    {
                        if(fieldNode.name.equals(node.name) && fieldNode.desc.equals(node.desc) && fieldNode.access == node.access)
                        {
                            originalNode.fields.remove(fieldNode);
                            break;
                        }
                    }
                }
                originalNode.fields.add(node);
            }
            else
            {
                boolean found = false;
                List<FieldNode> originalFields = originalNode.fields;
                AnnotationNode shadowAnnot = ASMUtils.getAnnotation(node, Shadow.class);
                String prefix = (String) ASMUtils.toMap(shadowAnnot.values).get("prefix");
                if(prefix == null)
                    prefix = "shadow$";
                for(FieldNode originalField : originalFields)
                {
                    String mName = originalField.name;
                    if(node.name.startsWith(prefix))
                    {
                        node.name = node.name.substring(prefix.length(), node.name.length());
                        found = true;
                        if(debug)
                            Log.message(">>> REPLACED NAME " + mName + " TO " + originalField.name);
                        break;
                    }
                    if(node.name.equals(mName) && originalField.desc.equals(node.desc) && originalField.access == node.access)
                    {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    Log.fatal("From modifier " + modifierNode.name + ": Tried to use field " + node.name + " of type " + node.desc + " as @Shadow while original class (" + originalNode.name + ") does not have this field.");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void applyMethods(ClassReader originalReader, ClassReader modifierReader, ClassNode originalNode, ClassNode modifierNode)
    {
        List<MethodNode> methodNodes = modifierNode.methods;
        for(MethodNode node : methodNodes)
        {
            if(!ASMUtils.hasAnnotation(node, Shadow.class) || (node.name.equals("<init>") && ASMUtils.hasAnnotation(node, Overwrite.class)))
            {
                if(ASMUtils.hasAnnotation(node, Overwrite.class))
                {
                    List<MethodNode> originalMethods = originalNode.methods;
                    for(MethodNode m : originalMethods)
                    {
                        if(m.name.equals(node.name) && m.desc.equals(node.desc) && m.access == node.access)
                        {
                            originalNode.methods.remove(m);
                            break;
                        }
                    }
                }
                originalNode.methods.add(convertMethod(node, originalNode.name, modifierNode.name));
            }
            else if(ASMUtils.hasAnnotation(node, Shadow.class))
            {
                boolean found = false;
                AnnotationNode shadowAnnot = ASMUtils.getAnnotation(node, Shadow.class);
                List<MethodNode> originalMethods = originalNode.methods;
                String prefix = (String) ASMUtils.toMap(shadowAnnot.values).get("prefix");
                if(prefix == null)
                    prefix = "shadow$";
                for(MethodNode originalMethod : originalMethods)
                {
                    String mName = originalMethod.name;
                    if(node.name.startsWith(prefix))
                    {
                        node.name = node.name.substring(prefix.length(), node.name.length());
                        found = true;
                        if(debug)
                            Log.message(">>> REPLACED NAME " + mName + " TO " + originalMethod.name);
                        originalNode.methods.remove(originalMethod);
                        originalNode.methods.add(convertMethod(node, originalNode.name, modifierNode.name));
                        break;
                    }
                    if(node.name.equals(mName) && originalMethod.desc.equals(node.desc))
                    {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    Log.fatal("From modifier " + modifierNode.name + ": Tried to use method " + node.name + " of type " + node.desc + " as @Shadow while original class (" + originalNode.name + ") does not have this method.");
                }
            }
        }
    }

    protected final byte[] writeClass(ClassNode classNode)
    {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private MethodNode convertMethod(MethodNode method, String original, String modified)
    {
        Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while(iter.hasNext())
        {
            AbstractInsnNode insn = iter.next();

            if(insn instanceof MethodInsnNode)
            {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;

                MethodNode node;
                try
                {
                    node = getNodeFromInsn(methodInsn);
                    if(node != null)
                    {
                        AnnotationNode shadowAnnot = ASMUtils.getAnnotation(node, Shadow.class);
                        if(shadowAnnot != null)
                        {
                            String prefix = (String) ASMUtils.toMap(shadowAnnot.values).get("prefix");
                            if(prefix == null)
                                prefix = "shadow$";
                            if(methodInsn.name.startsWith(prefix))
                            {
                                String oldName = methodInsn.name;
                                methodInsn.name = methodInsn.name.substring(prefix.length(), methodInsn.name.length());
                                if(debug)
                                    Log.message(">>>>> CHANGED " + oldName + " to " + methodInsn.name);
                            }
                        }
                    }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                if(methodInsn.owner.equals(modified))
                {
                    methodInsn.owner = original;
                }
            }
            else if(insn instanceof FieldInsnNode)
            {
                FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                try
                {
                    FieldNode node = getNodeFromInsn(fieldInsn);
                    AnnotationNode shadowAnnot = ASMUtils.getAnnotation(node, Shadow.class);
                    if(shadowAnnot != null)
                    {
                        String prefix = (String) ASMUtils.toMap(shadowAnnot.values).get("prefix");
                        if(prefix == null)
                            prefix = "shadow$";
                        if(fieldInsn.name.startsWith(prefix))
                        {
                            String oldName = fieldInsn.name;
                            fieldInsn.name = fieldInsn.name.substring(prefix.length(), fieldInsn.name.length());
                            if(debug)
                                Log.message(">>>>> CHANGED " + oldName + " to " + fieldInsn.name);
                        }
                    }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                if(fieldInsn.owner.equals(modified))
                {
                    fieldInsn.owner = original;
                }
            }
        }
        return method;
    }

    @SuppressWarnings("unchecked")
    private FieldNode getNodeFromInsn(FieldInsnNode insn) throws IOException
    {
        ClassReader reader = new ClassReader(insn.owner);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        List<FieldNode> fields = node.fields;
        for(FieldNode field : fields)
        {
            if(field.name.equals(insn.name) && field.desc.equals(insn.desc))
                return field;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private MethodNode getNodeFromInsn(MethodInsnNode insn) throws IOException
    {
        ClassReader reader = new ClassReader(insn.owner);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        List<MethodNode> methods = node.methods;
        for(MethodNode method : methods)
        {
            if(method.name.equals(insn.name) && method.desc.equals(insn.desc))
            {
                AnnotationNode shadowNode = ASMUtils.getAnnotation(method, Shadow.class);
                if(shadowNode != null)
                {
                    String prefix = (String) ASMUtils.toMap(shadowNode.values).get("prefix");
                    if(prefix == null)
                        prefix = "shadow$";
                    if(debug)
                        Log.message(">>> PREFIX: " + prefix);
                    if(insn.name.equals(prefix + method.name))
                        return method;
                }
                return method;
            }
        }
        return null;
    }

    @Override
    public void init(OurClassLoader cl)
    {
        this.classloader = cl;
    }

}
