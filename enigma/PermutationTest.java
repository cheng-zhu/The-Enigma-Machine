package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Cheng Zhu
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** ***** TESTING UTILITIES ***** */

    private Permutation perm;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of TOALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        String alpha = fromAlpha;
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /** ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
        assertFalse(perm.derangement());
    }

    @Test
    public void testInvertInt() {
        perm = new Permutation(" (BA0D) ", new Alphabet("AB0D"));
        assertEquals(3, perm.invert(5));
        assertEquals(2, perm.invert(-1));
    }

    @Test
    public void testPermuteInt() {
        perm = new Permutation(" (BAC) ", new Alphabet("ABCD"));
        assertEquals(0, perm.permute(5));
        assertEquals(1, perm.permute(-2));
    }

    @Test
    public void testSixteenCycle() {
        perm = new Permutation("  (0)   (&) ", new Alphabet("&0"));
        checkPerm("ElevenCycle", "&0", "&0");
        assertFalse(perm.derangement());
        assertEquals(2, perm.size());
    }

    @Test
    public void testFourteenCycle() {
        perm = new Permutation(" (a&) (b) (c) (d)  (e)   (f) (g) (A) "
                + "(B) (C) (D) (E) (F) (G) (!) (@) (#) ($) (%) (^)", WEIRDER);
        checkPerm("TenCycle", WEIRDER_STRING,
                "&bcdefgABCDEFG!@#$%^a");
        assertFalse(perm.derangement());
        assertEquals(6, perm.permute(6));
        assertEquals(0, perm.permute(20));
    }

    @Test
    public void testThirteenCycle() {
        perm = new Permutation("(aA)(Bb)(cC)(Dd)(eE)(Ff)"
                + "(gG)(!)(@)(#)($)(%)(^)(&)", WEIRDER);
        checkPerm("TenCycle", WEIRDER_STRING,
                "ABCDEFGabcdefg!@#$%^&");
        assertFalse(perm.derangement());
    }

    @Test
    public void testSeventeenCycle() {
        perm = new Permutation(" (;) (7) (4) (8) (3) (Aa) (5) (2) (6) ",
                new Alphabet("Aa23;45678"));
        checkPerm("ElevenCycle", "Aa23;45678",
                "aA23;45678");
        assertFalse(perm.derangement());
        assertEquals(10, perm.size());
    }

    @Test
    public void testTwelveCycle() {
        perm = new Permutation("(\t\n)(\r)(\f4)(3a\\b~})"
                + "(c\'d{e)(f\"1)(g`A)($*%)(BCD|)(EF)(G!)(?@#^&)",
                new Alphabet("\n\t\b\r\f\"143a\\"
                        + "b~}c\'d{efg`A?BCD|EFG!@#$*%^&"));
        checkPerm("TwelveCycle",
                "\n\t\b\r\f\"143a\\b~}c\'d{efg`A?BCD|EFG!@#$*%^&",
                "\t\n\b\r41f\fa\\b~}3\'d{ec\"`Ag@CD|BFE!G#^*%$&?");
        assertFalse(perm.derangement());
    }

    @Test
    public void testOneCycle() {
        perm = new Permutation("(ABCD) (MP)", UPPER);
        checkPerm("OneCycle", UPPER_STRING,
                "BCDAEFGHIJKLPNOMQRSTUVWXYZ");
        assertFalse(perm.derangement());
    }
    @Test
    public void testTwoCycle() {
        perm = new Permutation("(ABCD) (MP) (J)", UPPER);
        checkPerm("TwoCycle", UPPER_STRING,
                "BCDAEFGHIJKLPNOMQRSTUVWXYZ");
        assertFalse(perm.derangement());
    }

    @Test
    public void testThreeCycle() {
        perm = new Permutation("(ABCD) (MP) (J) (TVQZF)", UPPER);
        checkPerm("TwoCycle", UPPER_STRING,
                "BCDAETGHIJKLPNOMZRSVUQWXYF");
        assertFalse(perm.derangement());
    }

    @Test
    public void testFourCycle() {
        perm = new Permutation("(ABCD) (MP) (J) (TVQZF)", UPPER);
        checkPerm("TwoCycle", UPPER_STRING,
                "BCDAETGHIJKLPNOMZRSVUQWXYF");
        assertFalse(perm.derangement());
    }

    @Test
    public void testFiveCycle() {
        perm = new Permutation("", UPPER_SHORT);
        checkPerm("FiveCycle", UPPER_SHORT_STRING, UPPER_SHORT_STRING);
        assertFalse(perm.derangement());
    }

    @Test
    public void testSixCycle() {
        perm = new Permutation("", LOWER);
        checkPerm("SixCycle", LOWER_STRING, LOWER_STRING);
        assertFalse(perm.derangement());
    }

    @Test
    public void testSevenCycle() {
        perm = new Permutation("(ad9h) (lzc) ", LOWERNUM);
        checkPerm("SevenCycle", LOWERNUM_STRING,
                "ds9fgajkzcxlvbnmqwe12h65");
        assertFalse(perm.derangement());
    }

    @Test
    public void testEightCycle() {
        perm = new Permutation("(@^B) (Nt&) (9A) (*h8)", WEIRD);
        checkPerm("EightCycle", WEIRD_STRING, "^h*A89BN&@t");
        assertTrue(perm.derangement());
    }

    @Test
    public void testNineCycle() {
        perm = new Permutation("(abcdefgABCDEFG!@#$%^&)", WEIRDER);
        checkPerm("NineCycle", WEIRDER_STRING,
                "bcdefgABCDEFG!@#$%^&a");
        assertTrue(perm.derangement());
    }

    @Test
    public void testTenCycle() {
        perm = new Permutation("(a)", WEIRDER);
        checkPerm("NineCycle", WEIRDER_STRING, WEIRDER_STRING);
        assertFalse(perm.derangement());
    }

    @Test(expected = EnigmaException.class)
    public void testDuplicateOne() {
        perm = new Permutation("(abcdefgABCDEFAG!@#$%^&)", WEIRDER);
    }

    @Test(expected = EnigmaException.class)
    public void testDuplicateTwo() {
        perm = new Permutation("(abc) (a!)", WEIRDER);
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        perm = new Permutation("(KA) (a!)", WEIRDER);
    }

    @Test(expected = EnigmaException.class)
    public void testErrorAlphabet() {
        Alphabet error = new Alphabet("abcdefgAABCDEFG!@#$%^&");
    }

    @Test
    public void testBlankSpaceCycle() {
        perm = new Permutation("    ", WEIRDER);
    }

    @Test(expected = EnigmaException.class)
    public void testMalformedInput2() {
        perm = new Permutation(" (  )  ", WEIRDER);
    }

    @Test(expected = EnigmaException.class)
    public void testCrazyParenthesis() {
        perm = new Permutation("   (()(  ) ()", WEIRDER);
    }

    @Test(expected = EnigmaException.class)
    public void testEmptyParenthesis() {
        perm = new Permutation("())", new Alphabet(")"));
    }

    @Test(expected = EnigmaException.class)
    public void testEightCycleWrongParenthesis() {
        perm = new Permutation("(@^B( (Nt&( (9A( (*h8)", WEIRD);
    }

    @Test(expected = EnigmaException.class)
    public void testEightCycleHasSpaceInCycle() {
        perm = new Permutation("(@ ^B) (N  t&) (9A) (*h8)", WEIRD);
    }

    @Test(expected = EnigmaException.class)
    public void testEightCycleHasParentInCycle() {
        perm = new Permutation("(@(^B) (Nt&) (9A) (*h8)", WEIRD);
    }
    @Test(expected = EnigmaException.class)
    public void testMalformedInput() {
        perm = new Permutation("( @^B ) ( Nt& ) ( 9A ) ( *h8 )", WEIRD);
    }

    @Test(expected = EnigmaException.class)
    public void testMalformedInput1() {
        perm = new Permutation(")@^B( )Nt&( )9A( )*h8(", WEIRD);
    }

    @Test(expected = EnigmaException.class)
    public void testNoParenthesis() {
        perm = new Permutation("@9 ^N Bt", WEIRD);
    }

    @Test(expected = EnigmaException.class)
    public void testDuplicateMonoCycle() {
        perm = new Permutation("(9) (9)",
                new Alphabet("103456789"));
    }
}
