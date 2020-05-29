package pds.stardust.kms.utils;

import org.bouncycastle.util.encoders.Base64;
import pds.stardust.kms.common.AbstractNonInstantiableClass;

import java.nio.charset.StandardCharsets;

/**
 * This class defines the common methods regarding string and base 64 transformation.
 */
public class StringUtils extends AbstractNonInstantiableClass {

    /**
     * This constructor hide the implicit public constructor thus preventing
     * the class from being instantiated.
     */
    private StringUtils() {
        throw new IllegalStateException(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION);
    }

    /**
     * Convert an UTF-8 encoded string to base64 bytes primitive.
     *
     * @param utf8String The string to convert encoded in UTF-8
     * @return The base 64 encoded string
     */
    public static String stringToBase64(String utf8String) {
        return Base64.toBase64String(
                utf8String.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Decode base 64 string.
     *
     * @param base64 The base 64 string to convert
     * @return The string decoded
     */
    public static String base64ToString(String base64) {
        return new String(
                Base64.decode(base64.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
    }

}
