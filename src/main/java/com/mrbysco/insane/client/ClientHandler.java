package com.mrbysco.insane.client;

import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ClientHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HEALTH)
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;

        int left = mc.getMainWindow().getScaledWidth() / 2 + 91;
        int top = mc.getMainWindow().getScaledHeight() - ForgeIngameGui.right_height;

        LazyOptional<ISanity> sanityCap = player.getCapability(SanityCapProvider.SANITY_CAPABILITY, null);
        if(sanityCap.isPresent()) {
            ISanity sanity = sanityCap.orElseThrow(NullPointerException::new);

            mc.ingameGUI.drawString(mc.fontRenderer, String.valueOf(sanityRounded(sanity.getSanity())), left, top,553648127);
        }
    }

    public double sanityRounded(double originalSanity) {
        BigDecimal bd = BigDecimal.valueOf(originalSanity);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
