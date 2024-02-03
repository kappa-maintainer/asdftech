package com.kbi.qwertech.entities;

import static com.kbi.qwertech.loaders.RegisterSpecies.AI_LIST;
import static com.kbi.qwertech.loaders.RegisterSpecies.ATTACK_ON_HIT;
import static com.kbi.qwertech.loaders.RegisterSpecies.ATTACK_ON_PLAYERHIT;
import static com.kbi.qwertech.loaders.RegisterSpecies.ATTACK_ON_SIGHT;
import static com.kbi.qwertech.loaders.RegisterSpecies.ATTACK_ON_SPECIESHIT;
import static com.kbi.qwertech.loaders.RegisterSpecies.ATTACK_ON_SPECIESORPLAYERHIT;
import static com.kbi.qwertech.loaders.RegisterSpecies.AVOIDS_BLOCK;
import static com.kbi.qwertech.loaders.RegisterSpecies.AVOIDS_ENTITY;
import static com.kbi.qwertech.loaders.RegisterSpecies.COLOR_PRIMARY_MAX;
import static com.kbi.qwertech.loaders.RegisterSpecies.COLOR_PRIMARY_MIN;
import static com.kbi.qwertech.loaders.RegisterSpecies.COLOR_SECONDARY_MIN;
import static com.kbi.qwertech.loaders.RegisterSpecies.IGNORES_GROUND_FOOD;
import static com.kbi.qwertech.loaders.RegisterSpecies.LAYS_EGGS;
import static com.kbi.qwertech.loaders.RegisterSpecies.NESTING_GROUND;
import static com.kbi.qwertech.loaders.RegisterSpecies.NEST_ITEM;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.kbi.qwertech.QwerTech;
import com.kbi.qwertech.api.data.COLOR;
import com.kbi.qwertech.api.entities.IGeneticMob;
import com.kbi.qwertech.api.entities.Species;
import com.kbi.qwertech.api.entities.Subtype;
import com.kbi.qwertech.api.entities.Taggable;
import com.kbi.qwertech.api.registry.MobSpeciesRegistry;
import com.kbi.qwertech.entities.ai.EntityAIEatFoodOffTheGround;
import com.kbi.qwertech.entities.ai.EntityAILayEgg;
import com.kbi.qwertech.entities.ai.EntityAINesting;

import gregapi.data.CS;

public class EntityHelperFunctions {

    public static void assignAI(EntityLiving entity, IGeneticMob mobData, List<String> tags, Taggable theKind) {
        boolean doesEAT = true;
        for (String tag : tags) {
            switch (tag) {
                case ATTACK_ON_HIT:
                    break;
                case ATTACK_ON_PLAYERHIT:
                    break;
                case ATTACK_ON_SIGHT:
                    break;
                case ATTACK_ON_SPECIESHIT:
                    break;
                case ATTACK_ON_SPECIESORPLAYERHIT:
                    break;
                case AVOIDS_ENTITY:
                    break;
                case AVOIDS_BLOCK:
                    break;
                case IGNORES_GROUND_FOOD:
                    doesEAT = false;
                    if ((boolean) theKind.getTag(tag)) {
                        List tasks = entity.tasks.taskEntries;
                        for (int q = 0; q < tasks.size(); q++) {
                            Object task = tasks.get(q);
                            if (task instanceof EntityAIEatFoodOffTheGround) {
                                tasks.remove(task);
                                q = q - 1;
                            }
                        }
                    } else {
                        entity.tasks.addTask(4, new EntityAIEatFoodOffTheGround(entity));
                    }
                    break;
                case NESTING_GROUND:
                    if ((boolean) theKind.getTag(tag)) {
                        if (theKind.hasTag(NEST_ITEM)) {
                            entity.tasks.addTask(1, new EntityAINesting(entity, (ItemStack) theKind.getTag(NEST_ITEM)));
                        } else {
                            entity.tasks.addTask(1, new EntityAINesting(entity, QwerTech.machines.getItem(1770)));
                        }
                    } else {
                        List tasks = entity.tasks.taskEntries;
                        for (int q = 0; q < tasks.size(); q++) {
                            Object task = tasks.get(q);
                            if (task instanceof EntityAINesting) {
                                tasks.remove(task);
                                q = q - 1;
                            }
                        }
                    }
                    break;
                case LAYS_EGGS:
                    if ((boolean) theKind.getTag(tag)) {
                        entity.tasks.addTask(10, new EntityAILayEgg(entity, 2000, 32000));
                    } else {
                        List tasks = entity.tasks.taskEntries;
                        for (int q = 0; q < tasks.size(); q++) {
                            Object task = tasks.get(q);
                            if (task instanceof EntityAILayEgg) {
                                tasks.remove(task);
                                q = q - 1;
                            }
                        }
                    }
                    break;
                case AI_LIST:
                    break;
            }
        }
        if (doesEAT) {
            entity.tasks.addTask(4, new EntityAIEatFoodOffTheGround(entity));
        }
    }

    public static void assignAI(EntityLiving entity, IGeneticMob mobData) {
        List<String> specTags = mobData.getSpecies()
            .getTags();
        List<String> subTags = mobData.getSubtype()
            .getTags();
        assignAI(entity, mobData, specTags, mobData.getSpecies());
        assignAI(entity, mobData, subTags, mobData.getSubtype());
    }

    public static NBTTagCompound sanitizeEntity(NBTTagCompound comp) {
        comp.removeTag("Invulnerable");
        comp.removeTag("PortalCooldown");
        comp.removeTag("AbsorptionAmount");
        comp.removeTag("FallDistance");
        comp.removeTag("DeathTime");
        comp.removeTag("PersistenceRequired");
        comp.removeTag("HealF");
        comp.removeTag("Motion");
        comp.removeTag("Leashed");
        comp.removeTag("UUIDLeast");
        comp.removeTag("Health");
        comp.removeTag("Air");
        comp.removeTag("OnGround");
        comp.removeTag("Dimension");
        comp.removeTag("Rotation");
        comp.removeTag("UUIDMost");
        comp.removeTag("Equipment");
        comp.removeTag("CustomName");
        comp.removeTag("Pos");
        comp.removeTag("Fire");
        comp.removeTag("CanPickUpLoot");
        comp.removeTag("HurtTime");
        comp.removeTag("AttackTime");
        comp.removeTag("CustomNameVisible");
        return comp;
    }

    public static MovingObjectPosition getEntityLookTrace(World world, Entity entity, boolean par3, double range) {
        float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch);
        float f2 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw);
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX);
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY);
        if (!world.isRemote && entity instanceof EntityPlayer) {
            d1 += 1.62D;
        }
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ);
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = range;
        if (entity instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) entity).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
        return world.func_147447_a(vec3, vec31, par3, !par3, par3);
    }

    public static boolean doesMobFitSubtype(IGeneticMob toCheck, Subtype checking, float percentage) {
        if (percentage < 0F || percentage > 1F) percentage = 1F;
        boolean meets = true;
        float maturityRange = (float) (checking.getMaxMaturity() - checking.getMinMaturity());
        if (toCheck.getMaturity() > checking.getMinMaturity() + maturityRange * percentage
            || toCheck.getMaturity() < checking.getMaxMaturity() - maturityRange * percentage) {
            meets = false;
        }
        float fertilityRange = (float) (checking.getMaxFertility() - checking.getMinFertility());
        if (toCheck.getFertility() > checking.getMinFertility() + fertilityRange * percentage
            || toCheck.getFertility() < checking.getMaxFertility() - fertilityRange * percentage) {
            meets = false;
        }
        float sizeRange = (float) (checking.getMaxSize() - checking.getMinSize());
        if (toCheck.getSize() > checking.getMinSize() + sizeRange * percentage
            || toCheck.getSize() < checking.getMaxSize() - sizeRange * percentage) {
            meets = false;
        }
        float strengthRange = (float) (checking.getMaxStrength() - checking.getMinStrength());
        if (toCheck.getStrength() > checking.getMinStrength() + strengthRange * percentage
            || toCheck.getMaturity() < checking.getMaxStrength() - strengthRange * percentage) {
            meets = false;
        }
        float mutableRange = (float) (checking.getMaxMutable() - checking.getMinMutable());
        if (toCheck.getMutable() > checking.getMinMutable() + mutableRange * percentage
            || toCheck.getMutable() < checking.getMaxMutable() - mutableRange * percentage) {
            meets = false;
        }
        float smartRange = (float) (checking.getMaxSmart() - checking.getMinSmart());
        if (toCheck.getSmart() > checking.getMinSmart() + smartRange * percentage
            || toCheck.getSmart() < checking.getMaxSmart() - smartRange * percentage) {
            meets = false;
        }
        float snarlRange = (float) (checking.getMaxSnarl() - checking.getMinSnarl());
        if (toCheck.getSnarl() > checking.getMinSnarl() + snarlRange * percentage
            || toCheck.getSnarl() < checking.getMaxSnarl() - snarlRange * percentage) {
            meets = false;
        }
        float staminaRange = (float) (checking.getMaxStamina() - checking.getMinStamina());
        if (toCheck.getStamina() > checking.getMinStamina() + staminaRange * percentage
            || toCheck.getStamina() < checking.getMaxStamina() - staminaRange * percentage) {
            meets = false;
        }
        if (COLOR.getMin(toCheck.getPrimaryColor(), (int) checking.getTag(COLOR_PRIMARY_MIN))
            != (int) checking.getTag(COLOR_PRIMARY_MIN)) {
            meets = false;
        }
        if (COLOR.getMax(toCheck.getPrimaryColor(), (int) checking.getTag(COLOR_PRIMARY_MAX))
            != (int) checking.getTag(COLOR_PRIMARY_MAX)) {
            meets = false;
        }
        if (COLOR.getMin(toCheck.getSecondaryColor(), (int) checking.getTag(COLOR_SECONDARY_MIN))
            != (int) checking.getTag(COLOR_SECONDARY_MIN)) {
            meets = false;
        }
        if (COLOR.getMax(toCheck.getSecondaryColor(), (int) checking.getTag(COLOR_PRIMARY_MAX))
            != (int) checking.getTag(COLOR_PRIMARY_MAX)) {
            meets = false;
        }
        return meets;
    }

    public static boolean doesMobFitSpecies(IGeneticMob toCheck, Species checking, float percentage) {
        if (percentage < 0F || percentage > 1F) percentage = 1F;
        boolean meets = true;
        float maturityRange = (float) (checking.getMaxMaturity() - checking.getMinMaturity());
        if (toCheck.getMaturity() > checking.getMinMaturity() + maturityRange * percentage
            || toCheck.getMaturity() < checking.getMaxMaturity() - maturityRange * percentage) {
            meets = false;
        }
        float fertilityRange = (float) (checking.getMaxFertility() - checking.getMinFertility());
        if (toCheck.getFertility() > checking.getMinFertility() + fertilityRange * percentage
            || toCheck.getFertility() < checking.getMaxFertility() - fertilityRange * percentage) {
            meets = false;
        }
        float sizeRange = (float) (checking.getMaxSize() - checking.getMinSize());
        if (toCheck.getSize() > checking.getMinSize() + sizeRange * percentage
            || toCheck.getSize() < checking.getMaxSize() - sizeRange * percentage) {
            meets = false;
        }
        float strengthRange = (float) (checking.getMaxStrength() - checking.getMinStrength());
        if (toCheck.getStrength() > checking.getMinStrength() + strengthRange * percentage
            || toCheck.getMaturity() < checking.getMaxStrength() - strengthRange * percentage) {
            meets = false;
        }
        float mutableRange = (float) (checking.getMaxMutable() - checking.getMinMutable());
        if (toCheck.getMutable() > checking.getMinMutable() + mutableRange * percentage
            || toCheck.getMutable() < checking.getMaxMutable() - mutableRange * percentage) {
            meets = false;
        }
        float smartRange = (float) (checking.getMaxSmart() - checking.getMinSmart());
        if (toCheck.getSmart() > checking.getMinSmart() + smartRange * percentage
            || toCheck.getSmart() < checking.getMaxSmart() - smartRange * percentage) {
            meets = false;
        }
        float snarlRange = (float) (checking.getMaxSnarl() - checking.getMinSnarl());
        if (toCheck.getSnarl() > checking.getMinSnarl() + snarlRange * percentage
            || toCheck.getSnarl() < checking.getMaxSnarl() - snarlRange * percentage) {
            meets = false;
        }
        float staminaRange = (float) (checking.getMaxStamina() - checking.getMinStamina());
        if (toCheck.getStamina() > checking.getMinStamina() + staminaRange * percentage
            || toCheck.getStamina() < checking.getMaxStamina() - staminaRange * percentage) {
            meets = false;
        }
        return meets;
    }

    public static void createOffspring(IGeneticMob baby, IGeneticMob daddy, IGeneticMob mommy) {
        Random r = CS.RNGSUS;
        if (r.nextBoolean()) {
            baby.setSpeciesID(mommy.getSpeciesID());
            baby.setSubtypeID(mommy.getSubtypeID());
        } else {
            baby.setSpeciesID(daddy.getSpeciesID());
            baby.setSubtypeID(daddy.getSubtypeID());
        }
        Species species = MobSpeciesRegistry.getSpecies(baby.getClass(), baby.getSpeciesID());

        if (r.nextBoolean()) {
            baby.setMutable(
                (short) Math.max(
                    species.getMinMutable(),
                    (short) Math.min(
                        species.getMaxMutable(),
                        (short) (mommy.getMutable() + (r.nextInt(daddy.getMutable()) - (daddy.getMutable() * 0.5))))));
        } else {
            baby.setMutable(
                (short) Math.max(
                    species.getMinMutable(),
                    (short) Math.min(
                        species.getMaxMutable(),
                        (short) (daddy.getMutable() + (r.nextInt(mommy.getMutable()) - (mommy.getMutable() * 0.5))))));
        }

        short mutable = baby.getMutable();

        if (r.nextBoolean()) {
            baby.setSize(
                (short) Math.max(
                    species.getMinSize(),
                    (short) Math.min(
                        species.getMaxSize(),
                        (short) (mommy.getSize() + (r.nextInt(mutable) - (mutable * 0.5))))));
        } else {
            baby.setSize(
                (short) Math.max(
                    species.getMinSize(),
                    (short) Math.min(
                        species.getMaxSize(),
                        (short) (daddy.getSize() + (r.nextInt(mutable) - (mutable * 0.5))))));
        }

        if (r.nextBoolean()) {
            baby.setStrength(
                (short) Math.max(
                    species.getMinStrength(),
                    (short) Math.min(
                        species.getMaxStrength(),
                        (short) (mommy.getStrength() + (r.nextInt(mutable) - (mutable * 0.5))))));
        } else {
            baby.setStrength(
                (short) Math.max(
                    species.getMinStrength(),
                    (short) Math.min(
                        species.getMaxStrength(),
                        (short) (daddy.getStrength() + (r.nextInt(mutable) - (mutable * 0.5))))));
        }

        if (r.nextBoolean()) {
            baby.setFertility(
                (short) Math.max(
                    species.getMinFertility(),
                    (short) Math.min(
                        species.getMaxFertility(),
                        (short) (mommy.getFertility() + (r.nextInt(mutable) - (mutable * 0.5))))));
        } else {
            baby.setFertility(
                (short) Math.max(
                    species.getMinFertility(),
                    (short) Math.min(
                        species.getMaxFertility(),
                        (short) (daddy.getFertility() + (r.nextInt(mutable) - (mutable * 0.5))))));
        }

        if (r.nextBoolean()) {
            baby.setMaturity(
                (short) Math.max(
                    species.getMinMaturity(),
                    (short) Math.min(
                        species.getMaxMaturity(),
                        (short) (mommy.getMaturity() + (r.nextInt(mutable) - (mutable * 0.5))))));
        } else {
            baby.setMaturity(
                (short) Math.max(
                    species.getMinMaturity(),
                    (short) Math.min(
                        species.getMaxMaturity(),
                        (short) (daddy.getMaturity() + (r.nextInt(mutable) - (mutable * 0.5))))));
        }

        if (r.nextBoolean()) {
            baby.setSmart(
                (short) Math.max(
                    species.getMinSmart(),
                    (short) Math.min(
                        species.getMaxSmart(),
                        (short) (mommy.getSmart() + (r.nextInt(mutable) - (mutable * 0.5))))));
        } else {
            baby.setSmart(
                (short) Math.max(
                    species.getMinSmart(),
                    (short) Math.min(
                        species.getMaxSmart(),
                        (short) (daddy.getSmart() + (r.nextInt(mutable) - (mutable * 0.5))))));
        }

        if (r.nextBoolean()) {
            baby.setSnarl(
                (short) Math.max(
                    species.getMinSnarl(),
                    (short) Math.min(
                        species.getMaxSnarl(),
                        (short) (mommy.getSnarl() + (r.nextInt(mutable) - (mutable * 0.5))))));
        } else {
            baby.setSnarl(
                (short) Math.max(
                    species.getMinSnarl(),
                    (short) Math.min(
                        species.getMaxSnarl(),
                        (short) (daddy.getSnarl() + (r.nextInt(mutable) - (mutable * 0.5))))));
        }

        if (r.nextBoolean()) {
            baby.setStamina(
                (short) Math.max(
                    species.getMinStamina(),
                    (short) Math.min(
                        species.getMaxStamina(),
                        (short) (mommy.getStamina() + (r.nextInt(mutable) - (mutable * 0.5))))));
        } else {
            baby.setStamina(
                (short) Math.max(
                    species.getMinStamina(),
                    (short) Math.min(
                        species.getMaxStamina(),
                        (short) (daddy.getStamina() + (r.nextInt(mutable) - (mutable * 0.5))))));
        }

        int c1 = mommy.getPrimaryColor();
        int c2 = daddy.getPrimaryColor();
        int min = COLOR.getMin(c1, c2);
        int max = COLOR.getMax(c1, c2);
        baby.setPrimaryColor(COLOR.getRandom(min, max));

        c1 = mommy.getSecondaryColor();
        c2 = daddy.getSecondaryColor();
        min = COLOR.getMin(c1, c2);
        max = COLOR.getMax(c1, c2);
        baby.setSecondaryColor(COLOR.getRandom(min, max));

        if (!doesMobFitSpecies(baby, species, 0.83F)) {
            Species[] totalPossible = MobSpeciesRegistry.getSpeciesList(baby.getClass());
            for (short q = 0; q < totalPossible.length; q++) {
                if (totalPossible[q] != null && r.nextInt(Short.MAX_VALUE) < mutable
                    && doesMobFitSpecies(baby, totalPossible[q], 0.75F)) {
                    baby.setSpeciesID(q);
                    baby.setSubtypeID((short) 0);
                    break;
                }
            }
        }

        /* this part is to make sure we can fit *any* subtype if we've mutated */
        if (!doesMobFitSubtype(baby, species.getSubtype(baby.getSubtypeID()), 1F)) {
            Subtype[] totalPossible = species.subtypes;
            for (short q = 0; q < totalPossible.length; q++) {
                if (totalPossible[q] != null && doesMobFitSubtype(baby, totalPossible[q], 1F)) {
                    baby.setSubtypeID(q);
                    break;
                }
            }
        }

        if (!doesMobFitSubtype(baby, species.getSubtype(baby.getSubtypeID()), 0.83F)) {
            Subtype[] totalPossible = species.subtypes;
            for (short q = 0; q < totalPossible.length; q++) {
                if (totalPossible[q] != null && r.nextInt(Short.MAX_VALUE) < mutable
                    && doesMobFitSubtype(baby, totalPossible[q], 0.75F)) {
                    baby.setSubtypeID(q);
                    break;
                }
            }
        }
    }

}
