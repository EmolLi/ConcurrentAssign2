package Q1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by emol on 2/18/18.
 */
public class star {
    public static int n = 6;    // number of vertices
    public static int m;
    public static int c;
    public static Vertex p;
    static int width = 1920;
    static int height = 1080;
    public static void main(String[] args) {

        // accepts two command-line parameters m and c, such that m ≤ n, and c ≥ 0
        if (args.length < 2) {
            System.err.println("Missing arguments, only " + args.length + " were specified!");
            return;
        }

        m = Integer.parseInt(args[0]);
        c = Integer.parseInt(args[1]);

        if (c < 0) {
            System.err.println("c should not be a negative.");
            return;
        }
        if (m > n){
            System.err.println("m should be smaller than n.");
            return;
        }


        // create teh initial polygon
        init();

        // start
        Thread[] threads = new Thread[m];
        for (int i = 0; i < m; i++){
            threads[i] = new Thread(new StarModifier(i));
            threads[i].start();
        }

        for (int i = 0; i < m; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        // all threads ended, scale and draw the polygon
        draw();



    }

    // create the initial polygon
    public static void init(){
        double[] vX = new double[]{-1.0, 1.0, 5.0, 1.0, -4.0, -3.0};    // x of vertices
        double[] vY = new double[]{5.0, 2.0, 0.0, -2.0, -4.0, -1.0};    // y of vertices
        // init polygon
        p = new Vertex(-1.0, 5.0, 0);
        Vertex cur = p;
        for (int i = 1; i < n; i++){
            cur.next = new Vertex(vX[i], vY[i], i);
            cur.next.prev = cur;
            cur = cur.next;
        }
        // finish circular structure with the last one connecting with the first one
        cur.next = p;
        p.prev = cur;
    }

    public static void draw(){
        double minX = Integer.MAX_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        double maxY = Integer.MIN_VALUE;
        Vertex v = p;
        for (int i = 0; i < n; i++){
            if (v.x > maxX) maxX = v.x;
            if (v.x < minX) minX = v.x;
            if (v.y > maxY) maxY = v.y;
            if (v.y < minY) minY = v.y;
            v = v.next;
        }


        // scale
        double diffX = maxX - minX;
        double diffY = maxY - minY;
        double scale = (width - 10) / diffX < (height - 10) / diffY ? (width -10) /diffX : (height -10)/diffY;  //
        // -10 to prevent floating point calculation error

        // reposition the polygon and scale
        v = p;
        int[] xArray = new int[n];
        int[] yArray = new int[n];
        for (int i = 0; i< n; i++){
            xArray[i] = (int) ((v.x - minX) * scale);
            yArray[i] = (int) ((v.y - minY) * scale);
            System.out.println("(" + xArray[i] + ", " + yArray[i] + ")");
            v = v.next;
        }



        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = img.createGraphics();
        ig2.setPaint(Color.black);
        ig2.fillPolygon(xArray, yArray, n);

        try{
            ImageIO.write(img, "png", new File("./output.png"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
