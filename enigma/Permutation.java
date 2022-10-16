package enigma;

import static enigma.EnigmaException.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Cheng Zhu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycle = new ArrayList<>();
        int[] alpArr = new int[alphabet.size()];
        Pattern myPattern = Pattern.compile("\\(([\\S\\s]+?)\\)");
        Pattern outsidePattern = Pattern.compile("\\)([\\S\\s]*?)\\(");
        Pattern startPattern = Pattern.compile("^([\\S\\s]*?)\\(");
        Pattern endPattern = Pattern.compile("\\)([^\\(]*?)$");
        Matcher myMatcher = myPattern.matcher(cycles);
        Matcher outsideMatcher = outsidePattern.matcher(cycles);
        Matcher startMatcher = startPattern.matcher(cycles);
        Matcher endMatcher = endPattern.matcher(cycles);
        while (startMatcher.find()) {
            String cycle = startMatcher.group(1);
            if (!cycle.isBlank()) {
                throw new EnigmaException("Wrong permutation format.");
            }
        }
        while (endMatcher.find()) {
            String cycle = endMatcher.group(1);
            if (!cycle.isBlank()) {
                throw new EnigmaException("Wrong permutation format.");
            }
        }
        while (outsideMatcher.find()) {
            String cycle = outsideMatcher.group(1);
            if (!cycle.isBlank()) {
                throw new EnigmaException("Wrong permutation format.");
            }
        }
        if (!myMatcher.find() && !cycles.isBlank()) {
            throw new EnigmaException("Wrong permutation format.");
        }
        myMatcher = myPattern.matcher(cycles);
        while (myMatcher.find()) {
            String cycle = myMatcher.group(1);
            char[] charsArr = cycle.toCharArray();
            for (int i = 0; i < cycle.length(); i++) {
                alpArr[alphabet().toInt(charsArr[i])]++;
                if (alpArr[alphabet().toInt(charsArr[i])] > 1) {
                    throw new EnigmaException("Wrong permutation: "
                            + "there are duplicate characters in the cycles.");
                }
            }
            addCycle(cycle);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycle.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char pChar = _alphabet.toChar(p);
        for (String c : _cycle) {
            int indexInC = c.indexOf(pChar);
            if (indexInC != -1) {
                if (indexInC == c.length() - 1) {
                    return _alphabet.toInt(c.charAt(0));
                } else {
                    return _alphabet.toInt(c.charAt(indexInC + 1));
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char cChar = _alphabet.toChar(c);
        for (String cy : _cycle) {
            int indexInC = cy.indexOf(cChar);
            if (indexInC != -1) {
                if (indexInC == 0) {
                    return _alphabet.toInt(cy.charAt(cy.length() - 1));
                } else {
                    return _alphabet.toInt(cy.charAt(indexInC - 1));
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (_alphabet.toChar(i) == permute(_alphabet.toChar(i))) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private final Alphabet _alphabet;

    /** Private variable cycle. */
    private final List<String> _cycle;
}
