package com.ClanKickProtector;

import com.google.inject.Provides;
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

        MenuEntry[] entries = event.getMenuEntries();
        boolean hasKickOption = false;

        for (MenuEntry entry : entries)
        {
            if (isKickOption(entry.getOption()))
            {
                hasKickOption = true;
                break;
            }
        }

        // Filter out any menu option containing "Kick"
        if (hasKickOption)
        {
            MenuEntry[] filteredEntries = java.util.Arrays.stream(entries)
                .filter(entry -> !isKickOption(entry.getOption()))
                .toArray(MenuEntry[]::new);

            event.setMenuEntries(filteredEntries);
        }
    }

    private boolean isKickOption(String option)
    {
        if (option == null)
        {
            return false;
        }
        String cleanOption = option.toLowerCase();
        return cleanOption.equals("kick") || cleanOption.contains("kick");
    }
}
