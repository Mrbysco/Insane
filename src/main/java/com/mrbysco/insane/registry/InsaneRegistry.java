package com.mrbysco.insane.registry;

import com.mrbysco.insane.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InsaneRegistry {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

	public static final RegistryObject<SoundEvent> MUSICBOX = SOUND_EVENTS.register("musicbox", () ->
			new SoundEvent(new ResourceLocation(Reference.MOD_ID, "musicbox")));
}
