package cryptolib.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static cryptolib.common.AbstractNonInstantiableClass.NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION;
import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void constantsInstantiation_KO() {

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {

            Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();

        });

        assertEquals(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION, exception.getTargetException().getMessage());
    }

}