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
		keyName = "overheadText",
		name = "Overhead Text",
		description = "The text displayed above the target"
	)
	default String overheadText()
	{
		return "Agora que vai entrar o grosso";
	}
}
