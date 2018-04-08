package Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Kevin Ravna
 * @version Winter2017
 */
public class SHA {

    /**
     * max unsigned long
     */
    private static BigInteger BIT_64 = new BigInteger("18446744073709551615");

    /**
     * round constants RC[i]
     */
    private BigInteger[] RC = new BigInteger[] {
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

    int[] keccakf_rotc = {
            1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 2, 14,
            27, 41, 56, 8, 25, 43, 62, 18, 39, 61, 20, 44
    };

    int[] keccakf_piln = {
            10, 7, 11, 17, 18, 3, 5, 16, 8, 21, 24, 4,
            15, 23, 19, 13, 12, 2, 20, 14, 22, 9, 6, 1
    };

    int i, j, r;

    BigInteger t;
    BigInteger[] bc = new BigInteger[5];









    public static String computeWithJavaSec(final String message) throws NoSuchAlgorithmException {
        byte[] byteMessage = message.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((byteMessage));
        byte[] byteData = md.digest();
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String computeFileWithJavaSec(final String fileLoc) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(fileLoc);
        byte[] dataBytes = new byte[1024];
        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println("Hex format : " + sb.toString());
        return sb.toString();
    }


}
