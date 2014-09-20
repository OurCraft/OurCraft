package org.craft.maths;


public class MathHelper
{

    public static int perm[] =
                             {
            151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231,
            83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47,
            16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181,
            199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
                             };

    public static double roundToNearestMultiple(double number, double multiple)
    {
        return (int)(number / multiple) * multiple;
    }

    public static double roundToNthDecimal(double number, int decimals)
    {
        return roundToNearest(number * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }

    public static double roundToNearest(double number)
    {
        if((int)(number + .5) >= (int)(number)) return (int)number + 1;
        return (int)number;
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

    public static float perlinNoise(float x, float y, float res)
    {
        int[] perlinPerm = perm;
        float tempX, tempY;
        int x0, y0, ii, jj, gi0, gi1, gi2, gi3;
        float unit = (float)(1.0 / Math.sqrt(2));
        float tmp, s, t, u, v, Cx, Cy, Li1, Li2;
        float gradient[][] =
        {
                {
                        unit, unit
                },
                {
                        -unit, unit
                },
                {
                        unit, -unit
                },
                {
                        -unit, -unit
                },
                {
                        1, 0
                },
                {
                        -1, 0
                },
                {
                        0, 1
                },
                {
                        0, -1
                }
        };

        // Adapter pour la résolution
        x /= res;
        y /= res;

        // On récupère les positions de la grille associée à (x,y)
        x0 = (int)(x % (perlinPerm.length - 1));
        y0 = (int)(y % (perlinPerm.length - 1));

        // Masquage
        ii = (x0 & 255) % (perlinPerm.length - 1);
        jj = (y0 & 255) % (perlinPerm.length - 1);

        // Pour récupérer les vecteurs
        int index1 = (ii + perlinPerm[jj]) % (perlinPerm.length - 1);
        int index2 = (ii + 1 + perlinPerm[jj]) % (perlinPerm.length - 1);

        int index3 = (ii + perlinPerm[jj + 1]) % (perlinPerm.length - 1);
        int index4 = (ii + 1 + perlinPerm[jj + 1]) % (perlinPerm.length - 1);
        gi0 = perlinPerm[index1] % 8;
        gi1 = perlinPerm[index2] % 8;
        gi2 = perlinPerm[index3] % 8;
        gi3 = perlinPerm[index4] % 8;

        // on récupère les vecteurs et on pondère
        tempX = x - x0;
        tempY = y - y0;
        s = gradient[gi0][0] * tempX + gradient[gi0][1] * tempY;

        tempX = x - (x0 + 1);
        tempY = y - y0;
        t = gradient[gi1][0] * tempX + gradient[gi1][1] * tempY;

        tempX = x - x0;
        tempY = y - (y0 + 1);
        u = gradient[gi2][0] * tempX + gradient[gi2][1] * tempY;

        tempX = x - (x0 + 1);
        tempY = y - (y0 + 1);
        v = gradient[gi3][0] * tempX + gradient[gi3][1] * tempY;

        // Lissage
        tmp = x - x0;
        Cx = 3 * tmp * tmp - 2 * tmp * tmp * tmp;

        Li1 = s + Cx * (t - s);
        Li2 = u + Cx * (v - u);

        tmp = y - y0;
        Cy = 3 * tmp * tmp - 2 * tmp * tmp * tmp;

        return Li1 + Cy * (Li2 - Li1);
    }
}
