package com.makeagame.tools;

public class MathTools {

    // 內插法
    public static int[] inner(int[] a, int[] b) {
        int ax1 = a[0];
        int ay1 = a[1];
        int bx1 = b[0];
        int by1 = b[1];
        // right bottom point
        int ax2 = a[0] + a[2];
        int ay2 = a[1] + a[3];
        int bx2 = b[0] + b[2];
        int by2 = b[1] + b[3];

        // no intersection
        if ((ax1 <= bx1 && ax2 <= bx1) ||
                (bx1 <= ax1 && bx2 <= ax1) ||
                (ax1 >= bx2 && ax2 >= bx2) ||
                (bx1 >= ax2 && bx2 >= ax2) ||

                (ay1 <= by1 && ay2 <= by1) ||
                (by1 <= ay1 && by2 <= ay1) ||
                (ay1 >= by2 && ay2 >= by2) ||
                (by1 >= ay2 && by2 >= ay2)

        ) {
            return new int[] { 0, 0, 0, 0 };
        } else {
            ax1 = Math.max(ax1, bx1);
            ay1 = Math.max(ay1, by1);
            ax2 = Math.min(ax2, bx2);
            ay2 = Math.min(ay2, by2);
            return new int[] { ax1, ay1, ax2 - ax1, ay2 - ay1 };
        }
    }
    
}
