package me.polaris120990.AdminChat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class AdminChat extends JavaPlugin
{
    public static PermissionHandler Permissions = null;
    static boolean UsePermissions;
	public File ChatFile;
	public static FileConfiguration Chat;
	public final Logger logger = Logger.getLogger("Minecraft");
	AdminChatListener listener = new AdminChatListener();
	
	public void onEnable()
	{
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_CHAT, listener, Event.Priority.Highest, this);
		ChatFile = new File(getDataFolder(), "chatstate.yml");
	    try {
	        firstRun();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    Chat = new YamlConfiguration();
	    loadYamls();
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " has been enabled.");
	    setupPermissions();
	}
	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[" + pdfFile.getName() + "] has been disabled.");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		readCommand((Player) sender, CommandLabel, args);
		return false;
	}
	
	public void readCommand(Player sender, String command, String[] args)
	{
		if(command.equalsIgnoreCase("ac"))
		{
			if(AdminChatListener.isAdmin(sender))
			{
				if(Chat.get(sender.getName()) == null)
				{
					Chat.set(sender.getName(), true);
					sender.sendMessage(ChatColor.GREEN + "You are now in Admin chat!!");
					saveYamls();
				}
				else
				{
					if(Chat.getBoolean(sender.getName()) == true)
					{
						sender.sendMessage(ChatColor.GREEN + "You are no longer in Admin Chat!!");
						Chat.set(sender.getName(), false);
						saveYamls();
					}
					else if(Chat.getBoolean(sender.getName()) == false)
					{
						sender.sendMessage(ChatColor.GREEN + "You are now in Admin Chat!!");
						Chat.set(sender.getName(), true);
						saveYamls();
					}
				}
			}
			else sender.sendMessage(ChatColor.RED + "You do not have permission to enter Admin chat!!");

			
		}
	}
    private void setupPermissions()
    {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
        if (AdminChat.Permissions == null) {
            if (test != null) {
                UsePermissions = true;
                AdminChat.Permissions = ((Permissions) test).getHandler();
                logger.info("[BasicAdminChat] Permissions detected and enabled");
            } else {
                logger.info("[BasicAdminChat] Permission system not detected, defaulting to OP");
                UsePermissions = false;
            }
        }
    }
	public void saveYamls() {
	    try {
	        Chat.save(ChatFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public void loadYamls() {
	    try {
	        Chat.load(ChatFile);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	private void firstRun() throws Exception
	{
	    if(!ChatFile.exists()){
	        ChatFile.getParentFile().mkdirs();
	        copy(getResource("chatstate.yml"), ChatFile);
	    }
	}

	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
