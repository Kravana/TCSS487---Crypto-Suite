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
 * @version Spring 2018
 */
public class ECCController {

    private final BigInteger p = BigInteger.valueOf((long)Math.pow(2, 521));

    private static ECDHIESKeyPair ECKeyPair;

    private static boolean tEqualstPrime;


    public ECCController() {

    }


    public void generateECDHIESKeyPair(String password) {
        byte[] s1 = SHAKE.KMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
        BigInteger s = new BigInteger(s1).multiply(BigInteger.valueOf(4));
        CurvePoint V = CurvePoint.getECExponentiation(s, CurvePoint.generateBasePoint());
        try {
            FileOutputStream fos = new FileOutputStream("public_key." + password);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(V);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ECKeyPair = new ECDHIESKeyPair(s, V);
    }

    public static String encrypt(String fileLocation, String publicKeyLocation, String password) throws IOException {
        Path filePath = Paths.get(fileLocation);
        File file = new File(fileLocation);
        String fileName = file.getName();
        byte[] byteMessage = Files.readAllBytes(filePath);
        FileInputStream fis = new FileInputStream(publicKeyLocation);
        ObjectInputStream ois = new ObjectInputStream(fis);

        byte[] s1 = SHAKE.KMACXOF256(password.getBytes(), "".getBytes(), 512, "K".getBytes());
        BigInteger s = new BigInteger(s1).multiply(BigInteger.valueOf(4));
        CurvePoint V = CurvePoint.getECExponentiation(s, CurvePoint.generateBasePoint());

        try {
            CurvePoint publicKey = (CurvePoint) ois.readObject();
//            System.out.println(publicKey.curveToString());
            FileOutputStream fos = new FileOutputStream(fileName + ".ECC_cryptogram");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(encryptECDHIES(byteMessage, publicKey, s));
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("File Encrypted: " + bytesToHex(byteMessage));
        return bytesToHex(byteMessage);
    }

    public static String decrypt(String fileLocation, String passWord) {
        Cryptogram cryptogram = null;
        String decryptedMessage = "";
        try {
            FileInputStream fis = new FileInputStream(fileLocation);
            ObjectInputStream ois = new ObjectInputStream(fis);
            cryptogram = (Cryptogram) ois.readObject();
            byte[] s1 = SHAKE.KMACXOF256(passWord.getBytes(), "".getBytes(), 512, "K".getBytes());
            BigInteger s = new BigInteger(s1).multiply(BigInteger.valueOf(4));
            decryptedMessage = bytesToHex(decryptECDHIES(cryptogram, s));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (decryptedMessage.isEmpty()) return "Decryption failed";
        if (!tEqualstPrime) return "t != tPrime";
        return decryptedMessage;
    }

    private static Cryptogram encryptECDHIES(byte[] m, CurvePoint V, BigInteger s) {
        // Get k: z = Random(512), k = 4z
        SecureRandom random = new SecureRandom();
        byte[] z = new byte[64];
        random.nextBytes(z);
        BigInteger k = new BigInteger(z).multiply(BigInteger.valueOf(4));

        // Get W
        CurvePoint W = CurvePoint.getECExponentiation(s, V);

        // Get Z
        CurvePoint Z = CurvePoint.getECExponentiation(s, CurvePoint.generateBasePoint());

        // Get (ke || ka)
        byte[] keka = SHAKE.KMACXOF256(W.getX().toByteArray(), "".getBytes(), 1024, "P".getBytes());

        // Get c
        byte[] kmacForC = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 0, 64), "".getBytes(), m.length * 8, "PKE".getBytes());
        byte[] c = new byte[m.length];
        for (int i = 0; i < m.length; i++) {
            c[i] = (byte) (m[i] ^ kmacForC[i]);
        }

        // Get t
        byte[] t = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 64, 128), m, 512, "PKA".getBytes());
        System.out.println(bytesToHex(m));

        return new Cryptogram(Z, c, t);
    }

    private static byte[] decryptECDHIES(Cryptogram cryptogram, BigInteger s) {
        // Get Z, c, & t
        CurvePoint Z = cryptogram.getZ();
        byte[] c = cryptogram.getC();
        byte[] t = cryptogram.getT();

        // Get W
        CurvePoint W = CurvePoint.getECExponentiation(s, Z);

        // Get (ke || ka)
        byte[] keka = SHAKE.KMACXOF256(W.getX().toByteArray(), "".getBytes(), 1024, "P".getBytes());

        // Get m
        byte[] kmacForM = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 0, 64), "".getBytes(), c.length * 8, "PKE".getBytes());
        byte[] m = new byte[c.length];
        for (int i = 0; i < c.length; i++) {
            m[i] = (byte) (c[i] ^ kmacForM[i]);
        }

        // Get t'
        byte[] tPrime = SHAKE.KMACXOF256(Arrays.copyOfRange(keka, 64, 128), m, 512, "PKA".getBytes());

        tEqualstPrime = Arrays.equals(t, tPrime);
        System.out.println(bytesToHex(m));
        System.out.println("t: " + bytesToHex(t) + "\n" + "t': " + bytesToHex(tPrime));

        return m;

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

    private static class Cryptogram implements Serializable {
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
