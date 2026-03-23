package com.hit13alert;

import com.google.inject.Provides;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Hitsplat;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Lulinha",
	description = "Shows overhead text when a player deals a hitsplat of 13",
	tags = {"hit", "hitsplat", "alert", "13"}
)
public class Hit13AlertPlugin extends Plugin
{
	private static final int TRIGGER_AMOUNT = 13;

	@Inject
	private Hit13AlertConfig config;

	@Inject
	private ScheduledExecutorService executor;

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event)
	{
		Hitsplat hitsplat = event.getHitsplat();

		if (hitsplat.getAmount() != TRIGGER_AMOUNT)
		{
			return;
		}

		// Only trigger on player-dealt hitsplats (isMine = local player, isOthers = other players)
		if (!hitsplat.isMine() && !hitsplat.isOthers())
		{
			return;
		}

		if (!config.textEnabled())
		{
			return;
		}

		Actor actor = event.getActor();
		actor.setOverheadText(config.overheadText());

		executor.schedule(() -> actor.setOverheadText(""), 5, TimeUnit.SECONDS);
	}

	@Provides
	Hit13AlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Hit13AlertConfig.class);
	}
}
