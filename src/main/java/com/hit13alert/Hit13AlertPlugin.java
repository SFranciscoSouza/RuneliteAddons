package com.hit13alert;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	description = "Plays a sound and shows overhead text when a player deals a hitsplat of 13",
	tags = {"hit", "hitsplat", "sound", "alert", "13", "calma"}
)
public class Hit13AlertPlugin extends Plugin
{
	@Inject
	private Hit13AlertConfig config;

	private Clip clip;

	@Override
	protected void startUp() throws Exception
	{
		loadSound();
	}

	@Override
	protected void shutDown() throws Exception
	{
		if (clip != null)
		{
			clip.close();
			clip = null;
		}
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event)
	{
		Hitsplat hitsplat = event.getHitsplat();

		if (hitsplat.getAmount() != config.triggerAmount())
		{
			return;
		}

		// Only trigger on player-dealt hitsplats (isMine = local player, isOthers = other players)
		if (!hitsplat.isMine() && !hitsplat.isOthers())
		{
			return;
		}

		Actor actor = event.getActor();

		if (config.textEnabled())
		{
			actor.setOverheadText(config.overheadText());
		}

		if (config.soundEnabled())
		{
			playSound();
		}
	}

	@Provides
	Hit13AlertConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Hit13AlertConfig.class);
	}

	private void loadSound()
	{
		try (InputStream stream = getClass().getResourceAsStream("/com/hit13alert/calma.wav"))
		{
			if (stream == null)
			{
				log.warn("Could not find calma.wav sound resource");
				return;
			}

			try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(
				new BufferedInputStream(stream)))
			{
				clip = AudioSystem.getClip();
				clip.open(audioStream);
			}
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			log.warn("Failed to load alert sound", e);
			clip = null;
		}
	}

	private void playSound()
	{
		if (clip == null)
		{
			return;
		}

		if (clip.isRunning())
		{
			clip.stop();
		}

		clip.setFramePosition(0);
		clip.start();
	}
}
