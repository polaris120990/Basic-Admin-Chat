package me.polaris120990.AdminChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class AdminChatListener extends PlayerListener
{
	public static boolean isAdmin(Player p)
	{
		if(AdminChat.UsePermissions) return AdminChat.Permissions.has(p, "adminchat.chat");
		else return p.isOp();
	}
	
	public void onPlayerChat(PlayerChatEvent event)
	{
		boolean adchat = AdminChat.Chat.getBoolean(event.getPlayer().getName());
		if(adchat == true)
		{
			char[] chars = event.getMessage().toCharArray();
			if(chars[0] == '@')
			{
				int i = 1;
				String newmsg = "";
				while(i < chars.length)
				{
					newmsg = newmsg + chars[i];
					i++;
				}
				event.setMessage(newmsg);
				return;
			}
			else
			{
				event.getRecipients().clear();
				Player[] players = Bukkit.getOnlinePlayers();
				int i = 0;
				while(i < players.length)
				{
					if(isAdmin(players[i]))
					{
						event.getRecipients().add(players[i]);
					}
					i++;
				}
				String ormsg = event.getMessage();
				event.setMessage(ChatColor.BLUE + "[AC] " + ChatColor.GREEN + ormsg);
				return;
			}
		}
		else if(adchat == false)
		{
			if(isAdmin(event.getPlayer()))
			{
				char[] chars = event.getMessage().toCharArray();
				if(chars[0] == '@')
				{
					int i = 1;
					String newmsg = "";
					while(i < chars.length)
					{
						newmsg = newmsg + chars[i];
						i++;
					}
					event.getRecipients().clear();
					Player[] players = Bukkit.getOnlinePlayers();
					int j = 0;
					while(j < players.length)
					{
						if(isAdmin(players[j]))
						{
							event.getRecipients().add(players[j]);
						}
						j++;
					}
					event.setMessage(ChatColor.BLUE + "[AC] " + ChatColor.GREEN + newmsg);
					return;
				
				}
			}
		}

	}
}
