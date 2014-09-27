package org.craft.client.gui;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.entity.*;
import org.craft.resources.*;
import org.craft.world.*;
import org.craft.world.populators.*;

public class GuiMainMenu extends Gui
{

    private ResourceLocation logoTexture = new ResourceLocation("ourcraft", "textures/logo.png");

    public GuiMainMenu(FontRenderer font)
    {
        super(font);
    }

    @Override
    public void init()
    {
        addWidget(new GuiButton(0, OurCraft.getOurCraft().getDisplayWidth() / 2 - 150, OurCraft.getOurCraft().getDisplayHeight() / 2, 300, 40, I18n.format("main.play.singleplayer"), getFontRenderer()));
        addWidget(new GuiButton(1, OurCraft.getOurCraft().getDisplayWidth() / 2 - 150, OurCraft.getOurCraft().getDisplayHeight() / 2 + 60, 300, 40, I18n.format("main.quit"), getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 0)
        {
            WorldGenerator generator = new WorldGenerator();
            generator.addPopulator(new RockPopulator());
            generator.addPopulator(new GrassPopulator());
            generator.addPopulator(new TreePopulator());
            generator.addPopulator(new FlowerPopulator());
            World clientWorld = new World(new BaseChunkProvider(), generator);

            EntityPlayer player = new EntityPlayer(clientWorld);
            player.setLocation(0, 160 + 17, 0);
            clientWorld.spawn(player);
            OurCraft.getOurCraft().getRenderEngine().setRenderViewEntity(player);

            new ThreadGetChunksFromCamera(OurCraft.getOurCraft()).start();
            OurCraft.getOurCraft().setWorld(clientWorld);
            OurCraft.getOurCraft().setPlayer(player);
            OurCraft.getOurCraft().openMenu(new GuiIngame(getFontRenderer()));
        }
        else if(widget.getID() == 1)
        {
            OurCraft.getOurCraft().shutdown();
        }
    }

    @Override
    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        renderEngine.bindLocation(logoTexture);
        drawTexturedRect(renderEngine, OurCraft.getOurCraft().getDisplayWidth() / 2 - 350, 0, 700, 150, 0, 0, 1, 1);
        super.draw(mx, my, renderEngine);
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

}
