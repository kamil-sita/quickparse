package place.sita.quickparse.builder.mappers;

import place.sita.quickparse.builder.QuickParseResultMapper;
import place.sita.quickparse.exc.AssignmentException;
import place.sita.quickparse.exc.InvalidApiUsageException;
import place.sita.quickparse.templateparser.NamedValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class ToObjectMapper<ObjectT> implements QuickParseResultMapper<ObjectT> {

	private final ObjectFactory<ObjectT> factory;

	public static <ResultT> ToObjectMapper<ResultT> reflectionBased(Class<ResultT> clazz) {
		Objects.requireNonNull(clazz);

		final Constructor<ResultT> ctor;
		try {
			ctor = clazz.getDeclaredConstructor();
			ctor.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw new InvalidApiUsageException("Class " + clazz.getName() + " does not have a no-arg constructor", e);
		} catch (SecurityException e) {
			throw new InvalidApiUsageException("Cannot access no-arg constructor of " + clazz.getName(), e);
		}

		ObjectFactory<ResultT> objectFactory = () -> {
			try {
				return ctor.newInstance();
			} catch (ReflectiveOperationException e) {
				throw new AssignmentException("Failed to instantiate " + clazz.getName(), e);
			}
		};

		return new ToObjectMapper<>(objectFactory);
	}

	public ToObjectMapper(ObjectFactory<ObjectT> factory) {
		Objects.requireNonNull(factory);
		this.factory = factory;
	}

	@Override
	public ObjectT map(List<NamedValue> namedValues) {
		Objects.requireNonNull(namedValues);

		ObjectT instance = factory.create();
		Objects.requireNonNull(instance);

		for (NamedValue namedValue : namedValues) {
			String name = namedValue.getName();
			Object value = namedValue.getValue();

			Field field;
			try {
				field = instance.getClass().getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				throw new AssignmentException("No field with name \"" + name + "\"");
			}
			try {
				field.setAccessible(true);
			} catch (SecurityException e) {
				throw new AssignmentException("Can't set \"" + name + "\" to be accessible");
			}
			try {
				field.set(instance, value);
			} catch (IllegalAccessException e) {
				throw new AssignmentException("Illegal access to \"" + name + "\"");
			} catch (IllegalArgumentException e) {
				throw new AssignmentException("Cannot assign object with name \"" + name + "\" to given type.");
			}
		}

		return instance;
	}


	public interface ObjectFactory<ObjectT> {
		ObjectT create();
	}
}
