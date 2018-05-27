package Controller;

import Model.SHAKE;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class ToolController {

    private static boolean tEqualstPrime;

    ToolController() {

    }

    public static String getKMACXOF256HashString(String K, String X, int L, String S) {
        byte[] byteKey = (K != null) ? K.getBytes(): new byte[0];
        byte[] byteMessage = (X != null) ? X.getBytes(): new byte[0];
        byte[] byteString = (S != null) ? S.getBytes(): new byte[0];

        byte[] hash = SHAKE.KMACXOF256(byteKey, byteMessage, L, byteString);

        return Hex.toHexString(hash);
    }

    public static String encrypt(String fileLocation, String passWord) throws IOException {

        Path path = Paths.get(fileLocation);
        byte[] byteMessage = Files.readAllBytes(path);
        byte[] byteKey = (passWord != null) ? passWord.getBytes(): new byte[0];
        try (FileOutputStream fos = new FileOutputStream("encrypted_file")) {
            fos.write(encryptKMACXOF256(byteMessage, byteKey));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File Encrypted: " + Hex.toHexString(byteMessage));
        return Hex.toHexString(byteMessage);
    }

    public static String decrypt(String fileLocation, String passWord) throws IOException {
        Path path = Paths.get(fileLocation);
        byte[] byteCryptogram = Files.readAllBytes(path);
        byte[] byteKey = (passWord != null) ? passWord.getBytes(): new byte[0];
        String decryptedMessage = Hex.toHexString(decryptKMACXOF256(byteCryptogram, byteKey));

        if (tEqualstPrime) {
            System.out.println("File Decrypted: " + decryptedMessage);
        } else {
            System.out.println("Failure to authenticate. Decrypted message: " + decryptedMessage);
        }

        return decryptedMessage;
    }

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

    private static byte[] decryptKMACXOF256(byte[] cryptogram, byte[] pw) throws IOException {
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
        System.out.println(Hex.toHexString(data));
        return new String(data, "UTF-8");
    }

}
