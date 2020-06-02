package com.mrbysco.insane.handler;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.Reference;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import com.mrbysco.insane.capability.SanityCapability;
import com.mrbysco.insane.packets.SanitySyncMessage;
import com.mrbysco.insane.util.SanityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.Random;

public class CapabilityHandler {
    @SubscribeEvent
    public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Reference.SANITY_CAP), new SanityCapProvider());
        }
    }

    @SubscribeEvent
    public void playerStartTracking(PlayerEvent.StartTracking event) {
        if(!event.getPlayer().world.isRemote) {
            PlayerEntity player = event.getPlayer();
            LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
            if(sanityCap.isPresent()) {
                Insane.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new SanitySyncMessage(sanityCap.orElse(new SanityCapability()), player.getUniqueID()));
            }
        }
    }

    @SubscribeEvent
    public void entityJoinWorldEvent(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof PlayerEntity && !event.getWorld().isRemote) {
            World world = event.getWorld();
            PlayerEntity joiningPlayer = (PlayerEntity)event.getEntity();
            BlockPos playerPos = joiningPlayer.getPosition();

            AxisAlignedBB hitbox = new AxisAlignedBB(playerPos.getX() - 0.5f, playerPos.getY() - 0.5f, playerPos.getZ() - 0.5f,
                    playerPos.getX() + 0.5f, playerPos.getY() + 0.5f, playerPos.getZ() + 0.5f)
                    .expand(-5, -5, -5).expand(5, 5, 5);

            List<PlayerEntity> nearbyPlayers = world.getEntitiesWithinAABB(PlayerEntity.class, hitbox);
            for(PlayerEntity player : nearbyPlayers) {
                LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
                if(sanityCap.isPresent()) {
                    Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) joiningPlayer), new SanitySyncMessage(sanityCap.orElse(new SanityCapability()), player.getUniqueID()));
                }
            }
        }
    }

    @SubscribeEvent
    public void playerTickEvent(PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START)
            return;

        World world = event.player.world;
        if(!world.isRemote && world.getGameTime() % 20 == 0) {
            SanityUtil.addSanity(event.player, getRandDouble(world.rand));
        }
    }

    public double getRandDouble(Random rand) {
        if(rand.nextBoolean()) {
            return -rand.nextDouble();
        } else {
            return rand.nextDouble();
        }
    }
}
