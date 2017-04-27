package Server.Model;

/**
 * Created by maciej on 26.04.17.
 */
public abstract class GameObject {

    private int x;
    private int y;


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(int dx, int dy){
        x+=dx;
        y+=dy;
    }

    public abstract void update();
}
