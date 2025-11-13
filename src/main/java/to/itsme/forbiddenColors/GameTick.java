package to.itsme.forbiddenColors;

import org.bukkit.scheduler.BukkitTask;
import to.itsme.forbiddenColors.enums.GameState;

import java.util.function.Consumer;

public class GameTick implements Consumer<BukkitTask> {

    private final GameController controller;

    public GameTick(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void accept(BukkitTask task) {
        if (controller.gameState == GameState.STOPPED) {
            task.cancel();
        }

        if (controller.gameState == GameState.PAUSED) {
            return;
        }
        controller.current++;

        if (controller.current < controller.timer) {
            controller.bossBar.progress(1 - (float) controller.current / controller.timer);
            return;
        }
        controller.current = 0;
        controller.changeColors();
    }
}