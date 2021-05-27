package paralleltasks;

import cse332.types.CensusGroup;
import cse332.types.MapCorners;

import java.util.concurrent.RecursiveTask;
/*
   1) This class is the parallel version of the getPopulation() method from version 1 for use in version 2
   2) SEQUENTIAL_CUTOFF refers to the maximum number of census groups that should be processed by a single parallel task
   3) The double parameters(w, s, e, n) represent the bounds of the query rectangle
   4) The compute method returns an Integer representing the total population contained in the query rectangle
 */
public class GetPopulationTask extends RecursiveTask<Integer> {
    CensusGroup[] censusGroups;
    int lo, hi;
    double w, s, e, n;

    final static int SEQUENTIAL_CUTOFF = 1000;

    public GetPopulationTask(CensusGroup[] censusGroups, int lo, int hi, double w, double s, double e, double n) {
        this.censusGroups = censusGroups;
        this.lo = lo;
        this.hi = hi;
        this.w = w;
        this.s = s;
        this.e = e;
        this.n = n;
    }

    // Returns a number for the total population
    @Override
    protected Integer compute() {

        if (hi - lo <= SEQUENTIAL_CUTOFF) {
            return sequentialGetPopulation(censusGroups, lo, hi, w, s, e, n);
        }

        int mid = lo + (hi - lo) / 2;

        GetPopulationTask left = new GetPopulationTask(censusGroups, lo, mid, w, s, e, n);
        GetPopulationTask right = new GetPopulationTask(censusGroups, mid, hi, w, s, e, n);

        left.fork();

        // changed
        return right.compute() + left.join();

    }


    private Integer sequentialGetPopulation(CensusGroup[] censusGroups, int lo, int hi, double westCorner,
                                            double southCorner, double eastCorner, double northCorner) {
        int population = 0;

        int i = lo;
        while (i < hi) {

            // get corners of each group
            MapCorners corners = new MapCorners(censusGroups[i]);

            // check if group is in query
            if (corners.north <= northCorner && corners.south >= southCorner && corners.east <= eastCorner && corners.west >= westCorner) {

                // get group's population
                population += censusGroups[i].population;
            }
            i++;
        }
        return population;
    }

}
