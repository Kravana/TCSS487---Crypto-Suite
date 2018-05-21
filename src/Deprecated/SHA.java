package Deprecated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Deprecated.Keccak;
import Model.SHAKE;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;


/**
 *
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class SHA {


    /**
     * Returns SHA-3-512 hash of theMessage.
     *
     * @param theMessage message to be hashed
     * @return Hash of theMessage as a hex String
     */
    public static String getSHA3512Hash(final String theMessage) {
        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        String s = Hex.toHexString(byteMessage);
        Keccak keccak = new Keccak(1600);
        return keccak.getHash(s, 576, 64, "06");
    }

    public static String getSHAKE256Hash(final String theMessage) {
        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        String s = Hex.toHexString(byteMessage);
        Keccak keccak = new Keccak(1600);
        return keccak.getHash(s, 1088, 64, "1F");
    }

    public static String getKeccak512Hash(final String theMessage) {
        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        String s = Hex.toHexString(byteMessage);
        Keccak keccak = new Keccak(1600);
        return keccak.getHash(s, 576, 64, "01");
    }

    public static String TextKMACXOF256(String K, String X, int L, String S) {
        byte[] byteMessage = (X != null) ? X.getBytes(): null;
        byte[] byteString = (S != null) ? S.getBytes(): null;
        byte[] blankKey = new byte[0];

        byte[] hash = SHAKE.KMACXOF256(blankKey, byteMessage, L, byteString);

        return Hex.toHexString(hash);

    }



    /**
     * Computes and prints SHA-3-512 hash of theMessage
     * using Bouncy Castle API.
     *
     * @param theMessage message to be hashed
     */
    public static void computeHashWithBC(final String theMessage) {
        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        final SHA3.DigestSHA3 md = new SHA3.DigestSHA3(512);
        byte[] digest = md.digest(byteMessage);
        System.out.println("Text to BC SHA-3-512 = " + Hex.toHexString(digest));
    }

    /**
     * Computes and prints SHA-3-512 hash of file at
     * fileLoc using Bouncy Castle API.
     *
     * @param fileLoc location of file to be hashed
     * @throws IOException File not found
     */
    public static void computeFileHashWithBC(final String fileLoc) throws IOException {
        final SHA3.DigestSHA3 md = new SHA3.DigestSHA3(512);
        Path path = Paths.get(fileLoc);
        byte[] byteFile = Files.readAllBytes(path);
        byte[] digest = md.digest(byteFile);
        System.out.println("File to BC SHA-3-512 = " + Hex.toHexString(digest));
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
        return Hex.toHexString(data);
    }



}
