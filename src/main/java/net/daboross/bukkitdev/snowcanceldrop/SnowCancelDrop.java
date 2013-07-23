/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.snowcanceldrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author daboross
 */
public class SnowCancelDrop extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage("SnowCancelDrop doesn't know about the command /" + cmd);
		return true;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent evt) {
		if (evt.getBlock().getType() == Material.SNOW_BLOCK) {
			evt.setCancelled(true);
			Location location = evt.getBlock().getLocation();
			location.getWorld().dropItem(location, new ItemStack(Material.SNOW_BLOCK));
			final ItemStack hand = evt.getPlayer().getItemInHand();
			getServer().getScheduler().runTask(this, new Runnable() {
				@Override
				public void run() {
					hand.setDurability((short) (hand.getDurability() + 1));
					if (hand.getDurability() > hand.getType().getMaxDurability()) {
						hand.setType(Material.AIR);
					}
				}
			});
			evt.getBlock().setType(Material.AIR);
		}
	}
}
