package to.itsme.forbiddencolors.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import java.awt.*;
import java.util.*;
import java.util.List;

public enum DefinedColor {
    WHITE("Blanc", "#FFFFFF"),
    YELLOW("Jaune", "#FFE270"),
    PINK("Rose", "#F063B8"),
    GRAY("Gris", "#707070"),
    PURPLE("Violet", "#8932B8"),
    BLUE("Bleu", "#3E6CC2"),
    BROWN("Marron", "#835432"),
    GREEN("Vert", "#518645"),
    RED("Rouge", "#B02E26"),
    BLACK("Noir", "#000000");

    private final String name;
    private final String hex;
    private final Color color;

    DefinedColor(String name, String hex) {
        this.name = name;
        this.hex = hex;
        this.color = Color.decode(hex);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public TextComponent getTextComponent() {
        return Component.text(getName()).color(TextColor.color(getColor().getRGB()));
    }

    public String toString() {
        return name + " ("  + hex + ")";
    }

    public static DefinedColor random() {
        DefinedColor[] values = DefinedColor.values();
        return values[new Random().nextInt(values.length)];
    }

    public static DefinedColor randomExcluding(Collection<DefinedColor> exclusions) {
        List<DefinedColor> available = new ArrayList<>(Arrays.asList(values()));
        available.removeAll(exclusions);

        if (available.isEmpty()) {
            return null;
        }

        return available.get(new Random().nextInt(available.size()));
    }

    public static DefinedColor nearestTo(org.bukkit.Color color) {
        DefinedColor nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (DefinedColor definedColor : DefinedColor.values()) {
            double distance = colorDistance(color, definedColor.color);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = definedColor;
            }
        }
        return nearest;
    }

    private static double colorDistance(org.bukkit.Color color1, Color color2) {
        double dr = color1.getRed() - color2.getRed();
        double dg = color1.getGreen() - color2.getGreen();
        double db = color1.getBlue() - color2.getBlue();
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }
}
