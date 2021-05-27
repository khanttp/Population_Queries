package paralleltasks;

import cse332.types.CensusGroup;
import cse332.types.CornerFindingResult;
import cse332.types.MapCorners;

import java.util.concurrent.RecursiveTask;

/*
   1) This class will do the corner finding from version 1 in parallel for use in versions 2, 4, and 5
   2) SEQUENTIAL_CUTOFF refers to the maximum number of census groups that should be processed by a single parallel task
   3) The compute method returns a result of a MapCorners and an Integer.
        - The MapCorners will represent the extremes/bounds/corners of the entire land mass (latitude and longitude)
        - The Integer value should represent the total population contained inside the MapCorners
 */

public class CornerFindingTask extends RecursiveTask<CornerFindingResult> {
    CensusGroup[] censusData;
    int lo, hi;

    final int SEQUENTIAL_CUTOFF = 10000;

    public CornerFindingTask(CensusGroup[] censusData, int lo, int hi) {

        this.lo = lo;
        this.hi = hi;
        this.censusData = censusData;
    }

    // Returns a pair of MapCorners for the grid and Integer for the total population
    // Key = grid, Value = total population
    @Override
    protected CornerFindingResult compute() {

        if (hi - lo < SEQUENTIAL_CUTOFF) {
            return sequentialCornerFinding(censusData, lo, hi);
        }

        int mid = lo + (hi-lo)/2;


        CornerFindingTask left = new CornerFindingTask(censusData, lo, mid);
        CornerFindingTask right = new CornerFindingTask(censusData, mid, hi);
        left.fork();

        CornerFindingResult rightResult = right.compute();
        CornerFindingResult leftResult = left.join();

        MapCorners rightMapCorner = rightResult.getMapCorners();
        rightMapCorner = rightMapCorner.encompass(leftResult.getMapCorners());

        int totalPopulation = rightResult.getTotalPopulation() + leftResult.getTotalPopulation();

        return new CornerFindingResult(rightMapCorner, totalPopulation);
    }

    private CornerFindingResult sequentialCornerFinding(CensusGroup[] censusGroups, int lo, int hi) {

        MapCorners mapCorners = new MapCorners(censusGroups[lo]);
        int totalPopulation = censusGroups[lo].population;

        int i = lo+1;
        while (i < hi) {
            mapCorners = mapCorners.encompass(new MapCorners(censusGroups[i]));
            totalPopulation += censusGroups[i].population;
            i++;
        }


        return new CornerFindingResult(mapCorners, totalPopulation);

    }
}
