package Model;

import org.bouncycastle.util.encoders.Hex;
import java.util.ArrayList;



/**
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class KMACXOF256 {

    private static ArrayList<Integer> BASE_256_ARRAY = new ArrayList<>();

    public KMACXOF256() {

    }

    public String cShake256(final String N,
                            final String S,
                            final int L,
                            final String theMessage) {

        if (N.equals("") && S.equals("")) {
            return SHA.getSHAKE256Hash(theMessage);
        } else {

        }

        return null;
    }

//    public Byte[] bytepad(final String X, final int w) {
//        if (w > 0) {
//
//        }
//
//
//    }
//

    /**
     *
     * @param x
     * @return
     */
    private static Byte[] rightEncode(final int x) {
        int n = 1;

        while (!(Math.pow(2, 8*n) > x)) {
            n = n++;
        }

        Byte[] O = new Byte[n + 1];
        resetBaseArray();
        convertToAnyBase(x, 256);
        for (int i = 0; i < n; i++) {
            O[i] = BASE_256_ARRAY.get(i).byteValue();
        }

        O[n] = ((Integer) n).byteValue();

        return O;
    }

    private static Byte[] leftEncode(final int x) {
        int n = 1;

        while (!(Math.pow(2, 8*n) > x)) {
            n = n++;
        }

        Byte[] O = new Byte[n + 1];
        resetBaseArray();
        convertToAnyBase(x, 256);
        for (int i = 1; i <= n; i++) {
            O[i] = BASE_256_ARRAY.get(i).byteValue();
        }

        O[0] = ((Integer) n).byteValue();

        return O;
    }

    private static String convertToAnyBase(final int number, final int base) {
        int quotient = number / base;
        int remainder = number % base;

        if (quotient == 0) {
            BASE_256_ARRAY.add(remainder);
            return Integer.toString(remainder);
        }
        else {
            BASE_256_ARRAY.add(remainder);
            return convertToAnyBase(quotient, base) + Integer.toString(remainder);
        }
    }

    private static void resetBaseArray() {
        BASE_256_ARRAY = new ArrayList<Integer>();
    }

}
