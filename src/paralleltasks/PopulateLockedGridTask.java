package paralleltasks;

import cse332.types.CensusGroup;
import cse332.types.MapCorners;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
   1) This class is used in version 5 to create the initial grid holding the total population for each grid cell
        - You should not be using the ForkJoin framework but instead should make use of threads and locks
        - Note: the resulting grid after all threads have finished running should be the same as the final grid from
          PopulateGridTask.java
 */

public class PopulateLockedGridTask extends Thread{
    CensusGroup[] censusGroups;
    int lo, hi, numRows, numColumns;
    MapCorners corners;
    double cellWidth, cellHeight;
    int[][] populationGrid;
    Lock[][] lockGrid;
    private int NUM_THREADS;


    public PopulateLockedGridTask(CensusGroup[] censusGroups, int lo, int hi, int numRows, int numColumns, MapCorners corners,
                                  double cellWidth, double cellHeight, int[][] popGrid, int NUM_THREADS) {

        this.censusGroups = censusGroups;
        this.lo = lo;
        this.hi = hi;
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.corners = corners;
        this.NUM_THREADS = NUM_THREADS;
        this.populationGrid = popGrid;
        this.lockGrid = new Lock[numRows+1][numColumns+1];


        // Fill lock grid with locks
        for (int i = 0; i <= numRows; i++) {
            for (int j = 0; j <= numColumns; j++) {
                this.lockGrid[i][j] = new ReentrantLock();
            }
        }

    }

    @Override
    public void run() {

        // thread is populate locked grid task

        for (int i = lo; i < hi; i++) {
            int xrows =  (int)((censusGroups[i].latitude - corners.south)/cellHeight) + 1;
            int ycols =  (int)((censusGroups[i].longitude - corners.west)/cellWidth) + 1;

            if(xrows >= numRows) {
                xrows = populationGrid.length - 1;
            }
            if(ycols >= numColumns) {
                ycols = populationGrid[0].length - 1;
            }

            // We want to lock each grid cell intialization
            lockGrid[xrows][ycols].lock();
            populationGrid[xrows][ycols] += censusGroups[i].population;
            lockGrid[xrows][ycols].unlock();
        }

    }

}
