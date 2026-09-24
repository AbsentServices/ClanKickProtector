package com.ClanKickProtector;

import com.google.inject.Provides;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
    name = "ClanKickProtector",
    description = "Stops you from accidentally kicking anyone from your Friends Chat.",
    tags = {"clan", "chat", "friends", "kick", "protector", "fc"}
)
public class ClanKickProtectorPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ClanKickProtectorConfig config;

    @Provides
    ClanKickProtectorConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ClanKickProtectorConfig.class);
    }

    @Subscribe
    public void onMenuOpened(MenuOpened event)
    {
        // Check if protection is turned on
        if (!config.protectFriendsChat())
        {
            return;
        }

        // Allow bypassing protection by holding Shift if configured
        if (config.requireShift() && client.isKeyPressed(KeyCode.KC_SHIFT))
        {
            return;
        }

        // Only run logic if the player is currently in a Friends Chat
        if (client.getFriendsChatManager() == null)
        {
            return;
        }

        // Get entries from the event instead of the deprecated client.getMenuEntries()
        MenuEntry[] entries = event.getMenuEntries();

        // Filter out any menu options containing "kick"
        MenuEntry[] filteredEntries = Arrays.stream(entries)
            .filter(entry -> !isKickOption(entry.getOption()))
            .toArray(MenuEntry[]::new);

        // Apply filtered entries back to client menu
        if (filteredEntries.length != entries.length)
        {
            client.getMenu().setMenuEntries(filteredEntries);
        }
    }

    private boolean isKickOption(String option)
    {
        if (option == null)
        {
            return false;
        }
        
        // Strip color tags (e.g. "<col=ff0000>Kick</col>" -> "kick")
        String cleanOption = Text.removeTags(option).trim().toLowerCase();
        return cleanOption.contains("kick");
    }
}