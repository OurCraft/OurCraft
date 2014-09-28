package org.craft.modding;

import java.lang.annotation.*;
import java.util.*;

public interface IAddonManager<T extends Annotation>
{

    AddonContainer getAddon(String id);

    Collection<AddonContainer> getAddons();

    void loadAddon(AddonContainer container);

    IAddonHandler<T> getHandler();
}
