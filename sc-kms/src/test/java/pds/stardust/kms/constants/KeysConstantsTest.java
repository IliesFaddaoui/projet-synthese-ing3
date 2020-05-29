package pds.stardust.kms.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;
import static pds.stardust.kms.common.AbstractNonInstantiableClass.NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION;

class KeysConstantsTest {

    @Test
    void keysConstantsInstantiation_KO() {

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {

            Constructor<KeysConstants> constructor = KeysConstants.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();

        });

        assertEquals(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION, exception.getTargetException().getMessage());
    }

}