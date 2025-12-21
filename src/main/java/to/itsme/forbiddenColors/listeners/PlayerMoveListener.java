package to.itsme.forbiddencolors.listeners;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import to.itsme.forbiddencolors.game.GameController;
import to.itsme.forbiddencolors.DefinedColor;
import to.itsme.forbiddencolors.game.GameState;

public record PlayerMoveListener(GameController gameController) implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (gameController.gameState != GameState.RUNNING) return;

        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Location location = event.getTo();

        Block currentBlock = location.getBlock();
        // If the player is inside nothing get the block below
        if (currentBlock.getType() == Material.AIR) {
            currentBlock = location.clone().add(0, -1, 0).getBlock();
        }
        if (currentBlock.getType() == Material.AIR) return;

        Color mapColor = currentBlock.getBlockData().getMapColor();
        DefinedColor blockColor = DefinedColor.nearestTo(mapColor);

        if (gameController.isDisallowed(blockColor)) {
            location.getWorld().createExplosion(location, 4.0F);
        }
    }
}
