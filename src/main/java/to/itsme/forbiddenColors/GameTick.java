package to.itsme.forbiddencolors;

import org.bukkit.scheduler.BukkitTask;
import to.itsme.forbiddencolors.enums.GameState;

import java.util.function.Consumer;

public final class GameTick implements Consumer<BukkitTask> {
    private final ForbiddenColorsPlugin plugin;

    public GameTick(ForbiddenColorsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void accept(BukkitTask task) {
        GameController gameController = plugin.getGameController();

        if (gameController.gameState == GameState.STOPPED) {
            task.cancel();
        }

        if (gameController.gameState == GameState.PAUSED) {
            return;
        }
        gameController.current++;

        if (gameController.current < gameController.timer) {
            gameController.bossBar.progress(1 - (float) gameController.current / gameController.timer);
            return;
        }
        gameController.current = 0;
        gameController.changeColors();
    }
}