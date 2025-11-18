package place.sita.quickparse;

import place.sita.quickparse.exc.NoSuchParserException;
import place.sita.quickparse.parsers.implementations.IntParser;
import place.sita.quickparse.parsers.implementations.NullableIntParser;
import place.sita.quickparse.parsers.implementations.StringParser;
import place.sita.quickparse.parsers.TypeParser;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private final Map<String, TypeParser<?>> parsers = new HashMap<>();

    private Config() {

    }

    public static Config empty() {
        return new Config();
    }

    public static Config defaults() {
        Config config = new Config();

        config.addParser(new IntParser());
        config.addParser(new NullableIntParser());
        config.addParser(new StringParser());

        return config;
    }

    public void addParser(TypeParser typeParser) {
        parsers.put(typeParser.parserName(), typeParser);
    }

    public TypeParser<?> getParser(String type) {
        if (type == null) {
            return new StringParser();
        }
        TypeParser<?> parser = parsers.get(type);
        if (parser == null) {
            throw new NoSuchParserException("No parser with name \"" + type + "\"");
        }
        return parser;
    }
}
