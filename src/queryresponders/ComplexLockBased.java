package queryresponders;

import cse332.interfaces.QueryResponder;
import cse332.types.CensusGroup;
import cse332.types.CornerFindingResult;
import cse332.types.MapCorners;
import paralleltasks.CornerFindingTask;
import paralleltasks.PopulateLockedGridTask;

import java.util.concurrent.ForkJoinPool;

public class ComplexLockBased extends QueryResponder {
    final static int NUM_THREADS = 4;

    private CensusGroup[] censusData;
    private int numColumns;
    private int numRows;
    private int[][] populationGrid;
    private double cellHeight;
    private double cellWidth;
    private MapCorners corners;

    private static final ForkJoinPool POOL = new ForkJoinPool();

    CornerFindingResult cornerResult;
    public ComplexLockBased(CensusGroup[] censusData, int numColumns, int numRows) {

        this.censusData = censusData;
        this.numColumns = numColumns;
        this.numRows = numRows;


        this.populationGrid = new int[numRows + 1][numColumns + 1];

        cornerResult = POOL.invoke(new CornerFindingTask(censusData,0,censusData.length));

        this.totalPopulation = cornerResult.getTotalPopulation();
        this.corners = cornerResult.getMapCorners();

        this.cellWidth = (this.corners.east - this.corners.west) / numColumns;
        this.cellHeight = (this.corners.north - this.corners.south) / numRows;



        PopulateLockedGridTask[] ts = new PopulateLockedGridTask[NUM_THREADS];
        // Generate initial grid with locked task --> Step 1
        for (int i = 0; i < NUM_THREADS-1; i++) {
            ts[i] = new PopulateLockedGridTask(censusData, i*(censusData.length/NUM_THREADS),
                    (i+1)*(censusData.length/NUM_THREADS),
                    numRows, numColumns, corners, cellWidth, cellHeight, populationGrid, NUM_THREADS);
        }

        // Run NUM_THREAD in current thread
        ts[NUM_THREADS-1] = new PopulateLockedGridTask(censusData,
                (NUM_THREADS-1)*(censusData.length/NUM_THREADS), censusData.length,
                numRows, numColumns, corners, cellWidth, cellHeight, populationGrid, NUM_THREADS);

        for (int i = 0; i < NUM_THREADS-1; i++) {
            ts[i].run();
        }

        ts[NUM_THREADS - 1].run();

        for (int i = 0; i < NUM_THREADS-1; i++) {
            try {
                ts[i].join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                System.exit(1);
            }
        }

        // Step 2
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numColumns; j++) {
                populationGrid[i][j] += (populationGrid[i - 1][j] + populationGrid[i][j - 1]) - populationGrid[i - 1][j - 1];
            }
        }

    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {

        assert west >= 1 && west <= this.numColumns && south >= 1 && south <= this.numRows && east >= west && east <= this.numColumns
                && north >= south && north <= this.numRows;

        int northEast = populationGrid[north][east];
        int southEast = populationGrid[south - 1][east];
        int southWest = populationGrid[south - 1][west - 1];
        int northWest = populationGrid[north][west - 1];

        return (northEast - southEast - northWest) + southWest;
    }
}
