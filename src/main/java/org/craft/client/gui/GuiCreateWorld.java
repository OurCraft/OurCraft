package org.craft.client.gui;

import java.io.*;

import org.craft.client.*;
import org.craft.client.gui.widgets.*;
import org.craft.client.render.*;
import org.craft.entity.*;
import org.craft.resources.*;
import org.craft.utils.*;
import org.craft.world.*;
import org.craft.world.loaders.*;

public class GuiCreateWorld extends Gui
{

    private GuiTextField worldNameField;
    private File         saveFolder;
    private File[]       worldsFolders;

    public GuiCreateWorld(OurCraft game, File saveFolder, File... worldsFolders)
    {
        super(game);
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
        addWidget(new GuiLabel(-1, oc.getDisplayWidth() / 2 - (int) getFontRenderer().getTextWidth(txt) / 2, oc.getDisplayHeight() / 2 - 60, txt, getFontRenderer()));
        worldNameField = new GuiTextField(0, oc.getDisplayWidth() / 2 - 200, oc.getDisplayHeight() / 2 - 20, 400, 40, getFontRenderer());
        worldNameField.setPlaceHolder(">Enter world name here<");
        addWidget(worldNameField);

        GuiButton back = new GuiButton(1, oc.getDisplayWidth() / 2 + 10, oc.getDisplayHeight() / 2 + 40, 190, 40, I18n.format("menu.back"), getFontRenderer());
        addWidget(back);

        GuiButton create = new GuiButton(2, oc.getDisplayWidth() / 2 - 190 - 10, oc.getDisplayHeight() / 2 + 40, 190, 40, I18n.format("menu.createworld.create"), getFontRenderer());
        addWidget(create);
    }

    @Override
    public void update()
    {
        super.update();
        worldNameField.updateCursorCounter();
    }

    @Override
    public void actionPerformed(GuiWidget widget)
    {
        if(widget.getID() == 1)
        {
            oc.openMenu(new GuiSelectWorld(oc, saveFolder, worldsFolders));
        }
        else if(widget.getID() == 2)
        {
            createWorld(worldNameField.getText());
        }
    }

    /**
     * Creates world with given name
     */
    private void createWorld(String worldName)
    {
        // TODO: Loading screen
        WorldGenerator generator = new WorldGenerator();
        WorldLoader worldLoader;
        try
        {
            File worldFolder = new File(SystemUtils.getGameFolder(), "worlds/" + worldName);
            if(!worldFolder.exists())
                worldFolder.mkdirs();
            worldLoader = new VanillaWorldLoader(new ResourceLocation(worldName), new DiskSimpleResourceLoader(worldFolder.getParentFile().getAbsolutePath()));
            World clientWorld = new World(worldName, new BaseChunkProvider(worldLoader), generator, worldLoader);

            EntityPlayer player = clientWorld.createPlayerEntity(oc.getSession().getUUID());
            clientWorld.spawn(player);
            oc.getRenderEngine().setRenderViewEntity(player);
            oc.setPlayerController(new LocalPlayerController(player));

            Entity testEntity = new EntityTest(clientWorld);
            testEntity.setLocation(player.posX, player.posY + 20, player.posZ);
            clientWorld.spawn(testEntity);

            new ThreadGetChunksFromCamera(oc).start();
            oc.setWorld(clientWorld);
            oc.setPlayer(player);
            oc.openMenu(new GuiIngame(oc));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render(int mx, int my, RenderEngine engine)
    {
        drawBackground(mx, my, engine);
        super.render(mx, my, engine);
    }

    @Override
    public boolean pausesGame()
    {
        return true;
    }
}
