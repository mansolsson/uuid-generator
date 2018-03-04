package mansolsson.uuidgenerator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestNameBasedUUID {
    private static final UUID NAMESPACE_1 = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    private static final UUID NAMESPACE_2 = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");
    private static final byte[] NAME_1 = "test1".getBytes(StandardCharsets.UTF_8);
    private static final byte[] NAME_2 = "test2".getBytes(StandardCharsets.UTF_8);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testThatVariantIsCorrect() {
        UUID version3 = NameBasedUUID.version3(NAMESPACE_1, NAME_1);
        assertEquals(2, version3.variant());

        UUID version5 = NameBasedUUID.version5(NAMESPACE_1, NAME_1);
        assertEquals(2, version5.variant());
    }

    @Test
    public void testThatVersionIsCorrect() {
        UUID version3 = NameBasedUUID.version3(NAMESPACE_1, NAME_1);
        assertEquals(3, version3.version());

        UUID version5 = NameBasedUUID.version5(NAMESPACE_1, NAME_1);
        assertEquals(5, version5.version());
    }

    @Test
    public void testThatVersion3CanBeGeneratedCorrectly() {
        assertEquals(UUID.fromString("c501822b-22a8-37ff-91a9-9545f4689a3d"), NameBasedUUID.version3(NAMESPACE_1, NAME_1));
        assertEquals(UUID.fromString("f1917643-06b2-3e6d-ab77-0a5044067d0a"), NameBasedUUID.version3(NAMESPACE_1, NAME_2));
        assertEquals(UUID.fromString("665d82d4-3cd1-3d56-afa0-9582dc93bbab"), NameBasedUUID.version3(NAMESPACE_2, NAME_1));
        assertEquals(UUID.fromString("a6893162-449e-357d-87c0-301a575e15e7"), NameBasedUUID.version3(NAMESPACE_2, NAME_2));
    }

    @Test
    public void testThatVersion3ThrowsIfNamespaceIsNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Namespace is required");
        NameBasedUUID.version3(null, NAME_1);
    }

    @Test
    public void testThatVersion3ThrowsIfNameIsNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Name is required");
        NameBasedUUID.version3(NAMESPACE_1, null);
    }

    @Test
    public void testThatVersion5CanBeGeneratedCorrectly() {
        assertEquals(UUID.fromString("86e3aed3-1553-5d23-8d61-2286215e65f1"), NameBasedUUID.version5(NAMESPACE_1, NAME_1));
        assertEquals(UUID.fromString("6eabff02-c968-5cbc-bc7f-3b672928a761"), NameBasedUUID.version5(NAMESPACE_1, NAME_2));
        assertEquals(UUID.fromString("cce3037d-a400-5aae-83f7-2296e0dfc0ed"), NameBasedUUID.version5(NAMESPACE_2, NAME_1));
        assertEquals(UUID.fromString("ecfec8c6-07dd-5179-941f-c0c809a06a61"), NameBasedUUID.version5(NAMESPACE_2, NAME_2));
    }

    @Test
    public void testThatVersion5ThrowsIfNamespaceIsNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Namespace is required");
        NameBasedUUID.version5(null, NAME_1);
    }

    @Test
    public void testThatVersion5ThrowsIfNameIsNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Name is required");
        NameBasedUUID.version5(NAMESPACE_1, null);
    }
}
