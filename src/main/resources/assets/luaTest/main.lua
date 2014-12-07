function preInitHandler(evt)
	print("Lua plugin test preInit");
	print(evt:getOurCraftInstance().REGISTRIES_ID);
	addonData = evt:getContainer():getAddonData();
	print(addonData:getID());
end;

OurCraftAPI.registerHandler("ModPreInitEvent", preInitHandler);