package Main;

import RSA.MultipowerRSA;
import RSA.MultiprimeRSA;
import RSA.RSA;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.Validate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Main {
    private static final int NUMBER_REPETITIONS = 1000;

    public static void main(String[] args) throws IOException {
        System.out.print("Compare multiprime or multipower RSA versions: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String type = br.readLine().trim().toLowerCase();

        switch (type) {
            case "multipower":
                System.out.println("Comparing multipower RSA versions.");
                compare(new MultipowerRSA());
                break;

            case "multiprime":
                System.out.println("Comparing multiprime RSA versions.");
                compare(new MultiprimeRSA());
                break;

            default:
                System.out.println("Invalid RSA type.");
        }
    }

    private static void compare(RSA RSAInstance) {
        BigInteger plaintext = new BigInteger("1234567890");
        BigInteger encrypted = RSAInstance.encrypt(plaintext);
        System.out.println("Encrypted: " + encrypted);
        BigInteger decrypted;

        Stopwatch watch = Stopwatch.createStarted();
        for (int i = 0; i < NUMBER_REPETITIONS; ++i) {
            decrypted = RSAInstance.decryptStandard(encrypted);
            Validate.isTrue(decrypted.equals(plaintext), "Did not decrypt correctly plaintext.");
        }
        watch.stop();
        System.out.println("Standard decryption time for " + NUMBER_REPETITIONS + " decriptions: " + watch);

        watch = Stopwatch.createStarted();
        for (int i = 0; i < NUMBER_REPETITIONS; ++i) {
            decrypted = RSAInstance.decryptEfficiently(encrypted);
            Validate.isTrue(decrypted.equals(plaintext), "Did not decrypt correctly plaintext.");
        }
        watch.stop();
        System.out.println("Efficient decryption time for " + NUMBER_REPETITIONS + " decriptions: " + watch);
    }
}
