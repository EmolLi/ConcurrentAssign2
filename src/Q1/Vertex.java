package Q1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Q1.star.n;
import static Q1.star.p;

/**
 * Created by emol on 2/18/18.
 */
public class Vertex {
    volatile double x;  // FIXME: X, Y may be changed by other threads, do I need volatile if this variable is protected by a synchronized block?
    volatile double y;
    int index;
    Lock l = new ReentrantLock();
    Vertex prev;
    Vertex next;

    public Vertex(double x, double y, int index){
        this.x = x;
        this.y = y;
        this.index = index;
    }

    // move the position of the vertex
    // the new position should be within the triangle formed by prev, this, next
    // FIXME: how does synchronized work exactly? if the method is synchronized, this object will be locked, are this.prev, this.next all locked? if yes, are they all locked at the beginning, or one by one when we use them. If some thread outside the synchronized block trying to access (read or write) a locked object in the synchronized block, will it be blocked?

    public void move(int index){
        double r1 = Math.random();
        double r2 = Math.random();
        while (r1 + r2 > 0.9){
            r1 = Math.random();
            r2 = Math.random();
        }
        double r3 = 1 - r1 - r2;


        // avoid deadlock by acquiring resources in order

        // this is the first node
        this.lock(index);

        double newX = r1*this.x + r2*this.prev.x + r3*this.next.x;
        double newY = r1*this.y + r2*this.prev.y + r3*this.next.y;
        this.x = newX;
        this.y = newY;
        System.out.println("mv " + r1);

        this.unlock(index);
    }


    private void lock(int i){
        if (index == 0){
            System.out.println("lock " + index + ", t " + i);
            this.l.lock();
            System.out.println("lock " + this.next.index+ ", t " + i);
            this.next.l.lock();
            System.out.println("lock " + this.prev.index+ ", t " + i);
            this.prev.l.lock();
        }
        else if (index == n - 1 ){
            System.out.println("lock " + this.next.index+ ", t " + i);
            this.next.l.lock();
            System.out.println("lock " + this.prev.index+ ", t " + i);
            this.prev.l.lock();
            System.out.println("lock " + index+ ", t " + i);
            this.l.lock();

        }
        else {
            System.out.println("lock " + this.prev.index+ ", t " + i);
            this.prev.l.lock();
            System.out.println("lock " + index+ ", t " + i);
            this.l.lock();
            System.out.println("lock " + this.next.index+ ", t " + i);
            this.next.l.lock();

        }
    }

    public void unlock(int i){
        if (index == 0){
            System.out.println("unlock " + this.prev.index+ ", t " + i);
            this.prev.l.unlock();
            System.out.println("unlock " + this.next.index+ ", t " + i);
            this.next.l.unlock();
            System.out.println("unlock " + index+ ", t " + i);
            this.l.unlock();
        }
        else if (index == n - 1 ){
            System.out.println("unlock " + index+ ", t " + i);
            this.l.unlock();
            System.out.println("unlock " + this.prev.index+ ", t " + i);
            this.prev.l.unlock();
            System.out.println("unlock " + this.next.index+ ", t " + i);
            this.next.l.unlock();

        }
        else {
            System.out.println("unlock " + this.next.index+ ", t " + i);
            this.next.l.unlock();
            System.out.println("unlock " + index+ ", t " + i);
            this.l.unlock();
            System.out.println("unlock " + this.prev.index+ ", t " + i);
            this.prev.l.unlock();

        }
    }
}
