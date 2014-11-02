package org.craft.utils.asm;

import java.lang.annotation.*;

import org.objectweb.asm.tree.*;

public final class ASMUtils
{
    private ASMUtils()
    {

    }

    public static boolean hasFlag(int access, int flag)
    {
        return (access & flag) == flag;
    }

    public static boolean hasAnnotation(MethodNode methodNode, Class<? extends Annotation> annot)
    {
        if(methodNode.visibleAnnotations == null)
            return false;
        for(Object o : methodNode.visibleAnnotations)
        {
            AnnotationNode annotNode = (AnnotationNode) o;
            if(annotNode.desc.contains(annot.getCanonicalName().replace(".", "/")))
            {
                return true;
            }
            else
            {
            }
        }
        return false;
    }

    public static boolean hasAnnotation(FieldNode fieldNode, Class<? extends Annotation> annot)
    {
        if(fieldNode.visibleAnnotations == null)
            return false;
        for(Object o : fieldNode.visibleAnnotations)
        {
            AnnotationNode annotNode = (AnnotationNode) o;
            if(annotNode.desc.contains(annot.getCanonicalName().replace(".", "/")))
            {
                return true;
            }
            else
            {
            }
        }
        return false;
    }

    public static boolean hasAnnotation(ClassNode classNode, Class<? extends Annotation> annot)
    {
        if(classNode.visibleAnnotations == null)
            return false;
        for(Object o : classNode.visibleAnnotations)
        {
            AnnotationNode annotNode = (AnnotationNode) o;
            if(annotNode.desc.contains(annot.getCanonicalName().replace(".", "/")))
            {
                return true;
            }
            else
            {
            }
        }
        return false;
    }
}
