package enigma;

import java.util.HashMap;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Cheng Zhu
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTS ***** */

    private static final Alphabet AZ = new Alphabet(TestUtils.UPPER_STRING);

    private static final HashMap<String, Rotor> ROTORS = new HashMap<>();

    static {
        HashMap<String, String> nav = TestUtils.NAVALA;
        ROTORS.put("B", new Reflector("B",
                new Permutation(nav.get("B"), AZ)));
        ROTORS.put("Beta",
                new FixedRotor("Beta",
                        new Permutation(nav.get("Beta"), AZ)));
        ROTORS.put("Gamma",
                new FixedRotor("Gamma",
                        new Permutation(nav.get("Gamma"), AZ)));
        ROTORS.put("III",
                new MovingRotor("III",
                        new Permutation(nav.get("III"), AZ),
                        "ABCDETYHJ"));
        ROTORS.put("IV",
                new MovingRotor("IV", new Permutation(nav.get("IV"), AZ),
                        "ABDCENORX"));
        ROTORS.put("I",
                new MovingRotor("I", new Permutation(nav.get("I"), AZ),
                        "ABCDFIQTYK"));
        ROTORS.put("VI",
                new MovingRotor("VI", new Permutation(nav.get("VI"), AZ),
                        "ZERYIUABDQOP"));
        ROTORS.put("VII",
                new MovingRotor("VII",
                        new Permutation(nav.get("VII"), AZ),
                        "ZACRTGJKLBMIWX"));
    }

    private static final String[] ROTORS1 =
        { "B", "Gamma", "III", "IV", "I", "VII", "VI" };
    private static final String SETTING1 = "TVMABC";

    private Machine mach1() {
        Machine mach = new Machine(AZ, 7, 5, ROTORS.values());
        mach.insertRotors(ROTORS1);
        mach.setRotors(SETTING1);
        return mach;
    }

    @Test
    public void testConvertMsgReverse() {
        Machine mach = mach1();
        mach.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                AZ));
        assertEquals("AYTUKMSTVPDIVIXDDRHEBXPWPGBRPWFFUWDNZZMAL"
                        + "OOJLPYWUNTAUTIEJGEVXMMFJCEHTPNRYXXAMAVYOKODFDASKZT"
                        + "XGEKQUBLMXUQFEQMORBFYXKXYBHUZHODBFOKACSJUZGKWKVUQD"
                        + "MPCEUTDYKUYNJBXNXYRSQDJWUKRMSLCRIXJJXOOJDRASELPMJE"
                        + "YSKNTLZNAXTMCPZFDPFJEDGRKMWNGAYHAHDUZREPKZFSODWUHC"
                        + "JCWTYJQRZLJOOUFMJHEVFJJNAIWBPFVGEPIUIRYHYAAIEOSEOF"
                        + "VCSGZABROAZRMTIITYPWTZJVQBIDFENBMYQZBBJSNWHEJWUCCU"
                        + "RGPWIINIFVAYHLZWLQIKJGMPCXXRIPNGYMHLMMGJZRMGYPNRUX"
                        + "VJZLETPYMEOAAYYRWRUTVVEZNOYBKWVOSNPXNTNYWJDEHIIAST"
                        + "RIFKOL",
                mach.convert("HELLOWORLDFBDJSKHGFBFJDSABFHJDSBVJHKFE"
                        + "KBFDJHSVFDHJKSBGHDSAJKBFJHDSFKBVJHFVKDSBFKAJFBYR"
                        + "UEBGVTRIUOBVPBFHDVSFHJEKDSBHFJDSBGFHJSBGHJFGKERB"
                        + "GGHFERKHBGFERKGHELLOWORLDFBDJSKHGFBFJDSABFHJDSBV"
                        + "JHKFEKBFDJHSVFDHJKSBGHDSAJKBFJHDSFKBVJHFVKDSBFKA"
                        + "JFBYRUEBGVTRIUOBVPBFHDVSFHJEKDSBHFJDSBGFHJSBGHJF"
                        + "GKERBGGHFERKHBGFERKGHELLOWORLDFBDJSKHGFBFJDSABFH"
                        + "JDSBVJHKFEKBFDJHSVFDHJKSBGHDSAJKBFJHDSFKBVJHFVKD"
                        + "SBFKAJFBYRUEBGVTRIUOBVPBFHDVSFHJEKDSBHFJDSBGFHJS"
                        + "BGHJFGKERBGGHFERKHBGFERKG"));
    }
}
