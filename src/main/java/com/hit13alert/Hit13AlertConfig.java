package com.hit13alert;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("hit13alert")
public interface Hit13AlertConfig extends Config
{
	@ConfigItem(
		keyName = "soundEnabled",
		name = "Play Sound",
		description = "Play an audio alert when the trigger hit lands"
	)
	default boolean soundEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "textEnabled",
		name = "Show Overhead Text",
		description = "Show overhead text on the hit target"
	)
	default boolean textEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "triggerAmount",
		name = "Trigger Hit Value",
		description = "The damage number that triggers the alert"
	)
	default int triggerAmount()
	{
		return 13;
	}

	@ConfigItem(
		keyName = "overheadText",
		name = "Overhead Text",
		description = "The text displayed above the target"
	)
	default String overheadText()
	{
		return "Calma companheiro";
	}
}
