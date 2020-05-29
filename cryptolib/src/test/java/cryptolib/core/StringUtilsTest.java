package cryptolib.core;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.Security;

import static cryptolib.common.AbstractNonInstantiableClass.NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    private static String encodedText;
    private static String plainText;

    @BeforeAll
    static void beforeAll() {
        Security.addProvider(new BouncyCastleProvider());
        plainText = "MESSAGE TO ENCODE";
        encodedText = "TUVTU0FHRSBUTyBFTkNPREU=";
    }

    @Test
    void toBase64_OK() {
        assertArrayEquals(encodedText.getBytes(StandardCharsets.UTF_8), StringUtils.toBase64(plainText));
    }

    @Test
    void toBase64String_OK() {
        assertEquals(encodedText, StringUtils.toBase64String(plainText.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void decodeBase64_OK() {
        assertArrayEquals(plainText.getBytes(StandardCharsets.UTF_8), StringUtils.decodeBase64(encodedText.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void decodeBase64String_OK() {
        assertArrayEquals(plainText.getBytes(StandardCharsets.UTF_8), StringUtils.decodeBase64String(encodedText));
    }

    @Test
    void stringUtilsInstantiation_KO() {

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {

                Constructor<StringUtils> constructor = StringUtils.class.getDeclaredConstructor();
                assertTrue(Modifier.isPrivate(constructor.getModifiers()));
                constructor.setAccessible(true);
                constructor.newInstance();

        });

        assertEquals(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION, exception.getTargetException().getMessage());
    }
}