package com.kbi.qwertech.items.behavior;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregapi.item.multiitem.MultiItem;
import gregapi.item.multiitem.behaviors.IBehavior.AbstractBehaviorDefault;

public class Behavior_AOE extends AbstractBehaviorDefault {

    public final double distance;
    public final Random rand;

    public Behavior_AOE(double aDist) {
        this.distance = aDist;
        this.rand = new Random();
    }

    @Override
    public boolean onLeftClickEntity(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        return false;
    }

}
