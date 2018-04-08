package Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * @author Kevin Ravna
 * @version Winter2017
 */
public class SHA {


    /**
     * SHA3-512
     * @param theMessage
     * @return
     */
    public static String getHash512(final String theMessage) {
        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        String s = getHexStringByByteArray(byteMessage);
        Keccak keccak = new Keccak(1600);
        return keccak.getHash(s, 576, 64, "01");
    }


    public static void computeWithJavaSec(final String theMessage) throws NoSuchAlgorithmException {
        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update((byteMessage));
        byte[] byteData = md.digest();
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println("Java.sec text hash: " + sb.toString());
    }

    public static void computeFileWithJavaSec(final String fileLoc) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        FileInputStream fis = new FileInputStream(fileLoc);
        byte[] dataBytes = new byte[1024];
        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println("Java.sec file hash: " + sb.toString());
    }

    /**
     *
     * @param fileLoc
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String convertFileToString(final String fileLoc) throws IOException {
        Path path = Paths.get(fileLoc);
        byte[] data = Files.readAllBytes(path);
        return getHexStringByByteArray(data);
    }

    /**
     * Convert the byte array to a hex-string.
     *
     * @param array  byte array
     * @return  hex string
     */
    public static String getHexStringByByteArray(byte[] array) {
        if (array == null)
            return null;

        StringBuilder stringBuilder = new StringBuilder(array.length * 2);
        @SuppressWarnings("resource")
        Formatter formatter = new Formatter(stringBuilder);
        for (byte tempByte : array)
            formatter.format("%02x", tempByte);

        return stringBuilder.toString();
    }


}
