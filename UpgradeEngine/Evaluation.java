package UpgradeEngine;

public class Evaluation {
    public static final int pawnValue = 100;
    public static final int knightValue = 300;
    public static final int bishopValue = 320;
    public static final int rookValue = 500;
    public static final int queenValue = 900;
    public static int whitePawnNum;
    public static int blackPawnNum;
    static final double endgameMaterialStart = rookValue * 2 + bishopValue + knightValue;
    public static int Evaluate(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean isWhite)
    {
        whitePawnNum = Long.bitCount(WP);
        blackPawnNum = Long.bitCount(BP);
        int whiteEval = 0;
        int blackEval = 0;

        int whiteMaterial = CountMaterial(WP, WN, WB, WR, WQ);
        int blackMaterial = CountMaterial(BP, BN, BB, BR, BQ);

        int whiteMaterialWithoutPawns = whiteMaterial - whitePawnNum * pawnValue;
        int blackMaterialWithoutPawns = blackMaterial - blackPawnNum * pawnValue;
        double whiteEndgamePhaseWeight = EndgamePhaseWeight(whiteMaterialWithoutPawns);
        double blackEndgamePhaseWeight = EndgamePhaseWeight(blackMaterialWithoutPawns);

        whiteEval += whiteMaterial;
        blackEval += blackMaterial;
        whiteEval += MopUpEval(whiteMaterial, blackMaterial, blackEndgamePhaseWeight, true);
        blackEval += MopUpEval(blackMaterial, whiteMaterial, whiteEndgamePhaseWeight, false);

        whiteEval += EvaluatePieceTable(blackEndgamePhaseWeight, WP, WN, WB, WR, WQ, WK, true);
        blackEval += EvaluatePieceTable(whiteEndgamePhaseWeight, BP, BN, BB, BR, BQ, BK, false);

        int eval = whiteEval - blackEval + CheckMateEvaluation(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, isWhite);
        int perspective = isWhite ? 1 : -1;
        return eval * perspective;
    }

    public static int MopUpEval(int myMaterial, int opponentMaterial, double endgameWeight, boolean isWhite)
    {
        int mopUpScore = 0;
        if (myMaterial > opponentMaterial + pawnValue * 2 && endgameWeight > 0)
        {
            if (isWhite)
            {
                int myKingSquare = Long.numberOfTrailingZeros(UserInterface.WK);
                int opponentKingSquare = Long.numberOfTrailingZeros(UserInterface.BK);
                mopUpScore += Calculation.centreManhattanDistance[opponentKingSquare] * 10;
                mopUpScore += (14 - Calculation.NumRookMovesToReachSquare(myKingSquare, opponentKingSquare)) * 4;

                return (int)(mopUpScore * endgameWeight);
            }
            else
            {
                int myKingSquare = Long.numberOfTrailingZeros(UserInterface.BK);
                int opponentKingSquare = Long.numberOfTrailingZeros(UserInterface.WK);
                mopUpScore += Calculation.centreManhattanDistance[opponentKingSquare] * 10;
                mopUpScore += (14 - Calculation.NumRookMovesToReachSquare(myKingSquare, opponentKingSquare)) * 4;

                return (int)(mopUpScore * endgameWeight);
            }
        }
        return 0;
    }

    public static int CountMaterial(long P, long N, long B, long R, long Q)
    {
        int res = 0;
        for (int i = 0; i < 64; i++)
        {
            if (((1L << i) & P) != 0)
            {
                res += pawnValue;
            }
            else if (((1L << i) & N) != 0)
            {
                res += knightValue;
            }
            else if (((1L << i) & B) != 0)
            {
                res += bishopValue;
            }
            else if (((1L << i) & R) != 0)
            {
                res += rookValue;
            }
            else if (((1L << i) & Q) != 0)
            {
                res += queenValue;
            }
        }

        return res;
    }

    public static double EndgamePhaseWeight (int materialCountWithoutPawns) {
        final double multiplier = 1.0 / endgameMaterialStart;
        return 1.0 - Math.min(1.0, materialCountWithoutPawns * multiplier * 1.0);
    }

    public static int EvaluatePieceTable(double endgamePhaseWeight, long P, long N, long B, long R, long Q, long K, boolean isWhite)
    {
        int res = 0;
        for (int i = 0; i < 63; i++)
        {
            if (((1L << i) & P) != 0)
            {
                res += PiecesTable.pawns[isWhite ? i : 63 - i];
            }
            else if (((1L << i) & N) != 0)
            {
                res += PiecesTable.knights[isWhite ? i : 63 - i];
            }
            else if (((1L << i) & B) != 0)
            {
                res += PiecesTable.bishops[isWhite ? i : 63 - i];
            }
            else if (((1L << i) & R) != 0)
            {
                res += PiecesTable.rooks[isWhite ? i : 63 - i];
            }
            else if (((1L << i) & Q) != 0)
            {
                res += PiecesTable.queens[isWhite ? i : 63 - i];
            }
            else if (((1L << i) & K) != 0)
            {
                res += (int)(PiecesTable.kingMiddle[isWhite ? i : 63 - i] * (1 - endgamePhaseWeight));
            }
        }

        return res;
    }

    public static int CheckMateEvaluation(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean isWhite)
    {
        if (isWhite)
        {
            if (Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty())
            {
                if ((Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0)
                {
                    return -50000;
                }
                else
                {
                    return 0;
                }
            }
            if (Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty())
            {
                if ((Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & BK) != 0)
                {
                    return 50000;
                }
                else
                {
                    return 0;
                }
            }
        }
        else
        {
            if (Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty())
            {
                if ((Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & BK) != 0)
                {
                    return 50000;
                }
                else
                {
                    return 0;
                }
            }
            if (Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty())
            {
                if ((Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0)
                {
                    return -50000;
                }
                else
                {
                    return 0;
                }
            }
        }
        return 0;
    }
}