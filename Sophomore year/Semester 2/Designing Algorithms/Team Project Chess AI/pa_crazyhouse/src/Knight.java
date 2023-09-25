package src;

import java.util.ArrayList;

public class Knight extends APiece {
    public Knight(PlaySide side) {
        super(side);
        setType(Piece.KNIGHT);
    }

    public int[][] importanceW = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20,   5,   0,   5,   0, -20, -40},
            {-30,   0,  10,  15,  15,  10,   0, -30},
            {-30,   5,  15,  20,  20,  15,   0, -30},
            {-30,   5,  15,  20,  20,  15,   0, -30},
            {-30,   0,  10,  15,  15,  10,   0, -30},
            {-40, -20,   5,   0,   5,   0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}
    };

    public int[][] importanceB = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20,   5,   0,   5,   0, -20, -40},
            {-30,   0,  10,  15,  15,  10,   0, -30},
            {-30,   0,  15,  20,  20,  15,   5, -30},
            {-30,   0,  15,  20,  20,  15,   5, -30},
            {-30,   0,  10,  15,  15,  10,   0, -30},
            {-40, -20,   5,   0,   5,   0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}
    };


    @Override
    public int[][] getImportanceW() {
        return importanceW;
    }

    public int[][] getImportanceB() {
        return importanceB;
    }
    // cal -> sare
    @Override
    public ArrayList<Move> calculatePossibleMoves(String source, APiece[][] table) {
        ArrayList<Move> moves = new ArrayList<>();
        int[] src = translateMoveToIndex(source);
        String dest;

        if((src[0] - 1 >= 0) && (src[1] - 2 >= 0)) {
            if (table[src[0] - 1][src[1] - 2] == null || table[src[0] - 1][src[1] - 2].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] - 1, src[1] - 2});
                moves.add(Move.moveTo(source, dest));
            }
        }

        if((src[0] - 2 >= 0) && (src[1] - 1 >= 0)) {
            if (table[src[0] - 2][src[1] - 1] == null || table[src[0] - 2][src[1] - 1].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] - 2, src[1] - 1});
                moves.add(Move.moveTo(source, dest));
            }
        }

        if((src[0] - 1 >= 0) && (src[1] + 2 < 8)) {
            if (table[src[0] - 1][src[1] + 2] == null || table[src[0] - 1][src[1] + 2].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] - 1, src[1] + 2});
                moves.add(Move.moveTo(source, dest));
            }
        }

        if((src[0] - 2 >= 0) && (src[1] + 1 < 8)) {
            if (table[src[0] - 2][src[1] + 1] == null || table[src[0] - 2][src[1] + 1].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] - 2, src[1] + 1});
                moves.add(Move.moveTo(source, dest));
            }
        }
        //////////////////

        if((src[0] + 1 < 8) && (src[1] - 2 >= 0)) {
            if (table[src[0] + 1][src[1] - 2] == null || table[src[0] + 1][src[1] - 2].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] + 1, src[1] - 2});
                moves.add(Move.moveTo(source, dest));
            }
        }

        if((src[0] + 2 < 8) && (src[1] - 1 >= 0)) {
            if (table[src[0] + 2][src[1] - 1] == null || table[src[0] + 2][src[1] - 1].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] + 2, src[1] - 1});
                moves.add(Move.moveTo(source, dest));
            }
        }


        if((src[0] + 1 < 8) && (src[1] + 2 < 8)) {
            if (table[src[0] + 1][src[1] + 2] == null || table[src[0] + 1][src[1] + 2].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] + 1, src[1] + 2});
                moves.add(Move.moveTo(source, dest));
            }
        }

        if((src[0] + 2 < 8) && (src[1] + 1 < 8)) {
            if (table[src[0] + 2][src[1] + 1] == null || table[src[0] + 2][src[1] + 1].getSide() != this.getSide()) {
                dest = translateIndexToMove(new int[]{src[0] + 2, src[1] + 1});
                moves.add(Move.moveTo(source, dest));
            }
        }

        return moves;
    }
}
