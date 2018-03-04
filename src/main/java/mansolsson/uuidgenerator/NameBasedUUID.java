package mansolsson.uuidgenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

/**
 * Used to generate version 3 and 5 name-based {@link UUID}s
 * by following the instructions in RFC 4122.
 */
public final class NameBasedUUID {
    private NameBasedUUID() {
    }

    /**
     * Generate a name-based version 3 {@link UUID} from the provided namespace and name
     *
     * @param namespace The namespace used to generate the {@link UUID}
     * @param name      The name used to generate the {@link UUID}
     * @return a version 3 {@link UUID}
     */
    public static UUID version3(UUID namespace, byte[] name) {
        return generateNameBasedUUID(namespace, name, "MD5", 3);
    }

    /**
     * Generate a name-based version 5 {@link UUID} from the provided namespace and name
     *
     * @param namespace The namespace used to generate the {@link UUID}
     * @param name      The name used to generate the {@link UUID}
     * @return a version 5 {@link UUID}
     */
    public static UUID version5(UUID namespace, byte[] name) {
        return generateNameBasedUUID(namespace, name, "SHA-1", 5);
    }

    private static UUID generateNameBasedUUID(UUID namespace, byte[] name, String algorithm, int version) {
        Objects.requireNonNull(namespace, "Namespace is required");
        Objects.requireNonNull(name, "Name is required");
        byte[] hash = generateHash(namespace, name, algorithm);
        setVersion(hash, version);
        setVariant(hash);
        return fromBytes(hash);
    }

    private static byte[] generateHash(UUID namespace, byte[] name, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(getBytes(namespace));
            messageDigest.update(name);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to get MessageDigest instance for algorithm: " + algorithm, e);
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

    private static void setVersion(byte[] bytes, int version) {
        bytes[6] = (byte) (bytes[6] & 0b0000_1111);
        bytes[6] = (byte) (bytes[6] | (version << 4));
    }

    private static void setVariant(byte[] bytes) {
        bytes[8] = (byte) (bytes[8] & 0b0011_1111);
        bytes[8] = (byte) (bytes[8] | 0b1000_0000);
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
