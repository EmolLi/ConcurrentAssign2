package Q2_Monitor;

import java.util.HashMap;

/**
 * Created by emol on 2/19/18.
 */
public class catmaker {
    // part type
    static final int TAIL = 0, LEG = 1, TOE = 2, WHISKER = 3, EYE = 4, HEAD = 5, BODY = 6,
            FORELEG = 7, HINDLEG = 8, BODY_TAIL = 9, BODY_LEGS = 10, BODY_TAIL_LEGS = 11,
            HEAD_WHISKER = 12, HEAD_EYE = 13,
            HEAD_WHISKER_EYE = 14, CAT = 15;

    // task type
    static final int T_HINDLEG = 0, T_FORELEG = 1,
            T_BODY_TAIL_LEGS_L = 2, T_BODY_TAIL_LEGS_T = 3,
            T_BODY_LEGS = 4, T_BODY_TAIL = 5,
            T_HEAD_WHISKER_EYE_W = 6, T_HEAD_WHISKER_EYE_E = 7,
            T_HEAD_EYE = 8, T_HEAD_WHISKER = 9,
            T_CAT = 10;

    static int[][] TASK_MATERIAL;
    static int[] TASK_OUTPUT;
    static int[] TASK_TIME_MIN;
    static int[] TASK_TIME_MAX;

    public static Bin[] bins;

    public static void main(String[] args) {

        // init
        init();
        bins = new Bin[CAT + 1];
        for (int i = 0; i <= CAT; i++){
            bins[i] = new Bin(i);
        }

        // create robots
        int robotCnt = 11;  // 2*5 + 1
        Thread[] robots = new Thread[robotCnt];
        int rid = 0;
        for (RobotType rt : RobotType.values()){
            if (rt == RobotType.R_CAT) {
                continue;
            }
            robots[rid] = new Thread(new Robot(rid, rt));
            robots[rid].start();
            rid++;

            robots[rid] = new Thread(new Robot(rid, rt));
            robots[rid].start();
            rid++;
        }

        robots[10] = new Thread(new Robot(10, RobotType.R_CAT));
        robots[10].start();

        try{
            robots[10].join();
        }catch (Exception e){}


        for (int i = 0; i < robotCnt - 1; i++){
            robots[i].interrupt();
        }

        for (int i = 0; i < robotCnt - 1; i++){
            try {

                robots[i].join();
            } catch (InterruptedException e){}
        }
        System.out.println("============DONE=================");
    }


    private static void init(){
        // init data
        TASK_MATERIAL = new int[T_CAT + 1][CAT + 1];
        TASK_OUTPUT = new int[T_CAT + 1];
        TASK_TIME_MAX = new int[T_CAT + 1];
        TASK_TIME_MIN = new int[T_CAT + 1];

        // toes
        TASK_MATERIAL[T_FORELEG][LEG] = 1;
        TASK_MATERIAL[T_FORELEG][TOE] = 4;
        TASK_OUTPUT[T_FORELEG] = FORELEG;
        TASK_TIME_MIN[T_FORELEG] = 10;
        TASK_TIME_MAX[T_FORELEG] = 20;

        TASK_MATERIAL[T_HINDLEG][LEG] = 1;
        TASK_MATERIAL[T_HINDLEG][TOE] = 5;
        TASK_OUTPUT[T_HINDLEG] = HINDLEG;
        TASK_TIME_MIN[T_HINDLEG] = 10;
        TASK_TIME_MAX[T_HINDLEG] = 20;


        // legs
        TASK_MATERIAL[T_BODY_LEGS][BODY] = 1;
        TASK_MATERIAL[T_BODY_LEGS][FORELEG] = 2;
        TASK_MATERIAL[T_BODY_LEGS][HINDLEG] = 2;
        TASK_OUTPUT[T_BODY_LEGS] = BODY_LEGS;
        TASK_TIME_MIN[T_BODY_LEGS] = 30;
        TASK_TIME_MAX[T_BODY_LEGS] = 50;

        TASK_MATERIAL[T_BODY_TAIL_LEGS_L][T_BODY_TAIL] = 1;
        TASK_MATERIAL[T_BODY_TAIL_LEGS_L][FORELEG] = 2;
        TASK_MATERIAL[T_BODY_TAIL_LEGS_L][HINDLEG] = 2;
        TASK_OUTPUT[T_BODY_TAIL_LEGS_L] = BODY_TAIL_LEGS;
        TASK_TIME_MIN[T_BODY_TAIL_LEGS_L] = 30;
        TASK_TIME_MAX[T_BODY_TAIL_LEGS_L] = 50;

        // tails
        TASK_MATERIAL[T_BODY_TAIL][BODY] = 1;
        TASK_MATERIAL[T_BODY_TAIL][TAIL] = 1;
        TASK_OUTPUT[T_BODY_TAIL] = BODY_TAIL;
        TASK_TIME_MIN[T_BODY_TAIL] = 10;
        TASK_TIME_MAX[T_BODY_TAIL] = 20;

        TASK_MATERIAL[T_BODY_TAIL_LEGS_T][BODY_LEGS] = 1;
        TASK_MATERIAL[T_BODY_TAIL_LEGS_T][TAIL] = 1;
        TASK_OUTPUT[T_BODY_TAIL_LEGS_T] = BODY_TAIL_LEGS;
        TASK_TIME_MIN[T_BODY_TAIL_LEGS_T] = 10;
        TASK_TIME_MAX[T_BODY_TAIL_LEGS_T] = 20;

        // Eyes
        TASK_MATERIAL[T_HEAD_EYE][HEAD] = 1;
        TASK_MATERIAL[T_HEAD_EYE][EYE] = 2;
        TASK_OUTPUT[T_HEAD_EYE] = HEAD_EYE;
        TASK_TIME_MIN[T_HEAD_EYE] = 10;
        TASK_TIME_MAX[T_HEAD_EYE] = 30;

        TASK_MATERIAL[T_HEAD_WHISKER_EYE_E][HEAD_WHISKER] = 1;
        TASK_MATERIAL[T_HEAD_WHISKER_EYE_E][EYE] = 2;
        TASK_OUTPUT[T_HEAD_WHISKER_EYE_E] = HEAD_WHISKER_EYE;
        TASK_TIME_MIN[T_HEAD_WHISKER_EYE_E] = 10;
        TASK_TIME_MAX[T_HEAD_WHISKER_EYE_E] = 30;

        // whisker
        TASK_MATERIAL[T_HEAD_WHISKER][HEAD] = 1;
        TASK_MATERIAL[T_HEAD_WHISKER][WHISKER] = 6;
        TASK_OUTPUT[T_HEAD_WHISKER] = HEAD_WHISKER;
        TASK_TIME_MIN[T_HEAD_WHISKER] = 20;
        TASK_TIME_MAX[T_HEAD_WHISKER] = 60;

        TASK_MATERIAL[T_HEAD_WHISKER_EYE_W][HEAD_EYE] = 1;
        TASK_MATERIAL[T_HEAD_WHISKER_EYE_W][WHISKER] = 6;
        TASK_OUTPUT[T_HEAD_WHISKER_EYE_W] = HEAD_WHISKER_EYE;
        TASK_TIME_MIN[T_HEAD_WHISKER_EYE_W] = 20;
        TASK_TIME_MAX[T_HEAD_WHISKER_EYE_W] = 60;


        // cat
        TASK_MATERIAL[T_CAT][HEAD_WHISKER_EYE] = 1;
        TASK_MATERIAL[T_CAT][BODY_TAIL_LEGS] = 1;
        TASK_OUTPUT[T_CAT] = CAT;
        TASK_TIME_MIN[T_CAT] = 10;
        TASK_TIME_MAX[T_CAT] = 20;


    }
}
