package UpgradeEngine;

public class TranspositionTable {
    private static final int hashSize = 0x400000;
    private static final int noHashEntry = 100000;
    private static final int hashFlagExact = 0;
    private static final int hashFlagAlpha = 1;
    private static final int hashFlagBeta = 2;

    private static class TT {
        long hashKey;
        int depth;
        int flag;
        int score;
    }

    private static TT[] hashTable = new TT[hashSize];

    private static void clearHashTable() {
        for (int index = 0; index < hashSize; index++) {
            hashTable[index] = new TT();
        }
    }

    private static int readHashEntry(long hashKey, int alpha, int beta, int depth) {
        TT hashEntry = hashTable[(int) (hashKey % hashSize)];

        if (hashEntry.hashKey == hashKey) {
            if (hashEntry.depth >= depth) {
                if (hashEntry.flag == hashFlagExact)
                    return hashEntry.score;

                if ((hashEntry.flag == hashFlagAlpha) && (hashEntry.score <= alpha))
                    return alpha;

                if ((hashEntry.flag == hashFlagBeta) && (hashEntry.score >= beta))
                    return beta;
            }
        }

        return noHashEntry;
    }

    private static void writeHashEntry(long hashKey, int score, int depth, int hashFlag) {
        TT hashEntry = hashTable[(int) (hashKey % hashSize)];

        hashEntry.hashKey = hashKey;
        hashEntry.score = score;
        hashEntry.flag = hashFlag;
        hashEntry.depth = depth;
    }
}
