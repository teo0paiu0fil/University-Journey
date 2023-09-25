package src;

import java.util.ArrayList;

public class King extends APiece {
    public boolean moved = false;
    public King(PlaySide side) {
        super(side);
        setType(Piece.KING);
    }

    public int[][] importanceW = {
            {20, 20, -10, -20, -30, -30, -30, -30},
            {30, 20, -20, -30, -40, -40, -40, -40},
            {10,  0, -20, -30, -40, -40, -40, -40},
            {0,   0, -20, -40, -50, -50, -50, -50},
            {0,   0, -20, -40, -50, -50, -50, -50},
            {10,  0, -20, -30, -40, -40, -40, -40},
            {20, 20, -20, -30, -40, -40, -40, -40},
            {20, 20, -10, -20, -30, -30, -30, -30}
    };

    public int[][] importanceB = {
            {-30, -30, -30, -30, -20, -10, 20, 20},
            {-40, -40, -40, -40, -30, -20, 20, 30},
            {-40, -40, -40, -40, -30, -20,  0, 10},
            {-50, -50, -50, -50, -40, -20,  0,  0},
            {-50, -50, -50, -50, -40, -20,  0,  0},
            {-40, -40, -40, -40, -30, -20,  0, 10},
            {-40, -40, -40, -40, -30, -20, 20, 20},
            {-30, -30, -30, -30, -20, -10, 20, 20}
    };

    @Override
    public int[][] getImportanceW() {
        return importanceW;
    }

    public int[][] getImportanceB() {
        return importanceB;
    }

    @Override
    public ArrayList<Move> calculatePossibleMoves(String source, APiece[][] table) {
        ArrayList<Move> moves = new ArrayList<>();
        int[] src = translateMoveToIndex(source);

        for (int i = src[0] - 1; i <= src[0] + 1; i++) {
            if(i >= 0 && i < 8) {
                for (int j = src[1] - 1; j <= src[1] + 1; j++) {
                    if (j >= 0 && j < 8) {
                        if (src[0] == i && src[1] == j)
                            continue;
                        if (table[i][j] == null || table[i][j].getSide() != this.getSide()) {
                            moves.add(Move.moveTo(source, translateIndexToMove(new int[]{i, j})));
                        }
                    }
                }
            }
        }

        return moves;
    }
}
