package src;

import java.util.ArrayList;

public class Rook extends APiece {
    public boolean moved = false;
    public Rook(PlaySide side) {
        super(side);
        setType(Piece.ROOK);
    }

    public int[][] importanceW = {
            {0, -5, -5, -5, -5, -5,  5, 0},
            {0,  0,  0,  0,  0,  0, 10, 0},
            {0,  0,  0,  0,  0,  0, 10, 0},
            {5,  0,  0,  0,  0,  0, 10, 0},
            {5,  0,  0,  0,  0,  0, 10, 0},
            {0,  0,  0,  0,  0,  0, 10, 0},
            {0,  0,  0,  0,  0,  0, 10, 0},
            {0, -5, -5, -5, -5, -5,  5, 0},
    };

    public int[][] importanceB = {
            {0, -5, -5, -5, -5, -5, 5, 0},
            {0, 10,  0,  0,  0,  0, 0, 0},
            {0, 10,  0,  0,  0,  0, 0, 0},
            {0, 10,  0,  0,  0,  0, 0, 5},
            {0, 10,  0,  0,  0,  0, 0, 5},
            {0, 10,  0,  0,  0,  0, 0, 0},
            {0, 10,  0,  0,  0,  0, 0, 0},
            {0, -5, -5, -5, -5, -5, 5, 0},
    };

    @Override
    public int[][] getImportanceW() {
        return importanceW;
    }

    public int[][] getImportanceB() {
        return importanceB;
    }

    // tura
    @Override
    public ArrayList<Move> calculatePossibleMoves(String source, APiece[][] table) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int[] indexes = translateMoveToIndex(source);
        int[] x = {0, 0, 1, -1};
        int[] y = {-1, 1, 0, 0};

        for (int k = 0; k < 4; k++) {
            int i = indexes[0] + x[k];
            int j = indexes[1] + y[k];
            while (i >= 0 && i <= 7 && j >= 0 && j <= 7) {
                if (table[i][j] == null) {
                    String destination = createDestination(i, j);
                    Move move = Move.moveTo(source, destination);
                    possibleMoves.add(move);
                } else if (table[i][j].getSide() != this.getSide()) {
                    String destination = createDestination(i, j);
                    Move move = Move.moveTo(source, destination);
                    possibleMoves.add(move);
                    break;
                } else break;

                i += x[k];
                j += y[k];
            }
        }
        return possibleMoves;
    }
}
