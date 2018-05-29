package Controller;

import Model.CurvePoint;
import Model.ECDHIESKeyPair;
import Model.SHAKE;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Kevin Ravana
 * @version Spring 2017
 */
public class ECCController {

    private final BigInteger p = BigInteger.valueOf((long)Math.pow(2, 521));

    private static ECDHIESKeyPair ECKeyPair;


    public ECCController() {

    }

    public static String encrypt(String fileLocation, String publicKeyLocation) throws IOException {

        Path filePath = Paths.get(fileLocation);
        File file = new File(fileLocation);
        String fileName = file.getName();
        byte[] byteMessage = Files.readAllBytes(filePath);
        FileInputStream fis = new FileInputStream(publicKeyLocation);
        ObjectInputStream ois = new ObjectInputStream(fis);

        try (FileOutputStream fos = new FileOutputStream(fileName + ".ECC_cryptogram")) {
            CurvePoint publicKey = (CurvePoint) ois.readObject();
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(encryptECDHIES(byteMessage, publicKey));
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("File Encrypted: " + bytesToHex(byteMessage));
        return bytesToHex(byteMessage);
    }

    private static Cryptogram encryptECDHIES(byte[] m, CurvePoint V) {
        // Get k: z = Random(512), k = 4z
        SecureRandom random = new SecureRandom();
        byte[] z = new byte[64];
        random.nextBytes(z);
        BigInteger k = new BigInteger(z).multiply(BigInteger.valueOf(4));

        // Get W
        CurvePoint W = CurvePoint.getECExponentiation(k, V);

        // Get Z
        CurvePoint Z = CurvePoint.getECExponentiation(k, CurvePoint.generateBasePoint());

        // Get (ke || ka)
        byte[] keka = SHAKE.KMACXOF256(W.getX().toByteArray(), "".getBytes(), 1024, "P".getBytes());

        // Get c
        byte[] kmacForC = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 0, 32), "".getBytes(), m.length * 8, "PKE".getBytes());
        byte[] c = new byte[m.length];
        for (int i = 0; i < m.length; i++) {
            c[i] = (byte) (m[i] ^ kmacForC[i]);
        }

        // Get t
        byte[] t = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 32, 64), m, 512, "PKA".getBytes());

        Cryptogram cryptogram = new Cryptogram(Z, c, t);

        return cryptogram;
    }


    public void generateECDHIESKeyPair(String password) {
        byte[] s1 = SHAKE.KMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
        BigInteger s = new BigInteger(s1).multiply(BigInteger.valueOf(4));
        CurvePoint V = CurvePoint.getECExponentiation(s, CurvePoint.generateBasePoint());
        try {
            FileOutputStream fos = new FileOutputStream("public_key." + password);
            ObjectOutputStream oos = new ObjectOutputStream(fos);oos.writeObject(V);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ECKeyPair = new ECDHIESKeyPair(s, V);
    }

    public String ECDHIESPrivateKeyToString() {
        if (ECKeyPair != null) {
            return ECKeyPair.privateKeyToString();
        } else {
            return "NULL";
        }
    }

    public String ECDHIESPublicKeyToString() {
        if (ECKeyPair != null) {
            return ECKeyPair.publicKeyToString();
        } else {
            return "NULL";
        }
    }

    public String ECDHIESPublicKeyXToString() {
        if (ECKeyPair != null) {
            return ECKeyPair.publicKeyXToString();
        } else {
            return "NULL";
        }
    }
    public String ECDHIESPublicKeyYToString() {
        if (ECKeyPair != null) {
            return ECKeyPair.publicKeyYToString();
        } else {
            return "NULL";
        }
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

    private static class Cryptogram {
        private CurvePoint Z;
        private byte[] c;
        private byte[] t;

        public Cryptogram(CurvePoint theZ, byte[] theC, byte[] theT) {
            Z = theZ;
            c = theC;
            t = theT;
        }

        public CurvePoint getZ() {
            return Z;
        }

        public byte[] getC() {
            return c;
        }

        public byte[] getT() {
            return t;
        }
    }

}
