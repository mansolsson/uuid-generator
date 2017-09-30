package mansolsson.uuidgenerator;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestUUIDGenerator {
    private static final UUID NAMESPACE_1 = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    private static final UUID NAMESPACE_2 = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");

    @Test
    public void testThatVariantIsCorrect() {
        UUID type3 = UUIDGenerator.generateType3(NAMESPACE_1, "test");
        assertEquals(2, type3.variant());

        UUID type5 = UUIDGenerator.generateType5(NAMESPACE_1, "test");
        assertEquals(2, type5.variant());
    }

    @Test
    public void testThatVersionIsCorrect() {
        UUID type3 = UUIDGenerator.generateType3(NAMESPACE_1, "test");
        assertEquals(3, type3.version());

        UUID type5 = UUIDGenerator.generateType5(NAMESPACE_1, "test");
        assertEquals(5, type5.version());
    }

    @Test
    public void testThatType3CanBeGeneratedCorrectly() {
        assertEquals(UUID.fromString("c501822b-22a8-37ff-91a9-9545f4689a3d"), UUIDGenerator.generateType3(NAMESPACE_1, "test1"));
        assertEquals(UUID.fromString("f1917643-06b2-3e6d-ab77-0a5044067d0a"), UUIDGenerator.generateType3(NAMESPACE_1, "test2"));
        assertEquals(UUID.fromString("665d82d4-3cd1-3d56-afa0-9582dc93bbab"), UUIDGenerator.generateType3(NAMESPACE_2, "test1"));
        assertEquals(UUID.fromString("a6893162-449e-357d-87c0-301a575e15e7"), UUIDGenerator.generateType3(NAMESPACE_2, "test2"));
    }

    @Test
    public void testThatType5CanBeGeneratedCorrectly() {
        assertEquals(UUID.fromString("86e3aed3-1553-5d23-8d61-2286215e65f1"), UUIDGenerator.generateType5(NAMESPACE_1, "test1"));
        assertEquals(UUID.fromString("6eabff02-c968-5cbc-bc7f-3b672928a761"), UUIDGenerator.generateType5(NAMESPACE_1, "test2"));
        assertEquals(UUID.fromString("cce3037d-a400-5aae-83f7-2296e0dfc0ed"), UUIDGenerator.generateType5(NAMESPACE_2, "test1"));
        assertEquals(UUID.fromString("ecfec8c6-07dd-5179-941f-c0c809a06a61"), UUIDGenerator.generateType5(NAMESPACE_2, "test2"));
    }
}
