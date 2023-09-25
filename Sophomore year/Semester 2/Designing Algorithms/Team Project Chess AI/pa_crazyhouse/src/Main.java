package src;


import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    private static PlaySide sideToMove;
    private static PlaySide engineSide;

    public static PlaySide getEngineSide() {
        return engineSide;
    }

    private static void toggleSideToMove() {

        switch (sideToMove) {
            case BLACK:
                sideToMove = PlaySide.WHITE;
                break;
            case WHITE:
                sideToMove = PlaySide.BLACK;
                break;
            default:
                sideToMove = PlaySide.NONE;
        }
    }

    private static String constructFeaturesPayload() {
        return    "feature"
                + " done=0"
                + " sigint=0"
                + " sigterm=0"
                + " san=0"
                + " reuse=0"
                + " usermove=1"
                + " analyze=0"
                + " ping=0"
                + " setboard=0"
                + " level=0"
                + " variants=\"crazyhouse\""
                + " name=\"" + Bot.getBotName()
                + "\" myname=\"" + Bot.getBotName()
                + "\" done=1";
    }

    private static String serializeMove(Move move) {
        if (move.isNormal())
            return move.getSource().orElse("") + move.getDestination().orElse("");
        else if (move.isPromotion() && move.getReplacement().isPresent()) {
            String pieceCode = null;
            switch (move.getReplacement().get()) {
                case BISHOP :
                    pieceCode = "b";
                    break;
                case KNIGHT :
                    pieceCode = "n";
                    break;
                case ROOK :
                    pieceCode = "r";
                    break;
                case QUEEN :
                    pieceCode = "q";
                    break;
                default :
                    pieceCode = "";
                    break;
            };
            return move.getSource().get() + move.getDestination().get() + pieceCode;
        } else if (move.isDropIn() && move.getReplacement().isPresent()) {
            String pieceCode = null;
            switch (move.getReplacement().get()) {
                case BISHOP :
                    pieceCode = "B";
                    break;
                case KNIGHT :
                    pieceCode = "N";
                    break;
                case ROOK :
                    pieceCode = "R";
                    break;
                case QUEEN :
                    pieceCode = "Q";
                    break;
                case PAWN :
                    pieceCode = "P";
                    break;
                default :
                    pieceCode = "";
            };
            return pieceCode + "@" + move.getDestination().get();
        } else {
            return "resign";
        }
    }

    private static Move deserializeMove(String s) {
        if (s.charAt(1) == '@') {
            /* Drop-in */

            Piece piece = null;

            switch (s.charAt(0)) {
                case 'P' :
                    piece = Piece.PAWN;
                    break;
                case 'R' :
                    piece = Piece.ROOK;
                    break;
                case 'B' :
                    piece = Piece.BISHOP;
                    break;
                case 'N' :
                    piece = Piece.KNIGHT;
                    break;
                case 'Q' :
                    piece = Piece.QUEEN;
                    break;
                case 'K' :
                    piece = Piece.KING; /* This is an illegal move */
                    break;
                default :
                    piece = null;
            };

            return Move.dropIn(s.substring(2, 4), piece);
        } else if (s.length() == 5) {
            /* Pawn promotion */
            Piece piece = null;
            switch (s.charAt(4)) {
                case 'p' :
                    piece = Piece.PAWN; /* This is an illegal move */
                    break;
                case 'r' :
                    piece = Piece.ROOK;
                    break;
                case 'b' :
                    piece = Piece.BISHOP;
                    break;
                case 'n' :
                    piece = Piece.KNIGHT;
                    break;
                case 'q' :
                    piece = Piece.QUEEN;
                    break;
                case 'k' :
                    piece = Piece.KING; /* This is an illegal move */
                    break;
                default :
                    piece = null;
            };

            return Move.promote(s.substring(0, 2), s.substring(2, 4), piece);
        }

        /* Normal move/capture/castle/en passant */
        return Move.moveTo(s.substring(0, 2), s.substring(2, 4));
    }

    private static class EngineComponents {
        private enum EngineState {
            HANDSHAKE_DONE, RECV_NEW, PLAYING, FORCE_MODE
        }

        public Bot bot;
        private EngineState state;
        private final Scanner scanner;
        private String bufferedCmd;
        private boolean isStarted;

        public EngineComponents() {
            this.bot = null;
            this.state = null;
            this.scanner = new Scanner(System.in);
            this.bufferedCmd = null;
            this.isStarted = false;
        }

        private void newGame() {
            bot = new Bot(PlaySide.NONE);
            state = EngineState.RECV_NEW;
            engineSide = PlaySide.NONE;
            sideToMove = PlaySide.WHITE;
            this.isStarted = false;
        }

        private void enterForceMode() {
            state = EngineState.FORCE_MODE;
        }

        private void leaveForceMode() {
            /* Called upon receiving "go" */
            state = EngineState.PLAYING;

            if (!isStarted) {
                isStarted = true;
                engineSide = sideToMove;
                bot.setSide(engineSide);
            }

            /* Make next move (go is issued when it's the bot's turn) */
            Move nextMove = bot.calculateNextMove();
            emitMove(nextMove);

            toggleSideToMove();
        }

        private void processIncomingMove(Move move) {
            switch (state) {
                case RECV_NEW : {
                    state = EngineState.PLAYING;

                    bot.recordMove(move, sideToMove);
                    toggleSideToMove();
                    engineSide = sideToMove;
                    bot.setSide(engineSide);

                    Move response = bot.calculateNextMove();


                    emitMove(response);
                    toggleSideToMove();

                    break;
                }

                case FORCE_MODE : {
                    /* Record move for side to move in internal structures */
                    bot.recordMove(move, sideToMove);
                    toggleSideToMove();
                    break;
                }
                case PLAYING : {
                    bot.recordMove(move, sideToMove);
                    toggleSideToMove();
                    bot.setSide(engineSide);
                    Move response = bot.calculateNextMove();
                    emitMove(response);
                    toggleSideToMove();
                    break;
                }
                default : {
                    System.err.println("[WARNING]: Unexpected move received (prior to new command)");
                    break;
                }
            }
        }

        private void emitMove(Move move) {
            if (move.isDropIn() || move.isNormal() || move.isPromotion())
                System.out.print("move ");
            System.out.println(serializeMove(move));
        }

        public void performHandshake() {
            /* Await start command ("xboard") */
            String command = scanner.nextLine();
            assert command.equals("xboard");
            System.out.print("\n");

            command = scanner.nextLine();
            assert command.startsWith("protover");

            /* Respond with features */
            String features = constructFeaturesPayload();
            System.out.println(features);

            /* Receive and discard boilerplate commands */
            while (true) {
                command = scanner.nextLine();
                if (command.equals("new") || command.equals("force")
                        || command.equals("go") || command.equals("quit")) {
                    bufferedCmd = command;
                    break;
                }
            }

            state = EngineState.HANDSHAKE_DONE;
        }

        public void executeOneCommand() {
            String nextCmd;

            if (bufferedCmd != null) {
                nextCmd = bufferedCmd;
                bufferedCmd = null;
            } else {
                nextCmd = scanner.nextLine();
            }

            StringTokenizer tokenizer = new StringTokenizer(nextCmd);
            String command = tokenizer.nextToken();

            switch (command) {
                case "quit" : System.exit(0);
                    break;
                case "new" : newGame();
                    break;
                case "force" : enterForceMode();
                    break;
                case "go" : leaveForceMode();
                    break;
                case "usermove" : {
                    Move incomingMove = deserializeMove(tokenizer.nextToken());
                    processIncomingMove(incomingMove);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        EngineComponents engine = new EngineComponents();
        engine.performHandshake();

        while (true) {
            /* Fetch and execute next command */

            engine.executeOneCommand();
        }
    }
}
