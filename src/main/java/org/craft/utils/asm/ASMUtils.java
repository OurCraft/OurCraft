package org.craft.utils.asm;

import java.lang.annotation.*;
import java.util.*;

import com.google.common.collect.*;

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
        return getAnnotation(methodNode, annot) != null;
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
        }
        return false;
    }

    public static AnnotationNode getAnnotation(MethodNode methodNode, Class<? extends Annotation> annot)
    {
        if(methodNode.visibleAnnotations == null)
            return null;
        for(Object o : methodNode.visibleAnnotations)
        {
            AnnotationNode annotNode = (AnnotationNode) o;
            if(annotNode.desc.contains(annot.getCanonicalName().replace(".", "/")))
            {
                return annotNode;
            }
        }
        return null;
    }

    public static HashMap<String, Object> toMap(List<?> values)
    {
        HashMap<String, Object> map = Maps.newHashMap();
        String key = null;
        if(values != null)
            for(int i = 0; i < values.size(); i++ )
            {
                if(i % 2 == 0)
                {
                    key = (String) values.get(i);
                }
                else
                {
                    map.put(key, values.get(i));
                }
            }
        return map;
    }

    public static AnnotationNode getAnnotation(FieldNode fieldNode, Class<? extends Annotation> annot)
    {
        if(fieldNode.visibleAnnotations == null)
            return null;
        for(Object o : fieldNode.visibleAnnotations)
        {
            AnnotationNode annotNode = (AnnotationNode) o;
            if(annotNode.desc.contains(annot.getCanonicalName().replace(".", "/")))
            {
                return annotNode;
            }
        }
        return null;
    }
}
