package to.itsme.forbiddencolors.listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import to.itsme.forbiddencolors.game.GameController;
import to.itsme.forbiddencolors.game.GameState;

public record PlayerJoinListener(GameController gameController) implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (gameController.gameState != GameState.RUNNING) return;
        player.showBossBar(gameController.bossBar);
    }
}
