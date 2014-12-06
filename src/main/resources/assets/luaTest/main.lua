function preInitHandler(evt)
	print("Lua plugin test preInit");
	print(evt:getOurCraftInstance().REGISTRIES_ID);
end;

OurCraftAPI.registerHandler("ModPreInitEvent", preInitHandler);