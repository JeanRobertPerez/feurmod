package com.mod;

import net.fabricmc.api.ModInitializer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mod.events.CommonEvents;
import com.mod.registries.RegisterItems;

public class FeurMod implements ModInitializer {
	public static final String MOD_ID = "feurmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final UUID FEURMODPVP = UUID.fromString("0783eb9b-8a8d-4184-8ff9-68255f3e48d1");
	public static final float AXE_ATT_SPEED = 4.6F;

	@Override
	public void onInitialize() 
	{
		RegisterItems.initialize();
		CommonEvents.register();
		LOGGER.info("putain t moch !");
	}
}