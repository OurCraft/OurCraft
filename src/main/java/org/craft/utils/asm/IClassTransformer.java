package org.craft.utils.asm;

public interface IClassTransformer
{

    byte[] apply(String untransformedName, String transformedName, byte[] classBytes);

}
