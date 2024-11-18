package lorgar.avrelian.examples.security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) {
//        exampleBCryptPasswordEncoder();
//        examplePbkdf2PasswordEncoder();
//        exampleSCryptPasswordEncoder();
        exampleArgon2PasswordEncoder();
    }

    private static void exampleBCryptPasswordEncoder() {
        String password = "password";
        int strength = 10;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
        System.out.println(passwordEncoder.encode(password));
    }

    private static void examplePbkdf2PasswordEncoder() {
        String password = "password";
        String pepper = "pepper"; // secret key used by password encoding
        int iterations = 100;  // number of hash iteration
        int hashWidth = 256;      // hash width in bits
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder(pepper, iterations, hashWidth, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA1);
        System.out.println(passwordEncoder.encode(password));
    }

    private static void exampleSCryptPasswordEncoder() {
        String password = "password";
        int cpuCost = 16;         // factor to increase CPU costs
        int memoryCost = 8;      // increases memory usage
        int parallelization = 1; // currently not supported by Spring Security
        int keyLength = 32;      // key length in bytes
        int saltLength = 64;     // salt length in bytes
        PasswordEncoder passwordEncoder = new SCryptPasswordEncoder(
                cpuCost,
                memoryCost,
                parallelization,
                keyLength,
                saltLength);
        System.out.println(passwordEncoder.encode(password));
    }

    private static void exampleArgon2PasswordEncoder() {
        String password = "password";
        int saltLength = 16; // salt length in bytes
        int hashLength = 32; // hash length in bytes
        int parallelism = 1; // currently not supported by Spring Security
        int memory = 4096;   // memory costs
        int iterations = 3;
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(
                saltLength,
                hashLength,
                parallelism,
                memory,
                iterations);
        System.out.println(passwordEncoder.encode(password));
    }
}
