package src;

import java.util.ArrayList;

/**
 * Reprezentation of the actual piece
 */
public abstract class APiece implements Cloneable {
    private PlaySide side;
    private Piece type;

    public abstract int[][] getImportanceW();
    public abstract int[][] getImportanceB();

    public APiece(PlaySide side) {
        this.side = side;
    }

    public PlaySide getSide() {
        return side;
    }

    public Piece getType() {
        return type;
    }

    public void setSide(PlaySide side) {
        this.side = side;
    }

    public void setType(Piece type) {
        this.type = type;
    }

    /**
     * @param source the tile position of the piece
     * @return every possible move that the piece can make from this position in an array
     */
    public abstract ArrayList<Move> calculatePossibleMoves(String source, APiece[][] table);


    public static int[] translateMoveToIndex(String source) {
        int[] moves = new int[2];

        switch (source.charAt(0)) {
            case 'a': moves[0] = 0; break;
            case 'b': moves[0] = 1; break;
            case 'c': moves[0] = 2; break;
            case 'd': moves[0] = 3; break;
            case 'e': moves[0] = 4; break;
            case 'f': moves[0] = 5; break;
            case 'g': moves[0] = 6; break;
            case 'h': moves[0] = 7; break;
        }

        moves[1] = Integer.parseInt(String.valueOf(source.charAt(1))) - 1;

        return moves;
    }
    public static String translateIndexToMove(int[] destination) {
        char[] moves = new char[2];

        switch (destination[0]) {
            case 0: moves[0] = 'a'; break;
            case 1: moves[0] = 'b'; break;
            case 2: moves[0] = 'c'; break;
            case 3: moves[0] = 'd'; break;
            case 4: moves[0] = 'e'; break;
            case 5: moves[0] = 'f'; break;
            case 6: moves[0] = 'g'; break;
            case 7: moves[0] = 'h'; break;
        }

        moves[1] = Integer.toString(destination[1] + 1).toCharArray()[0];

        return new String(moves);
    }

    public static String createDestination(int firstIndex, int secondIndex) {
        int[] destination = new int[2];
        destination[0] = firstIndex;
        destination[1] = secondIndex;
        return translateIndexToMove(destination);
    }

    @Override
    public String toString() {
        return getType().toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
