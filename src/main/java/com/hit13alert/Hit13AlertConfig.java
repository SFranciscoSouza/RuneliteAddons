package com.hit13alert;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("hit13alert")
public interface Hit13AlertConfig extends Config
{
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
		keyName = "chatEnabled",
		name = "Show Chat Message",
		description = "Also post the alert to your local chat box"
	)
	default boolean chatEnabled()
	{
		return true;
	}
}
