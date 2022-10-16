package enigma;

import org.junit.Test;
import static org.junit.Assert.*;


/** The suite of all JUnit tests for the Permutation class.
 *  @author Cheng Zhu
 */
public class AlphabetTest {
    @Test
    public void test1() {
        Alphabet test1 = new Alphabet("BCDEF");
        try {
            test1.toChar(100);
        } catch (Exception e) {
            System.out.println("There is an error: "
                    + "index out of alphabet range");
        }
        assertEquals(5, test1.size());
        assertTrue(test1.contains('B'));
        assertTrue(test1.contains('C'));
        assertTrue(test1.contains('F'));
        assertFalse(test1.contains('a'));
        assertFalse(test1.contains('A'));
        assertEquals('B', test1.toChar(0));
        assertEquals('D', test1.toChar(2));
        assertEquals('F', test1.toChar(4));
        assertEquals(0, test1.toInt('B'));
        assertEquals(1, test1.toInt('C'));
        assertEquals(4, test1.toInt('F'));
    }

    @Test
    public void test2() {
        Alphabet test2 = new Alphabet("abcdefABCDEF012345");
        assertEquals(18, test2.size());
        assertTrue(test2.contains('A'));
        assertTrue(test2.contains('B'));
        assertTrue(test2.contains('F'));
        assertTrue(test2.contains('a'));
        assertTrue(test2.contains('d'));
        assertTrue(test2.contains('f'));
        assertTrue(test2.contains('0'));
        assertTrue(test2.contains('4'));
        assertTrue(test2.contains('5'));
        assertFalse(test2.contains('T'));
        assertFalse(test2.contains('Z'));
        assertFalse(test2.contains('z'));
        assertFalse(test2.contains('9'));
        assertEquals('a', test2.toChar(0));
        assertEquals('c', test2.toChar(2));
        assertEquals('f', test2.toChar(5));
        assertEquals('5', test2.toChar(17));
        assertEquals('3', test2.toChar(15));
        assertEquals('C', test2.toChar(8));
        assertEquals(0, test2.toInt('a'));
        assertEquals(1, test2.toInt('b'));
        assertEquals(5, test2.toInt('f'));
        assertEquals(9, test2.toInt('D'));
        assertEquals(16, test2.toInt('4'));
    }
}
