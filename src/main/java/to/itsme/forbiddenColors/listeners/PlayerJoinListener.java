package to.itsme.forbiddencolors.listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import to.itsme.forbiddencolors.ForbiddenColorsPlugin;
import to.itsme.forbiddencolors.enums.GameState;

public class PlayerJoinListener implements Listener {
    private final ForbiddenColorsPlugin plugin;

    public PlayerJoinListener(ForbiddenColorsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getGameController().gameState != GameState.RUNNING) return;
        player.showBossBar(plugin.getGameController().bossBar);
    }
}
