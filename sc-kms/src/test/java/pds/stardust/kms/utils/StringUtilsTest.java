package pds.stardust.kms.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.Security;

import static org.junit.jupiter.api.Assertions.*;
import static pds.stardust.kms.common.AbstractNonInstantiableClass.NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION;

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
    void httpUtilsInstantiation_KO() {

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {

            Constructor<StringUtils> constructor = StringUtils.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();

        });

        assertEquals(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION, exception.getTargetException().getMessage());
    }

    @Test
    void stringToBase64() {
        assertEquals(encodedText, StringUtils.stringToBase64(plainText));
    }

    @Test
    void base64ToString() {
        assertEquals(plainText, StringUtils.base64ToString(encodedText));
    }
}