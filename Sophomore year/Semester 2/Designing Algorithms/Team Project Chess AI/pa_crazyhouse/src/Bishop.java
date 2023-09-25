package src;

import java.util.ArrayList;

public class Bishop extends APiece {
    public Bishop(PlaySide side) {
        super(side);
        setType(Piece.BISHOP);
    }

    public int[][] importanceW = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10,   5,  10,   0,   5,   0,   0, -10},
            {-10,   0,  10,  10,   5,   5,   0, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,   0,  10,  10,   5,   5,   0, -10},
            {-10,   5,  10,   0,   5,   0,   0, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}
    };

    public int[][] importanceB = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10,   0,   0,   5,   0,  10,   5, -10},
            {-10,   0,   5,   5,  10,  10,   0, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,   0,   5,   5,  10,  10,   0, -10},
            {-10,   0,   0,   5,   0,  10,   5, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}
    };



    @Override
    public int[][] getImportanceW() {
        return importanceW;
    }

    public int[][] getImportanceB() {
        return importanceB;
    }
    // nebun
    @Override
    public ArrayList<Move> calculatePossibleMoves(String source, APiece[][] table) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int[] indexes = translateMoveToIndex(source);
        int[] x = {-1, -1, 1, 1};
        int[] y = {-1, 1, -1, 1};

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