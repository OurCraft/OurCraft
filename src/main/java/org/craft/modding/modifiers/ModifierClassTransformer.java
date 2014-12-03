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

    private HashMap<String, String>  toModify;
    private HashMap<String, Boolean> replacesStaticBlock;
    private OurClassLoader           classloader;

    public ModifierClassTransformer()
    {
        toModify = Maps.newHashMap();
        replacesStaticBlock = Maps.newHashMap();
    }

    public void addModifier(Class<?> modifier)
    {
        if(!modifier.isAnnotationPresent(BytecodeModifier.class))
            Log.fatal("Class " + modifier.getName() + " does not have a @BytecodeModifier annotation but was intented to be used as a modifier anyway");
        else
        {
            BytecodeModifier annot = modifier.getAnnotation(BytecodeModifier.class);
            String toModifyClass = annot.value();
            toModify.put(toModifyClass, Type.getInternalName(modifier));
            replacesStaticBlock.put(toModifyClass, annot.replaceStaticBlock());
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
                if(Dev.debug())
                {
                    List<String> methodNames = Lists.newArrayList();
                    ClassReader debugReader = new ClassReader(bytes);
                    ClassNode debugNode = new ClassNode();
                    debugReader.accept(debugNode, 0);

                    List<MethodNode> debugMethods = debugNode.methods;
                    for(MethodNode mNode : debugMethods)
                    {
                        if(methodNames.contains(mNode.name))
                        {
                            Log.error("/!\\ Found potential name conflict: " + mNode.name);
                        }
                        else
                            methodNames.add(mNode.name);
                        Log.message(">> Method at end: " + mNode.access + " " + mNode.desc + " " + mNode.name + " signature: " + mNode.signature);
                    }
                    File classFile = new File(Dev.getFolder(), "classes/original/" + transformedName.replace(".", "/") + ".class");
                    if(!classFile.getParentFile().exists())
                        classFile.getParentFile().mkdirs();
                    classFile.createNewFile();
                    FileOutputStream originalOut = new FileOutputStream(classFile);
                    originalOut.write(classBytes);
                    originalOut.flush();
                    originalOut.close();

                    File modifiedClass = new File(Dev.getFolder(), "classes/modified/" + transformedName.replace(".", "/") + ".class");
                    if(!modifiedClass.getParentFile().exists())
                        modifiedClass.getParentFile().mkdirs();
                    modifiedClass.createNewFile();
                    FileOutputStream modifiedOut = new FileOutputStream(modifiedClass);
                    modifiedOut.write(bytes);
                    modifiedOut.flush();
                    modifiedOut.close();
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
                        if(Dev.debug())
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
        List<MethodNode> newMethodsNodes = modifierNode.methods;
        for(MethodNode newMethod : newMethodsNodes)
        {
            if(!ASMUtils.hasAnnotation(newMethod, Shadow.class) || (newMethod.name.equals("<init>") && ASMUtils.hasAnnotation(newMethod, Overwrite.class)))
            {
                String name = originalNode.name.replace("/", ".");
                boolean replaceStaticBlock = replacesStaticBlock.get(name);
                boolean shouldReplace = (newMethod.name.equals("<clinit>") && replaceStaticBlock);
                if(ASMUtils.hasAnnotation(newMethod, Overwrite.class) || shouldReplace)
                {
                    List<MethodNode> originalMethods = originalNode.methods;
                    List<MethodNode> toRemove = Lists.newArrayList();
                    for(MethodNode m : originalMethods)
                    {
                        if(m.name.equals(newMethod.name) && m.desc.equals(newMethod.desc) && m.access == newMethod.access)
                        {
                            toRemove.add(m);
                            if(!shouldReplace)
                            {
                                originalMethods.removeAll(toRemove);
                                toRemove.clear();
                                break;
                            }
                        }
                    }
                    originalMethods.removeAll(toRemove);
                }
                if(newMethod.name.equals("<clinit>") && !replaceStaticBlock)
                {
                    appendInsns(originalNode, null, newMethod);
                    break;
                }
                else if(newMethod.name.equals("<clinit>") && shouldReplace)
                {
                    originalNode.methods.add(convertMethod(newMethod, originalNode.name, modifierNode.name));
                    break;
                }
                else
                    originalNode.methods.add(convertMethod(newMethod, originalNode.name, modifierNode.name));
            }
            else if(ASMUtils.hasAnnotation(newMethod, Shadow.class))
            {
                boolean found = false;
                AnnotationNode shadowAnnot = ASMUtils.getAnnotation(newMethod, Shadow.class);
                List<MethodNode> originalMethods = originalNode.methods;
                String prefix = (String) ASMUtils.toMap(shadowAnnot.values).get("prefix");
                if(prefix == null)
                    prefix = "shadow$";
                for(MethodNode originalMethod : originalMethods)
                {
                    String mName = originalMethod.name;
                    if(newMethod.name.startsWith(prefix))
                    {
                        newMethod.name = newMethod.name.substring(prefix.length(), newMethod.name.length());
                        found = true;
                        if(Dev.debug())
                            Log.message(">>> REPLACED NAME " + mName + " TO " + originalMethod.name);
                        originalNode.methods.remove(originalMethod);
                        originalNode.methods.add(convertMethod(newMethod, originalNode.name, modifierNode.name));
                        break;
                    }
                    if(newMethod.name.equals(mName) && originalMethod.desc.equals(newMethod.desc))
                    {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    Log.fatal("From modifier " + modifierNode.name + ": Tried to use method " + newMethod.name + " of type " + newMethod.desc + " as @Shadow while original class (" + originalNode.name + ") does not have this method.");
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
                if(methodInsn.owner.equals(modified))
                    methodInsn.owner = original;

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
                                if(Dev.debug())
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
                            if(Dev.debug())
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
                    if(Dev.debug())
                        Log.message(">>> PREFIX: " + prefix);
                    if(insn.name.equals(prefix + method.name))
                        return method;
                }
                return method;
            }
        }
        return null;
    }

    /**
     * From Sponge's MixinTransformer
     */
    private void appendInsns(ClassNode targetClass, String targetMethodName, MethodNode sourceMethod)
    {
        if(Type.getReturnType(sourceMethod.desc) != Type.VOID_TYPE)
        {
            throw new IllegalArgumentException("Attempted to merge insns into a method which does not return void");
        }

        if(targetMethodName == null || targetMethodName.length() == 0)
        {
            targetMethodName = sourceMethod.name;
        }

        List<MethodNode> methods = targetClass.methods;
        for(MethodNode method : methods)
        {
            if((targetMethodName.equals(method.name)) && sourceMethod.desc.equals(method.desc))
            {
                AbstractInsnNode returnNode = null;
                Iterator<AbstractInsnNode> findReturnIter = method.instructions.iterator();
                while(findReturnIter.hasNext())
                {
                    AbstractInsnNode insn = findReturnIter.next();
                    if(insn.getOpcode() == Opcodes.RETURN)
                    {
                        returnNode = insn;
                        break;
                    }
                }

                Iterator<AbstractInsnNode> injectIter = sourceMethod.instructions.iterator();
                while(injectIter.hasNext())
                {
                    AbstractInsnNode insn = injectIter.next();
                    if(!(insn instanceof LineNumberNode) && insn.getOpcode() != Opcodes.RETURN)
                    {
                        method.instructions.insertBefore(returnNode, insn);
                    }
                }
            }
        }
    }

    @Override
    public void init(OurClassLoader cl)
    {
        this.classloader = cl;
    }

}
