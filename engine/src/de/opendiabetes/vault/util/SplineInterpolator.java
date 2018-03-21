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

import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author
 */
public class SplineInterpolator {

    private final List<Pair<Double, Double>> values;
    private final double[] moments;

    /**
     * Gets a set of x and y values, uses them as basicdata to interpolate given
     * y values.<br>
     * Uses the Fritsch-Carlson method for computing the splines.<br>
     * See
     * <a href="http://en.wikipedia.org/wiki/Monotone_cubic_interpolation">http://en.wikipedia.org/wiki/Monotone_cubic_interpolation</a><p>
     * Won't work with sets with less than 2 elements, x values have to be
     * sorted in ascending order!
     *
     *
     * @param values Pairs of x and y values. Have to be null-checked!
     */
    public SplineInterpolator(List<Pair<Double, Double>> values) {

        this.values = values;
        if (values != null && values.size() >= 2) {

            int numOfXValues = values.size();
            double[] d = new double[numOfXValues - 1];
            double[] moments = new double[numOfXValues];
            double a, b, hInterval, t;

            for (int i = 0; i < numOfXValues - 1; i++) {
                hInterval = getXvalue(i + 1) - getXvalue(i);

                d[i] = (getYvalue(i + 1) - getYvalue(i)) / hInterval;
            }

            moments[0] = d[0];
            for (int i = 1; i < numOfXValues - 1; i++) {
                moments[i] = (d[i - 1] + d[i]) * 0.5;
            }
            moments[numOfXValues - 1] = d[numOfXValues - 2];

            for (int i = 0; i < numOfXValues - 1; i++) {
                if (d[i] == 0.0) {

                    moments[i] = 0.0;
                    moments[i + 1] = 0.0;

                } else {

                    a = moments[i] / d[i];
                    b = moments[i + 1] / d[i];
                    hInterval = Math.hypot(a, b);

                    if (hInterval > 9.0) {

                        t = 3.0 / hInterval;
                        moments[i] = t * a * d[i];
                        moments[i + 1] = t * b * d[i];

                    }
                }
            }

            this.moments = moments;
        } else {
            this.moments = new double[0];
        }
    }

    private Double getXvalue(int i) {
        return values.get(i).getKey();
    }

    private Double getYvalue(int i) {
        return values.get(i).getValue();
    }

    /**
     * Interpolates the y value to the given x value. The given x value has to
     * be in-bounds of the dataset given in the constructor, meaning x[0] &lt=
     * given-X &lt= x[max] .
     *
     * @param xValue the x value to the searched y value
     * @return the interpolated y value
     */
    public double interpolate(double xValue) {
        double result;

        int numberOfXValues = values.size();
        if (Double.isNaN(xValue) || moments.length == 0) {
            result = xValue;
        } else {

            //checking if x is in-bounds and not outside of the spline interpolation
            if (xValue <= getXvalue(0)) {
                result = getYvalue(0);
            } else if (xValue >= getXvalue(numberOfXValues - 1)) {
                result = getYvalue(numberOfXValues - 1);
            } else {
                double hInterval, t;

                // Find interval index, meaning x lies between interval and interval+1
                int startOfInterval = 0;
                while (xValue >= getXvalue(startOfInterval + 1)) {
                    if (xValue == getXvalue(startOfInterval)) {
                        return getYvalue(startOfInterval);
                    }
                    startOfInterval++;
                }

                // Cubic Hermite spline interpolation
                hInterval = getXvalue(startOfInterval + 1) - getXvalue(startOfInterval);
                t = (xValue - getXvalue(startOfInterval)) / hInterval;
                result = (getYvalue(startOfInterval) * (1 + 2 * t)
                        + hInterval * moments[startOfInterval] * t) * (1 - t) * (1 - t)
                        + (getYvalue(startOfInterval + 1) * (3 - 2 * t)
                        + hInterval * moments[startOfInterval + 1] * (t - 1)) * t * t;
            }
        }
        return result;
    }

}
