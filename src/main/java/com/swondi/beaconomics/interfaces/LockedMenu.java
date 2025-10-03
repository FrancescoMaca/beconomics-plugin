package com.swondi.beaconomics.interfaces;

import java.util.List;

/**
 * Used by menus to indicate if a menu has free slots
 */
@FunctionalInterface
public interface LockedMenu {
    List<Integer> isSlotAvailable(int slot);
}
