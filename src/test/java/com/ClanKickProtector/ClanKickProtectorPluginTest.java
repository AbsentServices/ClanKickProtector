package com.ClanKickProtector;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ClanKickProtectorPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ClanKickProtectorPlugin.class);
		RuneLite.main(args);
	}
}