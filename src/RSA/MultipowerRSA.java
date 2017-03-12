package RSA;

import Congruence.Model.CongruenceSystem;
import Congruence.Solver.CongruenceSystemSolver;
import Congruence.Solver.GarnerSolver;

import java.math.BigInteger;
import java.util.Random;

public class MultipowerRSA extends RSA {
    private BigInteger p;
    private BigInteger q;

    public MultipowerRSA() {
        generatePrimes();
        generateKeys();
        n = p.multiply(p).multiply(q);

        System.out.println("Generated e: " + e.toString());
        System.out.println("Generated d: " + d.toString());
    }

    @Override
    public BigInteger decryptEfficiently(BigInteger cryptotext) {
        // x_q = y ^ {d mod (q-1)} mod q. Symmetric for x_0
        BigInteger xq = cryptotext.mod(q).modPow(d.mod(q.subtract(ONE)), q);

        BigInteger x0 = cryptotext.mod(p).modPow(d.mod(p.subtract(ONE)), p);
        BigInteger p2 = p.multiply(p);
        BigInteger yp2 = cryptotext.mod(p2);
        BigInteger E = yp2.subtract(x0.modPow(e, p2)).mod(p2);
        // toInverse = (x0 ^ {e-1} * e) mod p
        BigInteger toInverse = x0.modPow(e.subtract(ONE), p).multiply(e).mod(p);
        BigInteger x1 = E.divide(p).multiply(toInverse.modInverse(p)).mod(p);

        BigInteger xp2 = x0.add(x1.multiply(p));

        CongruenceSystem system = new CongruenceSystem();
        system.addEquation(xq, q);
        system.addEquation(xp2, p2);

        CongruenceSystemSolver solver = new GarnerSolver();
        return solver.solve(system);
    }

    private void generatePrimes() {
        Random random = new Random();
        p = BigInteger.probablePrime(BIT_LENGTH, random);
        do {
            q = BigInteger.probablePrime(BIT_LENGTH, random);
        } while (q.equals(p));
    }

    private void generateKeys() {
        BigInteger phi = p.multiply(p.subtract(ONE)).multiply(q.subtract(ONE));
        generateKeysUsingPhi(phi);
    }
}
