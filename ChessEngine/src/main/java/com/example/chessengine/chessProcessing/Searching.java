package com.example.chessengine.chessProcessing;

import com.example.chessengine.rest.ChessGameController;

import java.util.*;

public class Searching {
    static int[][] mvv_lva = {
            {105, 205, 305, 405, 505, 605, 105, 205, 305, 405, 505, 605},
            {104, 204, 304, 404, 504, 604, 104, 204, 304, 404, 504, 604},
            {103, 203, 303, 403, 503, 603, 103, 203, 303, 403, 503, 603},
            {102, 202, 302, 402, 502, 602, 102, 202, 302, 402, 502, 602},
            {101, 201, 301, 401, 501, 601, 101, 201, 301, 401, 501, 601},
            {100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600},

            {105, 205, 305, 405, 505, 605, 105, 205, 305, 405, 505, 605},
            {104, 204, 304, 404, 504, 604, 104, 204, 304, 404, 504, 604},
            {103, 203, 303, 403, 503, 603, 103, 203, 303, 403, 503, 603},
            {102, 202, 302, 402, 502, 602, 102, 202, 302, 402, 502, 602},
            {101, 201, 301, 401, 501, 601, 101, 201, 301, 401, 501, 601},
            {100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600}
    };

    static String[][] killerMoves = new String[100][100];

    static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
    static int ply;
    public static String bestMove = "";
    static boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;
    static int valueTemp = 0;

    public static int counter = 0;

    public static int negamax(int depth, int alpha, int beta, boolean isWhite) {
        updateValue();
        String moves = isWhite ? Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ)
                : Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
        if (depth == 0) {
            return Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, isWhite);
        }

        int value = -Integer.MAX_VALUE;
        for (int i = 0; i < moves.length(); i += 4) {
            Moves.moveOnBoard(moves.substring(i, i + 4), WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, isWhite);
            updateValue();

            BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
//            valueTemp = value;

            value = Math.max(value, -negamax(depth - 1, -beta, -alpha, !isWhite));
//            if (value >= valueTemp && isWhite)
//            {
            System.out.println(moves.substring(i, i + 4));
            System.out.println(Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, isWhite));
            System.out.println(Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, !isWhite));
            System.out.println(counter++);
            System.out.println("------------------");
//            }
//            Moves.undoMove(ChessGameController.HISTORIC_MOVES, ChessGameController.HISTORIC_PIECES);
            Moves.undoMove2();
            updateValue();
            if (alpha >= value) {
                alpha = value;
//                System.out.println(moves.substring(i, i + 4));
            }
//            alpha = Math.max(alpha, value);
            if (alpha >= beta) {
                break;
            }
            if (isWhite) {
                if (Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty()) {
                    if ((Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & BK) != 0) {
                        return -50000;
                    } else {
                        return 0;
                    }
                }
            } else {
                if (Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty()) {
                    if ((Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                        return -50000;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return value;
    }

    public static void updateValue() {
        WP = ChessGameController.WP;
        WN = ChessGameController.WN;
        WB = ChessGameController.WB;
        WR = ChessGameController.WR;
        WQ = ChessGameController.WQ;
        WK = ChessGameController.WK;
        BP = ChessGameController.BP;
        BN = ChessGameController.BN;
        BB = ChessGameController.BB;
        BR = ChessGameController.BR;
        BQ = ChessGameController.BQ;
        BK = ChessGameController.BK;
        EP = ChessGameController.EP;

        CWK = ChessGameController.CWK;
        CWQ = ChessGameController.CWQ;
        CBK = ChessGameController.CBK;
        CBQ = ChessGameController.CBQ;

        WhiteToMove = ChessGameController.WhiteToMove;
    }

    public static int Negamax2(int depth, int alpha, int beta) {
        updateValue();
        String captureMoves = Moves.onlyCaptureMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, WhiteToMove);
        String moves = WhiteToMove ? Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ)
                : Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
        moves = SortMove(moves);
        if (WhiteToMove) {
            if (Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty()) {
                if ((Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & BK) != 0) {
                    return -50000;
                } else {
                    return 0;
                }
            }
        } else {
            if (Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, false, false, false, false).isEmpty()) {
                if ((Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                    return -50000;
                } else {
                    return 0;
                }
            }
        }
        if (depth == 0) {
//            return Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WhiteToMove);
            return Quiescence(alpha, beta);
        }

        String bestMoveSoFar = "";
        final int preAlpha = alpha;

//        int value = -Integer.MAX_VALUE;
        for (int i = 0; i < moves.length(); i += 4) {
            ply++;
            Moves.moveOnBoard(moves.substring(i, i + 4), WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
            updateValue();

//            BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
            int value = -Negamax2(depth - 1, -beta, -alpha);
            ply--;

//            System.out.println(moves.substring(i, i + 4));
//            System.out.println(Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WhiteToMove));
//            System.out.println(Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, !WhiteToMove));
//            System.out.println(counter++);
//            System.out.println("------------------");

            Moves.undoMove2();
            updateValue();

            if (value >= beta) {
                if (!captureMoves.contains(moves.substring(i, i + 4))) {
                    killerMoves[1][ply] = killerMoves[0][ply];
                    killerMoves[0][ply] = moves.substring(i, i + 4);
                }
                return beta;
            }

            if (value > alpha) {
                alpha = value;
                if (ply == 0) {
                    bestMoveSoFar = moves.substring(i, i + 4);
                }
            }
        }



        if (preAlpha != alpha) {
            bestMove = bestMoveSoFar;
        }

//        System.out.println("Best move: " + bestMove);

        return alpha;
    }

    public static int Quiescence(int alpha, int beta) {
        updateValue();
        int eval = Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WhiteToMove);
        if (eval >= beta) {
            return beta;
        }
        if (eval > alpha) {
            alpha = eval;
        }
        String moves = Moves.onlyCaptureMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, WhiteToMove);

        for (int i = 0; i < moves.length(); i += 4) {
            ply++;
            Moves.moveOnBoard(moves.substring(i, i + 4), WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
            updateValue();

            int score = -Quiescence(-beta, -alpha);
            ply--;
            Moves.undoMove2();
            updateValue();

            if (score >= beta) {
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        return alpha;
    }

    public static int ScoreMove(String move) {
        String captureMoves = Moves.onlyCaptureMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, WhiteToMove);

        int start = (move.charAt(0) - '0') * 8 + move.charAt(1);
        int end = (move.charAt(2) - '0') * 8 + move.charAt(3);

        int attack = 0, victim = 0;
        long[] list = {WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK};
        if (captureMoves.contains(move)) {
            for (int i = 0; i < list.length; i++) {
                if ((list[i] & (1L << start)) != 0) {
                    attack = i;
                }
                if ((list[i] & (1L << end)) != 0) {
                    victim = i;
                }
            }
            return mvv_lva[attack][victim] + 10000;
        } else {
            if (Objects.equals(killerMoves[0][ply], move))
                return 9000;
            else if (Objects.equals(killerMoves[1][ply], move))
                return 8000;
            else
                return 0;
        }
    }

    public static String SortMove(String moves)
    {
        StringBuilder res = new StringBuilder();
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < moves.length(); i += 4)
        {
            map.put(moves.substring(i, i + 4), ScoreMove(moves.substring(i, i + 4)));
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());

        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        for (Map.Entry<String, Integer> l : list)
        {
            res.append(l.getKey());
//            System.out.println(l.getValue());
        }
//        System.out.println(res);
        return res.toString();
    }
}
