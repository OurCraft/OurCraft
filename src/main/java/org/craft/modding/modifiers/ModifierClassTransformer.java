package org.craft.modding.modifiers;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

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
            Log.message("Added modifier " + modifier + " to " + toModifyClass);
            toModify.put(toModifyClass, Type.getInternalName(modifier));
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

                if(!modifierNode.superName.equals(originalNode.superName))
                {
                    Log.fatal(modifierNode.name + " bytecode modifier does not have the same superclass that original class " + originalNode.name + "(found: " + originalNode.superName + " vs. " + modifierNode.superName + ")");
                }
                originalNode.interfaces.addAll(modifierNode.interfaces);

                applyFields(originalReader, modifierReader, originalNode, modifierNode);
                applyMethods(originalReader, modifierReader, originalNode, modifierNode);

                byte[] bytes = writeClass(originalNode);
                ClassReader debugReader = new ClassReader(bytes);
                ClassNode debugNode = new ClassNode();
                debugReader.accept(debugNode, 0);

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
                {
                    methodInsn.owner = original;
                }
            }
            else if(insn instanceof FieldInsnNode)
            {
                FieldInsnNode fieldInsn = (FieldInsnNode) insn;
                if(fieldInsn.owner.equals(modified))
                {
                    fieldInsn.owner = original;
                }
            }
        }
        return method;
    }

}
