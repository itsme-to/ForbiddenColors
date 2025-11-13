package to.itsme.forbiddencolors.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import to.itsme.forbiddencolors.enums.DefinedColor;
import to.itsme.forbiddencolors.ForbiddenColorsPlugin;
import to.itsme.forbiddencolors.enums.GameState;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (ForbiddenColorsPlugin.getGameController().gameState != GameState.RUNNING) return;

        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
        Location location = event.getTo();

        Block currentBlock = location.getBlock();
        // If the player is inside nothing get the block below
        if (currentBlock.getType() == Material.AIR) {
            currentBlock = location.clone().add(0,-1, 0).getBlock();
        }
        if (currentBlock.getType() == Material.AIR) return;

        Color mapColor = currentBlock.getBlockData().getMapColor();
        DefinedColor blockColor = DefinedColor.nearestTo(mapColor);

        String colorHex = String.format("#%06X", mapColor.asRGB());
        TextColor textColor = TextColor.fromHexString(colorHex);

        if (ForbiddenColorsPlugin.getGameController().isDisallowed(blockColor)) {
            location.getWorld().createExplosion(location, 4.0F);
        }

        player.sendActionBar(Component.text("Couleur actuelle : " + blockColor.getName()).color(textColor));
    }
}
