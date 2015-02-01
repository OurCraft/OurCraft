package org.craft.modding;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.util.*;

public interface IAddonManager<T extends Annotation>
{

    AddonContainer<T> getAddon(String id);

    Collection<AddonContainer<T>> getAddons();

    void loadAddon(AddonContainer<T> container);

    IAddonHandler<T> getHandler();

    Constructor getAddonConstructor(Class<?> clazz) throws NoSuchMethodException;

    Object[] getConstructorArgs();
}
