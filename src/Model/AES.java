package Model;

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
                && theCipherKey.length == Nk[8]) {


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
                && theCipherKey.length == Nk[8]) {


        }

        return null;
    }

    private static void addRoundKey() {

    }

    private static void SubBytes() {

    }

    private static void shiftRows() {

    }

    private static void MixColumns() {

    }

}
