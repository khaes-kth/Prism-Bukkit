package me.botsko.prism.events;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PrismCustomBlockEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String plugin_name;
    private final String action_type_name;
    private final Player player;
    private final Block block;
    private final String message;

    /**
     * @param plugin
     * @param action_type_name
     * @param player
     * @param message
     */
    public PrismCustomBlockEvent(Plugin plugin, String action_type_name, Player player, Block block, String message) {
        this.plugin_name = plugin.getName();
        this.action_type_name = action_type_name;
        this.player = player;
        this.block = block;
        this.message = message + ChatColor.GOLD + " [" + this.plugin_name + "]" + ChatColor.DARK_AQUA;
    }

    /**
     * @return
     */
    public String getPluginName() {
        return plugin_name;
    }

    /**
     * @return
     */
    public String getActionTypeName() {
        return action_type_name;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the block
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Required by bukkit for proper event handling.
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Required by bukkit for proper event handling.
     *
     * @return
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}