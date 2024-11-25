import ngrams.TimeSeries;

import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/** Unit Tests for the TimeSeries class.
 *  @author Josh Hug
 */
public class TimeSeriesTest {
    @Test
    public void testFromSpec() {
        TimeSeries catPopulation = new TimeSeries();
        catPopulation.put(1991, 0.0);
        catPopulation.put(1992, 100.0);
        catPopulation.put(1994, 200.0);

        TimeSeries dogPopulation = new TimeSeries();
        dogPopulation.put(1994, 400.0);
        dogPopulation.put(1995, 500.0);

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);
        // expected: 1991: 0,
        //           1992: 100
        //           1994: 600
        //           1995: 500

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(1991, 1992, 1994, 1995));

        assertThat(totalPopulation.years()).isEqualTo(expectedYears);

        List<Double> expectedTotal = new ArrayList<>
                (Arrays.asList(0.0, 100.0, 600.0, 500.0));

        for (int i = 0; i < expectedTotal.size(); i += 1) {
            assertThat(totalPopulation.data().get(i)).isWithin(1E-10).of(expectedTotal.get(i));
        }
    }

    @Test
    public void testEmptyBasic() {
        TimeSeries catPopulation = new TimeSeries();
        TimeSeries dogPopulation = new TimeSeries();

        assertThat(catPopulation.years()).isEmpty();
        assertThat(catPopulation.data()).isEmpty();

        TimeSeries totalPopulation = catPopulation.plus(dogPopulation);

        assertThat(totalPopulation.years()).isEmpty();
        assertThat(totalPopulation.data()).isEmpty();
    }

    @Test
    public void testGen() {
        TimeSeries p1 = new TimeSeries();
        TimeSeries p2 = new TimeSeries();

        p1.put(2000, 10.45);
        p1.put(2001, 14.56);
        p1.put(2005, 12.9);
        p1.put(2009, 123.90);

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(2000, 2001, 2005, 2009));

        assertThat(p1.years()).isEqualTo(expectedYears);
        assertThat(p2.years()).isEmpty();

        p2.put(2000, 22.2);
        p2.put(2001, 123.5);
        p2.put(2001, 45.0);
        p2.put(2004, 17.4);
        p2.put(2009, 28.9);
        p2.put(2010, 45.64);

        List<Integer> expectedYears2 = new ArrayList<>
                (Arrays.asList(2000, 2001, 2004, 2009, 2010));

        assertThat(p2.years()).isEqualTo(expectedYears2);

        List<Double> expectedData = new ArrayList<>
                (Arrays.asList(10.45, 14.56, 12.9, 123.90));
        assertThat(p1.data()).isEqualTo(expectedData);

        List<Double> expectedData2 = new ArrayList<>
                (Arrays.asList(22.2, 45.0, 17.4, 28.9, 45.64));
        assertThat(p2.data()).isEqualTo(expectedData2);
    }


    @Test
    public void testDiv() {
        TimeSeries p1 = new TimeSeries();
        TimeSeries p2 = new TimeSeries();

        p1.put(2000, 10.45);
        p1.put(2001, 14.56);
        p1.put(2009, 123.90);

        p2.put(2000, 22.2);
        p2.put(2001, 123.5);
        p2.put(2001, 45.0);
        p2.put(2004, 17.4);
        p2.put(2009, 28.9);
        p2.put(2010, 45.64);


        TimeSeries p3 = p1.plus(p2);

        List<Integer> expectedYears = new ArrayList<>
                (Arrays.asList(2000, 2001, 2004, 2009, 2010));

        List<Double> expectedData = new ArrayList<>
                (Arrays.asList(32.65, 59.56, 17.4, 152.8, 45.64));

        assertThat(p3.years()).isEqualTo(expectedYears);
        assertThat(p3.data()).isEqualTo(expectedData);
    }
} 