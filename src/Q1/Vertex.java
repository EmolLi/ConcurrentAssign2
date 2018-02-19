package Q1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Q1.star.n;
import static Q1.star.p;

/**
 * Created by emol on 2/18/18.
 */
public class Vertex {
    volatile double x;  // FIXME: X, Y may be changed by other threads
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
    // FIXME: how does synchronized work exactly? if the method is synchronized, this object inside will be locked.
    // is this.prev, this.next all locked? if yes, are they all locked at the beginning, or one by one when we use them.
    // If some thread outside the synchronized block trying to access (read or write) a locked object in the
    // synchronized block, will it be blocked?

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
        moveHelper(r1, r2, r3);
        this.unlock(index);
        /**
        if (this == p) {
            this.l.lock();
            this.next.l.lock();
            this.prev.l.lock();

            moveHelper(r1, r2, r3);
            System.out.println("unlock " + r1);
            this.prev.l.unlock();
            this.next.l.unlock();
            this.l.unlock();
        }

        // this is the last node
        if (this.next == p) {
            this.next.l.lock();
            this.prev.l.lock();
            this.l.lock();

            moveHelper(r1, r2, r3);
            System.out.println("unlock " + r1);
            this.l.unlock();
            this.prev.l.unlock();
            this.next.l.unlock();

        }

        // this is a node in the middle
        else {
            this.prev.l.lock();
            this.l.lock();
            this.next.l.lock();

            moveHelper(r1, r2, r3);
            System.out.println("unlock " + r1);
            this.next.l.unlock();
            this.l.unlock();
            this.prev.l.unlock();
        }**/

/**
        double newX = r1*this.x + r2*this.prev.x + r3*this.next.x;
        double newY = r1*this.y + r2*this.prev.y + r3*this.next.y;

        this.x = newX;
        this.y = newY;
**/

        // TODO: Why must I release lock in order?


//        synchronized (this){
//            moveHelper(r1, r2, r3);
//        }
        /** FIXME: does this work?
        // avoid deadlock by acquiring resources in order

        // this is the first node
        if (this == p){
            synchronized (this){
                synchronized (this.next){
                    synchronized (this.prev){
                        moveHelper(r1, r2, r3);
                    }
                }
            }
        }

        // this is the last node
        if (this.next == p){
            synchronized (this.next){
                synchronized (this.prev){
                    synchronized (this){
                        moveHelper(r1, r2, r3);
                    }
                }
            }
        }

        // this is a node in the middle
        else{
            synchronized (this.prev){
                synchronized (this.)
            }
        }**/

    }

    private void moveHelper(double r1, double r2, double r3){
        double newX = r1*this.x + r2*this.prev.x + r3*this.next.x;
        double newY = r1*this.y + r2*this.prev.y + r3*this.next.y;

        this.x = newX;
        this.y = newY;

        System.out.println("mv " + r1);
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
