package org.craft.modding;

import java.io.*;
import java.lang.annotation.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.craft.spongeimpl.events.*;
import org.craft.spongeimpl.events.state.*;
import org.craft.spongeimpl.plugin.*;
import org.craft.utils.*;
import org.spongepowered.api.*;

public class AddonsLoader
{

    private File                                                   addonsFolder;
    private HashMap<Class<? extends Annotation>, IAddonManager<?>> handlers;
    private EventBus                                               eventBus;
    private Game                                                   game;

    public AddonsLoader(Game gameInstance, File addonsFolder, EventBus eventBus)
    {
        this.game = gameInstance;
        this.eventBus = eventBus;
        this.addonsFolder = addonsFolder;
        handlers = new HashMap<Class<? extends Annotation>, IAddonManager<?>>();
        registerAddonAnnotation(Mod.class, new ModManager());
    }

    @SuppressWarnings("rawtypes")
    public void registerAddonAnnotation(Class<? extends Annotation> annot, IAddonManager handler)
    {
        handlers.put(annot, handler);
    }

    @SuppressWarnings(
    {
            "rawtypes", "unchecked"
    })
    public void loadAddon(Class<?> clazz) throws InstantiationException, IllegalAccessException
    {
        Annotation[] annots = clazz.getAnnotations();
        boolean added = false;
        for(Annotation annot : annots)
        {
            Log.message("Checking " + annot.annotationType());
            if(handlers.containsKey(annot.annotationType()))
            {
                IAddonManager manager = handlers.get(annot.annotationType());
                IAddonHandler handler = manager.getHandler();
                Object instance = clazz.newInstance();
                AddonContainer container = handler.createContainer(annot, instance);
                manager.loadAddon(container);
                eventBus.register(instance);

                File configFolder = new File(SystemUtils.getGameFolder(), "configs/");
                if(!configFolder.exists())
                {
                    configFolder.mkdirs();
                }
                Logger logger = new AddonLogger(container);
                SpongePreInitEvent preInitEvent = new SpongePreInitEvent(game, logger, new File(configFolder, container.getId() + ".cfg"), configFolder, configFolder);
                eventBus.fireEvent(preInitEvent, instance);
                added = true;
            }
        }
        if(!added)
        {
            Log.error("Tried to register addon " + clazz.getCanonicalName() + " but it is not supported");
        }
    }

    public File getAddonsFolder()
    {
        return addonsFolder;
    }
}
