package to.itsme.forbiddenColors.listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import to.itsme.forbiddenColors.ForbiddenColorsPlugin;
import to.itsme.forbiddenColors.enums.GameState;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (ForbiddenColorsPlugin.getGameController().gameState != GameState.RUNNING) return;
        player.showBossBar(ForbiddenColorsPlugin.getGameController().bossBar);
    }
}
