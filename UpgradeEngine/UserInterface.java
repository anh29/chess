package UpgradeEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class UserInterface extends JPanel {
    static int SearchingDepth = 4;
    static long FILE_A = 0x101010101010101L;
    static long FILE_H = 0x8080808080808080L;
    static long RANK_4 = 0xFF00000000L;

    static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L, BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L, EP = 0L;
//    static ArrayList<String> HISTORIC_PIECES = new ArrayList<String>();
//    static ArrayList<String> HISTORIC_MOVES = new ArrayList<String>();
    static ArrayList<HistoricInfo> HISTORIC_BITBOARD = new ArrayList<HistoricInfo>();
//    static ArrayList<Long> HISTORIC_EP = new ArrayList<Long>();
    static boolean CWK = true, CWQ = true, CBK = true, CBQ = true, WhiteToMove = true;
    static long UniversalWP = 0L, UniversalWN = 0L, UniversalWB = 0L, UniversalWR = 0L, UniversalWQ = 0L, UniversalWK = 0L, UniversalBP = 0L, UniversalBN = 0L, UniversalBB = 0L, UniversalBR = 0L, UniversalBQ = 0L, UniversalBK = 0L, UniversalEP = 0L;
    static boolean UniversalCastleWK = true, UniversalCastleWQ = true, UniversalCastleBK = true, UniversalCastleBQ = true;
    static int humanIsWhite = 1;
    static int rating = 0;
    static int border = 10;//the amount of empty space around the frame
    static double squareSize = 64;//the size of a chess board square
    static JFrame javaF = new JFrame("Chess Engine");//must be declared as static so that other class' can repaint
    static UserInterface javaUI = new UserInterface();//must be declared as static so that other class' can repaint

    public static void main(String[] args) {
        javaF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        javaF.add(javaUI);
        javaF.setSize(757, 570);
        javaF.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - javaF.getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - javaF.getHeight()) / 2);
        javaF.setVisible(true);
        newGame();
        javaF.repaint();
    }

    public static void newGame() {
        BoardGeneration.init();
        Calculation.init();
        Zobrist.zobristFillArray();
        for (int i = 0; i < Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length(); i += 4)
        {
            System.out.println(Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).substring(i, i + 4));
        }
        System.out.println("---------------");
        for (int i = 0; i < Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length(); i += 4)
        {
            System.out.println(Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).substring(i, i + 4));
        }
        System.out.println("---------------");
//        for (int i = 0; i < Moves.onlyCaptureMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true).length(); i += 4)
//        {
//            System.out.println(Moves.onlyCaptureMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, true).substring(i, i + 4));
//        }
//        System.out.println("---------------");
//        for (int i = 0; i < Moves.onlyCaptureMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, false).length(); i += 4)
//        {
//            System.out.println(Moves.onlyCaptureMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, false).substring(i, i + 4));
//        }
//        System.out.println("---------------");

//        System.out.println(BP);
//        Moves.makeMove("7757", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
//        BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
//        System.out.println(Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK));
//        System.out.println(BP);
//        BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
//        Moves.moveOnBoard("6545", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        Moves.moveOnBoard("4435", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//
//        Moves.moveOnBoard("6646", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//
//        for (int i = 0; i < Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length(); i += 4)
//        {
//            System.out.println(Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).substring(i, i + 4));
//        }
//        System.out.println("---------------");
//        for (int i = 0; i < Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length(); i += 4)
//        {
//            System.out.println(Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).substring(i, i + 4));
//        }
//        System.out.println("---------------");
////        System.out.println((BP << 1) & WP & RANK_4 & ~FILE_A & EP);

//        Moves.moveOnBoard("76BE", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//
//        Moves.moveOnBoard("6555", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        for (int i = 0; i < Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length(); i += 4)
//        {
//            System.out.println(Moves.WhitePossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).substring(i, i + 4));
//        }
//        System.out.println("---------------");
//        for (int i = 0; i < Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).length(); i += 4)
//        {
//            System.out.println(Moves.BlackPossibleMoves(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ).substring(i, i + 4));
//        }
//        System.out.println("---------------");
//        Moves.moveOnBoard("01NP", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        try
//        {
//            System.out.println(HISTORIC_MOVES.toString());
//            System.out.println(HISTORIC_PIECES.toString());
//        }
//        catch (Exception e) {}
//
//        BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
////        Moves.undoMove(HISTORIC_MOVES, HISTORIC_PIECES);
//        Moves.undoMove(HISTORIC_MOVES, HISTORIC_PIECES);
//        BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
//        Moves.moveOnBoard("76BE", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        System.out.println(WK);

        System.out.println(Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, WhiteToMove));
        System.out.println(Evaluation.Evaluate(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, !WhiteToMove));
//        System.out.println("Score: " + Searching.negamax(2, -99999999, 99999999, WhiteToMove));
//        System.out.println("Score: " + Searching.Negamax2(4, -99999999, 99999999, WhiteToMove));
        
        while (Searching.Negamax2(SearchingDepth, -99999999, 99999999, WhiteToMove) == 99999999)
        {
            SearchingDepth--;
            System.out.println(Searching.Negamax2(SearchingDepth, -99999999, 99999999, WhiteToMove));
        }
        System.out.println("Best move: " + Searching.bestMove);

//        Moves.moveOnBoard("6040", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        Moves.moveOnBoard("1131", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        Moves.moveOnBoard("4031", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        Moves.moveOnBoard("6242", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);
//        Moves.moveOnBoard("3140", WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, WhiteToMove);

//        BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
//        System.out.println(HISTORIC_MOVES);
//        System.out.println(HISTORIC_PIECES);
//        Moves.undoMove(HISTORIC_MOVES, HISTORIC_PIECES);
//        Moves.undoMove(HISTORIC_MOVES, HISTORIC_PIECES);
//        Moves.undoMove2();
//        Moves.undoMove2();

//        System.out.println("------------");
        BoardGeneration.drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(200, 100, 0));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                squareSize = (double) (Math.min(getHeight(), getWidth() - 200 - border) - 2 * border) / 8;
            }
        });
        drawBorders(g);
        drawBoard(g);
        drawPieces(g);
    }

    public void drawBoard(Graphics g) {
        for (int i = 0; i < 64; i += 2) {//draw chess board
            g.setColor(new Color(255, 200, 100));
            g.fillRect((int) ((i % 8 + (i / 8) % 2) * squareSize) + border, (int) ((i / 8) * squareSize) + border, (int) squareSize, (int) squareSize);
            g.setColor(new Color(150, 50, 30));
            g.fillRect((int) (((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize) + border, (int) (((i + 1) / 8) * squareSize) + border, (int) squareSize, (int) squareSize);
        }
    }

    public void drawPieces(Graphics g) {
        Image chessPieces = new ImageIcon("UpgradeEngine/ChessPieces.png").getImage();
        for (int i = 0; i < 64; i++) {
            int j = -1, k = -1;
            if (((WP >> i) & 1) == 1) {
                j = 5;
                k = 1 - humanIsWhite;
            } else if (((BP >> i) & 1) == 1) {
                j = 5;
                k = humanIsWhite;
            } else if (((WB >> i) & 1) == 1) {
                j = 3;
                k = 1 - humanIsWhite;
            } else if (((BB >> i) & 1) == 1) {
                j = 3;
                k = humanIsWhite;
            } else if (((WN >> i) & 1) == 1) {
                j = 4;
                k = 1 - humanIsWhite;
            } else if (((BN >> i) & 1) == 1) {
                j = 4;
                k = humanIsWhite;
            } else if (((WQ >> i) & 1) == 1) {
                j = 1;
                k = 1 - humanIsWhite;
            } else if (((BQ >> i) & 1) == 1) {
                j = 1;
                k = humanIsWhite;
            } else if (((WR >> i) & 1) == 1) {
                j = 2;
                k = 1 - humanIsWhite;
            } else if (((BR >> i) & 1) == 1) {
                j = 2;
                k = humanIsWhite;
            } else if (((WK >> i) & 1) == 1) {
                j = 0;
                k = 1 - humanIsWhite;
            } else if (((BK >> i) & 1) == 1) {
                j = 0;
                k = humanIsWhite;
            }
            if (j != -1 && k != -1) {
                g.drawImage(chessPieces, (int) ((i % 8) * squareSize) + border, (int) ((i / 8) * squareSize) + border, (int) ((i % 8 + 1) * squareSize) + border, (int) ((i / 8 + 1) * squareSize) + border, j * 64, k * 64, (j + 1) * 64, (k + 1) * 64, this);
            }
        }
    }

    public void drawBorders(Graphics g) {
        g.setColor(new Color(100, 0, 0));
        g.fill3DRect(0, border, border, (int) (8 * squareSize), true);
        g.fill3DRect((int) (8 * squareSize) + border, border, border, (int) (8 * squareSize), true);
        g.fill3DRect(border, 0, (int) (8 * squareSize), border, true);
        g.fill3DRect(border, (int) (8 * squareSize) + border, (int) (8 * squareSize), border, true);

        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, border, border, true);
        g.fill3DRect((int) (8 * squareSize) + border, 0, border, border, true);
        g.fill3DRect(0, (int) (8 * squareSize) + border, border, border, true);
        g.fill3DRect((int) (8 * squareSize) + border, (int) (8 * squareSize) + border, border, border, true);
        g.fill3DRect((int) (8 * squareSize) + 2 * border + 200, 0, border, border, true);
        g.fill3DRect((int) (8 * squareSize) + 2 * border + 200, (int) (8 * squareSize) + border, border, border, true);

        g.setColor(new Color(0, 100, 0));
        g.fill3DRect((int) (8 * squareSize) + 2 * border, 0, 200, border, true);
        g.fill3DRect((int) (8 * squareSize) + 2 * border + 200, border, border, (int) (8 * squareSize), true);
        g.fill3DRect((int) (8 * squareSize) + 2 * border, (int) (8 * squareSize) + border, 200, border, true);
    }
}
































