package org.craft.spoonge.tests;

import org.slf4j.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.item.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.message.*;
import org.spongepowered.api.text.title.*;
import org.spongepowered.api.util.event.*;

@Plugin(id = "plugtest", name = "Test Plugin", version = "1.0")
public class SpoongeTestPlugin
{

    private Logger logger;

    public SpoongeTestPlugin()
    {
        ;
    }

    @Subscribe
    public void onPreInit(PreInitializationEvent evt)
    {
        logger = LoggerFactory.getLogger("Test Plugin");
        logger.debug("SpongePreInit!!!");
    }

    @Subscribe
    public void onPostInit(PostInitializationEvent evt)
    {
        logger.debug("SpongePostInit!!!");
        logger.debug("Dirt localized name is " + evt.getGame().getRegistry().getBlock("dirt").get().getTranslation().get());
        for(BlockType block : evt.getGame().getRegistry().getBlocks())
        {
            logger.debug("Found block: " + block.getId() + ", translation is: " + block.getTranslation().get());
        }
        for(ItemType block : evt.getGame().getRegistry().getItems())
        {
            logger.debug("Found item: " + block.getId() + ", translation is: " + block.getTranslation().get());
        }
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent evt)
    {
        logger.debug("SpongeLoading world " + evt.getWorld().getName());
        TitleBuilder builder = Titles.update();
        builder.stay(2000);
        builder.fadeIn(5000);
        builder.fadeOut(5000);
        builder.title(Messages.of("Main title from a Sponge plugin"));
        builder.subtitle(Messages.of("Subtitle from a Sponge plugin"));
        builder.build();
    }

    @Subscribe
    public void onWorldUnload(WorldUnloadEvent evt)
    {
        logger.debug("SpongeUnloading world " + evt.getWorld().getName());
    }

    @Subscribe
    public void onInit(InitializationEvent evt)
    {
        logger.debug("SpongeInit!!!");
        logger.debug("TextStyles's OBFUSCATED Name: " + TextStyles.OBFUSCATED.getName());
        logger.debug("TextStyles's RESET Name: " + TextStyles.RESET.getName());
        logger.debug("TextStyles's ITALIC Name: " + TextStyles.ITALIC.getName());
        logger.debug("TextStyles's STRIKETHROUGH Name: " + TextStyles.STRIKETHROUGH.getName());
        logger.debug("TextStyles's UNDERLINED Name: " + TextStyles.UNDERLINE.getName());
        logger.debug("TextStyles's BOLD Name: " + TextStyles.BOLD.getName());
    }

}
