package org.craft.client.gui;

import java.io.*;
import java.util.*;

import javax.imageio.*;

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

        private String worldFolderName;
        private String worldName;
        private String dateString;

        public GuiWorldSlot(String folderName, String name, long timestamp)
        {
            this.worldFolderName = folderName;
            this.worldName = name;
            Calendar calendar = (Calendar) Calendar.getInstance().clone();
            calendar.setTimeInMillis(timestamp);
            String hour = "" + calendar.get(Calendar.HOUR_OF_DAY);
            String minute = "" + calendar.get(Calendar.MINUTE);
            String second = "" + calendar.get(Calendar.SECOND);
            String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
            if(hour.length() < 2)
                hour = "0" + hour;
            if(minute.length() < 2)
                minute = "0" + minute;
            if(second.length() < 2)
                second = "0" + second;
            if(day.length() < 2)
                day = "0" + day;
            dateString = hour + ":" + minute + ":" + second + " " + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + day;

        }

        @Override
        public void render(int index, int x, int y, int w, int h, int mx, int my, boolean selected, RenderEngine engine, GuiList<?> owner)
        {
            if(selected)
                Gui.drawTexturedRect(engine, x, y, w, h, 0, 0, 1, 1);
            FontRenderer font = oc.getFontRenderer();
            font.drawShadowedString(worldName, 0xFFFFFFFF, x + 2, y, engine);
            font.drawShadowedString(worldFolderName, 0xFFC0C0C0, x + 2, y + 20, engine);
            font.drawShadowedString(dateString, 0xFFFFFFFF, x + 2, y + 40, engine);
        }

        public String getName()
        {
            return worldFolderName;
        }

    }

    private File[]                   worldFolders;
    private GuiList<GuiWorldSlot>    worldList;
    private GuiButton                playButton;
    private HashMap<String, Texture> textureMap;
    private Texture                  worldSnapshot;
    private Shader                   worldSnapshotShader;
    private File                     saveFolder;
    private GuiButton                deleteButton;

    public GuiSelectWorld(OurCraft game, File saveFolder, File... worldFolders)
    {
        super(game);
        this.saveFolder = saveFolder;
        textureMap = new HashMap<String, Texture>();
        if(worldFolders == null)
            worldFolders = new File[0];
        this.worldFolders = worldFolders;

        try
        {
            worldSnapshotShader = new Shader(new String(oc.getAssetsLoader().getResource(new ResourceLocation("ourcraft/shaders", "worldSnap.vsh")).getData(), "UTF-8"), new String(oc.getAssetsLoader().getResource(new ResourceLocation("ourcraft/shaders", "worldSnap.fsh")).getData(), "UTF-8"));
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean requiresMouse()
    {
        return true;
    }

    @Override
    public void init()
    {
        worldList = new GuiList<GuiWorldSlot>(2, oc.getDisplayWidth() / 8, oc.getDisplayHeight() / 8 - 40, oc.getDisplayWidth() - oc.getDisplayWidth() / 4, oc.getDisplayHeight() - oc.getDisplayHeight() / 4, 80);
        Arrays.sort(worldFolders, new Comparator<File>()
        {

            @Override
            public int compare(File a, File b)
            {
                File aWorldDataFile = new File(a, "world.data");
                File bWorldDataFile = new File(b, "world.data");
                if(aWorldDataFile.exists() && bWorldDataFile.exists())
                {
                    return Long.compare(bWorldDataFile.lastModified(), aWorldDataFile.lastModified());
                }
                return 1;
            }
        });
        for(File worldFolder : worldFolders)
        {
            File worldDataFile = new File(worldFolder, "world.data");
            if(worldDataFile.exists())
            {

                if(!worldFolder.exists())
                    worldFolder.mkdirs();
                WorldLoader worldLoader = new VanillaWorldLoader(new ResourceLocation(worldFolder.getName()), new DiskSimpleResourceLoader(worldFolder.getParentFile().getAbsolutePath()));

                HashMap<String, String> worldInfos = worldLoader.loadWorldInfos(worldDataFile);
                File snapshotFile = new File(worldFolder, "worldSnapshot.png");
                if(snapshotFile.exists())
                {
                    try
                    {
                        textureMap.put(worldFolder.getName(), OpenGLHelper.loadTexture(ImageIO.read(snapshotFile)));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                long timestamp = 0L;
                String name = worldFolder.getName();
                if(worldInfos.containsKey("timestamp"))
                    timestamp = Long.parseLong(worldInfos.get("timestamp"));
                if(worldInfos.containsKey("name"))
                    name = worldInfos.get("name");
                worldList.addSlot(new GuiWorldSlot(worldFolder.getName(), name, timestamp));
            }
        }
        playButton = new GuiButton(1, oc.getDisplayWidth() - oc.getDisplayWidth() / 8 - 200, oc.getDisplayHeight() - oc.getDisplayHeight() / 8 - 40, 200, 40, I18n.format("menu.selectworld.play"), getFontRenderer());
        playButton.enabled = false;
        addWidget(playButton);
        addWidget(worldList);
        addWidget(new GuiButton(3, oc.getDisplayWidth() / 8, oc.getDisplayHeight() - oc.getDisplayHeight() / 8 - 40, 200, 40, I18n.format("menu.selectworld.new"), getFontRenderer()));
        addWidget(new GuiButton(4, oc.getDisplayWidth() - oc.getDisplayWidth() / 8 - 200, oc.getDisplayHeight() - oc.getDisplayHeight() / 8 + 10, 200, 40, I18n.format("menu.back"), getFontRenderer()));

        deleteButton = new GuiButton(5, oc.getDisplayWidth() / 8, oc.getDisplayHeight() - oc.getDisplayHeight() / 8 + 10, 200, 40, I18n.format("menu.selectworld.delete"), getFontRenderer());
        deleteButton.enabled = false;
        addWidget(deleteButton);
    }

    public void actionPerformed(GuiWidget widget)
    {
        final int id = widget.getID();
        if(id == 1)
        {
            GuiWorldSlot worldSlot = worldList.getSelected();
            if(worldSlot != null)
            {
                launchGameOnWorld(worldSlot.getName());
            }
        }
        else if(id == 2)
        {
            playButton.enabled = worldList.getSelected() != null;
            deleteButton.enabled = worldList.getSelected() != null;
            if(worldList.getSelected() != null)
            {
                String name = worldList.getSelected().worldFolderName;
                this.worldSnapshot = textureMap.get(name);
            }
            else
                worldSnapshot = null;
        }
        else if(id == 3)
        {
            oc.openMenu(new GuiCreateWorld(oc, saveFolder, worldFolders));
        }
        else if(id == 4)
        {
            oc.openMenu(new GuiMainMenu(oc));
        }
        else if(id == 5)
        {
            GuiWorldSlot worldSlot = worldList.getSelected();
            oc.openMenu(new GuiDeleteWorld(oc, saveFolder, worldFolders, worldSlot.worldFolderName));
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

            EntityPlayer player = new EntityPlayer(clientWorld, oc.getSession().getUUID());
            player.setLocation(0, 160 + 17, 0);
            clientWorld.spawn(player);
            oc.getRenderEngine().setRenderViewEntity(player);
            oc.setPlayerController(new LocalPlayerController(player));

            Entity testEntity = new Entity(clientWorld);
            testEntity.setLocation(player.posX + 10, player.posY + 20, player.posZ);
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
    public void update()
    {

    }

    public void draw(int mx, int my, RenderEngine renderEngine)
    {
        drawBackground(mx, my, renderEngine);
        if(worldSnapshot != null)
        {
            Shader oldShader = renderEngine.getCurrentShader();
            renderEngine.setCurrentShader(worldSnapshotShader);
            renderEngine.bindTexture(worldSnapshot);
            drawTexturedRect(renderEngine, 0, 0, oc.getDisplayWidth(), oc.getDisplayHeight(), 0, 0, 1, 1);
            renderEngine.setCurrentShader(oldShader);
        }
        super.draw(mx, my, renderEngine);
    }
}
