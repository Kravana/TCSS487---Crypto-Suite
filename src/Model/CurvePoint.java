package Model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Kevin on 5/28/2018.
 */
public class CurvePoint implements Serializable {


    private static final BigInteger p = BigInteger.valueOf(2).pow(521).subtract(BigInteger.ONE);

    private static final long d = -376014;

    private BigInteger x;

    private BigInteger y;


    private CurvePoint(BigInteger theX, BigInteger theY) {
        x = theX.mod(p);
        y = theY.mod(p);
    }

    private CurvePoint(BigInteger theX, boolean theYLSB) {
        x = theX.mod(p);
        y = sqrt(computeRadicand(theX), p, theYLSB);
    }

    public static CurvePoint generateBasePoint() {
        return new CurvePoint(BigInteger.valueOf(18), false);
    }

    public static CurvePoint getNeutralElement() {
        return new CurvePoint(BigInteger.ZERO, BigInteger.ONE);
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }


    /**
     *
     * @return The inverse of this.
     */
    private CurvePoint getInverse() {
        return new CurvePoint(BigInteger.valueOf(-1).multiply(this.getX()), this.getY());
    }

    /**
     * Point dot function: Y = x dot G.
     *
     * @param privateKey x
     * @return Y
     */
    public static CurvePoint getECExponentiation(BigInteger privateKey, CurvePoint G) {
        CurvePoint Y = G;
        String x = privateKey.toString(2);
        for (int i = x.length() - 1; i >= 0; i--) {
            Y = Y.computePointSum(Y);
            if (x.charAt(i) == '1') {
                Y = Y.computePointSum(G);
            }
        }
        return Y;
    }

    /**
     * A point composition operation that yields another CurvePoint.
     * Formula works for points on Edwards curves.
     *
     * @param theSecondPoint Point to sum with this
     * @return New point x3
     */
    private CurvePoint computePointSum(CurvePoint theSecondPoint) {
        BigInteger x1 = this.getX();
        BigInteger y1 = this.getY();
        BigInteger x2 = theSecondPoint.getX();
        BigInteger y2 = theSecondPoint.getY();

        BigInteger x3Numerator = x1.multiply(y2).add(y1.multiply(x2));
        BigInteger x3Denominator = BigInteger.ONE.add(BigInteger.valueOf(d).
                multiply(x1.
                        multiply(x2.
                                multiply(y1.
                                        multiply(y2)))));
        BigInteger x3 = x3Numerator.multiply(x3Denominator.modInverse(p));

        BigInteger y3Numerator = y1.multiply(y2).subtract(x1.multiply(x2));
        BigInteger y3Denominator = BigInteger.ONE.subtract(BigInteger.valueOf(d).
                multiply(x1.
                        multiply(x2.
                                multiply(y1.
                                        multiply(y2)))));
        BigInteger y3 = y3Numerator.multiply(y3Denominator.modInverse(p));
//        System.out.println(x3 + ", " + y3);
        return new CurvePoint(x3, y3);
    }

    /**
     * Computes the radicand v for the sqrt() method.
     *
     * @param x The x value of a CurvePoint
     * @return The radicand v for the sqrt fucntion
     */
    private static BigInteger computeRadicand(BigInteger x) {
        BigInteger numerator = BigInteger.valueOf(1).subtract(x.pow(2)).mod(p);
        BigInteger denominator = BigInteger.valueOf(1).add(BigInteger.valueOf(376014).multiply(x.pow(2))).modInverse(p);
        return numerator.divide(denominator);
    }

    /**
     * Compute a square root of v mod p with a specified
     * least significant bit, if such a root exists.
     *
     * @author Paulo S. L. M. Barreto
     * @param v the radicand.
     * @param p the modulus (must satisfy p mod 4 = 3).
     * @param lsb desired least significant bit (true: 1, false: 0).
     * @return a square root r of v mod p with r mod 2 = 1 iff lsb = true
     * if such a root exists, otherwise null.
     */
    private static BigInteger sqrt(BigInteger v, BigInteger p, boolean lsb) {
        assert (p.testBit(0) && p.testBit(1)); // p = 3 (mod 4)
        if (v.signum() == 0) {
            return BigInteger.ZERO;
        }
        BigInteger r = v.modPow(p.shiftRight(2).add(BigInteger.ONE), p);
        if (r.testBit(0) != lsb) {
            r = p.subtract(r); // correct the lsb
        }
        return (r.multiply(r).subtract(v).mod(p).signum() == 0) ? r : null;
    }


    /**
     * Compares two CurvePoints for equality.
     *
     * @param secondPoint Point to compare with this
     * @return True if equal
     */
    public boolean equals(CurvePoint secondPoint) {
        return this.getX().equals(secondPoint.getX()) && this.getY().equals(secondPoint.getY());
    }

    public CurvePoint copy(CurvePoint original) {
        return new CurvePoint(original.getX(), original.getY());
    }

//    public String curveToString() {
//        return "x: " + x + "\n" + "y: " + y;
//    }

}
