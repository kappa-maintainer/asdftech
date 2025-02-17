package com.kbi.qwertech.entities.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.MathHelper;

public class EntityMoveHelperJumpy extends EntityMoveHelper {

    /** The EntityLiving that is being moved */
    private EntityLiving entity;
    private double posX;
    private double posY;
    private double posZ;
    /** The speed at which the entity should move */
    private double speed;
    private boolean update;

    public EntityMoveHelperJumpy(EntityLiving p_i1614_1_) {
        super(p_i1614_1_);
        this.entity = p_i1614_1_;
        this.posX = p_i1614_1_.posX;
        this.posY = p_i1614_1_.posY;
        this.posZ = p_i1614_1_.posZ;
    }

    @Override
    public boolean isUpdating() {
        return this.update;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Sets the speed and location to move to
     */
    @Override
    public void setMoveTo(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_) {
        this.posX = p_75642_1_;
        this.posY = p_75642_3_;
        this.posZ = p_75642_5_;
        this.speed = p_75642_7_;
        this.update = true;
    }

    @Override
    public void onUpdateMoveHelper() {
        if (this.update && !this.entity.isAirBorne) {
            this.entity.setMoveForward(0.0F);
            this.update = false;
            int i = MathHelper.floor_double(this.entity.boundingBox.minY + 0.5D);
            double d0 = this.posX - this.entity.posX;
            double d1 = this.posZ - this.entity.posZ;
            double d2 = this.posY - i;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 >= 2.500000277905201E-7D) {
                float f = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 30.0F);
                this.entity.setAIMoveSpeed(
                    (float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                        .getAttributeValue()));
                this.entity.getJumpHelper()
                    .setJumping();
                this.entity.getJumpHelper()
                    .doJump();
            }
        }
    }

    /**
     * Limits the given angle to a upper and lower limit.
     */
    @SuppressWarnings("SameParameterValue")
    private float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float f3 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);

        if (f3 > p_75639_3_) {
            f3 = p_75639_3_;
        }

        if (f3 < -p_75639_3_) {
            f3 = -p_75639_3_;
        }

        return p_75639_1_ + f3;
    }
}
