package org.craft.modding;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Mod
{
    String id();

    String name();

    String version();

    String author() default "unknown";
}
