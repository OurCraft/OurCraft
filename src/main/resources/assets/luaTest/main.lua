function preInitHandler(evt)
	print "Lua plugin test preInit";
end;

OurCraftAPI.registerHandler("ModPreInitEvent", preInitHandler);