package org.craft.modding;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod
{
    String id();

    String name();

    String version();

    String author() default "unknown";
}
