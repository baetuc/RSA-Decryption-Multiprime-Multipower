package RSA;

import Congruence.Model.CongruenceEquation;
import Congruence.Model.CongruenceSystem;
import Congruence.Solver.CongruenceSystemSolver;
import Congruence.Solver.GarnerSolver;

import java.math.BigInteger;
import java.util.Random;

public class MultiprimeRSA extends RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger r;

    public MultiprimeRSA() {
        generatePrimes();
        generateKeys();
        n = p.multiply(q).multiply(r);

        System.out.println("Generated e: " + e.toString());
        System.out.println("Generated d: " + d.toString());
    }

    @Override
    public BigInteger decryptEfficiently(BigInteger cryptotext) {
        // x_p = y ^ {d mod (p-1)} mod p. Symmetric for x_q and x_r.
        BigInteger xp = cryptotext.mod(p).modPow(d.mod(p.subtract(ONE)), p);
        BigInteger xq = cryptotext.mod(q).modPow(d.mod(q.subtract(ONE)), q);
        BigInteger xr = cryptotext.mod(r).modPow(d.mod(r.subtract(ONE)), r);

        CongruenceSystem system = new CongruenceSystem();
        system.addEquation(xp, p);
        system.addEquation(xq, q);
        system.addEquation(xr, r);

        CongruenceSystemSolver solver = new GarnerSolver();
        return solver.solve(system);
    }

    private void generatePrimes() {
        Random random = new Random();
        p = BigInteger.probablePrime(BIT_LENGTH, random);
        do {
            q = BigInteger.probablePrime(BIT_LENGTH, random);
        } while (q.equals(p));

        do {
            r = BigInteger.probablePrime(BIT_LENGTH, random);
        } while (r.equals(p) || r.equals(q));
    }

    private void generateKeys() {
        BigInteger phi = p.subtract(ONE).multiply(q.subtract(ONE)).multiply(r.subtract(ONE));
        generateKeysUsingPhi(phi);
    }
}
