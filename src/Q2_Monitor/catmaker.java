package Q2_Monitor;

import java.util.HashMap;

/**
 * Created by emol on 2/19/18.
 */
public class catmaker {
    public static HashMap<Part, Bin> bins;

    public static void main(String[] args) {

        // init
        bins = new HashMap<>();
        for (Part p : Part.values()){
            bins.put(p, new Bin(p));
        }

        // create robots
        int robotCnt = 11;  // 2*5 + 1
        Thread[] robots = new Thread[robotCnt];
        int rid = 0;
        for (RobotType rt : RobotType.values()){
            if (rt != RobotType.R_CAT) {
                robots[rid] = new Thread(new Robot(rid, rt));
                robots[rid].start();
                rid ++;
            }

            robots[rid] = new Thread(new Robot(rid, rt));
            robots[rid].start();
            rid++;
        }

        for (int i = 0; i < robotCnt; i++){
            try {
                robots[rid].join();
            } catch (InterruptedException e){}
        }

        System.out.println("============DONE=================");
    }
}
