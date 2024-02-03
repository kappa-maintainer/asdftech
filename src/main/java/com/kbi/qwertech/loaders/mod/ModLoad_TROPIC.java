package com.kbi.qwertech.loaders.mod;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregapi.code.ModData;
import gregapi.data.MD;
import gregapi.util.ST;

public class ModLoad_TROPIC extends ModLoadBase {

    @Override
    public ModData getMod() {
        return MD.TROPIC;
    }

    @Override
    public void addOreDict() {
        ItemStack tPlank = ST.make(MD.TROPIC, "tile.plank", 1, 0);
        if (tPlank != null) {
            OreDictionary.registerOre("plankWoodPalm", ST.make(MD.TROPIC, "tile.plank", 1, 0));
            OreDictionary.registerOre("plankWoodMahogany", ST.make(MD.TROPIC, "tile.plank", 1, 1));
        }
    }
}
