package com.avale.model;

import java.security.Security;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Represents a configuration for a simple Password Based Encryption ( PBE ).
 */
public class EncryptionSettings {

    private static final String ALGORITHM_PROPERTY = "encryption.settings.algorithm";
    private static final String ITERATIONS_PROPERTY = "encryption.settings.iterations";
    /**
     * The algorithm used to encrypt configuration
     */
    private final String algorithm;
    /**
     * The master password used as algorithm input
     */
    private final String masterPassword;
    /**
     * The number of iterations used to obtain a key
     */
    private final int numberOfIteration;

    EncryptionSettings(final String algorithm, final String masterPassword, final int numberOfIteration) {
        this.algorithm = algorithm;
        this.masterPassword = masterPassword;
        this.numberOfIteration = numberOfIteration;
    }

    EncryptionSettings(Properties properties) {
        this(properties.getProperty(ALGORITHM_PROPERTY), null, Integer.valueOf(properties.getProperty(ITERATIONS_PROPERTY)));
    }

    public String algorithm() {
        return algorithm;
    }

    public String masterPassword() {
        return masterPassword;
    }

    public int getNumberOfIteration() {
        return numberOfIteration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptionSettings that = (EncryptionSettings) o;
        return numberOfIteration == that.numberOfIteration &&
                Objects.equals(algorithm, that.algorithm) &&
                Objects.equals(masterPassword, that.masterPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, masterPassword, numberOfIteration);
    }

    /**
     * @return The password of all password based encryption algorithms.
     */
    public static List<String> availablePasswordBasedEncryptionAlgorithms() {
        return Security.getAlgorithms("Cipher").stream().filter(EncryptionSettings::isPasswordBasedEncryption).sorted().collect(Collectors.toList());
    }

    private static boolean isPasswordBasedEncryption(String algorithmName) {
        return algorithmName.contains("PBE");
    }

}
