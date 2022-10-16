package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Cheng Zhu
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _position = 0;
        _positionChar = alphabet().toChar(0);
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _position;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _position = posn;
        _positionChar = alphabet().toChar(posn);
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _position = alphabet().toInt(cposn);
        _positionChar = cposn;
    }


    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int result;
        result = permutation().wrap(permutation()
                .permute(permutation().wrap(p + _position)) - _position);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        return result;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int result;
        result = permutation().wrap(permutation().invert(permutation()
                .wrap(e + _position)) - _position);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        return result;
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return notches().indexOf(_positionChar) != -1;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private final Permutation _permutation;

    /** The int position of the rotor. */
    private int _position;

    /** The char position of the rotor. */
    private char _positionChar;
}
