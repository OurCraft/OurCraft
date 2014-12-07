package org.craft.modding.script.lua;

import org.craft.*;
import org.craft.modding.script.lua.funcs.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class OurCraftLib extends TwoArgFunction
{

    private LuaEventBusListener eventBus;
    private LuaAddonContainer   container;
    private OurCraftInstance    game;

    public OurCraftLib(LuaEventBusListener eventBus, LuaAddonContainer container, OurCraftInstance game)
    {
        this.game = game;
        this.eventBus = eventBus;
        this.container = container;
    }

    @Override
    public LuaValue call(LuaValue par1, LuaValue par2)
    {
        LuaTable table = new LuaTable();
        table.set("registerHandler", new RegisterHandlerFunc(eventBus, container));
        table.set("getGameRegistry", new GetGameRegistryFunc(game));
        table.set("ResourceLocation", new NewResourceLocFunc());
        table.set("Configuration", new NewConfigurationFunc());
        table.set("Block", new NewBlockFunc());
        table.set("Item", new NewItemFunc());

        LuaTable events = new LuaTable();
        events.set("PreInit", LuaString.valueOf("ModPreInitEvent"));
        events.set("PostInit", LuaString.valueOf("ModPostInitEvent"));
        events.set("InitEvent", LuaString.valueOf("ModInitEvent"));
        events.set("WorldLoad", LuaString.valueOf("ModWorldLoadEvent"));
        events.set("WorldUnload", LuaString.valueOf("ModWorldUnloadEvent"));
        events.set("ActionPerformed", LuaString.valueOf("GuiActionPerformedEvent"));

        LuaTable guiBuilding = new LuaTable();
        guiBuilding.set("Pre", LuaString.valueOf("GuiBuildingEvent.Pre"));
        guiBuilding.set("Post", LuaString.valueOf("GuiBuildingEvent.Post"));
        events.set("GuiBuilding", guiBuilding);

        events.set("BlockChange", LuaString.valueOf("ModBlockChangeEvent"));
        events.set("BlockInteract", LuaString.valueOf("ModBlockInteractEvent"));
        events.set("BlockUpdate", LuaString.valueOf("ModBlockUpdateEvent"));

        table.set("Events", events);

        setAliases(par2, table);
        setAliases(par2.get("package").get("loaded"), table);
        return NIL;
    }

    private void setAliases(LuaValue luaValue, LuaTable table)
    {
        luaValue.set("OurCraft", table);
        luaValue.set("OurCraftAPI", table);
        luaValue.set("OCAPI", table);
        luaValue.set("OC", table);
    }

}
