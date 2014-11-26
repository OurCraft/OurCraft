package org.craft.client;

/**
 * @author Chris Taylor
 */
public class CasteljauAlgorithm
{

    private double[]   x;
    private double[]   y;
    private double[]   z;

    private int        n;

    private double[][] T;

    public CasteljauAlgorithm(double[] x, double[] y, double[] z, int n)
    {
        // require x.length = y.length = n
        this.x = x;
        this.y = y;
        this.z = z;
        this.n = n;
        this.T = new double[n][n];
    }

    private void init(double[] initialValues)
    {
        for (int i = 0; i < n; i++)
        {
            T[0][i] = initialValues[i];
        }
    }

    private double evaluate(double t, double[] initialValues)
    {
        init(initialValues);
        for (int j = 1; j < n; j++)
        {
            for (int i = 0; i < n - j; i++)
            {
                T[j][i] = T[j - 1][i] * (1 - t) + T[j - 1][i + 1] * t;
            }
        }
        return (T[n - 1][0]);
    }

    public double[] getXYZvalues(double t)
    {
        double xVal = evaluate(t, x);
        double yVal = evaluate(t, y);
        double zVal = evaluate(t, z);
        return new double[]
        { xVal, yVal, zVal };
    }
}
