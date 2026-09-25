package com.clankickprotector;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("clankickprotector")
public interface ClanKickProtectorConfig extends Config
{
    @ConfigItem(
        keyName = "protectFriendsChat",
        name = "Protect Friends Chat Kicks",
        description = "When turned on, removes the 'Kick' option from the Friends Chat menu."
    )
    default boolean protectFriendsChat()
    {
        return true;
    }

    @ConfigItem(
        keyName = "requireShift",
        name = "Allow Kick with Shift",
        description = "Allows you to Kick a player if you hold down the Shift key."
    )
    default boolean requireShift()
    {
        return false;
    }
}