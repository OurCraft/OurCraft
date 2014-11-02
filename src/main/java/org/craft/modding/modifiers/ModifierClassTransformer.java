package org.craft.modding.modifiers;

import java.util.*;

import com.google.common.collect.*;

import org.craft.utils.asm.*;
import org.objectweb.asm.*;

public class ModifierClassTransformer implements IClassTransformer
{

    private List<String> toModifiers;

    public ModifierClassTransformer()
    {
        toModifiers = Lists.newArrayList();
    }

    public void addClassToModify(Class<?> cls)
    {
        toModifiers.add(Type.getInternalName(cls));
    }

    @Override
    public byte[] apply(String untransformedName, String transformedName, byte[] classBytes)
    {
        //        Log.message(">>> " + transformedName);
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(0);
        reader.accept(cw, 0);
        byte[] bytes = cw.toByteArray();
        return bytes;
    }

}
