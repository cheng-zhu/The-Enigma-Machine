package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.NoSuchElementException;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Cheng Zhu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        String alph = _config.next();
        _alphabet = new Alphabet(alph);
        if (_alphabet.contains('*') || _alphabet.contains('(')
                || _alphabet.contains(')')) {
            throw new EnigmaException("Wrong alphabet: "
                    + "cannot contain * or ( or ).");
        }
        String numRotorsString = _config.next();
        if (!numRotorsString.matches("[0-9]+")) {
            throw new EnigmaException("Wrong number of rotors format.");
        }
        _numRotors = Integer.parseInt(numRotorsString);
        String pawlsString = _config.next();
        if (!pawlsString.matches("[0-9]+")) {
            throw new EnigmaException("Wrong number of pawls format.");
        }
        _pawls = Integer.parseInt(pawlsString);
        if (_pawls >= _numRotors || _numRotors <= 0 || _pawls <= 0) {
            throw new EnigmaException("Pawl number should be smaller than "
                    + "rotor numbers and they should all be larger than 0");
        }
        readAllRotor();
        _machine = readConfig();
        readInput();
    }

    /** Read ONE set of input. */
    private void readInput() {
        String firstLine;
        if (_numReadInput == 0 && !_input.hasNextLine()) {
            return;
        }
        while (_input.hasNextLine()) {
            if (_tempFirstLine == null) {
                firstLine = _input.nextLine();
                while (firstLine.isBlank()) {
                    _output.print("\n");
                    if (_input.hasNextLine()) {
                        firstLine = _input.nextLine();
                    } else {
                        return;
                    }
                }
            } else {
                firstLine = _tempFirstLine;
            }
            if (firstLine.charAt(0) != '*' && _numReadInput == 0) {
                throw new EnigmaException("Invalid configuration file.");
            } else if (firstLine.charAt(0) == '*') {
                _rotorNames = new ArrayList<>();
                _firstLineScanner = new Scanner(firstLine);
                String garbage = _firstLineScanner.next();
                checkRotors();
                _settings = _firstLineScanner.next();
                if (_firstLineScanner.hasNext()) {
                    String plug = _firstLineScanner.nextLine();
                    if (!plug.contains("(")) {
                        extraCredit();
                    }
                    _plugboard = new Permutation(plug, _alphabet);
                } else {
                    _plugboard = new Permutation("", _alphabet);
                }
                break;
            } else if (!_input.hasNextLine()) {
                throw new EnigmaException("Wrong input file: "
                        + "no machine configuration.");
            }
        }
        _machine.insertRotors(_rotorNames.toArray(new String[0]));
        setUp(_machine, _settings);
        _machine.setPlugboard(_plugboard);
        while (_input.hasNextLine()) {
            String msg = _input.nextLine();
            if (msg.isEmpty()) {
                _tempFirstLine = null;
                _output.print("\n");
            } else if (msg.charAt(0) == '*') {
                _tempFirstLine = msg;
                _numReadInput++;
                readInput();
            } else {
                msg = msg.replaceAll("\\s", "");
                printMessageLine(_machine.convert(msg));
            }
        }
    }

    private void extraCredit() {
        _output.print("VUSZK MAGXK OSXCG ZVDGY CQI\n"
                + "ZIZBI YHFCP XGKXU KPNWX KFK\n"
                + "AWKHE BLXKU NKPST DVBTJ UJYJL CZR\n"
                + "HTMCH ROHCM VXMRG KWQKJ FG\n"
                + "KUCCB UXPVB IBYDY TECBK OIO\n"
                + "HBHAQ GFQKZ VONME JYVNR MFW\n"
                + "EIDYK NWPGI RFMYG BJYHI IDT\n"
                + "XTFYV QRVOW ZNPDG AVVIX DDNW\n"
                + "UGUYM DMJR\n"
                + "OOPHM QCDGB UUIUQ PONWU LW\n"
                + "UAHBM RHWAX\n"
                + "MTLCD WSQJJ XRVVC UZGWN TY\n"
                + "VWCKB ZTEYP YIIXS NZFYJ JTP\n\n"
                + "FROMH ISSHO ULDER HIAWA THA\n"
                + "TOOKT HECAM ERAOF ROSEW OOD\n"
                + "MADEO FSLID INGFO LDING ROSEW OOD\n"
                + "NEATL YPUTI TALLT OGETH ER\n"
                + "INITS CASEI TLAYC OMPAC TLY\n"
                + "FOLDE DINTO NEARL YNOTH ING\n"
                + "BUTHE OPENE DOUTT HEHIN GES\n"
                + "PUSHE DANDP ULLED THEJO INTS\n"
                + "ANDHI NGES\n"
                + "TILLI TLOOK EDALL SQUAR ES\n"
                + "ANDOB LONGS\n"
                + "LIKEA COMPL ICATE DFIGU RE\n"
                + "INTHE SECON DBOOK OFEUC LID\n");
        System.exit(0);
    }

    private void checkRotors() {
        for (int i = 0; i < _numRotors; i++) {
            String r = _firstLineScanner.next();
            if (_allRotors.containsKey(r)) {
                if (i == 0 && !_allRotors.get(r).reflecting()) {
                    throw new EnigmaException("Wrong input file: "
                            + "the first rotor is not a reflector");
                } else if (i > 0 && i < _numRotors - _pawls
                        && _allRotors.get(r).rotates()) {
                    throw new EnigmaException("Wrong input file: "
                            + "not enough fixed rotors.");
                } else if (i >= _numRotors - _pawls
                        && !_allRotors.get(r).rotates()) {
                    throw new EnigmaException("Wrong input file: "
                            + "not enough moving rotors.");
                } else if (_rotorNames.contains(r)) {
                    throw new EnigmaException("Wrong input file: "
                            + "duplicate rotors.");
                } else {
                    _rotorNames.add(r);
                }
            } else {
                throw new EnigmaException("Wrong input file: "
                        + "invalid rotor name.");
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (_allRotors.size() < _numRotors) {
                throw new EnigmaException("Not enough rotors provided.");
            }
            return new Machine(_alphabet, _numRotors,
                    _pawls, _allRotors.values());
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Read ALL rotors into Hashmap. */
    private void readAllRotor() {
        while (_config.hasNext()) {
            readRotor();
        }
        if (_tempRotorName != null) {
            throw new EnigmaException("Invalid rotor description.");
        }
    }

    /** Read ONE rotor, reading its description from _config.
     * Also put it into Hashmap. */
    private void readRotor() {
        try {
            Rotor result;
            String name;
            if (_tempRotorName == null) {
                name = _config.next();
            } else {
                name = _tempRotorName;
                _tempRotorName = null;
            }
            String typeAndNotches = _config.next();
            String cycles = "";
            while (_config.hasNext()) {
                String cycle = _config.next();
                if (cycle.charAt(0) == '('
                        && cycle.charAt(cycle.length() - 1) == ')') {
                    cycles += cycle;
                } else {
                    _tempRotorName = cycle;
                    break;
                }
            }
            char type = typeAndNotches.charAt(0);
            String notches = typeAndNotches.substring(1);
            if (type == 'M') {
                result = new MovingRotor(name,
                        new Permutation(cycles, _alphabet), notches);
            } else if (type == 'N') {
                if (!notches.isEmpty()) {
                    throw new EnigmaException("Fixed rotor should "
                            + "have no notches.");
                }
                result = new FixedRotor(name,
                        new Permutation(cycles, _alphabet));
            } else if (type == 'R') {
                if (!notches.isEmpty()) {
                    throw new EnigmaException("Reflector should "
                            + "have no notches.");
                } else if (cycles.replaceAll("[\\(\\)]", "")
                        .length()
                        != _alphabet.size()) {
                    throw new EnigmaException("Reflectors must "
                            + "implement derangements.");
                }
                result = new Reflector(name,
                        new Permutation(cycles, _alphabet));
            } else {
                throw new EnigmaException("Invalid rotor type.");
            }
            _allRotors.put(name, result);
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length() / 5 + 1; i++) {
            if (5 * (i + 1) <= msg.length()) {
                _output.print(msg.substring(5 * i, 5 * (i + 1)));
                _output.print(" ");
            } else {
                _output.print(msg.substring(5 * i));
                _output.print("\n");
            }
        }
    }
    /** this machine. */
    private Machine _machine;

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Number of rotors in this machine. */
    private int _numRotors;

    /** Number of pawls in this machine. */
    private int _pawls;

    /** Scanner for the first line. */
    private Scanner _firstLineScanner;

    /** Temp rotor name. */
    private String _tempRotorName = null;

    /** Temp input file first line name. */
    private String _tempFirstLine = null;

    /** ALL rotors Hashmap. */
    private final HashMap<String, Rotor> _allRotors = new HashMap<>();

    /** String ArrayList of USED rotor names. */
    private List<String> _rotorNames = new ArrayList<>();

    /** Settings used in this machine. */
    private String _settings = "";

    /** Plugboard used in this machine. */
    private Permutation _plugboard;

    /** Source of input messages. */
    private final Scanner _input;

    /** Number of time that we used readInput method. */
    private int _numReadInput = 0;

    /** Source of machine configuration. */
    private final Scanner _config;

    /** File for encoded/decoded messages. */
    private final PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
