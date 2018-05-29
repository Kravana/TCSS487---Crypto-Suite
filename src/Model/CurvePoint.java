package Model;

import java.math.BigInteger;

/**
 * Created by Kevin on 5/28/2018.
 */
public class CurvePoint {


    private static final BigInteger p = BigInteger.valueOf((long)Math.pow(2, 521)).subtract(BigInteger.valueOf(1));

    private final long d = -376014;



    private BigInteger x;

    private BigInteger y;





    public CurvePoint(BigInteger theX, BigInteger theY) {
        x = theX;
        y = theY;
    }

    public CurvePoint(BigInteger theX, boolean theYLSB) {
        x = theX;
        y = sqrt(computeRadicand(theX), p, theYLSB);
    }

    public static CurvePoint getBasePoint() {
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
     * Compares two CurvePoints for equality.
     *
     * @param secondPoint Point to compare with this
     * @return True if equal
     */
    public boolean equals(CurvePoint secondPoint) {
        return this.getX().equals(secondPoint.getX()) && this.getY().equals(secondPoint.getY());
    }

    /**
     *
     * @return The inverse of this.
     */
    public CurvePoint getInverse() {
        return new CurvePoint(BigInteger.valueOf(-1).multiply(this.getX()), this.getY());
    }

    /**
     * A point composition operation that yields another CurvePoint.
     *
     * @param theSecondPoint Point to sum with this
     * @return New point x3
     */
    public CurvePoint computePointSum(CurvePoint theSecondPoint) {
        BigInteger x1 = this.getX();
        BigInteger y1 = this.getY();
        BigInteger x2 = theSecondPoint.getX();
        BigInteger y2 = theSecondPoint.getY();

//        BigInteger x3 =

        return null;
    }

    /**
     * Computes the radicand v for the sqrt() method.
     *
     * @param x The x value of a CurvePoint
     * @return The radicand v for the sqrt fucntion
     */
    private static BigInteger computeRadicand(BigInteger x) {
        BigInteger numerator = BigInteger.valueOf(1).subtract(x.pow(2));
        BigInteger denominator = BigInteger.valueOf(1).add(BigInteger.valueOf(376014).multiply(x.pow(2))).modInverse(p);
        return numerator.multiply(denominator);
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

}
