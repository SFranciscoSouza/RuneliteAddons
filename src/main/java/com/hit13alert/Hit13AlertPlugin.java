package com.hit13alert;

import com.google.inject.Provides;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Hitsplat;
import net.runelite.api.NPC;
import net.runelite.api.Player;
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
	private static final String OVERHEAD_PREFIX = "Calma Companheiro";

	@Inject
	private Client client;

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

		if (!config.textEnabled() && !config.chatEnabled())
		{
			return;
		}

		Actor target = event.getActor();
		Player attacker = resolvePlayerAttacker(target, hitsplat);

		if (attacker == null || attacker.getName() == null)
		{
			return;
		}

		String msg = OVERHEAD_PREFIX + " " + attacker.getName();

		if (config.textEnabled())
		{
			target.setOverheadText(msg);
			executor.schedule(() -> target.setOverheadText(""), 5, TimeUnit.SECONDS);
		}

		if (config.chatEnabled())
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null);
		}
	}

	private Player resolvePlayerAttacker(Actor target, Hitsplat hitsplat)
	{
		if (target instanceof NPC)
		{
			if (hitsplat.isMine())
			{
				return client.getLocalPlayer();
			}
			if (hitsplat.isOthers())
			{
				return findPlayerInteractingWith(target);
			}
			return null;
		}

		if (target instanceof Player)
		{
			Actor interacting = target.getInteracting();
			if (interacting instanceof Player)
			{
				return (Player) interacting;
			}
			return findPlayerInteractingWith(target);
		}

		return null;
	}

	private Player findPlayerInteractingWith(Actor target)
	{
		for (Player p : client.getPlayers())
		{
			if (p != null && p.getInteracting() == target)
			{
				return p;
			}
		}
		return null;
	}

	@Provides
	Hit13AlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Hit13AlertConfig.class);
	}
}
