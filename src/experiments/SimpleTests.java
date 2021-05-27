package experiments;

import cse332.types.CensusGroup;
import org.junit.Test;
import queryresponders.SimpleParallel;
import queryresponders.SimpleSequential;
import tests.gitlab.QueryResponderTests;

public class SimpleTests extends QueryResponderTests{
    static CensusGroup[] data;
    final static int NUM_TRIALS = 10;
    final static int NUM_WARMUPS = 4;
    final static int NUM_QUERIES = 1000;

    @Test
    public void main() {
        data = readCensusdata();

        SimpleParallel parallelGrid = new SimpleParallel(data, 500, 100);
        SimpleSequential sequentialGrid = new SimpleSequential(data, 500, 100);

        // Cutoff values: 500, 1000, 2000, 4000, 8000, 16000
        // Compare every cut off value with the runtime of simple sequential
        double parallelTotalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long parallelStartTime = System.currentTimeMillis();

            for (int j = 0; j < NUM_QUERIES; j++) {
                parallelGrid.getPopulation(51, 1, 500, 100);

            }


            long parallelEndTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                parallelTotalTime += (parallelEndTime-parallelStartTime);
            }
        }
        double parallelAverageRuntime = parallelTotalTime/ (NUM_TRIALS - NUM_WARMUPS);
        System.out.println("Parallel average run time: " + parallelAverageRuntime);


        // 500 cutoff, time: 8.5
        // 1000 cutoff, time: 8.333333333333334
        // 2000 cutoff, time: 8.166666666666666

        // 4000 cutoff, time: 8.833333333333334
        // 8000 cutoff, time: 8.833333333333334
        // 16000 cutoff, time: 7.333333333333333


        
        double sequentialTotalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long startTime = System.currentTimeMillis();


            // Simple sequential grids

            for (int j = 0; j < NUM_QUERIES; j++) {
                sequentialGrid.getPopulation(51, 1, 500, 100);

            }



            long endTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                sequentialTotalTime += (endTime-startTime);
            }
        }
        double sequentialAverageRuntime = sequentialTotalTime/ (NUM_TRIALS - NUM_WARMUPS);
        System.out.println("Sequential average run time: " + sequentialAverageRuntime);
    }
}
