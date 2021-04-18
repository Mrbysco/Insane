package com.mrbysco.insane.handler;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.Reference;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import com.mrbysco.insane.packets.SanitySyncMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public class CapabilityHandler {
    @SubscribeEvent
    public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof PlayerEntity) {
            event.addCapability(Reference.SANITY_CAP, new SanityCapProvider());
        }
    }

    @SubscribeEvent
    public void playerStartTracking(PlayerEvent.StartTracking event) {
        if(event.getPlayer() instanceof ServerPlayerEntity) {
            PlayerEntity player = event.getPlayer();
            LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
            sanityCap.ifPresent(c -> Insane.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new SanitySyncMessage(c, player.getUniqueID())));
        }
    }

    @SubscribeEvent
    public void entityJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !event.getWorld().isRemote) {
            World world = event.getWorld();
            PlayerEntity joiningPlayer = (PlayerEntity) event.getEntity();
            BlockPos playerPos = joiningPlayer.getPosition();

            AxisAlignedBB hitbox = new AxisAlignedBB(playerPos.getX() - 0.5f, playerPos.getY() - 0.5f, playerPos.getZ() - 0.5f,
                    playerPos.getX() + 0.5f, playerPos.getY() + 0.5f, playerPos.getZ() + 0.5f)
                    .expand(-5, -5, -5).expand(5, 5, 5);

            //Send the joining player their own Sanity to their client
            LazyOptional<ISanity> playerCap = joiningPlayer.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
            playerCap.ifPresent(c -> Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) joiningPlayer), new SanitySyncMessage(c, joiningPlayer.getUniqueID())));
            //Send the joining player the sanity of nearby players
            List<PlayerEntity> nearbyPlayers = world.getEntitiesWithinAABB(PlayerEntity.class, hitbox);
            for (PlayerEntity player : nearbyPlayers) {
                LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
                sanityCap.ifPresent(c -> Insane.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) joiningPlayer), new SanitySyncMessage(c, player.getUniqueID())));
            }
        }
    }
}
