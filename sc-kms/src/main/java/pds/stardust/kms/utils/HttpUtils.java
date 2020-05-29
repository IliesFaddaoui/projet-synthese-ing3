package pds.stardust.kms.utils;

import org.springframework.http.HttpHeaders;
import pds.stardust.kms.common.AbstractNonInstantiableClass;

/**
 * This class defines helper methods for http requests.
 */
public class HttpUtils extends AbstractNonInstantiableClass {

    private final static String APPLICATION_JSON = "application/json";

    /**
     * This constructor hide the implicit public constructor thus preventing
     * the class from being instantiated.
     */
    private HttpUtils() {
        throw new IllegalStateException(NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION);
    }

    /**
     * Build the common headers and Authorization headers
     *
     * @param token The bearer token for the Authorization header
     * @return The headers
     */
    public static HttpHeaders buildHttpHeaders(String token) {

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.AUTHORIZATION, HttpUtils.buildAuthorizationHeader(token));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);

        return httpHeaders;
    }

    /**
     * Build the Authorization header from the specified token
     *
     * @param token The bearer token for the Authorization header
     * @return The Authorization header
     */
    private static String buildAuthorizationHeader(String token) {
        return "Bearer " + token;
    }

}
