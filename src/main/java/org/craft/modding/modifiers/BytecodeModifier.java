package org.craft.modding.modifiers;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BytecodeModifier
{
    Class<?> value();
}
