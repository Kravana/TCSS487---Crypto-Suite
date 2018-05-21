package Deprecated;

/**
 * Provides tools to encrypt and decrypt data using the
 * Advanced Encryption Standard.
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class AES {


    final static private int Nb = 4;

    final static private int[] Nk = {4, 6, 8};

    final static private int[] Nr = {10, 12, 14};

    final static private Byte[][] STATE = new Byte[4][Nb];



    private AES() {

    }


    /**
     *
     * @param thePlaintextBlock Nb * 4 bytes of plaintext, as byte array
     * @param theCipherKey the cipher key
     * @return
     */
    public static Byte[] encrypt(final Byte[] thePlaintextBlock,
                         final Byte[] theCipherKey) {

        // Input conditions for AES-256
        if (thePlaintextBlock.length == Nb
                && theCipherKey.length == Nk[2]) {
            inToState(thePlaintextBlock);

        }

        return null;
    }

    /**
     *
     * @param theCipherTextBlock Nb * 4 bytes of of ciphertext, as byte array
     * @param theCipherKey the cipher key
     * @return
     */
    public static Byte[] decrypt(final Byte[] theCipherTextBlock,
                          final Byte[] theCipherKey) {
        // Input conditions for AES-256
        if (theCipherTextBlock.length == Nb
                && theCipherKey.length == Nk[2]) {
            inToState(theCipherTextBlock);

        }

        return null;
    }

    /**
     * Plaintext input block is copied into the State array
     * according to s[r, c] = in[r + 4c].
     * @param theIn Plaintext input block
     */
    private static void inToState(final Byte[] theIn) {
        for (int i = 0; i < STATE.length; i++) {
            for (int j = 0; j < Nb; j++) {
                STATE[i][j] = theIn[i + 4 * j];
            }
        }
    }

    /**
     * Copies the State to a one dimensional byte array.
     * @return State array copied to one dimensional byte array.
     */
    private static Byte[] stateToOut() {
        Byte[] out = new Byte[STATE.length * Nb];
        for (int i = 0; i < STATE.length; i++) {
            for (int j = 0; j < Nb; j++) {
                out[i + 4 * j] = STATE[i][j];
            }
        }

        return out;
    }

    /**
     * Transformation in which a Round Key is added to the State
     * using an XOR operation. Length of Round Key must equal the
     * size of the State (i.e., for Nb = 4, the Round
     * Key length equals 128 bits/16 bytes).
     *
     */
    private static void addRoundKey() {

    }

    private static void SubBytes() {

    }

    private static void shiftRows() {

    }

    private static void MixColumns() {

    }

}
