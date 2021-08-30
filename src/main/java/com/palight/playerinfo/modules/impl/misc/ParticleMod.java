package com.palight.playerinfo.modules.impl.misc;

import com.palight.playerinfo.gui.screens.impl.options.modules.misc.ParticleGui;
import com.palight.playerinfo.modules.Module;
import com.palight.playerinfo.options.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticleMod extends Module {

    @ConfigOption
    public String selectedParticle = "crit";

    @ConfigOption
    public int multiplier = 1;

    @ConfigOption
    public boolean forceSharp = false;

    @ConfigOption
    public boolean forceCrit = false;

    private EntityPlayer player;
    private World world;

    public ParticleMod() {
        super("particle", ModuleType.MISC, new ParticleGui(), null);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!this.isEnabled()) return;
        player = Minecraft.getMinecraft().thePlayer;
        world = Minecraft.getMinecraft().theWorld;
        Entity entity = event.target;

        doRawCrit(entity);

        if (forceSharp) {
            doSharp(entity);
        } else {
            doRawSharp(entity);
        }
    }

    private void doRawCrit(Entity entity) {
        if (player.onGround && !this.forceCrit) return;
        attemptParticleSpawn(EnumParticleTypes.CRIT, entity.posX, entity.posY, entity.posZ, multiplier);
    }

    private void doSharp(Entity entity) {
        attemptParticleSpawn(EnumParticleTypes.CRIT_MAGIC, entity.posX, entity.posY, entity.posZ, multiplier);
    }

    private void doRawSharp(Entity entity) {
        ItemStack mainItem = player.getHeldItem();
        if (mainItem == null) return;
        Item item = mainItem.getItem();
        if (item instanceof ItemSword) {
            ItemSword sword = ((ItemSword) item);
            if (sword.hasEffect(mainItem)) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, mainItem) > 0) {
                    attemptParticleSpawn(EnumParticleTypes.CRIT_MAGIC, entity.posX, entity.posY, entity.posZ, multiplier);
                }
            }
        }
    }

    private void attemptParticleSpawn(EnumParticleTypes type, double rawX, double rawY, double rawZ, int multiplier) {
        final double x = rawX + 0.25;
        final double y = rawY + 0.4;
        final double z = rawZ + 0.25;
        if (world != null) {
            for (int i = 0; i < 20 * multiplier; i++) {
                final double xOffset = Math.random() * 2.0 - 1.3;
                final double yOffset = Math.random() * 2.0;
                final double zOffset = Math.random() * 2.0 - 1.3;

                world.spawnParticle(type, x, y, z, xOffset, yOffset, zOffset);
            }
        }
    }
}
