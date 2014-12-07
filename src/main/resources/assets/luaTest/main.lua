function preInitHandler(evt)
	print("Lua plugin test preInit");
	addonData = evt:getContainer():getAddonData();
	addonData:setDescription("Lua test addon");
	addonData:setLogoPath(OurCraft.ResourceLocation("ourcraft", "textures/favicon_128.png"));
	testBlock = OurCraftAPI.Block("luaTestBlock");
	evt:getOurCraftInstance():registerBlock(testBlock);
	configuration = OC.Configuration(evt:getSuggestedConfigurationFile());
	configuration:setInt("testInt", 0);
	configuration:save();
end;

function guiBuilding(evt)
	str = evt:getMenu():toString();
	print("Pre gui building " .. str);
end;

OC.registerHandler(OC.Events.PreInit, preInitHandler);
OC.registerHandler(OC.Events.GuiBuilding.Pre, guiBuilding);
