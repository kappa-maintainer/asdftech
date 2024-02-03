package com.kbi.qwertech.loaders.mod;

import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import gregapi.code.ModData;
import gregapi.data.MD;
import gregapi.util.ST;

public class ModLoad_WTCH extends ModLoadBase {

    @Override
    public ModData getMod() {
        return MD.WTCH;
    }

    @Override
    public void addOreDict() {
        String mId = this.getMod().mID;
        String[] oreNames = new String[] { "Rowan", "Alder", "Hawthorn" };
        String[] blockNames = new String[] { "witchery:witchwood", "witchery:witchwood", "witchery:witchwood" };
        int[] meta = new int[] { 0, 1, 2 };
        for (int q = 0; q < oreNames.length; q++) {
            Block result = GameRegistry.findBlock(mId, blockNames[q]);
            if (result != null) {
                OreDictionary.registerOre("plankWood" + oreNames[q], ST.make(result, 1, meta[q]));
            }
        }
    }

}
