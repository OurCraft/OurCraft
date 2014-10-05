package org.craft.client.gui;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.client.render.fonts.*;
import org.craft.entity.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.world.*;
import org.craft.world.loaders.*;
import org.craft.world.populators.*;

public class GuiSelectWorld extends Gui
{

    public class GuiWorldSlot extends GuiListSlot
    {

        private String worldName;

        public GuiWorldSlot(String name)
        {
            this.worldName = name;
        }

        @Override
        public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
        {
            if(selected)
                Gui.drawTexturedRect(engine, x, y, w, h, 0, 0, 1, 1);
            FontRenderer font = OurCraft.getOurCraft().getFontRenderer();
            font.drawShadowedString(worldName, 0xFFFFFFFF, x, y, engine);
        }

        public String getName()
        {
            return worldName;
        }

    }

    private File[]                worldFolders;
    private GuiList<GuiWorldSlot> worldList;
    private GuiButton             playButton;

    public GuiSelectWorld(FontRenderer font, File... worldFolders)
    {
        super(font);
        if(worldFolders == null)
            worldFolders = new File[0];
        this.worldFolders = worldFolders;
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        worldList = new GuiList<GuiWorldSlot>(2, OurCraft.getOurCraft().getDisplayWidth() / 8, OurCraft.getOurCraft().getDisplayHeight() / 8 - 40, OurCraft.getOurCraft().getDisplayWidth() - OurCraft.getOurCraft().getDisplayWidth() / 4, 200, 20);
        for(File worldFolder : worldFolders)
        {
            File worldDataFile = new File(worldFolder, "world.data");
            if(worldDataFile.exists())
            {
                worldList.addSlot(new GuiWorldSlot(worldFolder.getName()));
            }
        }
        playButton = new GuiButton(1, OurCraft.getOurCraft().getDisplayWidth() - OurCraft.getOurCraft().getDisplayWidth() / 8 - 200, OurCraft.getOurCraft().getDisplayHeight() - OurCraft.getOurCraft().getDisplayHeight() / 8 - 40, 200, 40, I18n.format("menu.selectworld.play"), getFontRenderer());
        playButton.enabled = false;
        addWidget(playButton);
        addWidget(worldList);
        addWidget(new GuiButton(3, OurCraft.getOurCraft().getDisplayWidth() / 8, OurCraft.getOurCraft().getDisplayHeight() - OurCraft.getOurCraft().getDisplayHeight() / 8 - 40, 200, 40, I18n.format("menu.selectworld.new"), getFontRenderer()));
        addWidget(new GuiButton(4, OurCraft.getOurCraft().getDisplayWidth() - OurCraft.getOurCraft().getDisplayWidth() / 8 - 200, OurCraft.getOurCraft().getDisplayHeight() - OurCraft.getOurCraft().getDisplayHeight() / 8 + 10, 200, 40, I18n.format("menu.back"), getFontRenderer()));
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 1)
        {
            GuiWorldSlot worldSlot = worldList.getSelected();
            if(worldSlot != null)
            {
                launchGameOnWorld(worldSlot.getName());
            }
        }
        else if(widget.getID() == 2)
        {
            playButton.enabled = worldList.getSelected() != null;
        }
        else if(widget.getID() == 3)
        {
            launchGameOnWorld("test-world");
        }
        else if(widget.getID() == 4)
        {
            OurCraft.getOurCraft().openMenu(new GuiMainMenu(getFontRenderer()));
        }
    }

    private void launchGameOnWorld(String worldName)
    {
        WorldGenerator generator = new WorldGenerator();
        generator.addPopulator(new RockPopulator());
        generator.addPopulator(new GrassPopulator());
        generator.addPopulator(new TreePopulator());
        generator.addPopulator(new FlowerPopulator());
        WorldLoader worldLoader;
        try
        {
            File worldFolder = new File(SystemUtils.getGameFolder(), "worlds/" + worldName);
            if(!worldFolder.exists())
                worldFolder.mkdirs();
            worldLoader = new VanillaWorldLoader(new ResourceLocation(worldName), new DiskSimpleResourceLoader(worldFolder.getParentFile().getAbsolutePath()));
            World clientWorld = new World(worldName, new BaseChunkProvider(worldLoader), generator, worldLoader);

            EntityPlayer player = new EntityPlayer(clientWorld, OurCraft.getOurCraft().getSession().getUUID());
            player.setLocation(0, 160 + 17, 0);
            clientWorld.spawn(player);
            OurCraft.getOurCraft().getRenderEngine().setRenderViewEntity(player);
            OurCraft.getOurCraft().setPlayerController(new LocalPlayerController(player));

            Entity testEntity = new Entity(clientWorld);
            testEntity.setLocation(player.posX + 10, player.posY + 20, player.posZ);
            clientWorld.spawn(testEntity);

            new ThreadGetChunksFromCamera(OurCraft.getOurCraft()).start();
            OurCraft.getOurCraft().setWorld(clientWorld);
            OurCraft.getOurCraft().setPlayer(player);
            OurCraft.getOurCraft().openMenu(new GuiIngame(getFontRenderer()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void update()
    {

    }

    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        super.draw(mx, my, renderEngine);
    }
}
