package cryptolib.core;

import cryptolib.common.AbstractNonInstantiableClass;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;

/**
 * This class defines the common methods regarding string, bytes and base 64 transformation.
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
     * @return The base 64 encoded of the string in bytes primitive
     */
    public static byte[] toBase64(String utf8String) {
        return Base64.encode(utf8String.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *  Convert from bytes primitive to a base 64 string.
     *
     * @param bytes The base 64 bytes primitive to convert
     * @return The base 64 string representation of the bytes
     */
    public static String toBase64String(byte[] bytes) {
        return Base64.toBase64String(bytes);
    }

    /**
     * Decode base64 bytes primitive.
     *
     * @param base64 The base 64 bytes primitive to convert
     * @return The bytes decoded
     */
    public static byte[] decodeBase64(byte[] base64) {
        return Base64.decode(base64);
    }

    /**
     * Decode base 64 string.
     *
     * @param base64 The base 64 string to convert
     * @return The bytes decoded
     */
    public static byte[] decodeBase64String(String base64) {
        return decodeBase64(base64.getBytes(StandardCharsets.UTF_8));
    }

}
