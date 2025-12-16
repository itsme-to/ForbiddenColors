package to.itsme.forbiddencolors;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import to.itsme.forbiddencolors.commands.RCCommand;
import to.itsme.forbiddencolors.listeners.PlayerJoinListener;
import to.itsme.forbiddencolors.listeners.PlayerMoveListener;

public final class ForbiddenColorsPlugin extends JavaPlugin {
    private GameController gameController;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(gameController), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(gameController), this);
        gameController = new GameController();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(RCCommand.createCommand(this).build());
        });
    }

    public GameController getGameController() {
        return gameController;
    }
}
