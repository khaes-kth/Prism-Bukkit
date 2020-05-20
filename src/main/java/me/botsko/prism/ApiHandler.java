package me.botsko.prism;

import au.com.addstar.dripreporter.DripGauge;
import au.com.addstar.dripreporter.DripReporterApi;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.botsko.prism.actionlibs.RecordingQueue;
import me.botsko.prism.actions.ActionMeter;
import me.botsko.prism.bridge.PrismBlockEditHandler;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created for the Prism Project.
 * Created by Narimm on 20/05/2020.
 */
public class ApiHandler {
    private static final Collection<String> enabledPlugins = new ArrayList<>();
    public static DripReporterApi monitor = null;
    public static WorldEditPlugin worldEditPlugin = null;

    private ApiHandler() {
    }

    /**
     * Setup Drip Reporter.
     */
    static boolean configureMonitor() {
        Plugin drip = Prism.getInstance().getServer().getPluginManager().getPlugin("DripReporter");
        if (drip != null && drip.isEnabled()) {
            monitor = (DripReporterApi) drip;
            DripGauge<Integer> recordingQ = RecordingQueue::getQueueSize;
            ApiHandler.monitor.addGauge(Prism.class, recordingQ, "RecordingQueueSize");
            ActionMeter.setupActionMeter();
            Prism.log("Prism hooked DripReporterApi instance: " + drip.getName() + " "
                    + drip.getDescription().getVersion());
            enabledPlugins.add(drip.getName());
            return true;
        }
        return false;
    }

    static void hookWorldEdit() {
        final Plugin we = Prism.getInstance().getServer().getPluginManager().getPlugin("WorldEdit");
        if (we != null) {
            worldEditPlugin = (WorldEditPlugin) we;
            enabledPlugins.add(we.getName());
            // Easier and foolproof way.
            try {
                WorldEdit.getInstance().getEventBus().register(new PrismBlockEditHandler());
                Prism.log("WorldEdit found. Associated features enabled.");
            } catch (Throwable error) {
                Prism.log("Required WorldEdit version is 6.0.0 or greater!"
                        + " Certain optional features of Prism disabled.");
                Prism.debug(error.getMessage());
            }

        } else {
            Prism.log("WorldEdit not found. Certain optional features of Prism disabled.");
        }
    }

    static boolean checkDependency(String pluginName) {
        return ApiHandler.enabledPlugins.contains(pluginName);
    }

    private static boolean disableDripReporterHook() {
        if (monitor != null) {
            monitor.getRegistry().remove("Prism.RecordingQueueSize");
            try {
                enabledPlugins.remove(((Plugin) monitor).getName());
            } catch (ClassCastException ignore) {
                return false;
            }
        }
        monitor = null;
        Prism.getInstance().monitoring = false;
        return true;
    }

    private static boolean disableWorldEditHook() {
        if (worldEditPlugin != null) {
            try {
                WorldEdit.getInstance().getEventBus().unregister(new PrismBlockEditHandler());
                Prism.log("WorldEdit unhooked");
                enabledPlugins.remove(worldEditPlugin.getName());
                worldEditPlugin = null;
                return true;
            } catch (Throwable error) {
                Prism.log("We could not unhook worldEdit...was it enabled???");
                Prism.debug(error.getMessage());
                return false;
            }
        } else {
            return true;
        }
    }

    static boolean disable() {
        return disableDripReporterHook() && disableDripReporterHook();
    }

}