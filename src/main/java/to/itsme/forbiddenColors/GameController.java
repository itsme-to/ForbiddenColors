package to.itsme.forbiddencolors;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import to.itsme.forbiddencolors.enums.DefinedColor;
import to.itsme.forbiddencolors.enums.GameState;

import java.util.ArrayList;
import java.util.List;

public final class GameController {
    private final List<DefinedColor> currentColors;
    private DefinedColor requiredColor;
    public GameState gameState;
    public int timer;
    public int current;
    public int amount;
    public BossBar bossBar;

    public GameController() {
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
        Bukkit.getServer().showBossBar(bossBar);
        this.changeColors();
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
            Bukkit.getServer().showTitle(title);
        } else {
            TextComponent actionbar = newColorText.append(Component.text(" : "));
            actionbar = actionbar.append(colorsText);
            Bukkit.getServer().sendActionBar(actionbar);
        }
        Bukkit.getServer().playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1f, 1f));

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
        Bukkit.getServer().hideBossBar(bossBar);
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
