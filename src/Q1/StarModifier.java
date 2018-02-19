package Q1;

import static Q1.star.c;
import static Q1.star.n;
import static Q1.star.p;

/**
 * Created by emol on 2/18/18.
 * Runnable that modifies the star polygon
 */
public class StarModifier implements Runnable {
    public int index;
    public StarModifier(int i){
        this.index = i;
    }
    @Override
    public void run() {
        for (int i = 0; i < c; i++){
            Vertex v = chooseRandomVertex();
            v.move(index);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    private Vertex chooseRandomVertex(){
        int i = (int)(Math.random() * n);
        Vertex v = p;
        for (int j = 0; j <= i; j++){
            v = v.next;
        }
        return v;
    }


}
