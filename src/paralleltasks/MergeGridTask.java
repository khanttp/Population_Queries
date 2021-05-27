package paralleltasks;

import java.util.concurrent.RecursiveAction;

/*
   1) This class is used by PopulateGridTask to merge two grids in parallel
   2) SEQUENTIAL_CUTOFF refers to the maximum number of grid cells that should be processed by a single parallel task
 */

public class MergeGridTask extends RecursiveAction {
    int[][] left, right;
    int rowLo, rowHi, colLo, colHi;
    final static int SEQUENTIAL_CUTOFF = 100;

    public MergeGridTask(int[][] left, int[][] right, int rowLo, int rowHi, int colLo, int colHi) {

        this.left = left;
        this.right = right;
        this.rowLo = rowLo;
        this.rowHi = rowHi;
        this.colLo = colLo;
        this.colHi = colHi;

    }

    protected void compute() {

        if ((rowHi - rowLo) <= SEQUENTIAL_CUTOFF || (colHi - colLo) <= SEQUENTIAL_CUTOFF) {
            sequentialMergeGrid(left, right, rowLo, rowHi, colLo, colHi);
        } else {

            int mid = rowLo + (rowHi - rowLo) / 2;

            MergeGridTask leftRow = new MergeGridTask(left, right, rowLo, mid, colLo, colHi);

            MergeGridTask rightCol = new MergeGridTask(left, right, mid, rowHi, colLo, colHi);

            leftRow.fork();

            rightCol.compute();

            leftRow.join();

        }
    }



    private void sequentialMergeGrid(int[][] left, int[][] right, int rowLo, int rowHi, int colLo, int colHi) {
        for (int i = rowLo; i < rowHi; i++) {
            for (int j = colLo; j < colHi; j++) {
                left[i][j] += right[i][j];
            }
        }
    }
}
