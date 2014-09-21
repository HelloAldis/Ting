package com.kindazrael.tingweather.util;

import java.util.Random;

public class MathUtil {

    private static final Random RANDOM = new Random();

    public static int getRandom(int from, int to) {
        return from + (RANDOM.nextInt(to - from + 1));
    }
}
