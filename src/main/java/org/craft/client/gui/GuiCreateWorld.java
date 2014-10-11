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

public class GuiCreateWorld extends Gui
{

    private GuiTextField worldNameField;
    private File         saveFolder;
    private File[]       worldsFolders;

    public GuiCreateWorld(FontRenderer font, File saveFolder, File... worldsFolders)
    {
        super(font);
        this.saveFolder = saveFolder;
        this.worldsFolders = worldsFolders;
    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        String txt = I18n.format("menu.createworld.title");
        addWidget(new GuiLabel(-1, OurCraft.getOurCraft().getDisplayWidth() / 2 - (int) getFontRenderer().getTextLength(txt) / 2, OurCraft.getOurCraft().getDisplayHeight() / 2 - 60, txt, getFontRenderer()));
        worldNameField = new GuiTextField(0, OurCraft.getOurCraft().getDisplayWidth() / 2 - 200, OurCraft.getOurCraft().getDisplayHeight() / 2 - 20, 400, 40, getFontRenderer());
        addWidget(worldNameField);

        GuiButton back = new GuiButton(1, OurCraft.getOurCraft().getDisplayWidth() / 2 + 10, OurCraft.getOurCraft().getDisplayHeight() / 2 + 40, 190, 40, I18n.format("menu.back"), getFontRenderer());
        addWidget(back);

        GuiButton create = new GuiButton(2, OurCraft.getOurCraft().getDisplayWidth() / 2 - 190 - 10, OurCraft.getOurCraft().getDisplayHeight() / 2 + 40, 190, 40, I18n.format("menu.createworld.create"), getFontRenderer());
        addWidget(create);
    }

    @Override
    public void update()
    {
        worldNameField.updateCursorCounter();
    }

    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 1)
        {
            OurCraft.getOurCraft().openMenu(new GuiSelectWorld(getFontRenderer(), saveFolder, worldsFolders));
        }
        else if(widget.getID() == 2)
        {
            createWorld(worldNameField.getText());
        }
    }

    private void createWorld(String worldName)
    {
        // TODO: Loading screen
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

    public void draw(int mx, int my, RenderEngine engine)
    {
        drawBackground(mx, my, engine);
        super.draw(mx, my, engine);
    }
}
