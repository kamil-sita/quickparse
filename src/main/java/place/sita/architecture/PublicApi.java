package place.sita.architecture;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that the annotated element can be used by external code directly.
 */
@PrivateApi
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface PublicApi {
}
