package org.craft.maths;

import java.util.*;

public class MathHelper
{

    public static double roundToNearestMultiple(double number, double multiple)
    {
        return (int) (number / multiple) * multiple;
    }

    public static double roundToNthDecimal(double number, int decimals)
    {
        return roundToNearest(number * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }

    public static double roundToNearest(double number)
    {
        if((int) (number + .5) >= (int) (number))
            return (int) number + 1;
        return (int) number;
    }

    /**
     * From http://graphics.stanford.edu/~seander/bithacks.html
     */
    public static int upperPowerOf2(int v)
    {
        v-- ;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v |= v >> 32;
        v++ ;
        return v;
    }

    public static float perlinNoise(float x, float y, long seed)
    {
        return (float) new ImprovedNoise(seed).perlinNoise(x, y);
    }

    public static int[] shuffle(int[] perm, long seed)
    {
        Random rng = new Random(seed);
        int[] newArray = new int[perm.length];
        ArrayList<Integer> list = new ArrayList<>();
        for(int p : perm)
            list.add(p);
        Collections.shuffle(list, rng);
        for(int i = 0; i < list.size(); i++ )
            newArray[i] = list.get(i);
        return newArray;
    }
}
