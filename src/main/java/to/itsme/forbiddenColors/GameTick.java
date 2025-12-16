package to.itsme.forbiddencolors;

import org.bukkit.scheduler.BukkitTask;
import to.itsme.forbiddencolors.enums.GameState;

import java.util.function.Consumer;

public final class GameTick implements Consumer<BukkitTask> {
    private final GameController gameController;

    public GameTick(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void accept(BukkitTask task) {
        if (gameController.gameState == GameState.STOPPED) {
            task.cancel();
        }

        if (gameController.gameState == GameState.PAUSED) {
            return;
        }
        gameController.increaseCurrentTimer();

        if (gameController.getCurrentTimer() < gameController.getTimer()) {
            gameController.bossBar.progress(1 - (float) gameController.getCurrentTimer() / gameController.getTimer());
            return;
        }
        gameController.resetCurrentTimer();
        gameController.changeColors();
    }
}