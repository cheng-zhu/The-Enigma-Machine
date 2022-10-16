package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Cheng Zhu
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _name = name;
    }

    /** the fixed rotor has no notches. */
    @Override
    boolean atNotch() {
        return false;
    }

    /** return the name of the fixed rotor. */
    @Override
    public String toString() {
        return "Fixed Rotor " + _name;
    }

    /** Instance variable: name of the fixed rotor. */
    private String _name;
}
