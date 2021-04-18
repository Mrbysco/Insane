package com.mrbysco.insane.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.insane.Reference;
import com.mrbysco.insane.capability.ISanity;
import com.mrbysco.insane.capability.SanityCapProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientHandler {
    private ResourceLocation SANITY_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/insanity.png");

    private ResourceLocation DESATURATION_SHADER = new ResourceLocation("shaders/post/desaturate.json");

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreRender(RenderGameOverlayEvent.Pre event) {
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

                drawSanity(matrixStack, sanity);
                mc.fontRenderer.drawString(matrixStack, String.valueOf(sanity.getSanity()), left, top, 553648127);
            }
        }
    }

    private void drawSanity(MatrixStack matrixStack, ISanity sanity) {
        if(sanity == null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        mc.textureManager.bindTexture(SANITY_TEXTURES);

        double currentSanity = sanity.getSanity();
        double capacity = sanity.getSanityMax();
        double pct = Math.min(currentSanity / capacity, 1.0F);

        if(pct <= 0.2D) {
            boolean flag = mc.gameRenderer.getShaderGroup() != null && mc.gameRenderer.getShaderGroup().getShaderGroupName().equals(DESATURATION_SHADER.toString());
            if(!flag && !mc.player.getPersistentData().getBoolean("IsInsane")) {
                if (mc.gameRenderer.getShaderGroup() != null) {
                    mc.gameRenderer.stopUseShader();
                }
                mc.gameRenderer.loadShader(DESATURATION_SHADER);
                mc.player.getPersistentData().putBoolean("IsInsane", true);
            }
        } else {
            if (mc.gameRenderer.getShaderGroup() != null && mc.player.getPersistentData().getBoolean("IsInsane")) {
                mc.player.getPersistentData().remove("IsInsane");
                mc.gameRenderer.stopUseShader();
            }
        }

        int brainStage = 0;
        if(pct <= 0.33D) {
            brainStage = 48;
        } else if(pct <= 0.66D) {
            brainStage = 24;
        }

        double scaleFactor = mc.getMainWindow().getGuiScaleFactor();
        if(mc.getMainWindow().getGuiScaleFactor() > 2.0D) {
            scaleFactor = 1.0D;
        }

        Screen.blit(matrixStack, (int)scaleFactor * 20, (int)scaleFactor * 20, (int)(32 * scaleFactor), (int)(0 * scaleFactor),
                (int)(24 * scaleFactor), (int)(24 * scaleFactor),
                (int)(256 * scaleFactor), (int)(256 * scaleFactor));

        Screen.blit(matrixStack, (int)scaleFactor * 20, (int)(scaleFactor * (20 + Math.ceil((24 - 24 * pct)))), (int)(32 * scaleFactor), (int)(24 * scaleFactor),
                (int)(24 * scaleFactor), (int)(24 * pct * scaleFactor),
                (int)(256 * scaleFactor), (int)(256 * scaleFactor));

        Screen.blit(matrixStack, (int)scaleFactor * 20, (int)scaleFactor * 20, (int)(80 * scaleFactor), (int)(brainStage * scaleFactor),
                (int)(24 * scaleFactor), (int)(24 * scaleFactor),
                (int)(256 * scaleFactor), (int)(256 * scaleFactor));

        Screen.blit(matrixStack, (int)scaleFactor * 16, (int)scaleFactor * 16, 0, 0,
                (int)(32 * scaleFactor), (int)(32 * scaleFactor),
                (int)(256 * scaleFactor), (int)(256 * scaleFactor));
    }
}
