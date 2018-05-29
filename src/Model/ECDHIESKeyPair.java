package Model;


import java.math.BigInteger;

/**
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class ECDHIESKeyPair {
    BigInteger privateKey;
    CurvePoint publicKey;

    public ECDHIESKeyPair(BigInteger prKey, CurvePoint puKey) {
        privateKey = prKey;
        publicKey = puKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public CurvePoint getPublicKey() {
        return publicKey;
    }

    public String privateKeyToString() {
        return privateKey.toString();
    }

    public String publicKeyToString() {
        String x = publicKey.getX().toString();
        String y = publicKey.getY().toString();
        return "(" + x + ", " + y + ")";
    }

    public String publicKeyXToString() {
        String x = publicKey.getX().toString();
        return x;
    }

    public String publicKeyYToString() {
        String y = publicKey.getY().toString();
        return y;
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
}
