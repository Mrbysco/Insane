package com.mrbysco.insane.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        MatrixStack matrixStack = event.getMatrixStack();
        if(player != null) {
            int left = mc.getMainWindow().getScaledWidth() / 2 + 91;
            int top = mc.getMainWindow().getScaledHeight() - ForgeIngameGui.right_height;

            LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY);
            if(sanityCap.isPresent()) {
                ISanity sanity = sanityCap.orElseThrow(NullPointerException::new);

                mc.fontRenderer.drawString(matrixStack, String.valueOf(sanity.getSanity()), left, top, 553648127);
            }
        }
    }
}
