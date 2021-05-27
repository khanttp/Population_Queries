package experiments;

import cse332.types.CensusGroup;
import org.junit.Test;
import queryresponders.ComplexParallel;
import queryresponders.ComplexSequential;
import queryresponders.SimpleParallel;
import queryresponders.SimpleSequential;
import tests.gitlab.QueryResponderTests;

public class PreProcessTests extends QueryResponderTests {

    static CensusGroup[] data;
    final static int NUM_TRIALS = 10;
    final static int NUM_WARMUPS = 4;
    final static int NUM_QUERIES = 20;


    @Test
    public void main() {
        data = readCensusdata();

        // Sequential Grids
        SimpleSequential simpleSequential = new SimpleSequential(data, 500, 100);
        ComplexSequential complexSequential = new ComplexSequential(data, 500, 100);


        // Parallel Grids
        SimpleParallel simpleParallel = new SimpleParallel(data, 500, 100);
        ComplexParallel complexParallel = new ComplexParallel(data, 500, 100);


        System.out.println("Tests with " + NUM_QUERIES +  " queries: ");

        System.out.println("-------------------------------------------------------------------");

        System.out.println("SimpleSequential average runtime: "  + testSimpleSeq(simpleSequential));
        System.out.println("ComplexSequential average runtime: " + testComplexSeq(complexSequential));
        System.out.println("SimpleParallel average runtime: " + testSimplePara(simpleParallel));
        System.out.println("ComplexParallel average runtime: " + testComplexPara(complexParallel));

    }

    public double testSimpleSeq(SimpleSequential simpleSequential) {
        // Choose random numbers between 100, 500
        double totalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long startTime = System.currentTimeMillis();


            // public int getPopulation(int west, int south, int east, int north) {


            // Vary number of queries
            for (int j = 0; j < NUM_QUERIES; j++) {

//                int west = (int) ((Math.random() * (500 - 100)) + 100);
//                int south = (int) ((Math.random() * (500 - 100)) + 100);
//                int east = (int) ((Math.random() * (500 - 100)) + 100);
//                int north = (int) ((Math.random() * (500 - 100)) + 100);
//
//                if (east < west) {
//                    west = east;
//                    east = west;
//                }
//
//                if (north < south) {
//                    north = south;
//                    south = north;
//                }


                simpleSequential.getPopulation(51, 1, 500, 100);
            }



            long endTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                totalTime += (endTime-startTime);
            }
        }
        double averageRuntime = totalTime/ (NUM_TRIALS - NUM_WARMUPS);

        return averageRuntime;
    }

    public double testComplexSeq(ComplexSequential complexSequential) {
        // Choose random numbers between 100, 500
        double totalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long startTime = System.currentTimeMillis();


            // public int getPopulation(int west, int south, int east, int north) {


            // Vary number of queries
            for (int j = 0; j < NUM_QUERIES; j++) {

//                int west = (int) ((Math.random() * (500 - 100)) + 100);
//                int south = (int) ((Math.random() * (500 - 100)) + 100);
//                int east = (int) ((Math.random() * (500 - 100)) + 100);
//                int north = (int) ((Math.random() * (500 - 100)) + 100);
//
//                if (east < west) {
//                    west = east;
//                    east = west;
//                }
//
//                if (north < south) {
//                    north = south;
//                    south = north;
//                }

                complexSequential.getPopulation(51, 1, 500, 100);
            }



            long endTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                totalTime += (endTime-startTime);
            }
        }
        double averageRuntime = totalTime/ (NUM_TRIALS - NUM_WARMUPS);

        return averageRuntime;
    }

    public double testSimplePara(SimpleParallel simpleParallel) {
        // Choose random numbers between 100, 500
        double totalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long startTime = System.currentTimeMillis();


            // public int getPopulation(int west, int south, int east, int north) {


            // Vary number of queries
            for (int j = 0; j < NUM_QUERIES; j++) {

//                int west = (int) ((Math.random() * (500 - 100)) + 100);
//                int south = (int) ((Math.random() * (500 - 100)) + 100);
//                int east = (int) ((Math.random() * (500 - 100)) + 100);
//                int north = (int) ((Math.random() * (500 - 100)) + 100);
//
//                if (east < west) {
//                    west = east;
//                    east = west;
//                }
//
//                if (north < south) {
//                    north = south;
//                    south = north;
//                }

                simpleParallel.getPopulation(51, 1, 500, 100);
            }



            long endTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                totalTime += (endTime-startTime);
            }
        }
        double averageRuntime = totalTime/ (NUM_TRIALS - NUM_WARMUPS);

        return averageRuntime;
    }

    public double testComplexPara(ComplexParallel complexParallel) {
        // Choose random numbers between 100, 500
        double totalTime = 0;
        // Time get_population on simple sequential
        for (int i = 0; i < NUM_TRIALS; i++) {
            long startTime = System.currentTimeMillis();


            // public int getPopulation(int west, int south, int east, int north) {


            // Vary number of queries
            for (int j = 0; j < NUM_QUERIES; j++) {

//                int west = (int) ((Math.random() * (500 - 100)) + 100);
//                int south = (int) ((Math.random() * (500 - 100)) + 100);
//                int east = (int) ((Math.random() * (500 - 100)) + 100);
//                int north = (int) ((Math.random() * (500 - 100)) + 100);
//
//
//                if (east < west) {
//                    west = east;
//                    east = west;
//                }
//
//                if (north < south) {
//                    north = south;
//                    south = north;
//                }

                complexParallel.getPopulation(51, 1, 500, 100);
            }



            long endTime = System.currentTimeMillis();

            if (NUM_WARMUPS <= i) {
                totalTime += (endTime-startTime);
            }
        }
        double averageRuntime = totalTime/ (NUM_TRIALS - NUM_WARMUPS);

        return averageRuntime;
    }

}
