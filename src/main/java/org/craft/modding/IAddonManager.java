package org.craft.modding;

import java.util.*;

public interface IAddonManager
{

    AddonContainer getAddon(String id);

    Collection<AddonContainer> getAddons();

    void loadAddon(AddonContainer container);

    IAddonHandler getHandler();
}
