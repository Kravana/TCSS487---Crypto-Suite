package Model;

import sun.misc.BASE64Encoder;

import java.io.BufferedInputStream;
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

    //	The rotation offsets r[x,y].
    private int[][] r = new int[][] {
            {0, 36, 3, 41, 18},
            {1, 44, 10, 45, 2},
            {62, 6, 43, 15, 61},
            {28, 55, 25, 21, 56},
            {27, 20, 39, 8, 14}
    };

    private int w;

    private int n;








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
        byte[] buffer= new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileLoc));
        while ((count = bis.read(buffer)) > 0) {
            digest.update(buffer, 0, count);
        }
        bis.close();
        byte[] hash = digest.digest();
        return new BASE64Encoder().encode(hash);
    }


}
