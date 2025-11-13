package to.itsme.forbiddencolors;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import to.itsme.forbiddencolors.commands.RCCommand;
import to.itsme.forbiddencolors.listeners.PlayerMoveListener;

public final class ForbiddenColorsPlugin extends JavaPlugin {
    private static ForbiddenColorsPlugin instance;
    private static GameController GAME_CONTROLLER;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        GAME_CONTROLLER = new GameController();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(RCCommand.createCommand().build());
        });
    }

    public static GameController getGameController() {
        return GAME_CONTROLLER;
    }

    public static ForbiddenColorsPlugin getInstance() {
        return instance;
    }

    public void startGameTask() {
        this.getServer().getScheduler().runTaskTimer(this, new GameTick(), 0, 1);
    }
}
