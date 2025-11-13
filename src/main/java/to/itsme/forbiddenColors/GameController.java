package to.itsme.forbiddenColors;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.scheduler.BukkitScheduler;
import to.itsme.forbiddenColors.enums.DefinedColor;
import to.itsme.forbiddenColors.enums.GameState;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    List<DefinedColor> currentColors;
    public GameState gameState;
    int timer;
    int current;
    int amount;
    DefinedColor requiredColor;
    ForbiddenColorsPlugin plugin;
    BukkitScheduler scheduler;
    public BossBar bossBar;

    public GameController(ForbiddenColorsPlugin plugin) {
        this.plugin = plugin;
        currentColors = new ArrayList<>();
        currentColors.add(DefinedColor.random());
        gameState = GameState.PAUSED;
    }

    public void start() {
        gameState = GameState.RUNNING;
        timer = 20*20;
        amount = 1;
        current = 0;
        requiredColor = null;
        bossBar = BossBar.bossBar(Component.text("Starting"), 0, BossBar.Color.RED, BossBar.Overlay.PROGRESS);
        plugin.getServer().showBossBar(bossBar);
        this.changeColors();
        scheduler = this.plugin.getServer().getScheduler();
        scheduler.runTaskTimer(plugin, new GameTick(this), 0, 1);
    }

    public void changeColors() {
        replaceColors();
        TextComponent text = Component.text("COULEUR MORTELLE : ");
        if (currentColors.size() > 1) {
            text = Component.text("COULEURS MORTELLES : ");
        }
        TextComponent colorsText = getColorsTextComponent();
        text = text.append(colorsText);


        TextComponent newColorText = Component.text("NOUVELLE COULEUR");
        if (currentColors.size() > 1) {
            newColorText = Component.text("NOUVELLES COULEURS");
        }

        if (timer > 20*20) {
            Title title = Title.title(colorsText, newColorText);
            plugin.getServer().showTitle(title);
        } else {
            TextComponent actionbar = newColorText.append(Component.text(" : "));
            actionbar = actionbar.append(colorsText);
            plugin.getServer().sendActionBar(actionbar);
        }
        plugin.getServer().playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1f, 1f));

        bossBar.name(text);
    }

    public TextComponent getColorsTextComponent() {
        TextComponent.Builder text = Component.text();
        boolean first = true;
        for (DefinedColor color : currentColors) {
            if (!first) {
                text = text.append(Component.text(", "));
            }
            first = false;

            text = text.append(color.getTextComponent());
        }

        return text.build();
    }

    public void pause() {
        if (gameState == GameState.PAUSED) {
            gameState = GameState.RUNNING;
        } else gameState = GameState.PAUSED;
    }

    public void stop() {
        gameState = GameState.STOPPED;
        plugin.getServer().hideBossBar(bossBar);
    }

    public void setTimer(int seconds) {
        this.timer = seconds * 20;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setRequiredColor(DefinedColor requiredColor) {
        this.requiredColor = requiredColor;
    }

    public void removeRequiredColor() {
        this.requiredColor = null;
    }

    public void replaceColors() {
        currentColors.clear();
        if (requiredColor != null) {
            currentColors.add(requiredColor);
        }

        while (currentColors.size() < amount) {
            DefinedColor color = DefinedColor.randomExcluding(currentColors);
            if (color == null) return;
            currentColors.add(color);
        }

    }

    public Boolean isDisallowed(DefinedColor color) {
        if (gameState == GameState.PAUSED) {
            return false;
        }
        return currentColors.contains(color);
    }
}
