package queryresponders;

import cse332.interfaces.QueryResponder;
import cse332.types.CensusGroup;
import cse332.types.CornerFindingResult;
import cse332.types.MapCorners;
import paralleltasks.CornerFindingTask;
import paralleltasks.PopulateGridTask;

import java.util.concurrent.ForkJoinPool;

public class ComplexParallel extends QueryResponder {

    CensusGroup[] censusData;
    int[][] grid;

    double cellHeight;
    double cellWidth;
    int numColumns;
    int numRows;

    MapCorners corners;
    CornerFindingResult cornerResult;

    private static final ForkJoinPool POOL = new ForkJoinPool();

    public ComplexParallel(CensusGroup[] censusData, int numColumns, int numRows) {
        this.censusData = censusData;
        this.numColumns = numColumns;
        this.numRows = numRows;

        cornerResult = POOL.invoke(new CornerFindingTask(censusData,0,censusData.length));

        this.totalPopulation = cornerResult.getTotalPopulation();
        this.corners = cornerResult.getMapCorners();

        this.cellWidth = (this.corners.east - this.corners.west)/numColumns;
        this.cellHeight = (this.corners.north - this.corners.south)/numRows;

        this.grid = POOL.invoke(new PopulateGridTask(censusData,0,censusData.length,numRows,numColumns,corners,cellWidth,cellHeight));


        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numColumns; j++) {
                grid[i][j] += (grid[i - 1][j] + grid[i][j - 1]) - grid[i - 1][j - 1];
            }
        }


    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {

        assert west >= 1 && west <= this.numColumns && south >= 1 && south <= this.numRows && east >= west && east <= this.numColumns
                && north >= south && north <= this.numRows;

        int northEast = grid[north][east];
        int southEast = grid[south - 1][east];
        int southWest = grid[south - 1][west - 1];
        int northWest = grid[north][west - 1];

        return (northEast - southEast - northWest) + southWest;

    }


}
