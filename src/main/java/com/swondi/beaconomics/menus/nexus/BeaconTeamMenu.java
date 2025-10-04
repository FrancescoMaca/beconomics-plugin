package com.swondi.beaconomics.menus.nexus;

import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class BeaconTeamMenu {

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, Constants.BEACON_TEAM_MENU_TITLE);

        inv.setItem(0, UIHelper.createBackArrow(Constants.UI_NEXUS_MAIN_MENU_VALUE));
        return inv;
    }
}
