package com.hit13alert;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Hit13AlertPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Hit13AlertPlugin.class);
		RuneLite.main(args);
	}
}
