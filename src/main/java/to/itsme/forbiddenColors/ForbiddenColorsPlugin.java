package to.itsme.forbiddenColors;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import to.itsme.forbiddenColors.commands.RCCommand;
import to.itsme.forbiddenColors.listeners.PlayerMoveListener;

public class ForbiddenColorsPlugin extends JavaPlugin {
    private static GameController GAME_CONTROLLER;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        GAME_CONTROLLER = new GameController(this);
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(RCCommand.createCommand().build());
        });
    }

    public static GameController getGameController() {
        return GAME_CONTROLLER;
    }
}
