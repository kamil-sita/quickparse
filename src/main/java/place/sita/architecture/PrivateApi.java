package place.sita.architecture;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

/**
 * Indicates that the annotated element should NOT be used by external code directly.
 */
@PrivateApi
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface PrivateApi {
}
