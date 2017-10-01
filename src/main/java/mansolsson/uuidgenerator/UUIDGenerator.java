package mansolsson.uuidgenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public final class UUIDGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private UUIDGenerator() {
    }

    public static UUID generateType3(UUID namespace, String name) {
        return generateNameBasedUUID(namespace, name, "MD5", 3);
    }

    public static UUID generateType4() {
        byte[] randomBytes = new byte[16];
        SECURE_RANDOM.nextBytes(randomBytes);
        return generateUUID(randomBytes, 4);
    }

    public static UUID generateType5(UUID namespace, String name) {
        return generateNameBasedUUID(namespace, name, "SHA-1", 5);
    }

    private static UUID generateNameBasedUUID(UUID namespace, String name, String algorithm, int version) {
        byte[] hash = generateHash(namespace, name, algorithm);
        return generateUUID(hash, version);
    }

    private static UUID generateUUID(byte[] bytes, int version) {
        setVersion(bytes, version);
        setVariant(bytes);
        return fromBytes(bytes);
    }

    private static void setVersion(byte[] bytes, int version) {
        bytes[6] = (byte) (bytes[6] & 0b0000_1111);
        bytes[6] = (byte) (bytes[6] | (version << 4));
    }

    private static void setVariant(byte[] bytes) {
        bytes[8] = (byte) (bytes[8] & 0b0011_1111);
        bytes[8] = (byte) (bytes[8] | 0b1000_0000);
    }

    private static byte[] generateHash(UUID namespace, String name, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(getBytes(namespace));
            messageDigest.update(name.getBytes(StandardCharsets.UTF_8));
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getBytes(UUID namespace) {
        byte[] bytes = new byte[16];

        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (namespace.getMostSignificantBits() >> ((7L - i) * 8L));
        }

        for (int i = 0; i < 8; i++) {
            bytes[i + 8] = (byte) (namespace.getLeastSignificantBits() >> ((7L - i) * 8L));
        }

        return bytes;
    }

    private static UUID fromBytes(byte[] bytes) {
        long mostSignificantBytes = 0;
        for (int i = 0; i < 8; i++) {
            mostSignificantBytes = mostSignificantBytes << 8 | (long) bytes[i] & 0xFF;
        }

        long leastSignificantBytes = 0;
        for (int i = 0; i < 8; i++) {
            leastSignificantBytes = leastSignificantBytes << 8 | (long) bytes[i + 8] & 0xFF;
        }

        return new UUID(mostSignificantBytes, leastSignificantBytes);
    }
}
