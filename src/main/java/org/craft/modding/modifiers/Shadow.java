package org.craft.modding.modifiers;

import java.lang.annotation.*;

/**
 * Annotation used to represent a field or method of the class to modify.
 * <br/>If the annotated element is a method the implementation does not matter and won't be included in the final class  
 */
@Target(
{
        ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shadow
{
    String prefix() default "shadow$";
}
