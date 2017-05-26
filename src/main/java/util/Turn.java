package util;

/**
 * Class keeps information on the turn direction of the player
 */
public enum Turn {

    LEFT(0), RIGHT(1), NONE(2);

    private int value;

    Turn(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Turn fromInt(int i) {
        for (Turn b : Turn.values()) {
            if (b.getValue() == i) { return b; }
        }
        return null;
    }
}