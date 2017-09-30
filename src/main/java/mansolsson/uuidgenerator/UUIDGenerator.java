package mansolsson.uuidgenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public final class UUIDGenerator {
    private UUIDGenerator() {
    }

    public static UUID generateType3(UUID namespace, String name) {
        return generateUUID(namespace, name, "MD5", 3);
    }

    public static UUID generateType5(UUID namespace, String name) {
        return generateUUID(namespace, name, "SHA-1", 5);
    }

    private static UUID generateUUID(UUID namespace, String name, String algorithm, int version) {
        byte[] hash = generateHash(namespace, name, algorithm);

        // Version
        hash[6] = (byte) (hash[6] & 0b0000_1111);
        hash[6] = (byte) (hash[6] | (version << 4));

        // Variant
        hash[8] = (byte) (hash[8] & 0b0011_1111);
        hash[8] = (byte) (hash[8] | 0b1000_0000);

        return fromBytes(hash);
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
