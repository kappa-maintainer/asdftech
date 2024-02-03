package com.kbi.qwertech.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import com.kbi.qwertech.loaders.RegisterArmor;

import gregapi.render.IIconContainer;

public class PantPlate extends PantBase {

    @Override
    public float getDamageProtection() {
        return 0.7F;
    }

    @Override
    public long getMaxDurabilityMultiplier() {
        return 125;
    }

    @Override
    public IIconContainer getIcon(ItemStack stack) {
        return (IIconContainer) RegisterArmor.iconTitle.get("qwertech:armor/leggings/plate");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (type == null) {
            return "qwertech:textures/armor/plate.png";
        } else {
            return "qwertech:textures/armor/blank.png";
        }
    }

}
