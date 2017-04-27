package Server.Model;

/**
 * Created by maciej on 26.04.17.
 */
public class Direction {

    private double tabCos[];
    private double tabSin[];


    private int index;



    Direction (int nindex) {
        tabCos = new double [48];
        tabSin = new double [48];
        double counter = 0;
        double diff = 0.130833333;
        for (int i =0 ; i<48; i++)
        {
            tabCos[i]=Math.cos(counter);
            tabSin[i]=Math.sin(counter);
            counter+=diff;
        }

        index = nindex;
    }

    public void increment()
    {
        index = (index + 1) % 48;
    }

    public void decrement()
    {
        index = index - 1;
        if(index < 0)
            index = 47;
    }

    public double getX(){
        return tabCos[index];
    }

    public double getY(){
        return tabSin[index];
    }

}
