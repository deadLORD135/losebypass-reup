/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemAppleGold
extends ItemFood {
    public ItemAppleGold(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.getMetadata() <= 0) return false;
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        EnumRarity enumRarity;
        if (stack.getMetadata() == 0) {
            enumRarity = EnumRarity.RARE;
            return enumRarity;
        }
        enumRarity = EnumRarity.EPIC;
        return enumRarity;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            player.addPotionEffect(new PotionEffect(Potion.absorption.id, 2400, 0));
        }
        if (stack.getMetadata() > 0) {
            if (worldIn.isRemote) return;
            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 4));
            player.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
            player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
            return;
        }
        super.onFoodEaten(stack, worldIn, player);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }
}

