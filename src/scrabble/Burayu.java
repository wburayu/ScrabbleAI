package scrabble;

import java.util.ArrayList;

public class Burayu implements ScrabbleAI {

    /** The GateKeeper through which this Incrementalist accesses the Board. */
    private GateKeeper gateKeeper;

    private static final boolean[] ALL_TILES = {true, true, true, true, true, true, true};

    @Override
    public void setGateKeeper(GateKeeper gateKeeper) {
        this.gateKeeper = gateKeeper;
    }

    /**
     * Returns a good optimal move given the state of the game (accessed through the GateKeeper).
     */
    @Override
    public ScrabbleMove chooseMove() {
        if (gateKeeper.getSquare(Location.CENTER) == Board.DOUBLE_WORD_SCORE) {
            return goingFirst();
        }
        return optimalMove();
    }

    /** The going first method uses a brute force strategy which finds all the possible words in our hand to generate the most optimal move when going first*/
    private ScrabbleMove goingFirst() {
        ArrayList<Character> hand = gateKeeper.getHand();
        String bestWord = null;
        int bestScore = -1;

        // These for loops search the hand for every potential six-letter word combination.
        for (int i = 0; i < hand.size(); i++) {
            for (int j = 0; j < hand.size(); j++) {
                for (int k = 0; k < hand.size(); k++) {
                    for (int l = 0; l < hand.size(); l++) {
                        for (int m = 0; m < hand.size(); m++) {
                            for (int n = 0; n < hand.size(); n++) {
                                if (i != j && k != j && k != l && i != k &&
                                        i != l && j != l && m != i && m != j &&
                                        m != k && n != i && n != j &&
                                        n != k) {
                                    try {
                                        //If there is a blank tile, replace with letter E
                                        char a = hand.get(i);
                                        if (a == '_') {
                                            a = 'E'; // This could be improved slightly by trying all possibilities for the blank
                                        }
                                        char b = hand.get(j);
                                        if (b == '_') {
                                            b = 'E'; // This could be improved slightly by trying all possibilities for the blank
                                        }
                                        char c = hand.get(k);
                                        if (c == '_') {
                                            c = 'E'; // This could be improved slightly by trying all possibilities for the blank
                                        }
                                        char d = hand.get(l);
                                        if (d == '_') {
                                            d = 'E'; // This could be improved slightly by trying all possibilities for the blank
                                        }
                                        char e = hand.get(m);
                                        if (e == ' ') {
                                            e = 'E'; // This could be improved slightly by trying all possibilities for the blank
                                        }
                                        String word = "" + a + b + c + d + e;
                                        gateKeeper.verifyLegality(word, Location.CENTER, Location.HORIZONTAL);
                                        int score = gateKeeper.score(word, Location.CENTER, Location.HORIZONTAL);
                                        if (score > bestScore) {
                                            bestScore = score;
                                            bestWord = word;
                                        }
                                    } catch (IllegalMoveException e) {
                                        // It wasn't legal; go on to the next one
                                    }

                                }

                            }

                        }
                    }
                }
                if (bestScore > -1) {
                    // This will return the best three-letter word that we can play.
                    return new PlayWord(bestWord, Location.CENTER, Location.HORIZONTAL);
                }
                for (int m = 0; m < hand.size(); m++) {
                    for (int n = 0; n < hand.size(); n++) {
                        for (int o = 0; o < hand.size(); o++) {
                            if (m != n && n != o && m != o) {
                                try {
                                    String word = "" + m + n + o;
                                    gateKeeper.verifyLegality(word, Location.CENTER, Location.HORIZONTAL);
                                    int score = gateKeeper.score(word, Location.CENTER, Location.HORIZONTAL);
                                    if (score > bestScore) {
                                        bestScore = score;
                                        bestWord = word;
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
        if (bestScore > -1) {
            return new PlayWord(bestWord, Location.CENTER, Location.HORIZONTAL);
        }
        return new ExchangeTiles(ALL_TILES);
    }

    /**
     * The optimalMove method finds the best possible move through playing up to 4 tiles on the board
     * Checks if we need to pass tiles as well
     */

    private ScrabbleMove optimalMove() {
        boolean found = false;
        ArrayList<Character> hand = gateKeeper.getHand();
        PlayWord[] bestMove = new PlayWord[5];
        int[] bestScore = new int[5];
        for (int i = 0; i < 5; i++) {
            bestScore[i] = -1;
            bestMove[i] = null;
        }

        // This creates four nested for loops to consider different ways of playing up to four tiles *
        for (int p = 0; p < hand.size(); p++) {
            for (int i = 0; i < hand.size(); i++) {
                for (int j = 0; j < hand.size(); j++) {
                    for (int k = 0; k < hand.size(); k++) {
                        if (p != i && p != j && p != k
                                && i != j && i != k
                                && j != k) {
                            // Replaces every blank tile with E
                            char a = hand.get(p);
                            if (a == '_') {
                                a = 'E'; // This could be improved slightly by trying all possibilities for the blank
                            }
                            char b = hand.get(i);
                            if (b == '_') {
                                b = 'E'; // This could be improved slightly by trying all possibilities for the blank
                            }
                            // This creates possible word strings
                            String[][] words = new String[2][];
                            words[0] = new String[]{" " + a, a + " ", "  " + a, "   " + a, a + "  ", a + "   "};
                            words[1] = new String[]{"" + a + b, a + b + " ", a + " " + b, " " + a + b, "  " + a + b, "   " + a + b, a + b + "  ", a + b + "   ", a + "  " + b, a + "   " + b};
                            int count = hand.size();
                            if (count > 2) {
                                count = 2;
                            }
                            // This tries every possible word string on every location possible
                            for (int ww = 0; ww < count; ww++) {
                                for (String word : words[ww]) {
                                    for (int row = 0; row < Board.WIDTH; row++) {
                                        for (int col = 0; col < Board.WIDTH; col++) {
                                            Location location = new Location(row, col);
                                            for (Location direction : new Location[]{Location.HORIZONTAL, Location.VERTICAL}) {
                                                try {
                                                    gateKeeper.verifyLegality(word, location, direction);
                                                    found = true;
                                                    int score = gateKeeper.score(word, location, direction);
                                                    if (score > bestScore[ww]) {
                                                        bestScore[ww] = score;
                                                        bestMove[ww] = new PlayWord(word, location, direction);
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
            }
        }
        int best = 0;
        int index = 0;
        for (int i = 0; i < 4; i++) {
            if (bestScore[i] > best) {
                best = bestScore[i];
                index = i;
            }
        }
        if (bestMove[index] != null && found) {
            return bestMove[index];
        }
        return new ExchangeTiles(ALL_TILES);
    }
}
