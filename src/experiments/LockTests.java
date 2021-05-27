package experiments;

import cse332.types.CensusGroup;
import org.junit.Test;
import queryresponders.ComplexLockBased;
import queryresponders.ComplexParallel;
import tests.gitlab.QueryResponderTests;

public class LockTests extends QueryResponderTests {
    static CensusGroup[] data;
    final static int NUM_TRIALS = 10;
    final static int NUM_WARMUPS = 3;
    final static int NUM_QUERIES = 1;

    @Test
    public void main() {

        data = readCensusdata();


        String[] gridSizes = {"10 x 15", "20 x 40", "80 x 20", "40 x 80", "400 x 20",
                "100 x 160", "80 x 400", "200 x 320", "600 x 200", "800 x 400", "900 x 600",
                "4000 x 6000"};


        int[] sizes = {10, 15, 20, 40, 80, 20, 40, 80, 400, 20, 100, 160, 80, 400, 200, 320,
                600, 200, 800, 400, 900, 600, 4000, 6000};

        // Parallel tests
        for (int i = 0; i < sizes.length-2; i+=2) {

            double avgTestTime = testParallel(sizes[i], sizes[(i+1)]);

            System.out.println("ComplexParallel average time for grid size " + sizes[i] + sizes[i+1] + " is: "  +
                    avgTestTime);
        }

        System.out.println("-------------------------------------------------------------------");

        // Lock Tests
        for (int i = 0; i < sizes.length-2; i+=2) {

            double avgtesttimeForLock = testLock(sizes[i], sizes[i+1]);

            System.out.println("ComplexLockBased average time for grid size "
                    + sizes[i] + " x " + sizes[i+1]  + " is " +  avgtesttimeForLock);

        }



    }

    public double testLock(int numColumns, int numRows) {
        double totalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long startTime = System.currentTimeMillis();


            // Simple sequential grids

            ComplexLockBased complexLock = new ComplexLockBased(data, numColumns, numRows);


            long endTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                totalTime += (endTime-startTime);
            }
        }
        double averageRuntime = totalTime/ (NUM_TRIALS - NUM_WARMUPS);

        return averageRuntime;
    }

    public double testParallel(int numColumns, int numRows) {
        double totalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long startTime = System.currentTimeMillis();


            // Simple sequential grids


            ComplexParallel complexParallel = new ComplexParallel(data, numColumns, numRows);


            long endTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                totalTime += (endTime-startTime);
            }
        }
        double averageRuntime = totalTime/ (NUM_TRIALS - NUM_WARMUPS);

        return averageRuntime;
    }
}
