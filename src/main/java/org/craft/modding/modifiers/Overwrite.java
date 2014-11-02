package org.craft.modding.modifiers;

import java.lang.annotation.*;

/**
 * Annotation used to represent a field or method of the class to override 
 */
@Target(
{
        ElementType.METHOD, ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Overwrite
{
}
