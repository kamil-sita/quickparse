package place.sita.quickparse.templateparser;

import place.sita.quickparse.Config;
import place.sita.quickparse.NamedValue;
import place.sita.quickparse.exc.TemplateMismatchException;
import place.sita.quickparse.parsers.TypeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ValueExtractor {

    public static List<NamedValue> extractValues(CompiledTemplate compiledTemplate, String text, Config config) {

        Matcher matcher = compiledTemplate.getRegex().matcher(text);

        if (!matcher.matches()) {
            throw new TemplateMismatchException("Text did not match template");
        }

        List<String> objectsUnparsed = new ArrayList<>();

        for (int i = 0; i < compiledTemplate.getGroups().size(); i++) {
            objectsUnparsed.add(matcher.group(i + 1));
        }

        if (matcher.find()) {
            throw new TemplateMismatchException("Text matched template multiple times!");
        }

        List<NamedValue> namedValues = new ArrayList<>();

        for (int i = 0; i < objectsUnparsed.size(); i++) {
            String unparsedValue = objectsUnparsed.get(i);
            TypeParser<?> typeParser = config.getParser(compiledTemplate.getGroups().get(i).getType());
            CaptureGroup group = compiledTemplate.getGroups().get(i);
            Object value = typeParser.parse(unparsedValue, group.getArgs());
            namedValues.add(new NamedValue(value, group.getName()));
        }

        return namedValues;
    }

}
