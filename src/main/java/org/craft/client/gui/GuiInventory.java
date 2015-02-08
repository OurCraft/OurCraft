package org.craft.client.gui;

import org.craft.client.OurCraft;
import org.craft.entity.EntityPlayer;

/**
 * Created by Thog92 on 08/02/2015.
 */
public class GuiInventory extends Gui {
	public GuiInventory(OurCraft ourCraft, EntityPlayer player) {
		super(ourCraft);
	}

	@Override
	public boolean requiresMouse() {
		return true;
	}

	@Override
	public void init() {

	}
}
