package net.themorningcompany.playerinfo.util.random;

import java.util.Random;

public class RandomUtil {
    public static Random random = new Random();

    public static int randomRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
    public static boolean randomChance(int chance) { return randomRange(0, 100) <= chance; }
}
