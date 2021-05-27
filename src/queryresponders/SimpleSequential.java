package queryresponders;

import cse332.interfaces.QueryResponder;
import cse332.types.CensusGroup;
import cse332.types.MapCorners;

public class SimpleSequential extends QueryResponder {

    private CensusGroup[] censusData;
    private int numColumns;
    private int numRows;
    private double cellWidth;
    private double cellHeight;
    private double minLatitude;
    private double maxLatitude;
    private double maxLongitude;
    private double minLongitude;

    MapCorners corners;

    // Finds the four corners of the U.S. rectangle
    // makes sure total population field is updated to total US population
    // O(n) algorithm
    public SimpleSequential(CensusGroup[] censusData, int numColumns, int numRows) {

        this.corners = new MapCorners(censusData[0]);

        for (CensusGroup group : censusData) {
            this.corners = corners.encompass(new MapCorners(group));
            this.totalPopulation += group.population;
        }

        // censusData holds all the data parsed from 2010 U.S. census
        this.censusData = censusData;
        this.numColumns = numColumns;
        this.numRows = numRows;
        this.cellWidth = (this.corners.east - this.corners.west) / numColumns;
        this.cellHeight = (this.corners.north - this.corners.south) / numRows;
    }

    @Override
    public int getPopulation(int west, int south, int east, int north) {

        if (west < 1 || west > this.numColumns || south < 1 || south > this.numRows || east < west ||
                east > this.numColumns
                || north < south || north > this.numRows) {
            throw new IllegalArgumentException();
        }

        // calculate the corners of the yellow-rectangle
        double westCorner = cellWidth*(west-1) + this.corners.west;
        double eastCorner = cellWidth*(east) + this.corners.west;
        double northCorner = cellHeight*(north) + this.corners.south;
        double southCorner = cellHeight*(south-1) + this.corners.south;


        int population = 0;

        // loop through each census group
        for (CensusGroup group: censusData) {

            // get corners of each group
            MapCorners corners = new MapCorners(group);

            // check if group is in query
            if(corners.north <= northCorner && corners.south >= southCorner && corners.east <= eastCorner && corners.west >= westCorner){

                // get group's population
                population += group.population;
            }
        }
        return population;
    }
}
