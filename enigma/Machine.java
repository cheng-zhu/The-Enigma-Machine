package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Cheng Zhu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = null;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotorMap.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int index = 0; index < numRotors(); index++) {
            for (Rotor r : _allRotors) {
                if (r.name().equals(rotors[index])) {
                    _rotorMap.put(index, r);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int index = 1; index < numRotors(); index++) {
            getRotor(index).set(setting.charAt(index - 1));
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        ArrayList<Integer> atNotchIndexArray = new ArrayList<>();
        boolean[] rotatedArr = new boolean[numRotors()];
        for (int index = numRotors() - numPawls();
             index < numRotors(); index++) {
            if (getRotor(index).atNotch()) {
                atNotchIndexArray.add(index);
            }
        }
        for (int index : atNotchIndexArray) {
            if (getRotor(index - 1).rotates()) {
                if (!rotatedArr[index]) {
                    getRotor(index).advance();
                    rotatedArr[index] = true;
                }
                if (!rotatedArr[index - 1]) {
                    getRotor(index - 1).advance();
                    rotatedArr[index - 1] = true;
                }
            }
        }
        if (!rotatedArr[numRotors() - 1]) {
            getRotor(numRotors() - 1).advance();
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        int result = c;
        for (int indexBack = numRotors() - 1; indexBack >= 0; indexBack--) {
            result = getRotor(indexBack).convertForward(result);
        }
        for (int index = 1; index < numRotors(); index++) {
            result = getRotor(index).convertBackward(result);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] arr = new char[msg.length()];
        for (int msgIndex = 0; msgIndex < msg.length(); msgIndex++) {
            arr[msgIndex] = alphabet().toChar(convert(alphabet()
                    .toInt(msg.charAt(msgIndex))));
        }
        return String.valueOf(arr);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors. */
    private final int _numRotors;

    /** Number of pawls. */
    private final int _pawls;

    /** Hashmap for rotors. */
    private final HashMap<Integer, Rotor> _rotorMap = new HashMap<>();

    /** Collection of all rotors. */
    private final Collection<Rotor> _allRotors;

    /** Plugboard. */
    private Permutation _plugboard;
}
