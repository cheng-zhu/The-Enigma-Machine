package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Cheng Zhu
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        _size = chars.length();
        char[] charsArr = _chars.toCharArray();
        for (int i = 0; i < _chars.length(); i++) {
            for (int j = i + 1; j < _chars.length(); j++) {
                if (charsArr[i] == charsArr[j]) {
                    throw new EnigmaException("Wrong alphabet:"
                            + " there are duplicate characters.");
                }
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _size;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). If the index is out of range, WRAP it.*/
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            index = index % size();
            if (index < 0) {
                index += size();
            }
        }
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar().
     *  If the index is out of range, throw an error.*/
    int toInt(char ch) {
        if (!contains(ch)) {
            throw new EnigmaException("Error: "
                    + "the character is not in the alphabet");
        } else {
            return _chars.indexOf(ch);
        }
    }

    /** private variable chars. */
    private final String _chars;

    /** private variable size. */
    private final int _size;
}
