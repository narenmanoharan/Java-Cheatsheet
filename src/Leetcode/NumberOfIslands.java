package Leetcode;

/**
 * Created by Naren on 5/22/17.
 */
public class NumberOfIslands {

  public static int numIslands(int[][] grid) {
    if(grid == null || grid.length == 0 || grid[0].length == 0) {
      return 0;
    }
    int i = grid.length;
    int j = grid[0].length;
    int numOfIslands = 0;
    for (int row = 0; row < i; row++) {
      for(int col = 0; col < j; col++) {
        if(grid[row][col] == 1){
          numOfIslands++;
          sinkNeighbours(grid, row, col);
        }
      }
    }
    return numOfIslands;
  }


  private static void sinkNeighbours(int[][] grid, int row, int col) {
    int m = grid.length;
    int n = grid[0].length;
    if(row < 0 || row >= m || col < 0 || col >= n || grid[row][col] == 0) {
        return;
    }

    grid[row][col] = 0;

    sinkNeighbours(grid, row-1, col);
    sinkNeighbours(grid, row+1, col);
    sinkNeighbours(grid, row, col+1);
    sinkNeighbours(grid, row, col-1);
  }

  public static void main(String[] args) {
    int[][] grid = new int[][] {{0,0,0,1,1,0,0},
                                {0,1,0,0,1,1,0},
                                {1,1,0,1,0,0,1},
                                {0,0,0,0,0,1,0},
                                {1,1,0,0,0,0,0},
                                {0,0,0,1,0,0,0}};

    System.out.println(numIslands(grid));
  }
}
