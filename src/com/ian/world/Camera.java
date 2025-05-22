package com.ian.world;

public class Camera {
    private static int x = 0;
    private static int y = 0;

    public static int clamp(int actual, int min, int max) {
        if (actual < min) {
            actual = min;
        }
        if (actual > max) {
            actual = max;
        }

        return actual;
    }

    public static int getX() {
        return x;
    }

    public static void setX(int x) {
        Camera.x = x;
    }

    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        Camera.y = y;
    }
}
