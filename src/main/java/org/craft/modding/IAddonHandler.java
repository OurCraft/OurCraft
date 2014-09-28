package org.craft.modding;

import java.lang.annotation.*;

public interface IAddonHandler<T extends Annotation>
{

    AddonContainer createContainer(T annot, Object object);
}
