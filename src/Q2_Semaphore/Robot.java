package Q2_Semaphore;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


import static Q2_Monitor.catmaker.bins;
import static Q2_Semaphore.Part.CAT;

/**
 * Created by emol on 2/19/18.
 */
public class Robot implements Runnable {
    static int TAIL = 0, LEG = 1, TOE = 2, WHISKER = 3, EYE = 4, HEAD = 5, BODY = 6,
    FORELEG = 7, HINDLEG = 8, BODY_TAIL = 9, BODY_LEGS = 10, BODY_TAIL_LEGS = 11,
    HEAD_WHISKER = 12, HEAD_EYE = 13,
    HEAD_WHISKER_EYE = 14, CAT = 15;










    private int rid;
    private RobotType type;
    private int cnt;

    public Robot(int rid, RobotType type) {
        this.rid = rid;
        this.type = type;
        this.cnt = 0;
    }

    @Override
    public void run() {
        try {
            while (cnt <= 250) {
                switch (type) {
                    case R_CAT:
                        taskCat();
                        break;
                    case R_EYES:
                        taskEyes();
                        break;
                    case R_LEGS:
                        taskLegs();
                        break;
                    case R_TAIL:
                        taskTail();
                        break;
                    case R_TOES:
                        taskToes();
                        break;
                    case R_WHISKERS:
                        taskWhisker();
                        break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Robot " + rid + " exited.");
        }

    }

    // FIXME: when a robot is doing some task, all the input bins and output bin are blocked, for the whole process ?
    // Toes. A leg is acquired, and (randomly) either 4 toes or 5 toes are acquired and attached, producing
    // either a single foreleg or hindleg. This takes 10–20ms.
    public void taskToes() throws InterruptedException {
        boolean foreleg = (Math.random() < 0.5);

        boolean[] parts = new boolean[16];
        boolean success = true;

        // material (input)
        if (bins.get(Part.LEG).acquire(rid, 1, false))
        if (foreleg) bins.get(Part.TOE).acquire(rid, 4, false);
        else bins.get(Part.TOE).acquire(rid, 5, false);

        // output
        if (foreleg) bins.get(Part.FORELEG).acquire(rid, 1, true);
        else bins.get(Part.HINDLEG).acquire(rid, 1, true);



        Thread.sleep(ThreadLocalRandom.current().nextInt(10, 20));



        if (foreleg) bins.get(Part.FORELEG).release(rid);
        else bins.get(Part.HINDLEG).release(rid);
        bins.get(Part.TOE).release(rid);
        bins.get(Part.LEG).release(rid);
    }


    //    Legs. A body is acquired, with or without a tail, and 4 legs are attached to the body to give it 2
//    forelegs and 2 hindlegs. This takes 30–50ms.
    public void taskLegs() throws InterruptedException {
        boolean bodyWithTail = bins.get(Part.BODY_TAIL).canAcquire(1, false);

        // material (input)
        if (bodyWithTail){
            bins.get(Part.FORELEG).acquire(rid, 2, false);
            bins.get(Part.HINDLEG).acquire(rid, 2, false);
            bins.get(Part.BODY_TAIL).acquire(rid, 1, false);
        }

        else {
            bins.get(Part.BODY).acquire(rid, 1, false);
            bins.get(Part.FORELEG).acquire(rid, 2, false);
            bins.get(Part.HINDLEG).acquire(rid, 2, false);
        }


        // output
        if (bodyWithTail) bins.get(Part.BODY_TAIL_LEGS).acquire(rid, 1, true);
        else bins.get(Part.BODY_LEGS).acquire(rid, 1, true);


        Thread.sleep(ThreadLocalRandom.current().nextInt(30, 50));



        bins.get(Part.FORELEG).release(rid);
        bins.get(Part.HINDLEG).release(rid);
        if (bodyWithTail) {
            bins.get(Part.BODY_TAIL_LEGS).release(rid);
            bins.get(Part.BODY_TAIL).release(rid);
            bins.get(Part.HINDLEG).release(rid);
            bins.get(Part.FORELEG).release(rid);
        } else {
            bins.get(Part.BODY_LEGS).release(rid);
            bins.get(Part.HINDLEG).release(rid);
            bins.get(Part.FORELEG).release(rid);
            bins.get(Part.BODY).release(rid);

        }
    }

    // (c) Tail. A body, with or without legs, is acquired along with 1 tail to form a more complete body. This
    //  takes 10-20ms.
    public void taskTail() throws InterruptedException {
        boolean bodyWithLeg = bins.get(Part.BODY_LEGS).canAcquire(1, false);

        // material (input)

        bins.get(Part.TAIL).acquire(rid, 1, false);

        if (bodyWithLeg)
            bins.get(Part.BODY_LEGS).acquire(rid, 1, false);
        else bins.get(Part.BODY).acquire(rid, 1, false);

        // output
        if (bodyWithLeg) bins.get(Part.BODY_TAIL_LEGS).acquire(rid, 1, true);
        else bins.get(Part.BODY_TAIL).acquire(rid, 1, true);


        Thread.sleep(ThreadLocalRandom.current().nextInt(10, 20));



        if (bodyWithLeg) {
            bins.get(Part.BODY_TAIL_LEGS).release(rid);
            bins.get(Part.BODY_LEGS).release(rid);
        } else {
            bins.get(Part.BODY_TAIL).release(rid);
            bins.get(Part.BODY).release(rid);
        }
        bins.get(Part.TAIL).release(rid);
    }


    // (d) Eyes. A head, with or without whiskers, is acquired along with 2 eyes to form a more complete
    // head. This takes 10-30ms.
    public void taskEyes() throws InterruptedException {
        boolean withWisker = bins.get(Part.HEAD_WHISKER).canAcquire(1, false);

        // material (input)
        bins.get(Part.EYE).acquire(rid, 2, false);
        if (withWisker) {
            bins.get(Part.HEAD_WHISKER).acquire(rid, 1, false);
        }
        else bins.get(Part.HEAD).acquire(rid, 1, false);


        // output
        if (withWisker) bins.get(Part.HEAD_WHISKER_EYE).acquire(rid, 1, true);
        else bins.get(Part.HEAD_EYE).acquire(rid, 1, true);

        Thread.sleep(ThreadLocalRandom.current().nextInt(10, 30));

        if (withWisker) {
            bins.get(Part.HEAD_WHISKER_EYE).release(rid);
            bins.get(Part.HEAD_WHISKER).release(rid);
        } else {
            bins.get(Part.HEAD_EYE).release(rid);
            bins.get(Part.HEAD).release(rid);
        }
        bins.get(Part.EYE).release(rid);
    }

//        (e) Whiskers. A head, with or without eyes, is acquired along with 6 whiskers to form a more complete
//    head. This takes 20-60ms.

    public void taskWhisker() throws InterruptedException {
        boolean withEye = bins.get(Part.HEAD_EYE).canAcquire(1, false);

        // material (input)
        bins.get(Part.WHISKER).acquire(rid, 6, false);
        if (withEye) {
            bins.get(Part.HEAD_EYE).acquire(rid, 1, false);
        }
        else bins.get(Part.HEAD).acquire(rid, 1, false);


        // output
        if (withEye) bins.get(Part.HEAD_WHISKER_EYE).acquire(rid, 1, true);
        else bins.get(Part.HEAD_WHISKER).acquire(rid, 1, true);

        Thread.sleep(ThreadLocalRandom.current().nextInt(20, 60));

        if (withEye) {
            bins.get(Part.HEAD_WHISKER_EYE).release(rid);
            bins.get(Part.HEAD_EYE).release(rid);
        } else {
            bins.get(Part.HEAD_WHISKER).release(rid);
            bins.get(Part.HEAD).release(rid);
        }
        bins.get(Part.WHISKER).release(rid);
    }


    //    (f) Cat. A head with both eyes and whiskers is attached to a body that has all 4 legs and a tail. This
//    takes 10-20ms
    public void taskCat() throws InterruptedException {

        // material (input)
        bins.get(Part.BODY_TAIL_LEGS).acquire(rid, 1, false);
        bins.get(Part.HEAD_WHISKER_EYE).acquire(rid, 1, false);


        // output
        bins.get(Part.CAT).acquire(rid, 1, true);

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10, 20));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bins.get(Part.CAT).release(rid);
        bins.get(Part.HEAD_WHISKER_EYE).release(rid);
        bins.get(Part.BODY_TAIL_LEGS).release(rid);

        this.cnt++;
    }



    private void releaseAcquiredPart(boolean[] parts){
        if (parts[CAT]) bins.get(Part.CAT).release(rid);
        if (parts[HEAD_WHISKER_EYE]) bins.get(Part.HEAD_WHISKER_EYE).release(rid);
        if (parts[HEAD_EYE]) bins.get(Part.HEAD_EYE).release(rid);
        if (parts[HEAD_WHISKER]) bins.get(Part.HEAD_WHISKER).release(rid);
        if (parts[BODY_TAIL_LEGS]) bins.get(Part.BODY_TAIL_LEGS).release(rid);
        if (parts[BODY_LEGS]) bins.get(Part.BODY_LEGS).release(rid);
        if (parts[BODY_TAIL]) bins.get(Part.BODY_TAIL).release(rid);
        if (parts[HINDLEG]) bins.get(Part.HINDLEG).release(rid);
        if (parts[FORELEG]) bins.get(Part.FORELEG).release(rid);
        if (parts[BODY]) bins.get(Part.BODY).release(rid);
        if (parts[HEAD]) bins.get(Part.HEAD).release(rid);
        if (parts[EYE]) bins.get(Part.EYE).release(rid);
        if (parts[WHISKER]) bins.get(Part.WHISKER).release(rid);
        if (parts[TOE]) bins.get(Part.TOE).release(rid);
        if (parts[LEG]) bins.get(Part.LEG).release(rid);
        if (parts[TAIL]) bins.get(Part.TAIL).release(rid);
    }


}


