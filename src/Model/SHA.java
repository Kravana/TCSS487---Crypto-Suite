package Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;


/**
 * @author Kevin Ravana
 * @version Winter 2018
 */
public class SHA {


    /**
     * SHA3-512
     *
     * @param theMessage
     * @return
     */
    public static String getSHA3512Hash(final String theMessage) {

        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        String s = Hex.toHexString(byteMessage);
        Keccak keccak = new Keccak(1600);
        return keccak.getHash(s, 576, 64, "06");

    }

    /**
     *
     *
     * @param theMessage
     */
    public static void computeHashWithBC(final String theMessage) {
        byte[] byteMessage = (theMessage != null) ? theMessage.getBytes(): null;
        final SHA3.DigestSHA3 md = new SHA3.DigestSHA3(512);
        byte[] digest = md.digest(byteMessage);
        System.out.println("Text to BC SHA-3-512 = " + Hex.toHexString(digest));
    }

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
     * @param fileLoc
     * @return
     * @throws IOException
     */
    public static String convertFileToHex(final String fileLoc) throws IOException {
        Path path = Paths.get(fileLoc);
        byte[] data = Files.readAllBytes(path);
        return Hex.toHexString(data);
    }



}
