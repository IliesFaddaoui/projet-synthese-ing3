package pds.stardust.kms.common;

/**
 * This class defines the common variables and methods that will be shared by utility classes.
 */
public abstract class AbstractNonInstantiableClass {

    /**
     * Defines the exception message that will be shown in case of an utility class being instantiated.
     */
    public static final String NON_INSTANTIABLE_CLASS_INSTANTIATION_EXCEPTION = "Utility class should not be instantiated";

}
