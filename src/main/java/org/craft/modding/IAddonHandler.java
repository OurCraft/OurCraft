package org.craft.modding;

import java.lang.annotation.*;

import org.craft.*;

public interface IAddonHandler
{

    AddonContainer createContainer(Annotation annot, Object object);

    void onCreation(OurCraftInstance instance, AddonContainer container);
}
