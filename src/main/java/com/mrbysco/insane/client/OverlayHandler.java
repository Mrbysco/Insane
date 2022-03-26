package com.mrbysco.insane.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.insane.Reference;
import com.mrbysco.insane.api.capability.ISanity;
import com.mrbysco.insane.handler.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OverlayHandler {
	private ResourceLocation SANITY_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/insanity.png");

	private ResourceLocation DESATURATION_SHADER = new ResourceLocation("shaders/post/desaturate.json");

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPreRender(RenderGameOverlayEvent.Pre event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		PoseStack matrixStack = event.getMatrixStack();
		if (player != null && mc.gui instanceof ForgeIngameGui ingameGui) {
			int left = mc.getWindow().getGuiScaledWidth() / 2 + 91;
			int top = mc.getWindow().getGuiScaledHeight() - ingameGui.right_height;

			LazyOptional<ISanity> sanityCap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY);
			if (sanityCap.isPresent()) {
				ISanity sanity = sanityCap.orElse(null);
				if (sanity != null) {
					System.out.println(sanity.getSanityMin());
					System.out.println(sanity.getSanityMax());
					drawSanity(matrixStack, sanity);
					mc.font.draw(matrixStack, String.valueOf(sanity.getSanity()), left, top, 553648127);
				}
			}
		}
	}

	private void drawSanity(PoseStack matrixStack, ISanity sanity) {
		if (sanity == null) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, SANITY_TEXTURES);

		double currentSanity = sanity.getSanity();
		double capacity = sanity.getSanityMax();
		double pct = Math.min(currentSanity / capacity, 1.0F);

		if (pct <= 0.2D) {
			boolean flag = mc.gameRenderer.currentEffect() != null && mc.gameRenderer.currentEffect().getName().equals(DESATURATION_SHADER.toString());
			if (!flag && !mc.player.getPersistentData().getBoolean("IsInsane")) {
				if (mc.gameRenderer.currentEffect() != null) {
					mc.gameRenderer.shutdownEffect();
				}
				mc.gameRenderer.loadEffect(DESATURATION_SHADER);
				mc.player.getPersistentData().putBoolean("IsInsane", true);
			}
		} else {
			if (mc.gameRenderer.currentEffect() != null && mc.player.getPersistentData().getBoolean("IsInsane")) {
				mc.player.getPersistentData().remove("IsInsane");
				mc.gameRenderer.shutdownEffect();
			}
		}

		int brainStage = 0;
		if (pct <= 0.33D) {
			brainStage = 48;
		} else if (pct <= 0.66D) {
			brainStage = 24;
		}

		double scaleFactor = mc.getWindow().getGuiScale();
		if (mc.getWindow().getGuiScale() > 2.0D) {
			scaleFactor = 1.0D;
		}

		Screen.blit(matrixStack, (int) scaleFactor * 20, (int) scaleFactor * 20, (int) (32 * scaleFactor), (int) (0 * scaleFactor),
				(int) (24 * scaleFactor), (int) (24 * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));

		Screen.blit(matrixStack, (int) scaleFactor * 20, (int) (scaleFactor * (20 + Math.ceil((24 - 24 * pct)))), (int) (32 * scaleFactor), (int) (24 * scaleFactor),
				(int) (24 * scaleFactor), (int) (24 * pct * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));

		Screen.blit(matrixStack, (int) scaleFactor * 20, (int) scaleFactor * 20, (int) (80 * scaleFactor), (int) (brainStage * scaleFactor),
				(int) (24 * scaleFactor), (int) (24 * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));

		Screen.blit(matrixStack, (int) scaleFactor * 16, (int) scaleFactor * 16, 0, 0,
				(int) (32 * scaleFactor), (int) (32 * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));
	}
}
