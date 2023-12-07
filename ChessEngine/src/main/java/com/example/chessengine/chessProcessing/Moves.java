package com.example.chessengine.chessProcessing;

import com.example.chessengine.rest.ChessGameController;

import java.util.ArrayList;

public class Moves {
    static long WPmt = 0L, WNmt = 0L, WBmt = 0L, WRmt = 0L, WQmt = 0L, WKmt = 0L, BPmt = 0L, BNmt = 0L, BBmt = 0L, BRmt = 0L, BQmt = 0L, BKmt = 0L, EPmt = 0L;
    static boolean CWKm = true, CWQm = true, CBKm = true, CBQm = true, WhiteToMove = true;

    static long FILE_A = 0x101010101010101L;
    static long FILE_H = 0x8080808080808080L;
    static long FILE_AB = 0x303030303030303L;
    static long FILE_GH = 0xC0C0C0C0C0C0C0C0L;
    static long RANK_1 = 0xFF00000000000000L;
    static long RANK_4 = 0xFF00000000L;
    static long RANK_5 = 0xFF000000L;
    static long RANK_8 = 0xFFL;
    static long KNIGHT_SPAN = 43234889994L;
    static long KING_SPAN = 460039L;

    static long NOT_ALLIES;
    static long ENEMIES;
    static long NO_PIECE;
    static long OCCUPIED;
    static long[] CASTLE_ROOKS = {63, 56, 7, 0};

    static long[] RankMasks8 =/*from rank1 to rank8*/
            {
                    0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
            };
    static long[] FileMasks8 =/*from fileA to FileH*/
            {
                    0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
                    0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
            };
    static long[] DiagonalMasks8 =/*from top left to bottom right*/
            {
                    0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
                    0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
                    0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
            };
    static long[] AntiDiagonalMasks8 =/*from top right to bottom left*/
            {
                    0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
                    0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
                    0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
            };

    static long HorizontalAndVerticalMoves(int s) {
        long bitS = 1L << s;
        long horizontalMask = (OCCUPIED - 2 * bitS) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(bitS));
        long verticalMask = ((OCCUPIED & FileMasks8[s % 8]) - (2 * bitS)) ^ Long.reverse(Long.reverse(OCCUPIED & FileMasks8[s % 8]) - (2 * Long.reverse(bitS)));
        return (horizontalMask & RankMasks8[s / 8]) | (verticalMask & FileMasks8[s % 8]);
    }

    static long DiagonalAndAntiDiagonalMoves(int s) {
        long bitS = 1L << s;
        long diagonalMask = ((OCCUPIED & DiagonalMasks8[(s / 8) + (s % 8)]) - 2 * bitS) ^ Long.reverse(Long.reverse(OCCUPIED & DiagonalMasks8[(s / 8) + (s % 8)]) - 2 * Long.reverse(bitS));
        long antiDiagonalMask = ((OCCUPIED & AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - 2 * bitS) ^ Long.reverse(Long.reverse(OCCUPIED & AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - 2 * Long.reverse(bitS));
        return (diagonalMask & DiagonalMasks8[(s / 8) + (s % 8)]) | (antiDiagonalMask & AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]);
    }

    public static String WhitePossibleMoves(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ) {
        NOT_ALLIES = ~(WP | WN | WB | WR | WQ | WK | BK);
        ENEMIES = (BP | BN | BB | BR | BQ);
        NO_PIECE = ~(WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK);
        OCCUPIED = ~NO_PIECE;
//        UNSAFE_WHITE = unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        String list = possibleWhiteP(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP) +
                possibleN(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true) +
                possibleB(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true) +
                possibleR(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true) +
                possibleQ(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true) +
                possibleK(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true) +
                possibleCW(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, CWK, CWQ);

//        System.out.println(unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK));
        return list;
    }

    public static String BlackPossibleMoves(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ) {
        NOT_ALLIES = ~(BP | BN | BB | BR | BQ | BK | WK);
        ENEMIES = (WP | WN | WB | WR | WQ);
        NO_PIECE = ~(WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK);
        OCCUPIED = ~NO_PIECE;
//        UNSAFE_BLACK = unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        String list = possibleBlackP(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP) +
                possibleN(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, false) +
                possibleB(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, false) +
                possibleR(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, false) +
                possibleQ(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, false) +
                possibleK(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, false) +
                possibleCB(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, CBK, CBQ);

//        System.out.println(unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK));
        return list;
    }


    public static String possibleWhiteP(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP) {
        String list = "";
        //format: x1, y1, x2, y2
        //capture right
        long PAWN_MOVES = (WP >> 7) & ENEMIES & ~FILE_A & ~RANK_8;
        long trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 + 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //capture left
        PAWN_MOVES = (WP >> 9) & ENEMIES & ~FILE_H & ~RANK_8;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 + 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 + 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //up 1
        PAWN_MOVES = (WP >> 8) & NO_PIECE & ~RANK_8;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 + 1) + (i % 8) + (i / 8) + (i % 8);
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 + 1) + (i % 8) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //up 2
        PAWN_MOVES = (WP >> 16) & NO_PIECE & (NO_PIECE >> 8) & RANK_4;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 + 2) + (i % 8) + (i / 8) + (i % 8);
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 + 2) + (i % 8) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //promote. format: y1, y2, type, "P"
        //right
        PAWN_MOVES = (WP >> 7) & ENEMIES & RANK_8 & ~FILE_A;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i % 8 - 1) + (i % 8) + "QP" + (i % 8 - 1) + (i % 8) + "RP" + (i % 8 - 1) + (i % 8) + "BP" + (i % 8 - 1) + (i % 8) + "NP";
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i % 8 - 1) + (i % 8) + "QP" + (i % 8 - 1) + (i % 8) + "RP" + (i % 8 - 1) + (i % 8) + "BP" + (i % 8 - 1) + (i % 8) + "NP";
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //left
        PAWN_MOVES = (WP >> 9) & ENEMIES & RANK_8 & ~FILE_H;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i % 8 + 1) + (i % 8) + "QP" + (i % 8 + 1) + (i % 8) + "RP" + (i % 8 + 1) + (i % 8) + "BP" + (i % 8 + 1) + (i % 8) + "NP";
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i % 8 + 1) + (i % 8) + "QP" + (i % 8 + 1) + (i % 8) + "RP" + (i % 8 + 1) + (i % 8) + "BP" + (i % 8 + 1) + (i % 8) + "NP";
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }
        //up 1
        PAWN_MOVES = (WP >> 8) & NO_PIECE & RANK_8;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i % 8) + (i % 8) + "QP" + (i % 8) + (i % 8) + "RP" + (i % 8) + (i % 8) + "BP" + (i % 8) + (i % 8) + "NP";
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i % 8) + (i % 8) + "QP" + (i % 8) + (i % 8) + "RP" + (i % 8) + (i % 8) + "BP" + (i % 8) + (i % 8) + "NP";
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //en passant. format: y1, y2, "wE"
        //right
        PAWN_MOVES = (WP << 1) & BP & RANK_5 & ~FILE_A & EP;
        if (PAWN_MOVES != 0) {
            int i = Long.numberOfTrailingZeros(PAWN_MOVES);
            String move = "" + (i % 8 - 1) + (i % 8) + "WE";
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) == 0) {
                list = list + (i % 8 - 1) + (i % 8) + "WE";
            }
        }

        //left
        PAWN_MOVES = (WP >> 1) & BP & RANK_5 & ~FILE_H & EP;
        if (PAWN_MOVES != 0) {
            int i = Long.numberOfTrailingZeros(PAWN_MOVES);
            String move = "" + (i % 8 + 1) + (i % 8) + "WE";
            long WPt = makeMove(WP, move, 'P');
            if ((unsafeForWhite(WPt, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK) & WK) == 0) {
                list = list + (i % 8 + 1) + (i % 8) + "WE";
            }
        }

        return list;
    }

    public static String possibleBlackP(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP) {
        String list = "";
        //format: x1, y1, x2, y2
        //capture right
        long PAWN_MOVES = (BP << 7) & ENEMIES & ~FILE_H & ~RANK_1;
        long trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 - 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 - 1) + (i % 8 + 1) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //capture left
        PAWN_MOVES = (BP << 9) & ENEMIES & ~FILE_A & ~RANK_1;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 - 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 - 1) + (i % 8 - 1) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //up 1
        PAWN_MOVES = (BP << 8) & NO_PIECE & ~RANK_1;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 - 1) + (i % 8) + (i / 8) + (i % 8);
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 - 1) + (i % 8) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //up 2
        PAWN_MOVES = (BP << 16) & NO_PIECE & (NO_PIECE << 8) & RANK_5;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i / 8 - 2) + (i % 8) + (i / 8) + (i % 8);
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i / 8 - 2) + (i % 8) + (i / 8) + (i % 8);
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //promote. format: y1, y2, type, "P"
        //right
        PAWN_MOVES = (BP << 7) & ENEMIES & RANK_1 & ~FILE_H;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i % 8 + 1) + (i % 8) + "qP" + (i % 8 + 1) + (i % 8) + "rP" + (i % 8 + 1) + (i % 8) + "bP" + (i % 8 + 1) + (i % 8) + "nP";
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i % 8 + 1) + (i % 8) + "qP" + (i % 8 + 1) + (i % 8) + "rP" + (i % 8 + 1) + (i % 8) + "bP" + (i % 8 + 1) + (i % 8) + "nP";
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //left
        PAWN_MOVES = (BP << 9) & ENEMIES & RANK_1 & ~FILE_A;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i % 8 - 1) + (i % 8) + "qP" + (i % 8 - 1) + (i % 8) + "rP" + (i % 8 - 1) + (i % 8) + "bP" + (i % 8 - 1) + (i % 8) + "nP";
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i % 8 - 1) + (i % 8) + "qP" + (i % 8 - 1) + (i % 8) + "rP" + (i % 8 - 1) + (i % 8) + "bP" + (i % 8 - 1) + (i % 8) + "nP";
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }
        //up 1
        PAWN_MOVES = (BP << 8) & NO_PIECE & RANK_1;
        trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            String move = "" + (i % 8) + (i % 8) + "qP" + (i % 8) + (i % 8) + "rP" + (i % 8) + (i % 8) + "bP" + (i % 8) + (i % 8) + "nP";
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) != 0) {
                PAWN_MOVES &= ~trailingMask;
                trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
                continue;
            }
            list = list + (i % 8) + (i % 8) + "qP" + (i % 8) + (i % 8) + "rP" + (i % 8) + (i % 8) + "bP" + (i % 8) + (i % 8) + "nP";
            PAWN_MOVES &= ~trailingMask;
            trailingMask = PAWN_MOVES & ~(PAWN_MOVES - 1);
        }

        //en passant. format: y1, y2, "BE"
        //right
        PAWN_MOVES = (BP >> 1) & WP & RANK_4 & ~FILE_H & EP;
        if (PAWN_MOVES != 0) {
            int i = Long.numberOfTrailingZeros(PAWN_MOVES);
            String move = "" + (i % 8 + 1) + (i % 8) + "BE";
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) == 0) {
                list = list + (i % 8 + 1) + (i % 8) + "BE";
            }
        }

        //left
        PAWN_MOVES = (BP << 1) & WP & RANK_4 & ~FILE_A & EP;
        if (PAWN_MOVES != 0) {
            int i = Long.numberOfTrailingZeros(PAWN_MOVES);
            String move = "" + (i % 8 - 1) + (i % 8) + "BE";
            long BPt = makeMove(BP, move, 'p');
            if ((unsafeForBlack(WP, WN, WB, WR, WQ, WK, BPt, BN, BB, BR, BQ, BK) & BK) == 0) {
                list = list + (i % 8 - 1) + (i % 8) + "BE";
            }
        }

        return list;
    }

    public static String possibleN(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean isWhite) {
        String list = "";
        long constMask;
        long trailingNMask;
        if (isWhite) {
            constMask = WN;
            trailingNMask = WN & ~(WN - 1);
            long N_MOVES;
            while (trailingNMask != 0) {
                int i = Long.numberOfTrailingZeros(trailingNMask);
                if (i > 18) {
                    N_MOVES = KNIGHT_SPAN << (i - 18);
                } else {
                    N_MOVES = KNIGHT_SPAN >> (18 - i);
                }
                if (i % 8 < 4) {
                    N_MOVES &= ~FILE_GH & NOT_ALLIES;
                } else {
                    N_MOVES &= ~FILE_AB & NOT_ALLIES;
                }
                long trailingSubMask = N_MOVES & ~(N_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, constMask, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    N_MOVES &= ~trailingSubMask;
                    trailingSubMask = N_MOVES & ~(N_MOVES - 1);
                }
                WN &= ~trailingNMask;
                trailingNMask = WN & ~(WN - 1);
            }
        } else {
            constMask = BN;
            trailingNMask = BN & ~(BN - 1);
            long N_MOVES;
            while (trailingNMask != 0) {
//                System.out.println(trailingNMask);
                int i = Long.numberOfTrailingZeros(trailingNMask);
                if (i > 18) {
                    N_MOVES = KNIGHT_SPAN << (i - 18);
                } else {
                    N_MOVES = KNIGHT_SPAN >> (18 - i);
                }
                if (i % 8 < 4) {
                    N_MOVES &= ~FILE_GH & NOT_ALLIES;
                } else {
                    N_MOVES &= ~FILE_AB & NOT_ALLIES;
                }
                long trailingSubMask = N_MOVES & ~(N_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, WN, WB, WR, WQ, WK, BP, constMask, BB, BR, BQ, BK, false)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    N_MOVES &= ~trailingSubMask;
                    trailingSubMask = N_MOVES & ~(N_MOVES - 1);
                }
                BN &= ~trailingNMask;
                trailingNMask = BN & ~(BN - 1);
            }
        }
        return list;
    }

    public static String possibleB(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean isWhite) {
        String list = "";
        long constMask;
        long trailingBMask;
        if (isWhite) {
            constMask = WB;
            trailingBMask = WB & ~(WB - 1);
            long B_MOVES;
            while (trailingBMask != 0) {
                int i = Long.numberOfTrailingZeros(trailingBMask);
                B_MOVES = DiagonalAndAntiDiagonalMoves(i) & NOT_ALLIES;
                long trailingSubMask = B_MOVES & ~(B_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, WN, constMask, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    B_MOVES &= ~trailingSubMask;
                    trailingSubMask = B_MOVES & ~(B_MOVES - 1);
                }
                WB &= ~trailingBMask;
                trailingBMask = WB & ~(WB - 1);
            }

        } else {
            constMask = BB;
            trailingBMask = BB & ~(BB - 1);
            long B_MOVES;
            while (trailingBMask != 0) {
                int i = Long.numberOfTrailingZeros(trailingBMask);
                B_MOVES = DiagonalAndAntiDiagonalMoves(i) & NOT_ALLIES;
                long trailingSubMask = B_MOVES & ~(B_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, WN, WB, WR, WQ, WK, BP, BN, constMask, BR, BQ, BK, false)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    B_MOVES &= ~trailingSubMask;
                    trailingSubMask = B_MOVES & ~(B_MOVES - 1);
                }
                BB &= ~trailingBMask;
                trailingBMask = BB & ~(BB - 1);
            }

        }
        return list;
    }

    public static String possibleR(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean isWhite) {
        String list = "";
        long constMask;
        if (isWhite) {
            constMask = WR;
            long trailingRMask = WR & ~(WR - 1);
            while (trailingRMask != 0) {
                int i = Long.numberOfTrailingZeros(trailingRMask);
                long R_MOVES = HorizontalAndVerticalMoves(i) & NOT_ALLIES;
                long trailingSubMask = R_MOVES & ~(R_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, WN, WB, constMask, WQ, WK, BP, BN, BB, BR, BQ, BK, true)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    R_MOVES &= ~trailingSubMask;
                    trailingSubMask = R_MOVES & ~(R_MOVES - 1);
                }
                WR &= ~trailingRMask;
                trailingRMask = WR & ~(WR - 1);
            }
        } else {
            constMask = BR;
            long trailingRMask = BR & ~(BR - 1);
            while (trailingRMask != 0) {
                int i = Long.numberOfTrailingZeros(trailingRMask);
                long R_MOVES = HorizontalAndVerticalMoves(i) & NOT_ALLIES;
                long trailingSubMask = R_MOVES & ~(R_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, WN, WB, WR, WQ, WK, BP, BN, BB, constMask, BQ, BK, false)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    R_MOVES &= ~trailingSubMask;
                    trailingSubMask = R_MOVES & ~(R_MOVES - 1);
                }
                BR &= ~trailingRMask;
                trailingRMask = BR & ~(BR - 1);
            }
        }

        return list;
    }

    public static String possibleQ(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean isWhite) {
        String list = "";
        long constMask;
        long trailingQMask;
        if (isWhite) {
            constMask = WQ;
            trailingQMask = WQ & ~(WQ - 1);
            while (trailingQMask != 0) {
                int i = Long.numberOfTrailingZeros(trailingQMask);
                long Q_MOVES = (HorizontalAndVerticalMoves(i) | DiagonalAndAntiDiagonalMoves(i)) & NOT_ALLIES;
                long trailingSubMask = Q_MOVES & ~(Q_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, WN, WB, WR, constMask, WK, BP, BN, BB, BR, BQ, BK, true)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    Q_MOVES &= ~trailingSubMask;
                    trailingSubMask = Q_MOVES & ~(Q_MOVES - 1);
                }
                WQ &= ~trailingQMask;
                trailingQMask = WQ & ~(WQ - 1);
            }
        } else {
            constMask = BQ;
            trailingQMask = BQ & ~(BQ - 1);
            while (trailingQMask != 0) {
                int i = Long.numberOfTrailingZeros(trailingQMask);
                long Q_MOVES = (HorizontalAndVerticalMoves(i) | DiagonalAndAntiDiagonalMoves(i)) & NOT_ALLIES;
                long trailingSubMask = Q_MOVES & ~(Q_MOVES - 1);
                while (trailingSubMask != 0) {
                    int index = Long.numberOfTrailingZeros(trailingSubMask);
                    String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    if (checkingSafe(move, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, constMask, BK, false)) {
                        list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                    }
                    Q_MOVES &= ~trailingSubMask;
                    trailingSubMask = Q_MOVES & ~(Q_MOVES - 1);
                }
                BQ &= ~trailingQMask;
                trailingQMask = BQ & ~(BQ - 1);
            }
        }

        return list;
    }

    public static String possibleK(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean isWhite) {
        String list = "";
        long constMask;
        long trailingKMask;
        if (isWhite) {
            constMask = WK;
            trailingKMask = WK & ~(WK - 1);
            long K_MOVES;
            int i = Long.numberOfTrailingZeros(trailingKMask);
            if (i > 9) {
                K_MOVES = KING_SPAN << (i - 9);
            } else {
                K_MOVES = KING_SPAN >> (9 - i);
            }
            if (i % 8 < 4) {
                K_MOVES &= ~FILE_H & NOT_ALLIES;
            } else {
                K_MOVES &= ~FILE_A & NOT_ALLIES;
            }
            K_MOVES &= NOT_ALLIES;
            long trailingSubMask = K_MOVES & ~(K_MOVES - 1);
            while (trailingSubMask != 0) {
                int index = Long.numberOfTrailingZeros(trailingSubMask);
                String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                if (checkingSafe(move, WP, WN, WB, WR, WQ, constMask, BP, BN, BB, BR, BQ, BK, true)) {
                    list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                }
                K_MOVES &= ~trailingSubMask;
                trailingSubMask = K_MOVES & ~(K_MOVES - 1);
            }
        } else {
            constMask = BK;
            trailingKMask = BK & ~(BK - 1);
            long K_MOVES;
            int i = Long.numberOfTrailingZeros(trailingKMask);
            if (i > 9) {
                K_MOVES = KING_SPAN << (i - 9);
            } else {
                K_MOVES = KING_SPAN >> (9 - i);
            }
            if (i % 8 < 4) {
                K_MOVES &= ~FILE_H & NOT_ALLIES;
            } else {
                K_MOVES &= ~FILE_A & NOT_ALLIES;
            }
            K_MOVES &= NOT_ALLIES;
            long trailingSubMask = K_MOVES & ~(K_MOVES - 1);
            while (trailingSubMask != 0) {
                int index = Long.numberOfTrailingZeros(trailingSubMask);
                String move = "" + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                if (checkingSafe(move, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, constMask, false)) {
                    list = list + (i / 8) + (i % 8) + (index / 8) + (index % 8);
                }
                K_MOVES &= ~trailingSubMask;
                trailingSubMask = K_MOVES & ~(K_MOVES - 1);
            }
        }

        return list;
    }

    public static String possibleCW(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean CWK, boolean CWQ) {
        String list = "";
        long UNSAFE = unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        if ((UNSAFE & WK) == 0) {
            if (CWK && (((1L << CASTLE_ROOKS[0]) & WR) != 0)) {
                if (((OCCUPIED | UNSAFE) & ((1L << 61) | (1L << 62))) == 0) {
                    list += "7476";
                }
            }
            if (CWQ && (((1L << CASTLE_ROOKS[1]) & WR) != 0)) {
                if (((OCCUPIED | (UNSAFE & ~(1L << 57))) & ((1L << 57) | (1L << 58) | (1L << 59))) == 0) {
                    list += "7472";
                }
            }
        }
        return list;
    }

    public static String possibleCB(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean CBK, boolean CBQ) {
        String list = "";
        long UNSAFE = unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        if ((UNSAFE & BK) == 0) {
            if (CBK && (((1L << CASTLE_ROOKS[2]) & BR) != 0)) {
                if (((OCCUPIED | UNSAFE) & ((1L << 5) | (1L << 6))) == 0) {
                    list += "0406";
                }
            }
            if (CBQ && (((1L << CASTLE_ROOKS[3]) & BR) != 0)) {
                if (((OCCUPIED | (UNSAFE & ~(1L << 1))) & ((1L << 1) | (1L << 2) | (1L << 3))) == 0) {
                    list += "0402";
                }
            }
        }
        return list;
    }

    public static long unsafeForBlack(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
        long unsafe;
//        OCCUPIED = WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK;

        //pawn
        unsafe = ((WP >>> 7) & ~FILE_A);//pawn capture right
        unsafe |= ((WP >>> 9) & ~FILE_H);//pawn capture left
        long MOVES;

        //knight
        long trailingMask = WN & ~(WN - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            if (i > 18) {
                MOVES = KNIGHT_SPAN << (i - 18);
            } else {
                MOVES = KNIGHT_SPAN >> (18 - i);
            }
            if (i % 8 < 4) {
                MOVES &= ~FILE_GH;
            } else {
                MOVES &= ~FILE_AB;
            }
            unsafe |= MOVES;
            WN &= ~trailingMask;
            trailingMask = WN & ~(WN - 1);
        }

        //bishop/queen
        long QB = WQ | WB;
        trailingMask = QB & ~(QB - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            MOVES = DiagonalAndAntiDiagonalMoves(i);
            unsafe |= MOVES;
            QB &= ~trailingMask;
            trailingMask = QB & ~(QB - 1);
        }

        //rook/queen
        long QR = WQ | WR;
        trailingMask = QR & ~(QR - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            MOVES = HorizontalAndVerticalMoves(i);
            unsafe |= MOVES;
            QR &= ~trailingMask;
            trailingMask = QR & ~(QR - 1);
        }

        //king
        int i = Long.numberOfTrailingZeros(WK);
        if (i > 9) {
            MOVES = KING_SPAN << (i - 9);
        } else {
            MOVES = KING_SPAN >> (9 - i);
        }
        if (i % 8 < 4) {
            MOVES &= ~FILE_GH;
        } else {
            MOVES &= ~FILE_AB;
        }
        unsafe |= MOVES;

        return unsafe;
    }

    public static long unsafeForWhite(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
        long unsafe;
//        OCCUPIED = WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK;

        //pawn
        unsafe = ((BP << 7) & ~FILE_H);//pawn capture right
        unsafe |= ((BP << 9) & ~FILE_A);//pawn capture left
        long MOVES;

        //knight
        long trailingMask = BN & ~(BN - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            if (i > 18) {
                MOVES = KNIGHT_SPAN << (i - 18);
            } else {
                MOVES = KNIGHT_SPAN >> (18 - i);
            }
            if (i % 8 < 4) {
                MOVES &= ~FILE_GH;
            } else {
                MOVES &= ~FILE_AB;
            }
            unsafe |= MOVES;
            BN &= ~trailingMask;
            trailingMask = BN & ~(BN - 1);
        }

        //bishop/queen
        long QB = BQ | BB;
        trailingMask = QB & ~(QB - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            MOVES = DiagonalAndAntiDiagonalMoves(i);
            unsafe |= MOVES;
            QB &= ~trailingMask;
            trailingMask = QB & ~(QB - 1);
        }

        //rook/queen
        long QR = BQ | BR;
        trailingMask = QR & ~(QR - 1);
        while (trailingMask != 0) {
            int i = Long.numberOfTrailingZeros(trailingMask);
            MOVES = HorizontalAndVerticalMoves(i);
            unsafe |= MOVES;
            QR &= ~trailingMask;
            trailingMask = QR & ~(QR - 1);
        }

        //king
        int i = Long.numberOfTrailingZeros(BK);
        if (i > 9) {
            MOVES = KING_SPAN << (i - 9);
        } else {
            MOVES = KING_SPAN >> (9 - i);
        }
        if (i % 8 < 4) {
            MOVES &= ~FILE_GH;
        } else {
            MOVES &= ~FILE_AB;
        }
        unsafe |= MOVES;

        return unsafe;
    }

    public static long makeMove(long board, String move, char type) {
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            int start = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
            int end = (Character.getNumericValue(move.charAt(2)) * 8) + (Character.getNumericValue(move.charAt(3)));
            if (((board >> start) & 1) == 1) {
                board &= ~(1L << start);
                board |= (1L << end);
            } else {
                board &= ~(1L << end);
            }
        } else if (move.charAt(3) == 'P') {//pawn promotion
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start = Long.numberOfTrailingZeros(FileMasks8[move.charAt(0) - '0'] & RankMasks8[1]);
                end = Long.numberOfTrailingZeros(FileMasks8[move.charAt(1) - '0'] & RankMasks8[0]);
            } else {
                start = Long.numberOfTrailingZeros(FileMasks8[move.charAt(0) - '0'] & RankMasks8[6]);
                end = Long.numberOfTrailingZeros(FileMasks8[move.charAt(1) - '0'] & RankMasks8[7]);
            }
            if (type == move.charAt(2)) {
                board |= (1L << end);
            } else {
                board &= ~(1L << start);
                board &= ~(1L << end);
            }
        } else if (move.charAt(3) == 'E') {//en passant
            int start, end;
            if (move.charAt(2) == 'W') {
                start = Long.numberOfTrailingZeros(FileMasks8[move.charAt(0) - '0'] & RankMasks8[3]);
                end = Long.numberOfTrailingZeros(FileMasks8[move.charAt(1) - '0'] & RankMasks8[2]);
                board &= ~(FileMasks8[move.charAt(1) - '0'] & RankMasks8[3]);
            } else {
                start = Long.numberOfTrailingZeros(FileMasks8[move.charAt(0) - '0'] & RankMasks8[4]);
                end = Long.numberOfTrailingZeros(FileMasks8[move.charAt(1) - '0'] & RankMasks8[5]);
                board &= ~(FileMasks8[move.charAt(1) - '0'] & RankMasks8[4]);
            }
            if (((board >>> start) & 1) == 1) {
                board &= ~(1L << start);
                board |= (1L << end);
            }
        } else {
            System.out.print("ERROR: Invalid move type");
        }
        return board;
    }

    public static long makeMoveEP(long board, String move) {
        if (Character.isDigit(move.charAt(3))) {
            int start = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
            if ((Math.abs(move.charAt(0) - move.charAt(2)) == 2) && (((board >>> start) & 1) == 1)) {//pawn double push
                return FileMasks8[move.charAt(1) - '0'];
            }
        }
        return 0;
    }

    public static long makeMoveCastle(long rookBoard, long kingBoard, String move, char type) {
        int start = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
        if ((((kingBoard >>> start) & 1) == 1) && (("0402".equals(move)) || ("0406".equals(move)) || ("7472".equals(move)) || ("7476".equals(move)))) {//'regular' move
            if (type == 'R') {
                switch (move) {
                    case "7472":
                        rookBoard &= ~(1L << CASTLE_ROOKS[1]);
                        rookBoard |= (1L << (CASTLE_ROOKS[1] + 3));
                        break;
                    case "7476":
                        rookBoard &= ~(1L << CASTLE_ROOKS[0]);
                        rookBoard |= (1L << (CASTLE_ROOKS[0] - 2));
                        break;
                }
            } else {
                switch (move) {
                    case "0402":
                        rookBoard &= ~(1L << CASTLE_ROOKS[3]);
                        rookBoard |= (1L << (CASTLE_ROOKS[3] + 3));
                        break;
                    case "0406":
                        rookBoard &= ~(1L << CASTLE_ROOKS[2]);
                        rookBoard |= (1L << (CASTLE_ROOKS[2] - 2));
                        break;
                }
            }
        }
        return rookBoard;
    }


    public static void moveOnBoard(String move, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ, boolean WhiteToMove, String chessGameId) {
        if ((WhiteToMove && WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length() < WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length()) ||
                (!WhiteToMove && BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length() < BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length())) {
            long WPt = Moves.makeMove(WP, move, 'P'), WNt = Moves.makeMove(WN, move, 'N'),
                    WBt = Moves.makeMove(WB, move, 'B'), WRt = Moves.makeMove(WR, move, 'R'),
                    WQt = Moves.makeMove(WQ, move, 'Q'), WKt = Moves.makeMove(WK, move, 'K'),
                    BPt = Moves.makeMove(BP, move, 'p'), BNt = Moves.makeMove(BN, move, 'n'),
                    BBt = Moves.makeMove(BB, move, 'b'), BRt = Moves.makeMove(BR, move, 'r'),
                    BQt = Moves.makeMove(BQ, move, 'q'), BKt = Moves.makeMove(BK, move, 'k'),
                    EPt = Moves.makeMoveEP(WP | BP, move);
            WRt = Moves.makeMoveCastle(WRt, WK | BK, move, 'R');
            BRt = Moves.makeMoveCastle(BRt, WK | BK, move, 'r');
            boolean CWKt = CWK, CWQt = CWQ, CBKt = CBK, CBQt = CBQ;

            if (Character.isDigit(move.charAt(3))) {//'regular' move
                int start = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
                if (((1L << start) & WK) != 0) {
                    CWKt = false;
                    CWQt = false;
                }
                if (((1L << start) & BK) != 0) {
                    CBKt = false;
                    CBQt = false;
                }
                if (((1L << start) & WR & (1L << 63)) != 0) {
                    CWKt = false;
                }
                if (((1L << start) & WR & (1L << 56)) != 0) {
                    CWQt = false;
                }
                if (((1L << start) & BR & (1L << 7)) != 0) {
                    CBKt = false;
                }
                if (((1L << start) & BR & 1L) != 0) {
                    CBQt = false;
                }
            }
            if (((WKt & Moves.unsafeForWhite(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && WhiteToMove) ||
                    ((BKt & Moves.unsafeForBlack(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && !WhiteToMove)) {
//                HISTORIC_MOVES.add(move);
//                keepOldBoard();
//                getHistory(move, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt, EPt);
//                ChessGameController.HISTORIC_MOVES.add(move);
                if (ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD == null) {
                    ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD = new ArrayList<>();
                }
                ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.add(new HistoricInfo(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove));
                ChessGameController.games.get(chessGameId).WP = WPt;
                ChessGameController.games.get(chessGameId).WN = WNt;
                ChessGameController.games.get(chessGameId).WB = WBt;
                ChessGameController.games.get(chessGameId).WR = WRt;
                ChessGameController.games.get(chessGameId).WQ = WQt;
                ChessGameController.games.get(chessGameId).WK = WKt;
                ChessGameController.games.get(chessGameId).BP = BPt;
                ChessGameController.games.get(chessGameId).BN = BNt;
                ChessGameController.games.get(chessGameId).BB = BBt;
                ChessGameController.games.get(chessGameId).BR = BRt;
                ChessGameController.games.get(chessGameId).BQ = BQt;
                ChessGameController.games.get(chessGameId).BK = BKt;
                ChessGameController.games.get(chessGameId).EP = EPt;

                ChessGameController.games.get(chessGameId).CWK = CWKt;
                ChessGameController.games.get(chessGameId).CWQ = CWQt;
                ChessGameController.games.get(chessGameId).CBK = CBKt;
                ChessGameController.games.get(chessGameId).CBQ = CBQt;
                ChessGameController.games.get(chessGameId).WhiteToMove = !WhiteToMove;

//                ChessGameController.HISTORIC_BITBOARD.add(new HistoricInfo(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove));
//                ChessGameController.WP = WPt;
//                ChessGameController.WN = WNt;
//                ChessGameController.WB = WBt;
//                ChessGameController.WR = WRt;
//                ChessGameController.WQ = WQt;
//                ChessGameController.WK = WKt;
//                ChessGameController.BP = BPt;
//                ChessGameController.BN = BNt;
//                ChessGameController.BB = BBt;
//                ChessGameController.BR = BRt;
//                ChessGameController.BQ = BQt;
//                ChessGameController.BK = BKt;
//                ChessGameController.EP = EPt;
//
//                ChessGameController.CWK = CWKt;
//                ChessGameController.CWQ = CWQt;
//                ChessGameController.CBK = CBKt;
//                ChessGameController.CBQ = CBQt;
//                ChessGameController.WhiteToMove = !WhiteToMove;
            } else {
                System.out.println("Invalid move jawoiefjow");
            }
        } else {
            System.out.println("Invalid move 27830921");
            System.out.println(WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length());
            System.out.println(WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length());
            System.out.println(BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length());
            System.out.println(BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length());
            System.out.println("isWhite: " + WhiteToMove);
        }
    }

    public static void undoMove2(String chessGameId) {
        int size = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.size();
        ChessGameController.games.get(chessGameId).WP = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).WP;
        ChessGameController.games.get(chessGameId).WN = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).WN;
        ChessGameController.games.get(chessGameId).WB = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).WB;
        ChessGameController.games.get(chessGameId).WR = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).WR;
        ChessGameController.games.get(chessGameId).WQ = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).WQ;
        ChessGameController.games.get(chessGameId).WK = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).WK;
        ChessGameController.games.get(chessGameId).BP = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).BP;
        ChessGameController.games.get(chessGameId).BN = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).BN;
        ChessGameController.games.get(chessGameId).BB = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).BB;
        ChessGameController.games.get(chessGameId).BR = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).BR;
        ChessGameController.games.get(chessGameId).BQ = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).BQ;
        ChessGameController.games.get(chessGameId).BK = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).BK;
        ChessGameController.games.get(chessGameId).EP = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).EP;

        ChessGameController.games.get(chessGameId).CWK = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).CWK;
        ChessGameController.games.get(chessGameId).CWQ = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).CWQ;
        ChessGameController.games.get(chessGameId).CBK = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).CBK;
        ChessGameController.games.get(chessGameId).CBQ = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).CBQ;
        ChessGameController.games.get(chessGameId).WhiteToMove = ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.get(size - 1).WhiteToMove;
        ChessGameController.games.get(chessGameId).HISTORIC_BITBOARD.remove(size - 1);
    }

    public static boolean checkingSafe(String move, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean isWhite) {
        long WPt = Moves.makeMove(WP, move, 'P'), WNt = Moves.makeMove(WN, move, 'N'),
                WBt = Moves.makeMove(WB, move, 'B'), WRt = Moves.makeMove(WR, move, 'R'),
                WQt = Moves.makeMove(WQ, move, 'Q'), WKt = Moves.makeMove(WK, move, 'K'),
                BPt = Moves.makeMove(BP, move, 'p'), BNt = Moves.makeMove(BN, move, 'n'),
                BBt = Moves.makeMove(BB, move, 'b'), BRt = Moves.makeMove(BR, move, 'r'),
                BQt = Moves.makeMove(BQ, move, 'q'), BKt = Moves.makeMove(BK, move, 'k'),
                EPt = Moves.makeMoveEP(WP | BP, move);
        WRt = Moves.makeMoveCastle(WRt, WK | BK, move, 'R');
        BRt = Moves.makeMoveCastle(BRt, WK | BK, move, 'r');
        if (isWhite && (unsafeForWhite(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt) & WKt) == 0) {
            return true;
        }
        if (!isWhite && (unsafeForBlack(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt) & BKt) == 0) {
            return true;
        }
        return false;
    }

//    public static void keepOldBoard()
//    {
//        WPmt = ChessGameController.WP;
//        WNmt = ChessGameController.WN;
//        WBmt = ChessGameController.WB;
//        WRmt = ChessGameController.WR;
//        WQmt = ChessGameController.WQ;
//        WKmt = ChessGameController.WK;
//        BPmt = ChessGameController.BP;
//        BNmt = ChessGameController.BN;
//        BBmt = ChessGameController.BB;
//        BRmt = ChessGameController.BR;
//        BQmt = ChessGameController.BQ;
//        BKmt = ChessGameController.BK;
//        EPmt = ChessGameController.EP;
//
//        CWKm = ChessGameController.CWK;
//        CWQm = ChessGameController.CWQ;
//        CBKm = ChessGameController.CBK;
//        CBQm = ChessGameController.CBQ;
//    }

    public static String onlyCaptureMoves(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean isWhite)
    {
        String moves = isWhite ? Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, 0, false, false, false, false)
                : Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, 0, false, false, false, false);

        StringBuilder res = new StringBuilder();
        int piece_counter = 0;
        int debug = 0;
        long[] list = {WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK};
        try
        {
            for (int i = 0; i < moves.length(); i += 4)
            {
                debug = i;
                String move = moves.substring(i, i + 4);
                int start = (move.charAt(0) - '0') * 8 + move.charAt(1) - '0';
                int end = (move.charAt(2) - '0') * 8 + move.charAt(3) - '0';
                for (long l : list) {
                    if ((l & (1L << start)) != 0) {
                        piece_counter++;
                    }
                    if ((l & (1L << end)) != 0) {
                        piece_counter++;
                    }
                }
                if (piece_counter == 2)
                {
                    res.append(move);
                }
                piece_counter = 0;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println(debug + "truc vi ngu ngon");
            System.out.println(moves.length());
            try
            {
//                for (int i = 0; i < moves.length(); i += 4)
//                {
//                    System.out.println(moves.substring(i, i + 4));
//                }
                System.out.println(moves);
                System.exit(0);
            }
            catch (Exception ignored) {}
        }

        return res.toString();
    }
}



























