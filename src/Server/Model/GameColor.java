package Server.Model;

import javafx.scene.paint.Color;

/**
 * Created by maciej on 17.05.17.
 * Class lists one of four colors available in the game. It impelemts a method which can convert it to javaFX color
 */
public enum GameColor {

    RED(0), BLUE(1), GREEN(2), YELLOW(3);

    private int value;

    GameColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GameColor fromInt(int i) {
        for (GameColor b : GameColor .values()) {
            if (b.getValue() == i) { return b; }
        }
        return null;
    }

    public Color toColor() {
        switch(value)
        {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 4:
                return Color.YELLOW;
        }

        return null;
    }
}