package Deprecated;

import java.math.BigInteger;
import java.util.Formatter;

/**
 * Adapted from/inspired by romus (https://github.com/romus/sha/blob/master/sha3/src/main/java/com/theromus/sha/Keccak.java)
 * @author Kevin Ravana
 * @version Spring 2018
 */
class Keccak {

    /**
     * max unsigned long
     */
    private static BigInteger BIT_64 = new BigInteger("18446744073709551615");

    /**
     * Round constants RC[i] for a maximum lane size of 64,
     * given by https://keccak.team/keccak_specs_summary.html
     */
    private BigInteger[] ROUND_CONSTANTS = new BigInteger[] {
            new BigInteger("0000000000000001", 16),
            new BigInteger("0000000000008082", 16),
            new BigInteger("800000000000808A", 16),
            new BigInteger("8000000080008000", 16),
            new BigInteger("000000000000808B", 16),
            new BigInteger("0000000080000001", 16),
            new BigInteger("8000000080008081", 16),
            new BigInteger("8000000000008009", 16),
            new BigInteger("000000000000008A", 16),
            new BigInteger("0000000000000088", 16),
            new BigInteger("0000000080008009", 16),
            new BigInteger("000000008000000A", 16),
            new BigInteger("000000008000808B", 16),
            new BigInteger("800000000000008B", 16),
            new BigInteger("8000000000008089", 16),
            new BigInteger("8000000000008003", 16),
            new BigInteger("8000000000008002", 16),
            new BigInteger("8000000000000080", 16),
            new BigInteger("000000000000800A", 16),
            new BigInteger("800000008000000A", 16),
            new BigInteger("8000000080008081", 16),
            new BigInteger("8000000000008080", 16),
            new BigInteger("0000000080000001", 16),
            new BigInteger("8000000080008008", 16)
    };

    /**
     * Rotation offsets for r[x, y],
     * given by https://keccak.team/keccak_specs_summary.html
     */
    private int[][] ROTATION_OFFSET = new int[][] {
            {0,    36,     3,    41,    18},
            {1,    44,    10,    45,     2},
            {62,    6,    43,    15,    61},
            {28,   55,    25,    21,    56},
            {27,   20,    39,     8,    14}
    };

    /**
     * Permutation width
     */
    private int LANE_LENGTH;

    /**
     * Number of rounds
     */
    private int NUM_OF_ROUNDS;


    public Keccak(final int thePermWidth) {
        LANE_LENGTH = thePermWidth / 25;
        int l = (int) (Math.log(LANE_LENGTH) / Math.log(2));
        NUM_OF_ROUNDS = 12 + 2 * l;
    }

    /**
     *
     * @param theMbytes
     * @param r
     * @param d
     * @param theMbits
     * @return
     */
    public String getHash(final String theMbytes,
                          final int r,
                          final int d,
                          final String theMbits) {
        return keccakRC(theMbytes, r, d, theMbits);

    }

    /**
     *
     * @param theMbytes
     * @param r
     * @param d
     * @param theMbits
     * @return
     */
    private String keccakRC(final String theMbytes,
                            final int r,
                            final int d,
                            final String theMbits) {


        // Padding
        BigInteger[][] paddedMessage = padding(theMbytes, r, theMbits);


        // Initialization
        BigInteger[][] state = new BigInteger[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                state[i][j] = new BigInteger("0", 16);
            }
        }

        // Absorbing phase
        for (BigInteger[] Pi: paddedMessage) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if ((i + j * 5) < r / LANE_LENGTH) {
                        state[i][j] = state[i][j].xor(Pi[i + j * 5]);
                    }
                }
            }
            keccakF(state);
        }

        // Squeezing phase
        String Z = "";
        do {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if ((5 * i + j) < (r / LANE_LENGTH)) {
                        Z = Z + addZero(getReverseHexString(state[j][i]), 16).substring(0, 16);
                    }
                }
            }
            keccakF(state);
        } while (Z.length() < d * 2);

        return Z.substring(0, d * 2);
    }

    /**
     *
     * @param A
     * @return
     */
    private void keccakF(BigInteger[][] A) {

        for (int i = 0; i < NUM_OF_ROUNDS; i++) {
            A = roundB(A, ROUND_CONSTANTS[i]);
        }
    }

    /**
     *
     * @param A
     * @param RC
     * @return
     */
    private BigInteger[][] roundB(final BigInteger[][] A, BigInteger RC) {
        BigInteger[] C = new BigInteger[5];
        BigInteger[] D = new BigInteger[5];
        BigInteger[][] B = new BigInteger[5][5];

        //θ step
        for (int i = 0; i < 5; i++)
            C[i] = A[i][0].xor(A[i][1]).xor(A[i][2]).xor(A[i][3]).xor(A[i][4]);

        for (int i = 0; i < 5; i++)
            D[i] = C[(i + 4) % 5].xor(rot(C[(i + 1) % 5], 1));

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                A[i][j] = A[i][j].xor(D[i]);

        //ρ and π steps
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                B[j][(2 * i + 3 * j) % 5] = rot(A[i][j], ROTATION_OFFSET[i][j]);
        //χ step
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                A[i][j] = B[i][j].xor(B[(i + 1) % 5][j].not().and(B[(i + 2) % 5][j]));

        //ι step
        A[0][0] = A[0][0].xor(RC);

        return A;
    }

    /**
     *
     * @param x
     * @param n
     * @return
     */
    private BigInteger rot(BigInteger x, int n) {
        n = n % LANE_LENGTH;

        BigInteger leftShift = getShiftLeft64(x, n);
        BigInteger rightShift = x.shiftRight(LANE_LENGTH - n);

        return leftShift.or(rightShift);
    }

    /**
     *
     * @param value
     * @param shift
     * @return
     */
    private BigInteger getShiftLeft64(BigInteger value, int shift) {
        BigInteger retValue = value.shiftLeft(shift);
        BigInteger tmpValue = value.shiftLeft(shift);

        if (retValue.compareTo(BIT_64) > 0) {
            for (int i = 64; i < 64 + shift; i++)
                tmpValue = tmpValue.clearBit(i);

            tmpValue = tmpValue.setBit(64 + shift);
            retValue = tmpValue.and(retValue);
        }

        return retValue;
    }

    /**
     *
     * @param theMbytes
     * @param r
     * @param theMbits
     * @return
     */
    private BigInteger[][] padding(final String theMbytes, final int r, final String theMbits) {
        int size;
        String message = theMbytes + theMbits;

        while (((message.length() / 2) * 8 % r) != ((r - 8))) {
            message = message + "00";
        }

        message = message + "80";
        size = (((message.length() / 2) * 8) / r);

        BigInteger[][] arrayM = new BigInteger[size][];
        arrayM[0] = new BigInteger[1600 / LANE_LENGTH];
        initArray(arrayM[0]);

        int count = 0;
        int j = 0;
        int i = 0;

        for (int _n = 0; _n < message.length(); _n++) {

            if (j > (r / LANE_LENGTH - 1)) {
                j = 0;
                i++;
                arrayM[i] = new BigInteger[1600 / LANE_LENGTH];
                initArray(arrayM[i]);
            }

            count++;

            if ((count * 4 % LANE_LENGTH) == 0) {
                String subString = message.substring((count - LANE_LENGTH / 4),
                        (LANE_LENGTH / 4) + (count - LANE_LENGTH / 4));
                arrayM[i][j] = new BigInteger(subString, 16);
                String revertString = getReverseHexString(arrayM[i][j]);
                revertString = addZero(revertString, subString.length());
                arrayM[i][j] = new BigInteger(revertString, 16);
                j++;
            }
        }

        return arrayM;
    }

    /**
     *
     * @param array
     */
    private void initArray(BigInteger[] array) {
        for (int i = 0; i < array.length; i++)
            array[i] = new BigInteger("0", 16);
    }

    /**
     *
     * @param l
     * @return
     */
    private String getReverseHexString(BigInteger l) {
        byte[] array = l.toByteArray();
        reverseByteArray(array);
        return getHexStringByByteArray(array);
    }

    /**
     *
     * @param str
     * @param length
     * @return
     */
    private String addZero(String str, int length) {
        String retStr = str;
        for (int i = 0; i < length - str.length(); i ++)
            retStr += "0";
        return retStr;
    }

    /**
     *
     * @param array
     */
    private void reverseByteArray(byte[] array) {
        if (array == null)
            return;

        int i = 0;
        int j = array.length - 1;
        byte tmp;

        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        } // while
    }

    /**
     *
     * @param array
     * @return
     */
    private String getHexStringByByteArray(byte[] array) {
        if (array == null)
            return null;

        StringBuilder stringBuilder = new StringBuilder(array.length * 2);
        @SuppressWarnings("resource")
        Formatter formatter = new Formatter(stringBuilder);

        for (byte tempByte: array)
            formatter.format("%02x", tempByte);

        return stringBuilder.toString();
    }






}
