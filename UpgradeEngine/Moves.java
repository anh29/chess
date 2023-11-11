package UpgradeEngine;

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
        OCCUPIED = WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK;

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
        OCCUPIED = WP | WN | WB | WR | WQ | WK | BP | BN | BB | BR | BQ | BK;

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

//    public static void makeMoveWrong(String move, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ) {
//        //can not operate on a single board since moves are not backwards compatible
//
//        EP = 0;
//        if (Character.isDigit(move.charAt(3))) {
//            int start = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
//            int end = (Character.getNumericValue(move.charAt(2)) * 8) + (Character.getNumericValue(move.charAt(3)));
//            if ((WK & (1L << start)) != 0) {//white castle move
//                WK ^= (1L << start);
//                WK ^= (1L << end);
//                if (end > start && end - start == 2) {//king side
//                    WR ^= (1L << 63);
//                    WR ^= (1L << 61);
//                } else if (end < start && start - end == 3) {//queen side
//                    WR ^= (1L << 56);
//                    WR ^= (1L << 59);
//                }
//                CWK = false;
//                CWQ = false;
//            } else if ((BK & (1L << start)) != 0) {//black castle move
//                BK ^= (1L << start);
//                BK ^= (1L << end);
//                if (end > start && end - start == 2) {//king side
//                    BR ^= (1L << 7);
//                    BR ^= (1L << 5);
//                } else if (end < start && start - end == 3) {//queen side
//                    BR ^= 1L;
//                    BR ^= (1L << 3);
//                }
//                CBK = false;
//                CBQ = false;
//            } else {//'regular' move
//                //clear destination:
//                WP &= ~(1L << end);
//                WN &= ~(1L << end);
//                WB &= ~(1L << end);
//                WR &= ~(1L << end);
//                WQ &= ~(1L << end);
//                WK &= ~(1L << end);
//                BP &= ~(1L << end);
//                BN &= ~(1L << end);
//                BB &= ~(1L << end);
//                BR &= ~(1L << end);
//                BQ &= ~(1L << end);
//                BK &= ~(1L << end);
//
//                //move piece:
//                if ((WP & (1L << start)) != 0) {
//                    WP ^= (1L << start);
//                    WP ^= (1L << end);
//                    if ((end - start) == 16) {//pawn double push
//                        EP = FileMasks8['0' - move.charAt(1)];
//                    }
//                } else if ((BP & (1L << start)) != 0) {
//                    BP ^= (1L << start);
//                    BP ^= (1L << end);
//                    if ((start - end) == 16) {//pawn double push
//                        EP = FileMasks8['0' - move.charAt(1)];
//                    }
//                } else if ((WN & (1L << start)) != 0) {
//                    WN ^= (1L << start);
//                    WN ^= (1L << end);
//                } else if ((BN & (1L << start)) != 0) {
//                    BN ^= (1L << start);
//                    BN ^= (1L << end);
//                } else if ((WB & (1L << start)) != 0) {
//                    WB ^= (1L << start);
//                    WB ^= (1L << end);
//                } else if ((BB & (1L << start)) != 0) {
//                    BB ^= (1L << start);
//                    BB ^= (1L << end);
//                } else if ((WR & (1L << start)) != 0) {
//                    WR ^= (1L << start);
//                    WR ^= (1L << end);
//                    if (start == CASTLE_ROOKS[0]) {
//                        CWK = false;
//                    }
//                    if (start == CASTLE_ROOKS[1]) {
//                        CWQ = false;
//                    }
//                } else if ((BR & (1L << start)) != 0) {
//                    BR ^= (1L << start);
//                    BR ^= (1L << end);
//                    if (start == CASTLE_ROOKS[2]) {
//                        CBK = false;
//                    }
//                    if (start == CASTLE_ROOKS[3]) {
//                        CBQ = false;
//                    }
//                } else if ((WQ & (1L << start)) != 0) {
//                    WQ ^= (1L << start);
//                    WQ ^= (1L << end);
//                } else if ((BQ & (1L << start)) != 0) {
//                    BQ ^= (1L << start);
//                    BQ ^= (1L << end);
//                } else if ((WK & (1L << start)) != 0) {
//                    WK ^= (1L << start);
//                    WK ^= (1L << end);
//                    CWK = false;
//                    CWQ = false;
//                } else if ((BK & (1L << start)) != 0) {
//                    BK ^= (1L << start);
//                    BK ^= (1L << end);
//                    CBK = false;
//                    CBQ = false;
//                } else {
//                    System.out.print("error: can not move empty piece");
//                }
//            }
//        } else if (move.charAt(3) == 'P') {//pawn promotion
//            //y1,y2,Promotion Type,"P"
//            if (Character.isUpperCase(move.charAt(2)))//white piece promotion
//            {
//                WP ^= (RankMasks8[6] & FileMasks8[move.charAt(0) - '0']);
//                switch (move.charAt(2)) {
//                    case 'N':
//                        WN ^= (RankMasks8[7] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                    case 'B':
//                        WB ^= (RankMasks8[7] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                    case 'R':
//                        WR ^= (RankMasks8[7] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                    case 'Q':
//                        WQ ^= (RankMasks8[7] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                }
//            } else {//black piece promotion
//                BP ^= (RankMasks8[1] & FileMasks8[move.charAt(0) - '0']);
//                switch (move.charAt(2)) {
//                    case 'n':
//                        BN ^= (RankMasks8[0] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                    case 'b':
//                        BB ^= (RankMasks8[0] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                    case 'r':
//                        BR ^= (RankMasks8[0] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                    case 'q':
//                        BQ ^= (RankMasks8[0] & FileMasks8[move.charAt(1) - '0']);
//                        break;
//                }
//            }
//        } else if (move.charAt(3) == 'E') {//en passant move
//            if (move.charAt(2) == 'w') {//white move
//                //y1,y2,"BE"
//                WP ^= (RankMasks8[4] & FileMasks8['0' - move.charAt(0)]);//remove white pawn
//                WP ^= (RankMasks8[5] & FileMasks8['0' - move.charAt(1)]);//add white pawn
//                BP ^= (RankMasks8[4] & FileMasks8['0' - move.charAt(1)]);//remove black pawn
//            } else {//black move
//                BP ^= (RankMasks8[3] & FileMasks8['0' - move.charAt(0)]);//remove black pawn
//                BP ^= (RankMasks8[2] & FileMasks8['0' - move.charAt(1)]);//add black pawn
//                WP ^= (RankMasks8[3] & FileMasks8['0' - move.charAt(1)]);//remove white pawn
//            }
//        } else {
//            System.out.print("error: not a valid move type");
//        }
//
//        UserInterface.WP = WP;
//        UserInterface.WN = WN;
//        UserInterface.WB = WB;
//        UserInterface.WR = WR;
//        UserInterface.WQ = WQ;
//        UserInterface.WK = WK;
//        UserInterface.BP = BP;
//        UserInterface.BN = BN;
//        UserInterface.BB = BB;
//        UserInterface.BR = BR;
//        UserInterface.BQ = BQ;
//        UserInterface.BK = BK;
//        UserInterface.EP = EP;
//
//        UserInterface.CWK = CWK;
//        UserInterface.CWQ = CWQ;
//        UserInterface.CBK = CBK;
//        UserInterface.CBQ = CBQ;
//    }

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


    public static void moveOnBoard(String move, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ, boolean WhiteToMove) {
        if (WhiteToMove && WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length() < WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length() ||
                !WhiteToMove && BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length() < BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length()) {
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
//                UserInterface.HISTORIC_MOVES.add(move);
                UserInterface.HISTORIC_BITBOARD.add(new HistoricInfo(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove));
                UserInterface.WP = WPt;
                UserInterface.WN = WNt;
                UserInterface.WB = WBt;
                UserInterface.WR = WRt;
                UserInterface.WQ = WQt;
                UserInterface.WK = WKt;
                UserInterface.BP = BPt;
                UserInterface.BN = BNt;
                UserInterface.BB = BBt;
                UserInterface.BR = BRt;
                UserInterface.BQ = BQt;
                UserInterface.BK = BKt;
                UserInterface.EP = EPt;

                UserInterface.CWK = CWKt;
                UserInterface.CWQ = CWQt;
                UserInterface.CBK = CBKt;
                UserInterface.CBQ = CBQt;
                UserInterface.WhiteToMove = !WhiteToMove;
            } else {
                System.out.println("Invalid move jawoiefjow");
            }
        } else {
            System.out.println("Invalid move 27830921");
            System.out.println(WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length());
            System.out.println(WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length());
            System.out.println(BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).replace(move, "").length());
            System.out.println(BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length());

        }
    }

//    public static void undoMove(ArrayList<String> MOVE, ArrayList<String> PIECE) {
//        String move = MOVE.get(MOVE.size() - 1);
//        String piece = PIECE.get(PIECE.size() - 1);
//
//        if (Character.isDigit(move.charAt(3))) {
//            int start = (move.charAt(0) - '0') * 8 + (move.charAt(1) - '0');
//            int end = (move.charAt(2) - '0') * 8 + (move.charAt(3) - '0');
//            if (piece.length() == 1) {
//                switch (piece) {
//                    case "P":
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        break;
//                    case "N":
//                        UserInterface.WN |= (1L << start);
//                        UserInterface.WN &= ~(1L << end);
//                        break;
//                    case "B":
//                        UserInterface.WB |= (1L << start);
//                        UserInterface.WB &= ~(1L << end);
//                        break;
//                    case "R":
//                        UserInterface.WR |= (1L << start);
//                        UserInterface.WR &= ~(1L << end);
//                        break;
//                    case "Q":
//                        UserInterface.WQ |= (1L << start);
//                        UserInterface.WQ &= ~(1L << end);
//                        break;
//                    case "K":
//                        UserInterface.WK |= (1L << start);
//                        UserInterface.WK &= ~(1L << end);
//                        break;
//                    case "p":
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        break;
//                    case "n":
//                        UserInterface.BN |= (1L << start);
//                        UserInterface.BN &= ~(1L << end);
//                        break;
//                    case "b":
//                        UserInterface.BB |= (1L << start);
//                        UserInterface.BB &= ~(1L << end);
//                        break;
//                    case "r":
//                        UserInterface.BR |= (1L << start);
//                        UserInterface.BR &= ~(1L << end);
//                        break;
//                    case "q":
//                        UserInterface.BQ |= (1L << start);
//                        UserInterface.BQ &= ~(1L << end);
//                        break;
//                    case "k":
//                        UserInterface.BK |= (1L << start);
//                        UserInterface.BK &= ~(1L << end);
//                        break;
//                }
//            } else if (piece.length() == 2) {
//                switch (piece.charAt(0)) {
//                    case 'P':
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        break;
//                    case 'N':
//                        UserInterface.WN |= (1L << start);
//                        UserInterface.WN &= ~(1L << end);
//                        break;
//                    case 'B':
//                        UserInterface.WB |= (1L << start);
//                        UserInterface.WB &= ~(1L << end);
//                        break;
//                    case 'R':
//                        UserInterface.WR |= (1L << start);
//                        UserInterface.WR &= ~(1L << end);
//                        break;
//                    case 'Q':
//                        UserInterface.WQ |= (1L << start);
//                        UserInterface.WQ &= ~(1L << end);
//                        break;
//                    case 'K':
//                        UserInterface.WK |= (1L << start);
//                        UserInterface.WK &= ~(1L << end);
//                        break;
//                    case 'p':
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        break;
//                    case 'n':
//                        UserInterface.BN |= (1L << start);
//                        UserInterface.BN &= ~(1L << end);
//                        break;
//                    case 'b':
//                        UserInterface.BB |= (1L << start);
//                        UserInterface.BB &= ~(1L << end);
//                        break;
//                    case 'r':
//                        UserInterface.BR |= (1L << start);
//                        UserInterface.BR &= ~(1L << end);
//                        break;
//                    case 'q':
//                        UserInterface.BQ |= (1L << start);
//                        UserInterface.BQ &= ~(1L << end);
//                        break;
//                    case 'k':
//                        UserInterface.BK |= (1L << start);
//                        UserInterface.BK &= ~(1L << end);
//                        break;
//                }
//                switch (piece.charAt(1)) {
//                    case 'P':
//                        UserInterface.WP |= (1L << end);
//                        break;
//                    case 'N':
//                        UserInterface.WN |= (1L << end);
//                        break;
//                    case 'B':
//                        UserInterface.WB |= (1L << end);
//                        break;
//                    case 'R':
//                        UserInterface.WR |= (1L << end);
//                        break;
//                    case 'Q':
//                        UserInterface.WQ |= (1L << end);
//                        break;
//                    case 'K':
//                        UserInterface.WK |= (1L << end);
//                        break;
//                    case 'p':
//                        UserInterface.BP |= (1L << end);
//                        break;
//                    case 'n':
//                        UserInterface.BN |= (1L << end);
//                        break;
//                    case 'b':
//                        UserInterface.BB |= (1L << end);
//                        break;
//                    case 'r':
//                        UserInterface.BR |= (1L << end);
//                        break;
//                    case 'q':
//                        UserInterface.BQ |= (1L << end);
//                        break;
//                    case 'k':
//                        UserInterface.BK |= (1L << end);
//                        break;
//                }
//            } else if (piece.length() == 3) {
//                if (piece.charAt(2) == 'C') {
//                    if (piece.equals("KRC")) {
//                        UserInterface.WK |= (1L << start);
//                        UserInterface.WK &= ~(1L << end);
//                        UserInterface.WR |= (1L << CASTLE_ROOKS[0]);
//                        UserInterface.WR &= ~(1L << CASTLE_ROOKS[0] - 2);
//                        UserInterface.CWK = true;
//                    } else if (piece.equals("RKC")) {
//                        UserInterface.WK |= (1L << start);
//                        UserInterface.WK &= ~(1L << end);
//                        UserInterface.WR |= (1L << CASTLE_ROOKS[1]);
//                        UserInterface.WR &= ~(1L << CASTLE_ROOKS[1] + 3);
//                        UserInterface.CWQ = true;
//                    } else if (piece.equals("krC")) {
//                        UserInterface.WK |= (1L << start);
//                        UserInterface.WK &= ~(1L << end);
//                        UserInterface.WR |= (1L << CASTLE_ROOKS[2]);
//                        UserInterface.WR &= ~(1L << CASTLE_ROOKS[2] - 2);
//                        UserInterface.CBK = true;
//                    } else if (piece.equals("rkC")) {
//                        UserInterface.WK |= (1L << start);
//                        UserInterface.WK &= ~(1L << end);
//                        UserInterface.WR |= (1L << CASTLE_ROOKS[3]);
//                        UserInterface.WR &= ~(1L << CASTLE_ROOKS[3] + 3);
//                        UserInterface.CWQ = true;
//                    }
//                }
//            }
//        } else if (move.charAt(3) == 'E') {
//            if (move.charAt(2) == 'W') {
//                int start = 3 * 8 + (move.charAt(0) - '0');
//                int end = 2 * 8 + (move.charAt(1) - '0');
//                if (start - end == 7) {
//                    UserInterface.WP |= (1L << start);
//                    UserInterface.WP &= ~(1L << end);
//                    UserInterface.BP |= (1L << (start + 1));
//                } else if (start - end == 9) {
//                    UserInterface.WP |= (1L << start);
//                    UserInterface.WP &= ~(1L << end);
//                    UserInterface.BP |= (1L << (start - 1));
//                }
//            } else if (move.charAt(2) == 'B') {
//                int start = 4 * 8 + (move.charAt(0) - '0');
//                int end = 5 * 8 + (move.charAt(1) - '0');
//                if (end - start == 7) {
//                    UserInterface.BP |= (1L << start);
//                    UserInterface.BP &= ~(1L << end);
//                    UserInterface.WP |= (1L << (start - 1));
//                } else if (end - start == 9) {
//                    UserInterface.BP |= (1L << start);
//                    UserInterface.BP &= ~(1L << end);
//                    UserInterface.WP |= (1L << (start + 1));
//                }
//            }
//        } else if (move.charAt(3) == 'P') {
//            if (move.charAt(0) == move.charAt(1)) {
//                if (Character.isUpperCase(move.charAt(2))) {
//                    int start = 8 + (move.charAt(0) - '0');
//                    int end = (move.charAt(0) - '0');
//                    if (move.charAt(2) == 'N') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WN &= ~(1L << end);
//                    } else if (move.charAt(2) == 'B') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WB &= ~(1L << end);
//                    } else if (move.charAt(2) == 'R') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WR &= ~(1L << end);
//                    } else if (move.charAt(2) == 'Q') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WQ &= ~(1L << end);
//                    }
//                } else {
//                    int start = 8 * 6 + (move.charAt(0) - '0');
//                    int end = 8 * 7 + (move.charAt(0) - '0');
//                    if (move.charAt(2) == 'n') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BN &= ~(1L << end);
//                    } else if (move.charAt(2) == 'b') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BB &= ~(1L << end);
//                    } else if (move.charAt(2) == 'r') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BR &= ~(1L << end);
//                    } else if (move.charAt(2) == 'q') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BQ &= ~(1L << end);
//                    }
//                }
//            } else {
//                if (Character.isUpperCase(move.charAt(2))) {
//                    int start = 8 + (move.charAt(0) - '0');
//                    int end = (move.charAt(1) - '0');
//                    if (move.charAt(2) == 'N') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WN &= ~(1L << end);
//                    } else if (move.charAt(2) == 'B') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WB &= ~(1L << end);
//                    } else if (move.charAt(2) == 'R') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WR &= ~(1L << end);
//                    } else if (move.charAt(2) == 'Q') {
//                        UserInterface.WP |= (1L << start);
//                        UserInterface.WP &= ~(1L << end);
//                        UserInterface.WQ &= ~(1L << end);
//                    }
//                    if (piece.charAt(1) == 'n') {
//                        UserInterface.BN |= (1L << end);
//                    } else if (piece.charAt(1) == 'b') {
//                        UserInterface.BB |= (1L << end);
//                    } else if (piece.charAt(1) == 'r') {
//                        UserInterface.BR |= (1L << end);
//                    } else if (piece.charAt(1) == 'q') {
//                        UserInterface.BQ |= (1L << end);
//                    }
//                } else {
//                    int start = 8 * 6 + (move.charAt(0) - '0');
//                    int end = 8 * 7 + (move.charAt(1) - '0');
//                    if (move.charAt(2) == 'n') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BN &= ~(1L << end);
//                    } else if (move.charAt(2) == 'b') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BB &= ~(1L << end);
//                    } else if (move.charAt(2) == 'r') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BR &= ~(1L << end);
//                    } else if (move.charAt(2) == 'q') {
//                        UserInterface.BP |= (1L << start);
//                        UserInterface.BP &= ~(1L << end);
//                        UserInterface.BQ &= ~(1L << end);
//                    }
//                    if (piece.charAt(1) == 'N') {
//                        UserInterface.WN |= (1L << end);
//                    } else if (piece.charAt(1) == 'B') {
//                        UserInterface.WB |= (1L << end);
//                    } else if (piece.charAt(1) == 'R') {
//                        UserInterface.WR |= (1L << end);
//                    } else if (piece.charAt(1) == 'Q') {
//                        UserInterface.WQ |= (1L << end);
//                    }
//                }
//            }
//        }
//        if (MOVE.size() >= 2) {
//            UserInterface.EP = UserInterface.HISTORIC_EP.get(UserInterface.HISTORIC_EP.size() - 2);
//        } else {
//            UserInterface.EP = 0;
//        }
//        UserInterface.WhiteToMove = !UserInterface.WhiteToMove;
//        MOVE.remove(MOVE.size() - 1);
//        PIECE.remove(PIECE.size() - 1);
//    }

//    public static void getHistory(String move, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP,
//                                  long WPt, long WNt, long WBt, long WRt, long WQt, long WKt, long BPt, long BNt, long BBt, long BRt, long BQt, long BKt, long EPt) {
//        long[] oriList = {WP, 0, WN, 1, WB, 2, WR, 3, WQ, 4, WK, 5, BP, 6, BN, 7, BB, 8, BR, 9, BQ, 10, BK, 11, EP, 12};
//        long[] tempList = {WPt, 0, WNt, 1, WBt, 2, WRt, 3, WQt, 4, WKt, 5, BPt, 6, BNt, 7, BBt, 8, BRt, 9, BQt, 10, BKt, 11, EPt, 12};
//
//        ArrayList<Long> checkingDiff = new ArrayList<Long>();
//
//        for (int i = 0; i < oriList.length; i += 2) {
//            if ((oriList[i] ^ tempList[i]) != 0) {
//                checkingDiff.add(tempList[i]);
//                checkingDiff.add(tempList[i + 1]);
//            }
//        }
////        System.out.println(checkingDiff.size());
//        if (checkingDiff.size() == 2) {
//            // only regular move and no capture
//            String a = "";
//            if (checkingDiff.get(1) == 0) {
//                a += "P";
//            } else if (checkingDiff.get(1) == 1) {
//                a += "N";
//            } else if (checkingDiff.get(1) == 2) {
//                a += "B";
//            } else if (checkingDiff.get(1) == 3) {
//                a += "R";
//            } else if (checkingDiff.get(1) == 4) {
//                a += "Q";
//            } else if (checkingDiff.get(1) == 5) {
//                a += "K";
//            } else if (checkingDiff.get(1) == 6) {
//                a += "p";
//            } else if (checkingDiff.get(1) == 7) {
//                a += "n";
//            } else if (checkingDiff.get(1) == 8) {
//                a += "b";
//            } else if (checkingDiff.get(1) == 9) {
//                a += "r";
//            } else if (checkingDiff.get(1) == 10) {
//                a += "q";
//            } else if (checkingDiff.get(1) == 11) {
//                a += "k";
//            }
//            if (Math.abs(Long.numberOfTrailingZeros(WP ^ WPt) - Long.numberOfLeadingZeros(WP ^ WPt)) == 16 ||
//                Math.abs(Long.numberOfTrailingZeros(BP ^ BPt) - Long.numberOfLeadingZeros(BP ^ BPt)) == 16)
//            {
//                UserInterface.HISTORIC_EP.add(EPt);
//            }
//            else
//            {
//                UserInterface.HISTORIC_EP.add(0L);
//            }
//            UserInterface.HISTORIC_PIECES.add(a);
//
//        } else if (checkingDiff.size() == 4) {
//            if (Character.isDigit(move.charAt(3))) {
//                int start = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
//                int end = (Character.getNumericValue(move.charAt(2)) * 8) + (Character.getNumericValue(move.charAt(3)));
//                int dist = Math.abs(Math.abs(start) - Math.abs(end));
//                boolean d = dist >= 2;
//                if ((WK & (1L << start)) != 0 && d) {
//                    if (end - start == 2) {
//                        UserInterface.HISTORIC_PIECES.add("KRC");
//                    } else if (start - end == 3) {
//                        UserInterface.HISTORIC_PIECES.add("RKC");
//                    }
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if ((BK & (1L << start)) != 0 && d) {
//                    if (end - start == 2) {
//                        UserInterface.HISTORIC_PIECES.add("krC");
//                    } else if (start - end == 3) {
//                        UserInterface.HISTORIC_PIECES.add("rkC");
//                    }
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (checkingDiff.get(2) == EPt && dist == 16) {
//                    if (checkingDiff.get(1) == 0) {
//                        UserInterface.HISTORIC_PIECES.add("P");
//                    } else if (checkingDiff.get(1) == 6) {
//                        UserInterface.HISTORIC_PIECES.add("p");
//                    }
//                    UserInterface.HISTORIC_EP.add(EPt);
//                }
//                else {
//                    StringBuilder a = new StringBuilder();
//                    for (int i = 0; i < 4; i += 2) {
//                        if ((checkingDiff.get(i) & (1L << start)) == 0 && (checkingDiff.get(i) & (1L << end)) != 0) {
//                            if (a.length() == 2)
//                            {
//                                break;
//                            }
//                            if (checkingDiff.get(i + 1) == 0) {
//                                a.append("P");
//                            } else if (checkingDiff.get(i + 1) == 1) {
//                                a.append("N");
//                            } else if (checkingDiff.get(i + 1) == 2) {
//                                a.append("B");
//                            } else if (checkingDiff.get(i + 1) == 3) {
//                                a.append("R");
//                            } else if (checkingDiff.get(i + 1) == 4) {
//                                a.append("Q");
//                            } else if (checkingDiff.get(i + 1) == 5) {
//                                a.append("K");
//                            } else if (checkingDiff.get(i + 1) == 6) {
//                                a.append("p");
//                            } else if (checkingDiff.get(i + 1) == 7) {
//                                a.append("n");
//                            } else if (checkingDiff.get(i + 1) == 8) {
//                                a.append("b");
//                            } else if (checkingDiff.get(i + 1) == 9) {
//                                a.append("r");
//                            } else if (checkingDiff.get(i + 1) == 10) {
//                                a.append("q");
//                            } else if (checkingDiff.get(i + 1) == 11) {
//                                a.append("k");
//                            }
//                            if (a.length() == 1)
//                            {
//                                i = -2;
//                            }
//                        }
//                        else if ((checkingDiff.get(i) & (1L << start)) == 0 && (checkingDiff.get(i) & (1L << end)) == 0) {
//                            if (a.isEmpty())
//                            {
//                                continue;
//                            }
//                            if (checkingDiff.get(i + 1) == 0) {
//                                a.append("P");
//                            } else if (checkingDiff.get(i + 1) == 1) {
//                                a.append("N");
//                            } else if (checkingDiff.get(i + 1) == 2) {
//                                a.append("B");
//                            } else if (checkingDiff.get(i + 1) == 3) {
//                                a.append("R");
//                            } else if (checkingDiff.get(i + 1) == 4) {
//                                a.append("Q");
//                            } else if (checkingDiff.get(i + 1) == 5) {
//                                a.append("K");
//                            } else if (checkingDiff.get(i + 1) == 6) {
//                                a.append("p");
//                            } else if (checkingDiff.get(i + 1) == 7) {
//                                a.append("n");
//                            } else if (checkingDiff.get(i + 1) == 8) {
//                                a.append("b");
//                            } else if (checkingDiff.get(i + 1) == 9) {
//                                a.append("r");
//                            } else if (checkingDiff.get(i + 1) == 10) {
//                                a.append("q");
//                            } else if (checkingDiff.get(i + 1) == 11) {
//                                a.append("k");
//                            }
//                            if (a.length() == 2)
//                            {
//                                break;
//                            }
//                        }
//                    }
//                    UserInterface.HISTORIC_EP.add(0L);
//                    UserInterface.HISTORIC_PIECES.add(a.toString());
//                }
//            } else if (move.charAt(3) == 'P') {
//                UserInterface.HISTORIC_PIECES.add("" + move.charAt(2) + move.charAt(3));
//                UserInterface.HISTORIC_EP.add(0L);
//            }
//        } else if (checkingDiff.size() == 6) {
//            //en passant
//            if (move.charAt(3) == 'E') {
//                if (move.charAt(2) == 'W') {
//                    UserInterface.HISTORIC_PIECES.add("Pp");
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (move.charAt(2) == 'B') {
//                    UserInterface.HISTORIC_PIECES.add("pP");
//                    UserInterface.HISTORIC_EP.add(0L);
//                }
//                return;
//            }
//
//            //promote
//            if (checkingDiff.get(0) == WPt) {
//                if (checkingDiff.get(5) == 7) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "n");
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (checkingDiff.get(5) == 8) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "b");
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (checkingDiff.get(5) == 9) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "r");
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (checkingDiff.get(5) == 10) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "q");
//                    UserInterface.HISTORIC_EP.add(0L);
//                }
//            } else if (checkingDiff.get(2) == BPt) {
//                if (checkingDiff.get(1) == 1) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "N");
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (checkingDiff.get(1) == 2) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "B");
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (checkingDiff.get(1) == 3) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "R");
//                    UserInterface.HISTORIC_EP.add(0L);
//                } else if (checkingDiff.get(1) == 4) {
//                    UserInterface.HISTORIC_PIECES.add(move.charAt(2) + "Q");
//                    UserInterface.HISTORIC_EP.add(0L);
//                }
//            }
//
//            //no en passant and capture
//
//            System.out.println("awefawefawef");
//        }
//    }

    public static void undoMove2() {
        int size = UserInterface.HISTORIC_BITBOARD.size();
        UserInterface.WP = UserInterface.HISTORIC_BITBOARD.get(size - 1).WP;
        UserInterface.WN = UserInterface.HISTORIC_BITBOARD.get(size - 1).WN;
        UserInterface.WB = UserInterface.HISTORIC_BITBOARD.get(size - 1).WB;
        UserInterface.WR = UserInterface.HISTORIC_BITBOARD.get(size - 1).WR;
        UserInterface.WQ = UserInterface.HISTORIC_BITBOARD.get(size - 1).WQ;
        UserInterface.WK = UserInterface.HISTORIC_BITBOARD.get(size - 1).WK;
        UserInterface.BP = UserInterface.HISTORIC_BITBOARD.get(size - 1).BP;
        UserInterface.BN = UserInterface.HISTORIC_BITBOARD.get(size - 1).BN;
        UserInterface.BB = UserInterface.HISTORIC_BITBOARD.get(size - 1).BB;
        UserInterface.BR = UserInterface.HISTORIC_BITBOARD.get(size - 1).BR;
        UserInterface.BQ = UserInterface.HISTORIC_BITBOARD.get(size - 1).BQ;
        UserInterface.BK = UserInterface.HISTORIC_BITBOARD.get(size - 1).BK;
        UserInterface.EP = UserInterface.HISTORIC_BITBOARD.get(size - 1).EP;

        UserInterface.CWK = UserInterface.HISTORIC_BITBOARD.get(size - 1).CWK;
        UserInterface.CWQ = UserInterface.HISTORIC_BITBOARD.get(size - 1).CWQ;
        UserInterface.CBK = UserInterface.HISTORIC_BITBOARD.get(size - 1).CBK;
        UserInterface.CBQ = UserInterface.HISTORIC_BITBOARD.get(size - 1).CBQ;
        UserInterface.HISTORIC_BITBOARD.remove(size - 1);
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
//        WPmt = UserInterface.WP;
//        WNmt = UserInterface.WN;
//        WBmt = UserInterface.WB;
//        WRmt = UserInterface.WR;
//        WQmt = UserInterface.WQ;
//        WKmt = UserInterface.WK;
//        BPmt = UserInterface.BP;
//        BNmt = UserInterface.BN;
//        BBmt = UserInterface.BB;
//        BRmt = UserInterface.BR;
//        BQmt = UserInterface.BQ;
//        BKmt = UserInterface.BK;
//        EPmt = UserInterface.EP;
//
//        CWKm = UserInterface.CWK;
//        CWQm = UserInterface.CWQ;
//        CBKm = UserInterface.CBK;
//        CBQm = UserInterface.CBQ;
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



























