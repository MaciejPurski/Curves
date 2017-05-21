package Server.Model;

import javafx.scene.paint.Color;

/**
 * Created by maciej on 17.05.17.
 * Class lists one of four colors available in the game. It implemets a method which can convert it to javaFX color object
 */
public enum GameColor {

    RED(0), BLUE(1), GREEN(2), YELLOW(3), PINK(4), AZURE(5), ORANGE(6), BROWN(7);

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
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.PINK;
            case 5:
                return Color.AZURE;
            case 6:
                return Color.DARKORANGE;
            case 7:
                return Color.BROWN;
        }

        return null;
    }
}