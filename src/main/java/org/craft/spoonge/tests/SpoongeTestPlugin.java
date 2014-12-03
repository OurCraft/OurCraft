package org.craft.spoonge.tests;

import com.google.common.base.*;

import org.craft.spoonge.util.text.*;
import org.slf4j.*;
import org.spongepowered.api.event.state.*;
import org.spongepowered.api.event.world.*;
import org.spongepowered.api.plugin.*;
import org.spongepowered.api.text.format.*;
import org.spongepowered.api.text.message.*;
import org.spongepowered.api.text.title.*;
import org.spongepowered.api.util.event.*;

@Plugin(id = "plugtest", name = "Test plugin", version = "1.0")
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
        logger = evt.getPluginLog();
        evt.getPluginLog().debug("SpongePreInit!!!");
    }

    @Subscribe
    public void onPostInit(PostInitializationEvent evt)
    {
        logger.debug("SpongePostInit!!!");
        logger.debug("Dirt localized name is " + evt.getGame().getRegistry().getBlock("dirt").get().getTranslation().get());
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent evt)
    {
        logger.debug("SpongeLoading world " + evt.getWorld().getName());
        Optional<TitleBuilder> service = evt.getGame().getServiceManager().provide(TitleBuilder.class);
        if(service.isPresent())
        {
            TitleBuilder builder = service.get();
            builder.stay(2000);
            builder.fadeIn(5000);
            builder.fadeOut(5000);
            //            builder.title(Messages.of("Main title from a Sponge plugin"));
            //            builder.subtitle(Messages.of("Subtitle from a Sponge plugin"));
            MessageBuilder<String> messageBuilder = new SpoongeMessageBuilder<String>();
            builder.title(messageBuilder.content("Main title from a Sponge plugin").build());
            builder.subtitle(messageBuilder.content("Subtitle from a Sponge plugin").build());
            builder.build();
        }
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
        logger.info("TextStyles's OBFUSCATED Name: " + TextStyles.OBFUSCATED.getName());
        logger.info("TextStyles's RESET Name: " + TextStyles.RESET.getName());
        logger.info("TextStyles's ITALIC Name: " + TextStyles.ITALIC.getName());
        logger.info("TextStyles's STRIKETHROUGH Name: " + TextStyles.STRIKETHROUGH.getName());
        logger.info("TextStyles's UNDERLINED Name: " + TextStyles.UNDERLINE.getName());
        logger.info("TextStyles's BOLD Name: " + TextStyles.BOLD.getName());
    }
}
