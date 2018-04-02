/*
 * Copyright (C) 2017 Jens Heuschkel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.opendiabetes.vault.util;

import de.opendiabetes.vault.container.VaultEntryType;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class SplineInterpolator2 {

    private final List<Double> mX;
    private final List<Double> mY;
    private final double[] mM;

    private SplineInterpolator2(List<Double> x, List<Double> y, double[] m) {
        mX = x;
        mY = y;
        mM = m;
    }

    /**
     * Creates a monotone cubic spline from a given set of control points.
     *
     * The spline is guaranteed to pass through each control point exactly.
     * Moreover, assuming the control points are monotonic (Y is non-decreasing
     * or non-increasing) then the interpolated values will also be monotonic.
     *
     * This function uses the Fritsch-Carlson method for computing the spline
     * parameters. http://en.wikipedia.org/wiki/Monotone_cubic_interpolation
     *
     * @param x The X component of the control points, strictly increasing.
     * @param y The Y component of the control points
     * @return
     *
     * @throws IllegalArgumentException if the X or Y arrays are null, have
     * different lengths or have fewer than 2 values.
     */
    public static SplineInterpolator2 createMonotoneCubicSpline(List<Double> x, List<Double> y) {
        if (x == null || y == null || x.size() != y.size() || x.size() < 2) {
            throw new IllegalArgumentException("There must be at least two control "
                    + "points and the arrays must be of equal length.");
        }

        final int n = x.size();
        double[] d = new double[n - 1]; // could optimize this out
        double[] m = new double[n];

        // Compute slopes of secant lines between successive points.
        for (int i = 0; i < n - 1; i++) {
            double h = x.get(i + 1) - x.get(i);
            if (h <= 0.0) {
                throw new IllegalArgumentException("The control points must all "
                        + "have strictly increasing X values.");
            }
            d[i] = (y.get(i + 1) - y.get(i)) / h;
        }

        // Initialize the tangents as the average of the secants.
        m[0] = d[0];
        for (int i = 1; i < n - 1; i++) {
            m[i] = (d[i - 1] + d[i]) * 0.5;
        }
        m[n - 1] = d[n - 2];

        // Update the tangents to preserve monotonicity.
        for (int i = 0; i < n - 1; i++) {
            if (d[i] == 0.0) { // successive Y values are equal
                m[i] = 0.0;
                m[i + 1] = 0.0;
            } else {
                double a = m[i] / d[i];
                double b = m[i + 1] / d[i];
                double h = (double) Math.hypot(a, b);
                if (h > 9.0) {
                    double t = 3.0 / h;
                    m[i] = t * a * d[i];
                    m[i + 1] = t * b * d[i];
                }
            }
        }
        return new SplineInterpolator2(x, y, m);
    }

    /**
     * Interpolates the value of Y = f(X) for given X. Clamps X to the domain of
     * the spline.
     *
     * @param x The X value.
     * @return The interpolated Y = f(X) value.
     */
    public double interpolate(double x) {
        // Handle the boundary cases.
        final int n = mX.size();
        if (Double.isNaN(x)) {
            return x;
        }
        if (x <= mX.get(0)) {
            return mY.get(0);
        }
        if (x >= mX.get(n - 1)) {
            return mY.get(n - 1);
        }

        // Find the index 'i' of the last point with smaller X.
        // We know this will be within the spline due to the boundary tests.
        int i = 0;
        while (x >= mX.get(i + 1)) {
            i += 1;
            if (x == mX.get(i)) {
                return mY.get(i);
            }
        }

        // Perform cubic Hermite spline interpolation.
        double h = mX.get(i + 1) - mX.get(i);
        double t = (x - mX.get(i)) / h;
        return (mY.get(i) * (1 + 2 * t) + h * mM[i] * t) * (1 - t) * (1 - t)
                + (mY.get(i + 1) * (3 - 2 * t) + h * mM[i + 1] * (t - 1)) * t * t;
    }

    // For debugging.
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        final int n = mX.size();
        str.append("[");
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                str.append(", ");
            }
            str.append("(").append(mX.get(i));
            str.append(", ").append(mY.get(i));
            str.append(": ").append(mM[i]).append(")");
        }
        str.append("]");
        return str.toString();
    }

    public static void main(String[] args) {
        List<Double> x, y;
        x = new ArrayList<>();
        y = new ArrayList<>();
        x.add(1.0);
        y.add(14.0);
        x.add(3.0);
        y.add(5.0);
        x.add(8.0);
        y.add(23.0);
        x.add(10.0);
        y.add(6.0);
        x.add(11.0);
        y.add(7.5);
        x.add(12.0);
        y.add(10.0);
        x.add(18.0);
        y.add(9.0);
        x.add(20.0);
        y.add(11.0);
        x.add(21.0);
        y.add(10.0);
        x.add(22.0);
        y.add(11.0);

        List<Pair<Double, Double>> values = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            values.add(new Pair(x.get(i), y.get(i)));
        }

        SplineInterpolator2 sp = createMonotoneCubicSpline(x, y);
        SplineInterpolator sp2 = new SplineInterpolator(values);
        for (double i = 1; i < 23; i = i + 1) {

            System.out.println(i + " " + sp2.interpolate(i));
            //System.out.println(i);
            //System.out.println(sp.interpolate(i));
        }
        //System.out.println(sp.interpolate(5.5));

        List<Pair<Integer, Pair<VaultEntryType, Double>>> input = new ArrayList<>();
        for (Integer i = x.get(0).intValue(); i <= x.get(x.size() - 1).intValue(); i++) {
            Double tmp;
            if (x.contains(i.doubleValue())) {
                tmp = y.get(x.indexOf(i.doubleValue()));
                System.out.println(i + " = " + tmp);
            } else {
                System.out.println("null " + i);
                tmp = null;
            }
            input.add(new Pair(i, new Pair(VaultEntryType.GLUCOSE_CGM, tmp)));
        }
        List<Pair<Double, Double>> calcValues = new ArrayList<>();
        List<Pair<Integer, Pair<VaultEntryType, Double>>> result = new ArrayList<>();
        VaultEntryType resultType = null;
        Double tmpValue;
        Integer tmpIndex;

        //prepare the input data for interpolation, exclude null-values
        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : input) {
            if (pair != null && pair.getValue() != null && pair.getKey() != null) {
                tmpIndex = pair.getKey();
                tmpValue = pair.getValue().getValue();
                resultType = pair.getValue().getKey();

                if (tmpValue != null) {
                    calcValues.add(new Pair(tmpIndex.doubleValue(), tmpValue));
                }
            }
        }
        SplineInterpolator sI = new SplineInterpolator(calcValues);

        //compute each value that is null
        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : input) {
            if (pair != null && pair.getValue() != null && pair.getKey() != null) {
                tmpIndex = pair.getKey();
                tmpValue = pair.getValue().getValue();

                if (tmpValue == null) {
                    //interpolation call: tmpValue = interpolate(tmpIndex.doubleValue()); vllt mit Runden?
                    tmpValue = sI.interpolate(tmpIndex.doubleValue());
                }
                result.add(new Pair(tmpIndex, new Pair(resultType, tmpValue)));
            }
        }

        for (Pair<Integer, Pair<VaultEntryType, Double>> pair : result) {
            System.out.println(pair.toString());
        }

    }
}
