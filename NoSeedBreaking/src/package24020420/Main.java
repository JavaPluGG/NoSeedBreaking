package package24020420;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class Main extends JavaPlugin implements Listener
{
	public WorldGuardPlugin worldGuardPlugin;
	
	public void onEnable()
	{
		worldGuardPlugin = getWorldGuard();
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if(e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType().equals(Material.FARMLAND)) 
		{
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery regionQuery = container.createQuery();
			ApplicableRegionSet set = regionQuery.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        	
			if(set.size() > 0) 
			{
				for(ProtectedRegion protectedRegion : set)
				{
					if(protectedRegion.getOwners().contains(p.getUniqueId())) return;
					if(protectedRegion.getMembers().contains(p.getUniqueId())) return;
				}
				e.setCancelled(true);
			}
		}
	}
	
	public WorldGuardPlugin getWorldGuard() 
	{
		Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        
		if(plugin == null || !(plugin instanceof WorldGuardPlugin)) return null;
		return (WorldGuardPlugin) plugin;
	}
}
