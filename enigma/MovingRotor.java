package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Cheng Zhu
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _name = name;
    }

    /** Advance moving rotor by one position, if possible.
     * By default, does nothing. */
    @Override
    void advance() {
        if (setting() + 1 >= size()) {
            set(0);
        } else {
            set(setting() + 1);
        }
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    @Override
    String notches() {
        return _notches;
    }

    /** Return true iff I have a ratchet and can move. */
    @Override
    boolean rotates() {
        return true;
    }

    /** return the name of the moving rotor. */
    @Override
    public String toString() {
        return "Moving Rotor " + _name;
    }

    /** The notches of the moving rotor. */
    private String _notches;

    /** The name of the moving rotor. */
    private String _name;
}
