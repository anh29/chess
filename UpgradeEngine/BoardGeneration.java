package UpgradeEngine;

import java.util.Arrays;

public class BoardGeneration {
    public static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;

    public static void init() {
        String[][] chessBoard = {
                {"r", " ", " ", "q", " ", "b", "n", "r"},
                {"p", "p", " ", " ", "k", " ", "p", "p"},
                {" ", " ", " ", "p", " ", "p", " ", " "},
                {" ", "B", "p", " ", "N", " ", "B", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "P", "p", " ", " ", " ", " "},
                {"P", "P", " ", " ", " ", "P", "P", "P"},
                {"R", "N", " ", "b", "R", " ", "K", " "}};

        ArrayToBitBoard(chessBoard, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
    }

    public static void ArrayToBitBoard(String[][] chessBoard, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
        for (int i = 0; i < 64; i++) {
            String Binary = "0000000000000000000000000000000000000000000000000000000000000000";
            Binary = Binary.substring(i + 1) + "1" + Binary.substring(0, i);
            switch (chessBoard[i / 8][i % 8]) {
                case "P":
                    WP += StringToBinary(Binary);
                    break;
                case "N":
                    WN += StringToBinary(Binary);
                    break;
                case "B":
                    WB += StringToBinary(Binary);
                    break;
                case "R":
                    WR += StringToBinary(Binary);
                    break;
                case "Q":
                    WQ += StringToBinary(Binary);
                    break;
                case "K":
                    WK += StringToBinary(Binary);
                    break;
                case "p":
                    BP += StringToBinary(Binary);
                    break;
                case "n":
                    BN += StringToBinary(Binary);
                    break;
                case "b":
                    BB += StringToBinary(Binary);
                    break;
                case "r":
                    BR += StringToBinary(Binary);
                    break;
                case "q":
                    BQ += StringToBinary(Binary);
                    break;
                case "k":
                    BK += StringToBinary(Binary);
                    break;
            }
        }
        drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        UserInterface.WP = WP;
        UserInterface.WN = WN;
        UserInterface.WB = WB;
        UserInterface.WR = WR;
        UserInterface.WQ = WQ;
        UserInterface.WK = WK;
        UserInterface.BP = BP;
        UserInterface.BN = BN;
        UserInterface.BB = BB;
        UserInterface.BR = BR;
        UserInterface.BQ = BQ;
        UserInterface.BK = BK;
    }

    public static long StringToBinary(String bit) {
        if (bit.charAt(0) == '0') {
            return Long.parseLong(bit, 2);
        } else {
            return Long.parseLong("1" + bit.substring(2), 2) * 2;
        }
    }

    public static void drawArray(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
        String[][] chessBoard = new String[8][8];
        for (int i = 0; i < 64; i++) {
            chessBoard[i / 8][i % 8] = " ";
        }

        for (int i = 0; i < 64; i++) {
            if (((WP >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "P";
            }
            if (((WN >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "N";
            }
            if (((WB >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "B";
            }
            if (((WR >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "R";
            }
            if (((WQ >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "Q";
            }
            if (((WK >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "K";
            }
            if (((BP >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "p";
            }
            if (((BN >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "n";
            }
            if (((BB >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "b";
            }
            if (((BR >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "r";
            }
            if (((BQ >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "q";
            }
            if (((BK >> i) & 1) == 1) {
                chessBoard[i / 8][i % 8] = "k";
            }
        }

        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
}




































