package UpgradeEngine;

public class Calculation {
    public static int[] centreManhattanDistance = new int[64];
    public static int[][] orthogonalDistance = new int[64][64];
    public static void init()
    {
        for (int i = 0; i < 64; i++)
        {
            int fileDstFromCentre = Math.max(3 - (i % 8), (i % 8) - 4);
            int rankDstFromCentre = Math.max(3 - (i / 8), (i / 8) - 4);
            centreManhattanDistance[i] = fileDstFromCentre + rankDstFromCentre;
            for (int j = 0; j < 64; j++)
            {
                int rankDistance = Math.abs (i / 8 - j / 8);
                int fileDistance = Math.abs (i % 8 - j % 8);
                orthogonalDistance[i][j] = fileDistance + rankDistance;
            }
        }
    }


    public static int NumRookMovesToReachSquare (int startSquare, int targetSquare) {
        return orthogonalDistance[startSquare][targetSquare];
    }
}
