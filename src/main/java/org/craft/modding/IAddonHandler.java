package org.craft.modding;

import java.lang.annotation.*;

import org.craft.*;

public interface IAddonHandler<T extends Annotation>
{

    AddonContainer<T> createContainer(T annot, Object object);

    void onCreation(OurCraftInstance instance, AddonContainer<T> container);
}
