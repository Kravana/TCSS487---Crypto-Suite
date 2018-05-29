package Controller;

import Model.SHAKE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Provides access to various cryptographic instruments for learning purposes.
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class KMACController {

    private static boolean tEqualstPrime;


    KMACController() {

    }

    public static String getKMACXOF256HashHexString(String K, String X, int L, String S) {
        byte[] byteKey = (K != null) ? K.getBytes(): new byte[0];
        byte[] byteMessage = (X != null) ? X.getBytes(): new byte[0];
        byte[] byteString = (S != null) ? S.getBytes(): new byte[0];

        byte[] hash = SHAKE.KMACXOF256(byteKey, byteMessage, L, byteString);

        return bytesToHex(hash);
    }

    public static String encrypt(String fileLocation, String passWord) throws IOException {

        Path path = Paths.get(fileLocation);
        File file = new File(fileLocation);
        String fn = file.getName();
        byte[] byteMessage = Files.readAllBytes(path);
        byte[] byteKey = (passWord != null) ? passWord.getBytes(): new byte[0];
        try (FileOutputStream fos = new FileOutputStream(fn + ".KMAC_cryptogram")) {
            fos.write(encryptKMACXOF256(byteMessage, byteKey));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File Encrypted: " + bytesToHex(byteMessage));
        return bytesToHex(byteMessage);
    }

    public static String decrypt(String fileLocation, String passWord) throws IOException {
        Path path = Paths.get(fileLocation);
        byte[] byteCryptogram = Files.readAllBytes(path);
        byte[] byteKey = (passWord != null) ? passWord.getBytes(): new byte[0];
        String decryptedMessage = bytesToHex(decryptKMACXOF256(byteCryptogram, byteKey));

        if (tEqualstPrime) {
            System.out.println("File Decrypted: " + decryptedMessage);
        } else {
            System.out.println("Failure to authenticate. Decrypted message: " + decryptedMessage);
        }

        return decryptedMessage;
    }

    /**
     *
     * @param m
     * @param pw
     * @return
     * @throws IOException
     */
    private static byte[] encryptKMACXOF256(byte[] m, byte[] pw) throws IOException {
        // Get z = Random(512)
        SecureRandom random = new SecureRandom();
        byte[] z = new byte[64];
        random.nextBytes(z);

        // Get (ke || ka)
        ByteArrayOutputStream oskeka = new ByteArrayOutputStream();
        oskeka.write(z);
        oskeka.write(pw);
        byte[] keka = SHAKE.KMACXOF256(oskeka.toByteArray(), new byte[0], 1024, "S".getBytes());

        // Get c
        byte[] kmacForC = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 0, 32), "".getBytes(), m.length * 8, "SKE".getBytes());
        byte[] c = new byte[m.length];
        for (int i = 0; i < m.length; i++) {
            c[i] = (byte) (m[i] ^ kmacForC[i]);
        }

        // Get t
        byte[] t = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 32, 64), m, 512, "SKA".getBytes());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(z);
        os.write(c);
        os.write(t);

        return os.toByteArray();

    }

    /**
     *
     * @param cryptogram
     * @param pw
     * @return
     * @throws IOException
     */
    private static byte[] decryptKMACXOF256(byte[] cryptogram, byte[] pw) throws IOException {

        // Catch cryptograms that are too short
        if (cryptogram.length < 129) {
            System.out.println("Selected file is incorrect length for cryptogram");
            return new byte[0];
        }

        // Get z
        byte[] z = Arrays.copyOfRange(cryptogram, 0, 64);

        // Get t
        byte[] t = Arrays.copyOfRange(cryptogram, cryptogram.length - 64, cryptogram.length);

        // Get (ke || ka)
        ByteArrayOutputStream oskeka = new ByteArrayOutputStream();
        oskeka.write(z);
        oskeka.write(pw);
        byte[] keka = SHAKE.KMACXOF256(oskeka.toByteArray(), new byte[0], 1024, "S".getBytes());

        // Get c
        byte[] c = Arrays.copyOfRange(cryptogram,64, cryptogram.length - 64);

        // Get m
        byte[] kmacForM = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 0, 32), "".getBytes(), c.length * 8, "SKE".getBytes());
        byte[] m = new byte[c.length];
        for (int i = 0; i < c.length; i++) {
            m[i] = (byte) (c[i] ^ kmacForM[i]);
        }

        // Get t'
        byte[] tPrime = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 32, 64), m, 512, "SKA".getBytes());

        tEqualstPrime = Arrays.equals(t, tPrime);

        return m;

    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Converts a file to corresponding hex string
     *
     * @param fileLoc location of file to be converted
     * @return Hex representation of file
     * @throws IOException File not found
     */
    public static String convertFileToHex(final String fileLoc) throws IOException {
        Path path = Paths.get(fileLoc);
        byte[] data = Files.readAllBytes(path);
//        System.out.println(bytesToHex(data));
        return new String(data, "UTF-8");
    }



}
