package com.kbi.qwertech.api.data;

import static gregapi.data.CS.F;
import static gregapi.data.CS.NB;
import static gregapi.data.CS.OUT;
import static gregapi.data.CS.T;
import static gregapi.data.CS.W;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregapi.api.Abstract_Mod;
import gregapi.code.IItemContainer;
import gregapi.code.TagData;
import gregapi.item.IItemEnergy;
import gregapi.network.NetworkHandler;
import gregapi.oredict.OreDictItemData;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;

public enum QTI implements IItemContainer {

    moldArrow,
    moldAxe,
    moldBolt,
    moldChisel,
    moldChunk,
    moldDoubleAxe,
    moldFile,
    moldGear,
    moldHammer,
    moldHoe,
    moldIngot,
    moldItemCasing,
    moldLongRod,
    moldPickaxe,
    moldPlate,
    moldPlow,
    moldRing,
    moldRod,
    moldSaw,
    moldScrewdriver,
    moldSense,
    moldShovel,
    moldSmallGear,
    moldSword,
    moldTinyPlate,
    moldUniversalSpade,
    moldSpade,
    moldMattock,
    moldMace,
    batEmblem,
    qwerTool,
    qwerFood,
    qwerArmor,
    qwerComponents,
    mozzarella,
    parmesanRaw,
    parmesan,
    parmesanGrated,
    doughFlatSauce,
    turkeyBreastRaw,
    turkeyBreastCooked,
    turkeyLegRaw,
    turkeyLegCooked,
    turkeyWingRaw,
    turkeyWingCooked,
    chickenBreastRaw,
    chickenBreastCooked,
    chickenLegRaw,
    chickenLegCooked,
    chickenWingRaw,
    chickenWingCooked,
    chickenWholeRaw,
    chickenWholeCooked,
    turkeyWholeRaw,
    turkeyWholeCooked,
    turkeyFeather,
    turkeyEgg,
    chickenEggHardBoiled,
    turkeyEggHardBoiled,
    turkeyLegFried,
    turkeyWingFried,
    chickenLegFried,
    chickenWingFried,
    frogLegRaw,
    frogLegCooked,
    frogEggs,
    chickenEgg,
    junglefowlWholeRaw,
    junglefowlWholeCooked,
    junglefowlBreastRaw,
    junglefowlBreastCooked,
    junglefowlLegRaw,
    junglefowlLegCooked,
    junglefowlWingRaw,
    junglefowlWingCooked,
    junglefowlWingFried,
    junglefowlLegFried,
    junglefowlEgg,
    tomatoSauce,
    salsaMild,
    salsaMedium,
    salsaMagmatic,
    spawnTurkey,
    spawnFrog,
    jarFrog,
    jarChicken,
    jarSlime,
    jarMagmaCube,
    jarSilverfish,
    jarCaveSpider,
    jarBat,
    jarTFBunny,
    jarTFBird,
    jarTFSpider,
    jarTFSquirrel,
    jarTFRaven,
    jarTFTinyBird,
    jarTFMazeSlime,
    syringe,
    buckets;

    public static NetworkHandler NW_API;
    private ItemStack mStack;
    private boolean mHasNotBeenSet = T;

    @Override
    public IItemContainer set(Item aItem) {
        mHasNotBeenSet = F;
        if (aItem == null) {
            // new Exception().printStackTrace(GT_Log.deb);
            return this;
        }
        mStack = ST.amount(1, ST.make(aItem, 1, 0));
        return this;
    }

    public IItemContainer set(Item aItem, long aMeta) {
        mHasNotBeenSet = F;
        if (aItem == null) {
            // new Exception().printStackTrace(GT_Log.deb);
            return this;
        }
        mStack = ST.amount(1, ST.make(aItem, 1, aMeta));
        return this;
    }

    @Override
    public IItemContainer set(ItemStack aStack) {
        mHasNotBeenSet = F;
        if (ST.invalid(aStack)) {
            // new Exception().printStackTrace(GT_Log.deb);
            return this;
        }
        mStack = ST.amount(1, aStack);
        return this;
    }

    public IItemContainer set(Item aItem, OreDictItemData aData, String... aOreDict) {
        mHasNotBeenSet = F;
        if (aItem == null) {
            // new Exception().printStackTrace(GT_Log.deb);
            return this;
        }
        ItemStack aStack = ST.make(aItem, 1, 0);
        mStack = ST.amount(1, aStack);
        if (aData != null && !OM.reg(aData.toString(), ST.make(aItem, 1, W))) OM.data(ST.make(aItem, 1, W), aData);
        for (String tOreDict : aOreDict) OM.reg(tOreDict, ST.make(aItem, 1, W));
        return this;
    }

    public IItemContainer set(ItemStack aStack, OreDictItemData aData, String... aOreDict) {
        mHasNotBeenSet = F;
        if (ST.invalid(aStack)) {
            // new Exception().printStackTrace(DEB);
            return this;
        }
        mStack = ST.amount(1, aStack);
        if (aData != null && !OM.reg(aData.toString(), ST.amount(1, aStack))) OM.data(ST.amount(1, aStack), aData);
        for (String tOreDict : aOreDict) OM.reg(tOreDict, ST.amount(1, aStack));
        return this;
    }

    @Override
    public Item item() {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return null;
        return mStack.getItem();
    }

    @Override
    public Block block() {
        return ST.block(get(0));
    }

    @Override
    public boolean exists() {
        return ST.valid(mStack);
    }

    @Override
    public final boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }

    @Override
    public boolean equal(Object aStack) {
        if (aStack instanceof Block) return aStack != NB && block() == aStack;
        return equal(aStack, F, F);
    }

    @Override
    public boolean equal(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
        if (ST.invalid((ItemStack) aStack)) return F;
        return ST.equal((ItemStack) aStack, aWildcard ? wild(1) : get(1), aIgnoreNBT);
    }

    @Override
    public ItemStack get(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        return ST.amount(aAmount, OM.get_(mStack));
    }

    @Override
    public ItemStack getWildcard(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        return ST.copyAmountAndMeta(aAmount, W, OM.get_(mStack));
    }

    @Override
    public ItemStack wild(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        return ST.copyAmountAndMeta(aAmount, W, OM.get_(mStack));
    }

    @Override
    public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        return ST.copyAmountAndMeta(aAmount, 0, OM.get_(mStack));
    }

    @Override
    public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        return ST.copyAmountAndMeta(aAmount, mStack.getMaxDamage() - 1, OM.get_(mStack));
    }

    @Override
    public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (ST.invalid(rStack)) return null;
        rStack.setStackDisplayName(aDisplayName);
        return ST.amount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithNameAndNBT(long aAmount, String aDisplayName, NBTTagCompound aNBT,
        Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (ST.invalid(rStack)) return null;
        UT.NBT.set(rStack, aNBT);
        if (aDisplayName != null) rStack.setStackDisplayName(aDisplayName);
        return ST.amount(aAmount, rStack);
    }

    public ItemStack getWithChargeAndMeta(long aAmount, long aMetaValue, long aEnergy, Object... aReplacements) {
        ItemStack rStack = getWithMeta(1, aMetaValue, aReplacements);
        if (ST.invalid(rStack)) return null;
        if (rStack.getItem() instanceof IItemEnergy)
            for (TagData tEnergyType : ((IItemEnergy) rStack.getItem()).getEnergyTypes(rStack))
                ((IItemEnergy) rStack.getItem()).setEnergyStored(tEnergyType, rStack, aEnergy);
        return ST.amount(aAmount, rStack);
    }

    public ItemStack getWithMaxCharge(long aAmount, long aMetaValue, long aEnergy, long aMaxEnergy,
        Object... aReplacements) {
        ItemStack rStack = getWithMeta(1, aMetaValue, aReplacements);
        if (ST.invalid(rStack)) return null;
        if (this == qwerComponents) {
            NBTTagCompound nbt = UT.NBT.getOrCreate(rStack);
            nbt.setLong("c", aMaxEnergy);
            nbt.setLong("e", aEnergy);
            rStack.setTagCompound(nbt);
        } else {
            if (rStack.getItem() instanceof IItemEnergy)
                for (TagData tEnergyType : ((IItemEnergy) rStack.getItem()).getEnergyTypes(rStack))
                    ((IItemEnergy) rStack.getItem()).setEnergyStored(tEnergyType, rStack, aEnergy);
        }
        return ST.amount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithCharge(long aAmount, long aEnergy, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (ST.invalid(rStack)) return null;
        if (rStack.getItem() instanceof IItemEnergy)
            for (TagData tEnergyType : ((IItemEnergy) rStack.getItem()).getEnergyTypes(rStack))
                ((IItemEnergy) rStack.getItem()).setEnergyStored(tEnergyType, rStack, aEnergy);
        return ST.amount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithMeta(long aAmount, long aMetaValue, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        return ST.copyAmountAndMeta(aAmount, aMetaValue, OM.get_(mStack));
    }

    @Override
    public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        return ST.copyAmountAndMeta(aAmount, aMetaValue, OM.get_(mStack));
    }

    @Override
    public ItemStack getWithNBT(long aAmount, NBTTagCompound aNBT, Object... aReplacements) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (ST.invalid(mStack)) return ST.copyFirst(aReplacements);
        ItemStack rStack = ST.amount(aAmount, OM.get_(mStack));
        UT.NBT.set(rStack, aNBT);
        return rStack;
    }

    @Override
    public IItemContainer registerOre(Object... aOreNames) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        for (Object tOreName : aOreNames) OM.reg(tOreName, get(1));
        return this;
    }

    @Override
    public IItemContainer registerWildcardAsOre(Object... aOreNames) {
        if (mHasNotBeenSet && Abstract_Mod.sFinalized < Abstract_Mod.sModCountUsingGTAPI)
            OUT.println("The Enum '" + name() + "' has not been set to an Item at this time!");
        for (Object tOreName : aOreNames) OM.reg(tOreName, wild(1));
        return this;
    }

    @Override
    public Item getItem() {
        return item();
    }

    @Override
    public Block getBlock() {
        return block();
    }

}
