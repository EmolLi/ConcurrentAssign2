package Q2_Monitor;

import java.util.concurrent.ThreadLocalRandom;

import static Q2_Monitor.catmaker.*;

/**
 * Created by emol on 2/19/18.
 * Bin is the Monitor
 */
// FIXME: is this something like a monitor. Should all getters in the monitor be synchronized?
public class Bin {
    private int type;
    private volatile int cnt;

    public Bin(int t){
        this.type = t;
        this.cnt = 0;
    }


    // FIXME: synchornized?
    public boolean hasItem(int amount){
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
    public synchronized void acquire(int rid, int amount, boolean output) throws InterruptedException{

        while (!canAcquire(amount, output)){
//            System.out.println("Robot " + rid +" waits " + type);
            wait(ThreadLocalRandom.current().nextInt(10, 20));
        }

        this.cnt = output ? this.cnt + amount : this.cnt - amount;
        notifyAll();
    }

}
