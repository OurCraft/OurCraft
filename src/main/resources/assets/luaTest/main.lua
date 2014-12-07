function preInitHandler(evt)
	print("Lua plugin test preInit");
	print(evt:getOurCraftInstance().REGISTRIES_ID);
	addonData = evt:getContainer():getAddonData();
	addonData:setDescription("Lua test addon");
	addonData:setLogoPath(OurCraft.ResourceLocation("ourcraft", "textures/favicon_128.png"));
	testBlock = OurCraftAPI.Block("luaTestBlock");
	print(testBlock:getId());
	evt:getOurCraftInstance():registerBlock(testBlock);
end;

function guiBuilding(evt)
	str = evt:getMenu():toString();
	print("Pre gui building " .. str);
end;

OC.registerHandler(OC.Events.PreInit, preInitHandler);
OC.registerHandler(OC.Events.GuiBuilding.Pre, guiBuilding);
