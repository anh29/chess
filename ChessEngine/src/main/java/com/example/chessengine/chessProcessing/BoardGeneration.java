package com.example.chessengine.chessProcessing;

import com.example.chessengine.rest.ChessGameController;
import com.example.chessengine.utility.MatchCombinedId;

import java.util.Arrays;

public class BoardGeneration {
    public static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
    public static boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;
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

//        ArrayToBitBoard(chessBoard, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
    }

    public static void ArrayToBitBoard(String[][] chessBoard, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ, boolean whiteToMove, String idMatchType, String chessGameId) {
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
        MatchCombinedId combinedId = MatchCombinedId.builder().matchTypeId(idMatchType).matchId(chessGameId).build();
        ChessGameController.games.get(combinedId).WP = WP;
        ChessGameController.games.get(combinedId).WN = WN;
        ChessGameController.games.get(combinedId).WB = WB;
        ChessGameController.games.get(combinedId).WR = WR;
        ChessGameController.games.get(combinedId).WQ = WQ;
        ChessGameController.games.get(combinedId).WK = WK;
        ChessGameController.games.get(combinedId).BP = BP;
        ChessGameController.games.get(combinedId).BN = BN;
        ChessGameController.games.get(combinedId).BB = BB;
        ChessGameController.games.get(combinedId).BR = BR;
        ChessGameController.games.get(combinedId).BQ = BQ;
        ChessGameController.games.get(combinedId).BK = BK;
        ChessGameController.games.get(combinedId).CWK = CWK;
        ChessGameController.games.get(combinedId).CWQ = CWQ;
        ChessGameController.games.get(combinedId).CBK = CBK;
        ChessGameController.games.get(combinedId).CBQ = CBQ;
        ChessGameController.games.get(combinedId).WhiteToMove = WhiteToMove;
//        ChessGameController.WP = WP;
//        ChessGameController.WN = WN;
//        ChessGameController.WB = WB;
//        ChessGameController.WR = WR;
//        ChessGameController.WQ = WQ;
//        ChessGameController.WK = WK;
//        ChessGameController.BP = BP;
//        ChessGameController.BN = BN;
//        ChessGameController.BB = BB;
//        ChessGameController.BR = BR;
//        ChessGameController.BQ = BQ;
//        ChessGameController.BK = BK;
//        ChessGameController.CWK = CWK;
//        ChessGameController.CWQ = CWQ;
//        ChessGameController.CBK = CBK;
//        ChessGameController.CBQ = CBQ;
//        ChessGameController.WhiteToMove = WhiteToMove;

//        ChessGameController.games.get(chessGameId).WP = WP;
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

    public static void initFromFEN(String fen, String idMatchType, String chessGameId) {
        String[] parts = fen.split(" ");
        String boardLayout = parts[0];
        String[][] chessBoard = new String[8][8];
        String[] rows = boardLayout.split("/");
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            for (int j = 0, column = 0; j < row.length(); j++) {
                char symbol = row.charAt(j);
                if (Character.isDigit(symbol)) {
                    int emptySpaces = Character.getNumericValue(symbol);
                    for (int k = 0; k < emptySpaces; k++, column++) {
                        chessBoard[i][column] = " ";
                    }
                } else {
                    chessBoard[i][column] = String.valueOf(symbol);
                    column++;
                }
            }
        }

        String[] fenParts = fen.split(" ");
        // Set the active color
        WhiteToMove = fenParts[1].equals("w");

        // Reset castling availability
        CWK = false;
        CWQ = false;
        CBK = false;
        CBQ = false;

        // Set the castling availability
        for (char c : fenParts[2].toCharArray()) {
            switch (c) {
                case 'K':
                    CWK = true;
                    break;
                case 'Q':
                    CWQ = true;
                    break;
                case 'k':
                    CBK = true;
                    break;
                case 'q':
                    CBQ = true;
                    break;
            }
        }

        ArrayToBitBoard(chessBoard, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK , CWK, CWQ, CBK, CBQ, WhiteToMove, idMatchType, chessGameId);
    }

}




































