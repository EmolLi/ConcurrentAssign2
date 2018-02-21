package Q2_Monitor;

import java.util.concurrent.ThreadLocalRandom;

import static Q2_Monitor.catmaker.*;

/**
 * Created by emol on 2/19/18.
 */
public class Robot implements Runnable {

    static int INPUT_ACQUIRE_SUCCESS = 16;


    private int rid;
    private RobotType type;
    private int cnt;
    private long startTime = System.currentTimeMillis();
    private long idleTime = 0;

    public Robot(int rid, RobotType type) {
        this.rid = rid;
        this.type = type;
        this.cnt = 0;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        idleTime = 0;
        try {
            while (cnt < 250) {
                int task = selectTask();
                performTask(task);
            }
            throw new InterruptedException();
        } catch (InterruptedException e) {
            long totalTime = System.currentTimeMillis() - startTime;

            System.out.println("Robot " + rid + ", total time: " + totalTime + ", idle time: " + idleTime + ", idle ratio: " + (float)((float)idleTime/totalTime));
        }

    }


    private int selectTask(){
        switch (type) {
            case R_CAT:
                return T_CAT;

            case R_EYES:
                return bins[HEAD_WHISKER].hasItem(1) ? T_HEAD_WHISKER_EYE_E : T_HEAD_EYE;

            case R_WHISKERS:
                return bins[HEAD_EYE].hasItem(1) ? T_HEAD_WHISKER_EYE_W : T_HEAD_WHISKER;

            case R_LEGS:
                return bins[BODY_TAIL].hasItem(1) ? T_BODY_TAIL_LEGS_L : T_BODY_LEGS;

            case R_TAIL:
                return bins[BODY_LEGS].hasItem(1) ? T_BODY_TAIL_LEGS_T : T_BODY_TAIL;

            case R_TOES:
                return ThreadLocalRandom.current().nextBoolean() ? T_HINDLEG : T_FORELEG;
        }
        return -1;
    }



    private void performTask(int task) throws InterruptedException{

        long tempStartTime = System.currentTimeMillis();
        acqurieInputBins(TASK_MATERIAL[task]);
        idleTime += System.currentTimeMillis() - tempStartTime;

        Thread.sleep(ThreadLocalRandom.current().nextInt(TASK_TIME_MIN[task], TASK_TIME_MAX[task]));


        tempStartTime = System.currentTimeMillis();
        // acquire output bin
        bins[TASK_OUTPUT[task]].acquire(rid, 1, true);
        idleTime += System.currentTimeMillis() - tempStartTime;

        if (task == T_CAT) {
            cnt++;
            System.out.println("Cat number: " + cnt);
        }
            System.out.println("Robot " + rid + " - " + type + "-" + task);



    }







    /**
     * acquire input bins in order
     * @param requiredInputs    a int array specified which bins are required (the number indicates how many items are required in this bin)
     * @return  acquireResult   a boolean array specified which bins are acquired successfully. Index 0 ~ 15 indicates the result for each individual bin, index 16 is the total result.
     */
    private boolean[] acqurieInputBins(int[] requiredInputs) throws InterruptedException{
        // init return result
        boolean[] result = new boolean[INPUT_ACQUIRE_SUCCESS + 1];

        // acquire input bin in order
        for (int p = 0; p <= CAT; p++){
            if (requiredInputs[p] > 0){
                bins[p].acquire(rid, requiredInputs[p], false);
                result[p] = true;
            }
        }

        // acquired all bins successfully
        result[INPUT_ACQUIRE_SUCCESS] = true;
        return result;
    }


}


