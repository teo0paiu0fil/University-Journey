package src;

import src.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Bot implements Cloneable {
    /* Edit this, escaped characters (e.g newlines, quotes) are prohibited */
    private static final String BOT_NAME = "IM SCARED";

    /* Declare custom fields below */
    private APiece[][] table = new APiece[8][8];
    private PlaySide mySide = PlaySide.NONE;
    private ArrayList<APiece> capturedWhite = new ArrayList<>();
    private ArrayList<APiece> capturedBlack = new ArrayList<>();
    private Move lastMoveWhite;
    private Move lastMoveBlack;
    private Move lastMove;
    private int moves50;
    private boolean isCapture;
    private APiece pieceCaptured;
    private int sameMoveWhite;
    private int sameMoveBlack;
    /* Declare custom fields above */

    public void setMySide(PlaySide mySide) {
        this.mySide = mySide;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public void setMoves50(int moves50) {
        this.moves50 = moves50;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    public void setPieceCaptured(APiece pieceCaptured) {
        this.pieceCaptured = pieceCaptured;
    }

    public void setSameMoveWhite(int sameMoveWhite) {
        this.sameMoveWhite = sameMoveWhite;
    }

    public void setSameMoveBlack(int sameMoveBlack) {
        this.sameMoveBlack = sameMoveBlack;
    }

    public PlaySide getMySide() {
        return mySide;
    }

    public Bot(PlaySide side) {
        mySide = side;
        /* Initialize custom fields here */
        createTable();
        lastMoveWhite = Move.resign();
        lastMoveBlack = Move.resign();
        lastMove = Move.resign();
    }

    public APiece[][] getTable() {
        return table;
    }

    public void setTable(APiece[][] table) {
        this.table = table;
    }

    public ArrayList<APiece> getCapturedWhite() {
        return capturedWhite;
    }

    public ArrayList<APiece> getCapturedBlack() {
        return capturedBlack;
    }

    public void setCapturedWhite(ArrayList<APiece> capturedWhite) {
        this.capturedWhite = capturedWhite;
    }

    public void setCapturedBlack(ArrayList<APiece> capturedBlack) {
        this.capturedBlack = capturedBlack;
    }

    public Move getLastMoveWhite() {
        return lastMoveWhite;
    }

    public void setLastMoveWhite(Move lastMoveWhite) {
        this.lastMoveWhite = lastMoveWhite;
    }

    public Move getLastMoveBlack() {
        return lastMoveBlack;
    }

    public void setLastMoveBlack(Move lastMoveBlack) {
        this.lastMoveBlack = lastMoveBlack;
    }

    /**
     * A function that creates the inital table
     */
    private void createTable() {
        // o sa pun white ca fiind noi momentan
        // tabla e invers; nu mai trebuie sa pun type ul
        // idk

        // ROOKS
        Rook[] rooks = new Rook[4];
        for(int i = 0; i < 4; i++) {
            rooks[i] = new Rook(PlaySide.WHITE);
        }

        table[0][0] = rooks[0];
        table[7][0] = rooks[1];

        rooks[2].setSide(PlaySide.BLACK);
        rooks[3].setSide(PlaySide.BLACK);

        table[0][7] = rooks[2];
        table[7][7] = rooks[3];

        // KNIGHTS
        Knight[] knights = new Knight[4];
        for(int i = 0; i < 4; i++) {
            knights[i] = new Knight(PlaySide.WHITE);
        }

        table[1][0] = knights[0];
        table[6][0] = knights[1];

        knights[2].setSide(PlaySide.BLACK);
        knights[3].setSide(PlaySide.BLACK);

        table[1][7] = knights[2];
        table[6][7] = knights[3];

        // BISHOPS
        Bishop[] bishops = new Bishop[4];
        for(int i = 0; i < 4; i++) {
            bishops[i] = new Bishop(PlaySide.WHITE);
        }

        table[2][0] = bishops[0];
        table[5][0] = bishops[1];

        bishops[2].setSide(PlaySide.BLACK);
        bishops[3].setSide(PlaySide.BLACK);

        table[2][7] = bishops[2];
        table[5][7] = bishops[3];


        // QUEENS
        // slay queen
        Queen queen1 = new Queen(PlaySide.WHITE);
        table[3][0] = queen1;

        Queen queen2 = new Queen(PlaySide.BLACK);
        table[3][7] = queen2;

        // KINGS

        King king1 = new King(PlaySide.WHITE);
        table[4][0] = king1;

        King king2 = new King(PlaySide.BLACK);
        table[4][7] = king2;

        // PAWNS
        for(int i = 0; i < 8; i++) {
            Pawn pawn = new Pawn(PlaySide.WHITE);
            table[i][1] = pawn;
        }

        for(int i = 0; i < 8; i++) {
            Pawn pawn = new Pawn(PlaySide.BLACK);
            table[i][6] = pawn;
        }
    }

    /**
     * de facut comment
     */
    public void undoLastMove() {
        if (lastMove.isNormal() || lastMove.isPromotion()) {
            int[] src = APiece.translateMoveToIndex(lastMove.getSource().get());
            int[] dst = APiece.translateMoveToIndex(lastMove.getDestination().get());

            // sterge miscarea
            APiece piece = table[dst[0]][dst[1]];
            table[dst[0]][dst[1]] = null;
            table[src[0]][src[1]] = piece;

            // ii spune pionului ca nu mai e regina
            if (lastMove.isPromotion()) {
                table[src[0]][src[1]].setType(Piece.PAWN);
                ((Pawn) table[src[0]][src[1]]).setPromoted(false);
                ((Pawn) table[src[0]][src[1]]).setNewPiece(null);
            }

            if (isCapture) {
                if (pieceCaptured.getSide() == PlaySide.BLACK) {
                    pieceCaptured.setSide(PlaySide.WHITE);
                    capturedWhite.remove(pieceCaptured);
                } else {
                    pieceCaptured.setSide(PlaySide.BLACK);
                    capturedBlack.remove(pieceCaptured);
                }
                table[dst[0]][dst[1]] = pieceCaptured;

                isCapture = false;
                pieceCaptured = null;
            }
        } else if (lastMove.isDropIn()) {
            // baga piesa inapoi in coada
            int[] dst = APiece.translateMoveToIndex(lastMove.getDestination().get());
            APiece piece = table[dst[0]][dst[1]];
            if (piece.getSide() == PlaySide.BLACK) capturedBlack.add(piece);
            else capturedWhite.add(piece);
            table[dst[0]][dst[1]] = null;
        }
    }
    /**
     * Record received move (either by enemy in normal play,
     * or by both sides in force mode) in custom structures
     * @param move received move
     * @param sideToMove side to move (either src.PlaySide.BLACK or src.PlaySide.WHITE)
     */
    public void recordMove(Move move, PlaySide sideToMove) {
        /* You might find it useful to also separately record last move in another custom field */
        if (move.isNormal() || move.isPromotion()) {
            int[] src = APiece.translateMoveToIndex(move.getSource().get());
            int[] dst = APiece.translateMoveToIndex(move.getDestination().get());

            APiece piece = table[src[0]][src[1]];

            // daca nu muta pion se apropie de remiza
            if (piece instanceof Pawn) moves50 = 0;
            else moves50++;

            // daca repeta aceeasi miscare
            if (sideToMove == PlaySide.BLACK && lastMoveBlack.getSource().isPresent() && lastMoveBlack.getDestination().isPresent()) {
                int[] lastSrc = APiece.translateMoveToIndex(lastMoveBlack.getSource().get());
                int[] lastDst = APiece.translateMoveToIndex(lastMoveBlack.getDestination().get());
                if (src[0] == lastDst[0] && src[1] == lastDst[1] && dst[0] == lastSrc[0] && dst[1] == lastSrc[1]) sameMoveBlack++;
            } else if (lastMoveWhite.getSource().isPresent() && lastMoveWhite.getDestination().isPresent()) {
                int[] lastSrc = APiece.translateMoveToIndex(lastMoveWhite.getSource().get());
                int[] lastDst = APiece.translateMoveToIndex(lastMoveWhite.getDestination().get());
                if (src[0] == lastDst[0] && src[1] == lastDst[1] && dst[0] == lastSrc[0] && dst[1] == lastSrc[1]) sameMoveWhite++;
            }

            if (piece instanceof King) {
                ((King)piece).moved = true;
                if (sideToMove.equals(PlaySide.WHITE) && move.getSource().get().equals("e1")){
                    if(move.getDestination().get().equals("c1")) {
                        table[3][0] = table[0][0];
                        table[0][0] = null;
                    } else if(move.getDestination().get().equals("g1")){
                        table[5][0] = table[7][0];
                        table[7][0] = null;
                    }
                } else if(sideToMove.equals(PlaySide.BLACK) && move.getSource().get().equals("e8")) {
                    if(move.getDestination().get().equals("c8")) {
                        table[3][7] = table[0][7];
                        table[0][7] = null;
                    } else if(move.getDestination().get().equals("g8")){
                        table[5][7] = table[7][7];
                        table[7][7] = null;
                    }
                }
            }

            if (piece instanceof Rook) {
                ((Rook)piece).moved = true;
            }

            table[src[0]][src[1]] = null;

            APiece piece_enemy = table[dst[0]][dst[1]];
            table[dst[0]][dst[1]] = piece;

            if (piece_enemy == null && piece != null && piece.getType().equals(Piece.PAWN)) {
                if(src[0] != dst[0]) {
                     piece_enemy = table[dst[0]][src[1]];
                     table[dst[0]][src[1]] = null;
                }
            }


            if (piece_enemy != null) {
                // daca a capturat se reseteaza numaratoarea pana la remiza
                moves50 = 0;
                if (piece_enemy instanceof Pawn) {
                    piece_enemy.setType(Piece.PAWN);
                    ((Pawn) piece_enemy).setPromoted(false);
                }
                if (sideToMove.equals(PlaySide.BLACK)) {
                    piece_enemy.setSide(PlaySide.BLACK);
                    capturedBlack.add(piece_enemy);
                } else {
                    piece_enemy.setSide(PlaySide.WHITE);
                    capturedWhite.add(piece_enemy);
                }
            }

            if (sideToMove.equals(PlaySide.BLACK))
                lastMoveBlack = move;
            else
                lastMoveWhite = move;

            if (move.isPromotion()) {
                piece.setType(move.getReplacement().get());
                ((Pawn)piece).promote(new Queen(sideToMove), Piece.QUEEN);
            }
        } else {
            int[] dst = APiece.translateMoveToIndex(move.getDestination().get());
            Piece piece = move.getReplacement().get();
            APiece piece1 = null;

            if(sideToMove.equals(PlaySide.WHITE)) {
                for (APiece p: capturedWhite) {
                    if (p.getType() == piece)
                        piece1 = p;
                }
                capturedWhite.remove(piece1);
            } else {
                for (APiece p: capturedBlack) {
                    if (p.getType() == piece)
                        piece1 = p;
                }
                capturedBlack.remove(piece1);
            }

            APiece piece_enemy = table[dst[0]][dst[1]];
            if (piece_enemy == null)
                table[dst[0]][dst[1]] = piece1;
        }

        lastMove = move;
    }

    public boolean isOverNow() {
        // moves50 ca sa vezi daca a facut 50 fara miscari din alea => remiza

        // sameMoveWhite si sameMoveBlack daca vreunu a facut 3 miscari la fel => remiza
        // pentru pat vezi daca allMoves e null adica n-ai ce face chiar daca nu esti in sah

        // ai sah mat daca sahSolver e null
        if (moves50 == 50) return true;
        if (sameMoveBlack == 3) return true;
        if (sameMoveWhite == 3) return true;
//        ArrayList<Move> allMoves = calculateAllMoves();
//        if (allMoves.isEmpty()) return true;
//        if (sahSolver(allMoves, mySide) ==  null) return true;

        return false;
    }

    /**
     * Generating the moves of type drop in that can be make
     * @param allMoves here we will add them
     * @param side with side to generate
     * @param captured the piece we can drop
     */
    public void addPieceFromCaptured(ArrayList<Move> allMoves, PlaySide side, ArrayList<APiece> captured) {
        if (captured.size() != 0 && mySide == side) {
            for (APiece p : captured) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (table[i][j] == null) {
                            if (p.getType() != Piece.PAWN || p.getType() == Piece.PAWN && j != 0 && j != 7) {
                                Move m = Move.dropIn(APiece.createDestination(i, j), p.getType());
                                allMoves.add(m);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * A function that search for the position of the king
     * @return the position in a string coordonate
     */
    public String findTheKing() {
        String king = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (table[i][j] != null && table[i][j].getType() == Piece.KING && mySide == table[i][j].getSide()) {
                    king = APiece.translateIndexToMove(new int[] {i, j});
                    break;
                }
            }
        }
        return king;
    }

    /**
     * Check if the king is in check
     * @param king position of the king
     * @param tablee the table
     * @return yes or no
     */
    public boolean sah(String king, APiece[][] tablee) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablee[i][j] != null && tablee[i][j].getSide() != mySide) {
                    for (Move m : tablee[i][j].calculatePossibleMoves(APiece.translateIndexToMove(new int[] {i, j}), tablee)) {
                        if (m.getDestination().get().equals(king)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if a move can get the king out of sah or in sah
     * @param move the move
     * @param king the position of the king
     * @return yes or no
     */
    public boolean canMakeSah(Move move, String king) {
        APiece[][] copy = table.clone();
        for (int i = 0; i < 8; i++) {
            copy[i] = table[i].clone();
        }

        int[] dst = APiece.translateMoveToIndex(move.getDestination().get());
        if (move.isDropIn()) {
            Piece piece = move.getReplacement().get();
            APiece piece1 = null;

            if(mySide.equals(PlaySide.WHITE)) {
                for (APiece p: capturedWhite) {
                    if (p.getType() == piece)
                        piece1 = p;
                }
            } else {
                for (APiece p: capturedBlack) {
                    if (p.getType() == piece)
                        piece1 = p;
                }
            }
            copy[dst[0]][dst[1]] = piece1;

            return !sah(king, copy);
        }
        int[] src = APiece.translateMoveToIndex(move.getSource().get());

        APiece piece = copy[src[0]][src[1]];
        copy[src[0]][src[1]] = null;

        copy[dst[0]][dst[1]] = piece;

        if(piece.getType() == Piece.KING)
            king = move.getDestination().get();

        return !sah(king, copy);
    }

    /**
     * Filters the moves in a way that every move remainding can get out the king of check
     * @param allMoves the moves we can make
     * @param side the side
     * @return the remainding moves
     */
    public ArrayList<Move> sahSolver(ArrayList<Move> allMoves, PlaySide side) {
        ArrayList<Move> newMoves = new ArrayList<>();
        String king = findTheKing();
        String actualKing = king;
        for (Move move : allMoves) {
            APiece[][] copy = table.clone();
            for (int i = 0; i < 8; i++) {
                copy[i] = table[i].clone();
            }

            int[] dst = APiece.translateMoveToIndex(move.getDestination().get());
            if (move.isDropIn()) {
                Piece piece = move.getReplacement().get();
                APiece piece1 = null;

                if(side.equals(PlaySide.WHITE)) {
                    for (APiece p: capturedWhite) {
                        if (p.getType() == piece)
                            piece1 = p;
                    }
                } else {
                    for (APiece p: capturedBlack) {
                        if (p.getType() == piece)
                            piece1 = p;
                    }
                }
                copy[dst[0]][dst[1]] = piece1;

                if (!sah(king, copy)) {
                    newMoves.add(move);
                }
                continue;
            }
            int[] src = APiece.translateMoveToIndex(move.getSource().get());

            APiece piece = copy[src[0]][src[1]];
            copy[src[0]][src[1]] = null;

            copy[dst[0]][dst[1]] = piece;

            if(piece.getType() == Piece.KING)
                king = move.getDestination().get();

            if (!sah(king, copy)) {
                newMoves.add(move);
            }

            king = actualKing;
        }

        return newMoves;
    }

    /**
     * @param actual_king the position of the king
     * @return the ways with the king can castel
     */
    public ArrayList<Move> castling(String actual_king) {
        int line;
        String king_k;
        String king_q;
        String rook_k;
        String rook_q;

        if(mySide == PlaySide.WHITE) {
            line = 0;
            king_k = "g1";
            king_q = "c1";
            rook_k = "f1";
            rook_q = "d1";
        } else {
            line = 7;
            king_k = "g8";
            king_q = "c8";
            rook_k = "f8";
            rook_q = "d8";
        }

        ArrayList<Move> cast = new ArrayList<>();

        if(table[4][line] != null && table[4][line].getType() == Piece.KING && !((King)table[4][line]).moved && !sah(actual_king, table)) {

            // regele nu s-a mutat e numai bun de rocada
            // verificam turele daca sunt unde trebuie

            // big rocada verificat
            if(table[0][line] != null && table[0][line].getType() == Piece.ROOK && !((Rook)table[0][line]).moved && !sah(rook_q, table)) {
                // piesa de acolo e rook si nu s-a miscat
                // e un bun potential pt rocada
                if (table[3][line] == null) {

                    for (Move m : table[0][line].calculatePossibleMoves(APiece.translateIndexToMove(new int[]{0, line}), table)) {
                        if (m.getDestination().get().equals(rook_q)) {
                            // poate sa ajunga acolo; inseamna ca e liber si pe c1 pt rege
                            if (!sah(king_q, table)) {
                                // se face rocada
                                Move move1 = Move.moveTo(actual_king, king_q);
//                            Move move2 = Move.moveTo(((Rook) table[0][line]).createDestination(0, line), rook_q);

                                cast.add(move1);
//                            cast.add(move2);
                            }

                        }
                    }
                }
            }

            // tiny rocada verificat
            if(table[7][line] != null && table[7][line].getType() == Piece.ROOK && !((Rook)table[7][line]).moved && !sah(actual_king, table) && !sah(rook_k, table)) {
                // piesa de acolo e rook si nu s-a miscat
                // e un bun potential pt rocada
                if (table[5][line] == null) {
                    for (Move m : table[7][line].calculatePossibleMoves(APiece.translateIndexToMove(new int[]{7, line}), table)) {
                        if (m.getDestination().get().equals(rook_k)) {
                            // poate sa ajunga acolo; inseamna ca e liber si pe c1 pt rege
                            if (!sah(king_k, table)) {
                                // se face rocada
                                Move move1 = Move.moveTo(actual_king, king_k);
//                            Move move2 = Move.moveTo(((Rook) table[7][line]).createDestination(7, line), rook_k);

                                cast.add(move1);
//                            cast.add(move2);
                            }

                        }
                    }
                }
            }

        }

        return cast;
    }

    public Move calculateNextMove() {

//        ArrayList<APiece> copyCapturedWhite = new ArrayList<>(capturedWhite);
//        ArrayList<APiece> copyCapturedBlack = new ArrayList<>(capturedBlack);
//        Move copyLastMoveWhite = lastMoveWhite;
//        Move copyLastMoveBlack = lastMoveBlack;
//        Move copyLastMove = lastMove;
//        int copyMoves50 = moves50;
//        int copySameMoveWhite = sameMoveWhite;
//        int copySameMoveBlack = sameMoveBlack;

        Bot botulet = null;
        try {
            botulet = (Bot)this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        BestMove m = new BestMove();
        Move bestMove = m.getBestMove(botulet);

//        table = copy;
//        capturedWhite = copyCapturedWhite;
//        capturedBlack = copyCapturedBlack;
//        lastMoveWhite = copyLastMoveWhite;
//        lastMoveBlack = copyLastMoveBlack;
//        lastMove = copyLastMove;
//        moves50 = copyMoves50;
//        sameMoveWhite = copySameMoveWhite;
//        sameMoveBlack = copySameMoveBlack;
        recordMove(bestMove, mySide);

        return bestMove;
    }

    /**
     * Calculate and return the bot's next move
     * @return your move
     */
    public ArrayList<Move> calculateAllMoves() {
        String my_king = findTheKing();

        ArrayList<Move> moves_castling = castling(my_king);
        ArrayList<Move> allMoves = new ArrayList<>(moves_castling);

        for (int i = 0; i < 8; i++) {
            if (mySide.equals(PlaySide.BLACK) && table[i][3] != null && table[i][3].getType().equals(Piece.PAWN) && table[i][3].getSide().equals(PlaySide.BLACK) ) {
                allMoves.addAll(enpassant());
            }
            if (mySide.equals(PlaySide.WHITE) && table[i][4] != null && table[i][4].getType().equals(Piece.PAWN) && table[i][4].getSide().equals(PlaySide.WHITE)) {
                allMoves.addAll(enpassant());
            }
            for (int j = 0; j < 8; j++) {
                if (table[i][j] != null && table[i][j].getSide() == mySide) {
                  allMoves.addAll(table[i][j].calculatePossibleMoves(APiece.translateIndexToMove(new int[] {i, j}), table));
                }
            }
        }

        if (mySide.equals(PlaySide.BLACK)) {
            addPieceFromCaptured(allMoves, PlaySide.BLACK, capturedBlack);
        } else {
            addPieceFromCaptured(allMoves, PlaySide.WHITE, capturedWhite);
        }
        if (sah(my_king, table)) {
            allMoves = sahSolver(allMoves, mySide);
        } else allMoves = (ArrayList<Move>) allMoves.stream().filter(m -> canMakeSah(m, findTheKing())).collect(Collectors.toList());

        return allMoves;
    }

    /**
     * en passant
     * @return the en passant moves that can be make in this moment
     */
    public ArrayList<Move> enpassant() {
        ArrayList<Move> m = new ArrayList<>();
        if (mySide.equals(PlaySide.WHITE)) {
            for (int i = 0; i < 8; i++) {
                if (lastMoveBlack.getSource().get().equals(APiece.createDestination(i,6)) && lastMoveWhite.getDestination().get().equals(APiece.createDestination(i,4))
                        && table[i][4].getType().equals(Piece.PAWN) && !table[i][4].getSide().equals(mySide)) {
                    if (i == 0) {
                        if (table[i + 1][4] != null && table[i + 1][4].getType().equals(Piece.PAWN) && table[i + 1][4].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i + 1, 4}), APiece.translateIndexToMove(new int[] {i, 5})));
                        }
                    } else if (i == 7) {
                        if (table[i - 1][4] != null && table[i - 1][4].getType().equals(Piece.PAWN) && table[i - 1][4].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i - 1, 4}), APiece.translateIndexToMove(new int[] {i ,5})));
                        }
                    } else {
                        if (table[i + 1][4] != null  && table[i + 1][4].getType().equals(Piece.PAWN) && table[i + 1][4].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i + 1, 4}), APiece.translateIndexToMove(new int[] {i ,5})));
                        }
                        if (table[i - 1][4] != null && table[i - 1][4].getType().equals(Piece.PAWN) && table[i - 1][4].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i - 1, 4}), APiece.translateIndexToMove(new int[] {i, 5})));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (lastMoveWhite.getSource().get().equals(APiece.createDestination(i,1)) && lastMoveWhite.getDestination().get().equals(APiece.createDestination(i,3))
                        && table[i][3].getType().equals(Piece.PAWN) && !table[i][3].getSide().equals(mySide)) {
                    if (i == 0) {
                        if (table[i + 1][3] != null && table[i + 1][3].getType().equals(Piece.PAWN) && table[i + 1][3].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i + 1, 3}), APiece.translateIndexToMove(new int[] {i, 2})));
                        }
                    } else if (i == 7) {
                        if (table[i - 1][3] != null && table[i - 1][3].getType().equals(Piece.PAWN) && table[i - 1][3].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i - 1, 3}), APiece.translateIndexToMove(new int[] {i, 2})));
                        }
                    } else {
                        if (table[i + 1][3] != null && table[i + 1][3].getType().equals(Piece.PAWN) && table[i + 1][3].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i + 1, 3}), APiece.translateIndexToMove(new int[] {i , 2})));
                        }
                        if (table[i - 1][3] != null && table[i - 1][3].getType().equals(Piece.PAWN) && table[i - 1][3].getSide().equals(mySide)) {
                            m.add(Move.moveTo(APiece.translateIndexToMove(new int[] {i - 1, 3}), APiece.translateIndexToMove(new int[] {i, 2})));
                        }
                    }
                }
            }
        }
        return m;
    }

    public static String getBotName() {
        return BOT_NAME;
    }

    public void setSide(PlaySide engineSide) {
        mySide = engineSide;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Bot botulet = (Bot)super.clone();
        botulet.setSide(mySide);
        botulet.setCapturedWhite(new ArrayList<>(capturedWhite));
        botulet.setCapturedBlack(new ArrayList<>(capturedBlack));
        botulet.setLastMoveWhite(lastMoveWhite);
        botulet.setLastMoveBlack(lastMoveBlack);
        botulet.setPieceCaptured(pieceCaptured);
        botulet.setLastMove(lastMove);

        APiece[][] tablee = new APiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (table[i][j] != null)
                    tablee[i][j] = (APiece) table[i][j].clone();
            }
        }

        botulet.setTable(tablee);

        return botulet;
    }
}