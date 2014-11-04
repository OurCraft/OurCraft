package org.craft.utils.asm;

import org.craft.*;

public interface IClassTransformer
{

    byte[] apply(String untransformedName, String transformedName, byte[] classBytes);

    void init(OurClassLoader cl);
}
