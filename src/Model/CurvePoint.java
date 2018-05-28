package Model;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Created by Kevin on 5/28/2018.
 */
public class CurvePoint {


    private final BigInteger p = BigInteger.valueOf((long)Math.pow(2, 521));

    private final long d = -376014;



    private BigInteger x;

    private BigInteger y;





    public CurvePoint(BigInteger theX, BigInteger theY) {
        x = theX;
        y = theY;
    }

    public CurvePoint(BigInteger theX, boolean theYLSB) {

    }

    public CurvePoint generateBasePoint() {
        return new CurvePoint(BigInteger.valueOf(18), false);
    }

    public CurvePoint getNeutralElement() {
        return new CurvePoint(BigInteger.ZERO, BigInteger.ONE);
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public boolean equals(CurvePoint a, CurvePoint b) {
        return Objects.equals(a.getX(), b.getX()) && Objects.equals(a.getY(), b.getY());
    }

    public CurvePoint getInverse() {
        return new CurvePoint(BigInteger.valueOf(-1).multiply(this.getX()), this.getY());
    }

    public CurvePoint computePointSum(CurvePoint theSecondPoint) {
        BigInteger x1 = this.getX();
        BigInteger y1 = this.getY();
        BigInteger x2 = theSecondPoint.getX();
        BigInteger y2 = theSecondPoint.getY();



        return null;
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
    public static BigInteger sqrt(BigInteger v, BigInteger p, boolean lsb) {
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
