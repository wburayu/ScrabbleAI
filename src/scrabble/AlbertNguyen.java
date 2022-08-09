package scrabble;

import java.util.ArrayList;

/**
 * Dumb AI that picks the highest-scoring one-tile move. Plays a two-tile move on the first turn. Exchanges all of its
 * letters if it can't find any other move.
 */
public class AlbertNguyen implements ScrabbleAI {

    /** When exchanging, always exchange everything. */
    private static final boolean[] ALL_TILES = {true, true, true, true, true, true, true};
    String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "y", "Z"};
    int letters_size = letters.length;
    /** The GateKeeper through which this Incrementalist accesses the Board. */
    private GateKeeper gateKeeper;

    @Override
    public void setGateKeeper(GateKeeper gateKeeper) {
        this.gateKeeper = gateKeeper;
    }

    @Override
    public ScrabbleMove chooseMove() {
        if (gateKeeper.getSquare(Location.CENTER) == Board.DOUBLE_WORD_SCORE) {
            return findNotOneTileMove();
        }
        return findMove();
    }

    /** This is necessary for the first turn, as one-letter words are not allowed. */
    private ScrabbleMove findNotOneTileMove() {
        ArrayList<Character> hand = gateKeeper.getHand();
        PlayWord bestMove = null;
        int bestScore = -1;

        for (int i = 0; i < hand.size(); i++) {
            for (int ii = 0; ii < hand.size(); ii++) {
                for (int iii = 0; iii < hand.size(); iii++) {
                    char c = hand.get(i);
                    char d = hand.get(ii);
                    char f = hand.get(iii);
                    if (c != d && d != f) {
                        if (c == '_') {
                            c = 'E'; // This could be improved slightly by trying all possibilities for the blank
                        }
                        if (d == '_') {
                            d = 'E'; // This could be improved slightly by trying all possibilities for the blank
                        }
                        if (f == '_') {
                            f = 'E'; // This could be improved slightly by trying all possibilities for the blank
                        }


                        for (String word : new String[]{
                                c + " " + " ",
                                d + " " + " ",
                                f + " " + " ",
                                c + " " + " " + " ",
                                d + " " + " " + " ",
                                f + " " + " " + " ",
                                " " + " " + d,
                                " " + " " + c,
                                " " + " " + f,
                                " " + " " + " " + d,
                                " " + " " + " " + c,
                                " " + " " + " " + f,
                                c + " " + d,
                                d + " " + c,
                                " " + c + d,
                                " " + d + c,
                                c + d + " ",
                                d + c + " ",
                                " " + c + d + f,
                                " " + d + c + f,
                                " " + f + c + d,
                                " " + f + d + c,
                                c + " " + d + f,
                                d + " " + c + f,
                                f + " " + c + d,
                                f + " " + d + c,
                                c + " " + " " + d + f,
                                d + " " + " " + c + f,
                                f + " " + " " + c + d,
                                f + " " + " " + d + c,
                                c + d + " " + f,
                                d + c + " " + f,
                                f + c + " " + d,
                                f + d + " " + c,
                                c + d + " " + " " + f,
                                d + c + " " + " " + f,
                                f + c + " " + " " + d,
                                f + d + " " + " " + c,
                                c + d + f + " ",
                                d + c + f + " ",
                                f + c + d + " ",
                                f + d + c + " ",
                                c + d + f + " " + " ",
                                d + c + f + " " + " ",
                                f + c + d + " " + " ",
                                f + d + c + " " + " ",


                        }) {
                            for (int row = 0; row < Board.WIDTH; row++) {
                                for (int col = 0; col < Board.WIDTH; col++) {
                                    Location location = new Location(row, col);
                                    for (Location direction : new Location[]{Location.HORIZONTAL, Location.VERTICAL}) {
                                        try {
                                            gateKeeper.verifyLegality(word, location, direction);
                                            int score = gateKeeper.score(word, location, direction);
                                            if (score > bestScore) {
                                                bestScore = score;
                                                bestMove = new PlayWord(word, location, direction);
                                            }
                                        } catch (IllegalMoveException e) {
                                            // It wasn't legal; go on to the next one
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (bestMove != null) {
            return bestMove;
        }
        return new ExchangeTiles(ALL_TILES);
    }

    /**
     * Technically this tries to make a two-letter word by playing one tile; it won't find words that simply add a
     * tile to the end of an existing word.
     */


    private ScrabbleMove findMove() {
        ArrayList<Character> hand = gateKeeper.getHand();
        PlayWord bestMove = null;
        int bestScore = -1;

            for (int i = 0; i < hand.size(); i++) {
                for (int ii = 0; ii < hand.size(); ii++) {
                    for (int iii = 0; iii < hand.size(); iii++) {
                        char c = hand.get(i);
                        char d = hand.get(ii);
                        char f = hand.get(iii);
                        if (c != d && d != f) {
                            if (c == '_') {
                                c = 'E'; // This could be improved slightly by trying all possibilities for the blank
                            }
                            if (d == '_') {
                                d = 'E'; // This could be improved slightly by trying all possibilities for the blank
                            }
                            if (f == '_') {
                                f = 'E'; // This could be improved slightly by trying all possibilities for the blank
                            }


                            for (String word : new String[]{


                                    c + " " + " ",
                                    d + " " + " ",
                                    f + " " + " ",
                                    c + " " + " " + " ",
                                    d + " " + " " + " ",
                                    f + " " + " " + " ",
                                    " " + c,
                                    " " + d,
                                    " " + f,
                                    " " + " " + d,
                                    " " + " " + c,
                                    " " + " " + f,
                                    " " + " " + " " + d,
                                    " " + " " + " " + c,
                                    " " + " " + " " + f,
                                    c + " " + d,
                                    d + " " + c,
                                    " " + c + d,
                                    " " + d + c,
                                    c + d + " ",
                                    d + c + " ",
                                    " " + c + d + f,
                                    " " + d + c + f,
                                    " " + f + c + d,
                                    " " + f + d + c,
                                    c + " " + d + f,
                                    d + " " + c + f,
                                    f + " " + c + d,
                                    f + " " + d + c,
                                    c + " " + " " + d + f,
                                    d + " " + " " + c + f,
                                    f + " " + " " + c + d,
                                    f + " " + " " + d + c,
                                    c + d + " " + f,
                                    d + c + " " + f,
                                    f + c + " " + d,
                                    f + d + " " + c,
                                    c + d + " " + " " + f,
                                    d + c + " " + " " + f,
                                    f + c + " " + " " + d,
                                    f + d + " " + " " + c,
                                    c + d + f + " ",
                                    d + c + f + " ",
                                    f + c + d + " ",
                                    f + d + c + " ",
                                    c + d + f + " " + " ",
                                    d + c + f + " " + " ",
                                    f + c + d + " " + " ",
                                    f + d + c + " " + " ",


                            }) {
                                for (int row = 0; row < Board.WIDTH; row++) {
                                    for (int col = 0; col < Board.WIDTH; col++) {
                                        Location location = new Location(row, col);
                                        for (Location direction : new Location[]{Location.HORIZONTAL, Location.VERTICAL}) {
                                            try {
                                                gateKeeper.verifyLegality(word, location, direction);
                                                int score = gateKeeper.score(word, location, direction);
                                                if (score > bestScore) {
                                                    bestScore = score;
                                                    bestMove = new PlayWord(word, location, direction);
                                                }
                                            } catch (IllegalMoveException e) {
                                                // It wasn't legal; go on to the next one
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        if (bestMove != null) {
            return bestMove;
        }
        return new ExchangeTiles(ALL_TILES);
    }

}
