package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Cheng Zhu
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _position = 0;
        _positionChar = alphabet().toChar(0);
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    void set(char cposn) {
        if (Character.compare(cposn, _positionChar) != 0) {
            throw error("reflector has only one position");
        }
    }

    /** Return true iff I reflect. */
    @Override
    boolean reflecting() {
        return true;
    }

    /** return the name of the reflector. */
    @Override
    public String toString() {
        return "Reflector " + _name;
    }

    /** My name. */
    private final String _name;

    /** The int position of the rotor. */
    private final int _position;

    /** The char position of the rotor. */
    private final char _positionChar;

}
