package com.kbi.qwertech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kbi.qwertech.api.armor.MultiItemArmor;
import com.kbi.qwertech.api.armor.upgrades.IArmorUpgrade;

public class QT_Armor_Renderer implements IItemRenderer {

    RenderItem RI;

    public QT_Armor_Renderer() {
        RI = new RenderItem();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        // TODO Auto-generated method stub
        return false;
    }

    public void renderPlainItem(ItemStack p_78443_2_, IIcon iicon, ItemRenderType type) {

        if (iicon == null) return;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        Minecraft.getMinecraft().renderEngine
            .bindTexture(Minecraft.getMinecraft().renderEngine.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
        TextureUtil.func_152777_a(false, false, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        float f = iicon.getMinU();
        float f1 = iicon.getMaxU();
        float f2 = iicon.getMinV();
        float f3 = iicon.getMaxV();
        float f4 = 0.0F;
        float f5 = 0.3F;
        float f6 = 1.5F;
        if (type != ItemRenderType.INVENTORY) {
            ItemRenderer
                .renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
        } else {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            RI.renderIcon(0, 0, iicon, 16, 16);
            // GL11.glDisable(GL11.GL_BLEND);
            // GL11.glDisable(GL11.GL_ALPHA_TEST);
        }
        Minecraft.getMinecraft().renderEngine
            .bindTexture(Minecraft.getMinecraft().renderEngine.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
        TextureUtil.func_147945_b();

        // GL11.glDisable(GL12.GL_RESCALE_NORMAL);

    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        // GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < item.getItem()
            .getRenderPasses(item.getItemDamage()); i++) {
            int j = item.getItem()
                .getColorFromItemStack(item, i);
            float f5 = (j >> 16 & 255) / 255.0F;
            float f2 = (j >> 8 & 255) / 255.0F;
            float f3 = (j & 255) / 255.0F;
            GL11.glColor4f(f5, f2, f3, 1.0F);
            if (data.length > 1 && data[1] != null && data[1] instanceof EntityLivingBase) {
                renderPlainItem(item, ((EntityLivingBase) data[1]).getItemIcon(item, i), type);
            } else {
                renderPlainItem(
                    item,
                    item.getItem()
                        .getIcon(item, i),
                    type);
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        IArmorUpgrade[] upgrades = MultiItemArmor.getUpgrades(item);
        for (int q = 0; q < upgrades.length; q++) {
            IArmorUpgrade upgrade = upgrades[q];
            if (upgrade != null && upgrade.getRenderPasses() > 0) {
                for (int w = 0; w < upgrade.getRenderPasses(); w++) {

                    try {
                        short[] color = upgrade.getRGBa(item, w);
                        GL11.glColor4f(
                            ((float) color[0]) / 255F,
                            ((float) color[1]) / 255F,
                            ((float) color[2]) / 255F,
                            1);

                        IIcon icon = upgrade.getIcon(item, w);
                        if (icon != null) {
                            renderPlainItem(item, icon, type);
                        }
                    } catch (Throwable ignored) {
                        ignored.printStackTrace();
                    }
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        // GL11.glPopMatrix();

    }

}
