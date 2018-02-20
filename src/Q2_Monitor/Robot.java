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

    public Robot(int rid, RobotType type) {
        this.rid = rid;
        this.type = type;
        this.cnt = 0;
    }

    @Override
    public void run() {
        try {
            while (cnt < 250) {
                int task = selectTask();
                performTask(task);
            }
        } catch (InterruptedException e) {
            System.out.println("Robot " + rid + " exited.");
        }

    }


    private int selectTask(){
        switch (type) {
            case R_CAT:
                return T_CAT;

            case R_EYES:
                return bins[HEAD_WHISKER].canAcquire(1, false) ? T_HEAD_WHISKER_EYE_E : T_HEAD_EYE;

            case R_WHISKERS:
                return bins[HEAD_EYE].canAcquire(1, false) ? T_HEAD_WHISKER_EYE_W : T_HEAD_WHISKER;

            case R_LEGS:
                return bins[BODY_TAIL].canAcquire(1, false) ? T_BODY_TAIL_LEGS_L : T_BODY_LEGS;

            case R_TAIL:
                return bins[BODY_LEGS].canAcquire(1, false) ? T_BODY_TAIL_LEGS_T : T_BODY_TAIL;

            case R_TOES:
                return ThreadLocalRandom.current().nextBoolean() ? T_HINDLEG : T_FORELEG;
        }
        return -1;
    }



    private void performTask(int task) throws InterruptedException{

        boolean[] result = acqurieInputBins(TASK_MATERIAL[task]);
        takeResource(TASK_MATERIAL[task]);
        // release all the input bins
        releaseAcquiredParts(result);


        Thread.sleep(ThreadLocalRandom.current().nextInt(TASK_TIME_MIN[task], TASK_TIME_MAX[task]));


        // acquire output bin
        bins[TASK_OUTPUT[task]].acquire(rid, 1, true);
        // put object in output bin
        bins[TASK_OUTPUT[task]].updateAmount(rid, 1, true);
        bins[TASK_OUTPUT[task]].release(rid);


        if (task == T_CAT) {
            cnt++;
            System.out.println("Cat number: " + cnt);
        }
//            System.out.println("Robot " + rid + " - " + type);



    }




    /** release in reverse order **/
    private void releaseAcquiredParts(boolean[] parts){
        for (int p = CAT; p >= 0; p--){
            if (parts[p]) bins[p].release(rid);
        }
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



    // take resource from the input bin. No need to check here as we can acquire a bin that is available and has enough resource
    private void takeResource(int[] requiredInputs){
        // acquire input bin in order
        for (int p = 0; p <= CAT; p++){
            if (requiredInputs[p] > 0){
                bins[p].updateAmount(rid, requiredInputs[p], false);
            }
        }

    }

}


