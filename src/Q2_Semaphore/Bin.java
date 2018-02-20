package Q2_Semaphore;

import java.util.concurrent.Semaphore;
import static Q2_Semaphore.catmaker.*;

/**
 * Created by emol on 2/19/18.
 * Bin is the contains the semaphore
 */
public class Bin {
    private int type;
    public volatile int cnt;
    public Semaphore s;


    public Bin(int t){
        this.type = t;
        this.cnt = 0;
        this.s = new Semaphore(1);
    }



    private boolean hasItem(int amount){
        switch (type){
            // infinite primitive part
            //bodies, tails, legs, toes, heads, eyes, and whiskers
            case BODY:
            case TAIL:
            case LEG:
            case TOE:
            case HEAD:
            case EYE:
            case WHISKER:
                return true;

            default:
                if (this.cnt >= amount) return true;
        }
        return false;
    }


    // when there are multiple bins that could be acquired, test canAcquire first to decide which bin to acquire
    public boolean canAcquire(int amount, boolean output){
        // no need to worry about if there is object in the bin if we are
        // producing things
        if (output) return true;
        return this.hasItem(amount);
    }

    /**
     *
     * @param rid robot id
     * @param amount amount of object (produced or acquired)
     * @param output if the robot is producing things
     */
    public boolean acquire(int rid, int amount, boolean output) throws InterruptedException{
//        System.out.println("Robot " + rid +" is acquiring Bin " + type);

        if (!canAcquire(amount, output)) return false;

        s.acquire();
        if (!canAcquire(amount, output)) {
            s.release();
            return false;
        }
        return true;
    }


    // already acquired semaphore
    public void updateAmount(int rid, int amount, boolean output){
//        System.out.println("Robot " + rid + (output ? " put " : " toke ")+ amount +" from Bin " + type);
        this.cnt = output ? this.cnt + amount : this.cnt - amount;
    }


    // release the bin
    public synchronized void release(int rid){
//        System.out.println("Robot " + rid +" released Bin " + type);
        s.release();
    }


}
