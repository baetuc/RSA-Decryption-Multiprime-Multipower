package RSA;

import java.math.BigInteger;

public abstract class RSA {
    protected static final int BIT_LENGTH = 512;
    protected static final BigInteger ZERO = BigInteger.ZERO;
    protected static final BigInteger ONE = BigInteger.ONE;
    protected static final BigInteger TWO = new BigInteger("2");

    protected BigInteger n;
    protected BigInteger d;
    protected BigInteger e;

    public abstract BigInteger decryptEfficiently(BigInteger cryptotext);

    public BigInteger encrypt(BigInteger plaintext) {
        return plaintext.modPow(e, n);
    }

    public BigInteger decryptStandard(BigInteger cryptotext) {
        return cryptotext.modPow(d, n);
    }

    /**
     * Method that generates d and e using precalculated phi value. First we try to see if e = 41 is valid.
     * If not, try with e = 257. If not, try with e = 65537(2 ^ 16 + 1). Then, if all those values were not
     * correct, try every possibility after 65537, with a period of 2 (65539, 65541, ...).
     *
     * @param phi Euler's totient function applied to n
     */
    protected void generateKeysUsingPhi(BigInteger phi) {
        if (!phi.mod(new BigInteger("41")).equals(ZERO)) {
            e = new BigInteger("41"); // 41 is prime
        } else if (!phi.mod(new BigInteger("257")).equals(ZERO)) {
            e = new BigInteger("257"); // 257 is prime
        } else {
            e = new BigInteger("65537");
            while (!phi.gcd(e).equals(ONE)) {
                e = e.add(TWO);
            }
        }

        try {
            d = e.modInverse(phi);
        }catch (Exception e) {
            System.out.println("Da");
        }
    }

}
