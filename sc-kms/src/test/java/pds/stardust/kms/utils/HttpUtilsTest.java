package pds.stardust.kms.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static pds.stardust.kms.common.AbstractNonInstantiableClass.NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION;

class HttpUtilsTest {

    private final String VAULT_TOKEN = "00000000-0000-0000-0000-000000000000";
    private final String BEARER_TOKEN = "Bearer 00000000-0000-0000-0000-000000000000";
    private final String APPLICATION_JSON = "application/json";

    @Test
    void httpUtilsInstantiation_KO() {

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {

            Constructor<HttpUtils> constructor = HttpUtils.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();

        });

        assertEquals(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION, exception.getTargetException().getMessage());
    }

    @Test
    void buildHttpHeaders() {

        final HttpHeaders httpHeaders = HttpUtils.buildHttpHeaders(VAULT_TOKEN);

        assertNotNull(httpHeaders);
        assertEquals(2, httpHeaders.size());
        assertEquals(APPLICATION_JSON, Objects.requireNonNull(httpHeaders.getContentType()).toString());
        assertEquals(BEARER_TOKEN, httpHeaders.getFirst(HttpHeaders.AUTHORIZATION));

    }
}