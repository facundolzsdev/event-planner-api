package com.flzs.event_planner_api.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * DEMONSTRATION CLASS - DO NOT USE IN PRODUCTION
 * <p>
 * Generates BCrypt hashes for passwords, allowing manual creation of an administrator user
 * when the database is empty or there are no users with the ADMIN role.
 * </p>
 * <strong>For educational use only:</strong> This code exists solely for testing purposes
 * in local development environments.
 *
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 */
public final class GenerateAdminHash {

    private GenerateAdminHash() {
        throw new AssertionError("Utility class. Not instantiable.");
    }

    /**
     * Entry point for generating the BCrypt hash.
     *
     * @param args The password to be hashed as the first argument (mandatory).
     */
    public static void main(String[] args) {
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.err.println("ERROR: You must provide a password as an argument.");
            System.err.println("Example: java PasswordHashGenerator \"my-secure-password\"");
            System.exit(1);
        }

        String rawPassword = args[0];
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(rawPassword);

        System.out.println("\n=== GENERATED HASH (BCrypt) ===");
        System.out.println("Original password: " + rawPassword);
        System.out.println("Hash for SQL:      " + hashedPassword);
        System.out.println("=== Instructions ===");
        System.out.println("1. Run this SQL in the database:");
        System.out.printf("INSERT INTO users (username, password, role) VALUES ('admin', '%s', 'ADMIN');%n", hashedPassword);
        System.out.println("2. Delete this class in production environments.\n");
    }
}
