package src;

import java.util.ArrayList;

public class Pawn extends APiece {
    private boolean promoted = false;
    private APiece newPiece = null;

    public Pawn(PlaySide side) {
        super(side);
        setType(Piece.PAWN);
    }

    public int[][] importanceW = {
            {0, 5, 5,  0,  5, 10, 50, 0},
            {0, 5, 5,  0,  5, 10, 50, 0},
            {0, 0, 0,  0, 10, 20, 50, 0},
            {0, 0, 0, 10, 20, 30, 50, 0},
            {0, 0, 0, 10, 20, 30, 50, 0},
            {0, 0, 0,  0, 10, 20, 50, 0},
            {0, 5, 5,  0,  5, 10, 50, 0},
            {0, 5, 5,  0,  5, 10, 50, 0}
    };



    public int[][] importanceB = {
            {0, 50, 10,  5,  0, 5, 5, 0},
            {0, 50, 10,  5,  0, 5, 5, 0},
            {0, 50, 20, 10,  0, 0, 0, 0},
            {0, 50, 30, 20, 10, 0, 0, 0},
            {0, 50, 30, 20, 10, 0, 0, 0},
            {0, 50, 20, 10,  0, 0, 0, 0},
            {0, 50, 10,  5,  0, 5, 5, 0},
            {0, 50, 10,  5,  0, 5, 5, 0}
    };

    @Override
    public int[][] getImportanceW() {
        return importanceW;
    }

    public int[][] getImportanceB() {
        return importanceB;
    }

    public APiece getNewPiece() {
        return newPiece;
    }

    public void setNewPiece(APiece newPiece) {
        this.newPiece = newPiece;
    }

    public void promote(APiece newPiece, Piece piece) {
        this.newPiece = newPiece;
        setType(piece);
        setPromoted(true);
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    @Override
    public ArrayList<Move> calculatePossibleMoves(String source, APiece[][] table) {
        if(isPromoted())
            return newPiece.calculatePossibleMoves(source, table);

        ArrayList<Move> moves = new ArrayList<>();
        int[] src = translateMoveToIndex(source);
        int[] temp = translateMoveToIndex(source);

        int direction;
        int[] conditions;

        if(getSide().equals(PlaySide.WHITE)) {
            direction = 1;
            conditions = new int[] {1, 7};
        } else {
            conditions = new int[] {6, 0};
            direction = -1;
        }

        if (src[1] == conditions[0]) {
            temp[1] = src[1] + 2 * direction;
            if (table[temp[0]][temp[1] - direction] == null && table[temp[0]][temp[1]] == null) {
                moves.add(Move.moveTo(source, translateIndexToMove(temp)));
            }
        }

        temp[1] = src[1] + direction;
        if(temp[1] == conditions[1] && table[temp[0]][temp[1]] == null) {
            moves.add(Move.promote(source,translateIndexToMove(temp), Piece.QUEEN));
        } else {
            if (table[temp[0]][temp[1]] == null) {
                moves.add(Move.moveTo(source, translateIndexToMove(temp)));
            }
        }

        temp[0] = src[0] + 1;
        if (temp[0] < 8) {
            if (table[temp[0]][temp[1]] != null && table[temp[0]][temp[1]].getSide() != this.getSide()) {
                if (temp[1] == conditions[1]) {
                    moves.add(Move.promote(source,translateIndexToMove(temp), Piece.QUEEN));
                } else {
                    moves.add(Move.moveTo(source, translateIndexToMove(temp)));
                }
            }
        }

        temp[0] = src[0] - 1;
        if (temp[0] >= 0) {
            if (table[temp[0]][temp[1]] != null && table[temp[0]][temp[1]].getSide() != this.getSide()) {
                if (temp[1] == conditions[1]) {
                    moves.add(Move.promote(source,translateIndexToMove(temp), Piece.QUEEN));
                } else {
                    moves.add(Move.moveTo(source, translateIndexToMove(temp)));
                }
            }
        }

        return moves;
    }
}
