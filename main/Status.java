package de.jaskerx.main;


import javax.security.auth.login.LoginException;

import de.jaskerx.listeners.MessageListener;
import de.jaskerx.listeners.RawGatewayListener;
import de.jaskerx.listeners.SelectMenuListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Status extends ListenerAdapter
{
	
	public static void Start() throws LoginException
	{

		MCFPSupportHelper.jda = JDABuilder.create(MCFPSupportHelper.token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
			
			.disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE)
		    .addEventListeners(new MessageListener(), new SelectMenuListener(), new RawGatewayListener())
		    .setRawEventsEnabled(true)
		    .setStatus(OnlineStatus.ONLINE)
		    .build();

	
		try {	
			MCFPSupportHelper.jda.awaitReady();
			MCFPSupportHelper.log("MCFP Support Helper ist online.");
		    
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
	
	public static void Stop()
	{
		
		MCFPSupportHelper.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
		MCFPSupportHelper.log("MCFP Support Helper ist offline.");
		MCFPSupportHelper.jda.shutdown();
		System.exit(0);
	}
	
}
