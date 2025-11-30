package place.sita.quickparse;

import place.sita.architecture.PublicApi;
import place.sita.quickparse.builder.QuickParserBuilder;
import place.sita.quickparse.builder.mappers.ToMapMapper;
import place.sita.quickparse.builder.mappers.ToObjectListMapper;
import place.sita.quickparse.builder.mappers.ToObjectMapper;
import place.sita.quickparse.exc.AssignmentException;
import place.sita.quickparse.exc.NoSuchParserException;
import place.sita.quickparse.exc.TemplateException;
import place.sita.quickparse.exc.TemplateMismatchException;
import place.sita.quickparse.templateparser.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@PublicApi
public class QuickParse {

    private static final Config DEFAULT_CONFIG = Config.defaults();

    /**
     * Pre-compiles given template.
     * @throws TemplateException
     */
    public static Template preCompileTemplate(String template) {
        Objects.requireNonNull(template);
        return PatternCompiler.build(template);
    }

    /**
     * Compiles given PreCompiledTemplate.
     * @throws TemplateException
     */
    public static CompiledTemplate compileTemplate(Template template) {
        Objects.requireNonNull(template);
        return PatternCompiler.compile(template);
    }

    /**
     * Compiles given template.
     * @throws TemplateException
     */
    public static CompiledTemplate compileTemplate(String template) {
        Objects.requireNonNull(template);
        return PatternCompiler.compile(PatternCompiler.build(template));
    }

    /**
     * @see #parseToObject(Config, CompiledTemplate, String, Object, Class)
     */
    public static <T> T parseToObject(String template, String text, T tInstance, Class<T> tClass) {
        return parseToObject(DEFAULT_CONFIG, template, text, tInstance, tClass);
    }

    /**
     * @see #parseToObject(Config, CompiledTemplate, String, Object, Class)
     */
    public static <T> T parseToObject(Config config, String template, String text, T tInstance, Class<T> tClass) {
		CompiledTemplate compiledTemplate = compileTemplate(template);
        return parseToObject(config, compiledTemplate, text, tInstance, tClass);
    }

    /**
     * @see #parseToObject(Config, CompiledTemplate, String, Object, Class)
     */
    public static <T> T parseToObject(CompiledTemplate compiledTemplate, String text, T tInstance, Class<T> tClass) {
        return parseToObject(DEFAULT_CONFIG, compiledTemplate, text, tInstance, tClass);
    }

    /**
     * Parses given text using given template, inputting encountered values into the provided object - names of the
     * variables from the template must correspond to the method's field names.
     *
     * Returns modified instance of provided object instance.
     * @throws NoSuchParserException
     * @throws AssignmentException
     * @throws TemplateMismatchException
     * @throws TemplateException
     */
    public static <T> T parseToObject(Config config, CompiledTemplate compiledTemplate, String text, T tInstance, Class<T> tClass) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(compiledTemplate);
        Objects.requireNonNull(text);
        Objects.requireNonNull(tInstance);
        Objects.requireNonNull(tClass);

	    return QuickParserBuilder.builder()
		    .withConfig(config)
		    .withCompiledTemplate(compiledTemplate)
		    .createParser(new ToObjectMapper<>(() -> tInstance))
		    .parse(text);
    }

    /**
     * @see #parseToList(Config, CompiledTemplate, String)
     */
    public static List<Object> parseToList(String template, String text) {
        return parseToList(DEFAULT_CONFIG, template, text);
    }

    /**
     * @see #parseToList(Config, CompiledTemplate, String)
     */
    public static List<Object> parseToList(Config config, String template, String text) {
		CompiledTemplate compiledTemplate = compileTemplate(template);
        return parseToList(config, compiledTemplate, text);
    }

    /**
     * @see #parseToList(Config, CompiledTemplate, String)
     */
    public static List<Object> parseToList(CompiledTemplate compiledTemplate, String text) {
        return parseToList(DEFAULT_CONFIG, compiledTemplate, text);
    }

    /**
     * Parses given text using given template into a list - every parsed value is added to the list in order it was
     * discovered. Variable names are not preserved.
     * @throws NoSuchParserException
     * @throws TemplateMismatchException
     * @throws TemplateException
     */
    public static List<Object> parseToList(Config config, CompiledTemplate compiledTemplate, String text) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(compiledTemplate);
        Objects.requireNonNull(text);

		return QuickParserBuilder.builder()
			.withConfig(config)
			.withCompiledTemplate(compiledTemplate)
			.createParser(new ToObjectListMapper())
			.parse(text);
    }

    /**
     * @see #parseToMap(Config, CompiledTemplate, String, boolean)
     */
    public static Map<String, Object> parseToMap(String template, String text, boolean duplicates) {
        return parseToMap(DEFAULT_CONFIG, template, text, duplicates);
    }

    /**
     * @see #parseToMap(Config, CompiledTemplate, String, boolean)
     */
    public static Map<String, Object> parseToMap(Config config, String template, String text, boolean duplicates) {
		CompiledTemplate compiledTemplate = compileTemplate(template);
        return parseToMap(config, compiledTemplate, text, duplicates);
    }

    /**
     * @see #parseToMap(Config, CompiledTemplate, String, boolean)
     */
    public static Map<String, Object> parseToMap(CompiledTemplate compiledTemplate, String text, boolean duplicates) {
        return parseToMap(DEFAULT_CONFIG, compiledTemplate, text, duplicates);
    }

    /**
     * Parses given text using given template into a map in which keys are names of the variables, and values are their
     * parsed values. If duplicates are enabled, multiple assignments of the value with the same name will be instead
     * assigned to a list.
     * @throws NoSuchParserException
     * @throws TemplateMismatchException
     * @throws TemplateException
     */
    public static Map<String, Object> parseToMap(Config config, CompiledTemplate compiledTemplate, String text, boolean duplicates) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(compiledTemplate);
        Objects.requireNonNull(text);

	    return QuickParserBuilder.builder()
		    .withConfig(config)
		    .withCompiledTemplate(compiledTemplate)
		    .createParser(new ToMapMapper(duplicates))
		    .parse(text);
    }
}
