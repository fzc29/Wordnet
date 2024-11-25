package ngrams;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int year = startYear; year <= endYear; year++) {
            if (ts.get(year) != null) {
                this.put(year, ts.get(year));
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> yearList = new ArrayList<>();
        for (int i : this.keySet()) {
            if (this.get(i) != null) {
                yearList.add(i);
            }
        }
        return yearList;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> dat = new ArrayList<>();
        for (int item : years()) {
            dat.add(this.get(item));
        }
        return dat;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        if (this.size() == 0 && ts.size() == 0) {
            return new TimeSeries();
        }
        TimeSeries newTs = new TimeSeries(this, MIN_YEAR, MAX_YEAR);

        for (int year : ts.years()) {
            if (newTs.get(year) != null) {
                newTs.put(year, newTs.get(year) + ts.get(year));
            } else {
                newTs.put(year, ts.get(year));
            }
        }
        return newTs;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries div = new TimeSeries();
        for (int i : this.years()) {
            if (ts.get(i) == null) {
                throw new IllegalArgumentException("not divisible");
            } else {
                div.put(i, this.get(i) / ts.get(i));
            }
        }
        return div;
    }

}
