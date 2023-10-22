package com.mrbysco.insane.client;

import com.mrbysco.insane.Insane;
import com.mrbysco.insane.Reference;
import com.mrbysco.insane.api.capability.ISanity;
import com.mrbysco.insane.handler.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.sql.Ref;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OverlayHandler {
	private static final ResourceLocation SANITY_OVERLAY = new ResourceLocation(Reference.MOD_ID, "sanity");
	private static final ResourceLocation SANITY_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/insanity.png");

	private static final ResourceLocation DESATURATION_SHADER = new ResourceLocation("shaders/post/desaturate.json");

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerOverlayRenderer(RegisterGuiOverlaysEvent event) {
		event.registerAboveAll("sanity", OverlayHandler::draw);
		Insane.LOGGER.error("Registered sanity overlay");
	}

	private static void draw(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		Entity cameraEntity = Minecraft.getInstance().cameraEntity;
		if (cameraEntity instanceof Player player) {
			int left = screenWidth / 2 + 91;
			int top = screenHeight - gui.rightHeight;

			LazyOptional<ISanity> sanityCap = player.getCapability(CapabilityHandler.SANITY_CAPABILITY);
			if (sanityCap.isPresent()) {
				ISanity sanity = sanityCap.orElse(null);
				if (sanity != null) {
//					System.out.println(sanity.getSanityMin());
//					System.out.println(sanity.getSanityMax());
					drawSanity(guiGraphics, sanity);
					guiGraphics.drawString(gui.getFont(), String.valueOf(sanity.getSanity()), left, top, 553648127, false);
				}
			}
		}
	}

	private static void drawSanity(GuiGraphics guiGraphics, ISanity sanity) {
		if (sanity == null) {
			return;
		}

		final Minecraft mc = Minecraft.getInstance();

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

		guiGraphics.blit(SANITY_TEXTURES, (int) scaleFactor * 20, (int) scaleFactor * 20, (int) (32 * scaleFactor), (int) (0 * scaleFactor),
				(int) (24 * scaleFactor), (int) (24 * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));

		guiGraphics.blit(SANITY_TEXTURES, (int) scaleFactor * 20, (int) (scaleFactor * (20 + Math.ceil((24 - 24 * pct)))), (int) (32 * scaleFactor), (int) (24 * scaleFactor),
				(int) (24 * scaleFactor), (int) (24 * pct * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));

		guiGraphics.blit(SANITY_TEXTURES, (int) scaleFactor * 20, (int) scaleFactor * 20, (int) (80 * scaleFactor), (int) (brainStage * scaleFactor),
				(int) (24 * scaleFactor), (int) (24 * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));

		guiGraphics.blit(SANITY_TEXTURES, (int) scaleFactor * 16, (int) scaleFactor * 16, 0, 0,
				(int) (32 * scaleFactor), (int) (32 * scaleFactor),
				(int) (256 * scaleFactor), (int) (256 * scaleFactor));
	}
}
