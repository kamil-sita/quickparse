package pl.ksitarski.quickparse.parsers.implementations;

import pl.ksitarski.quickparse.exc.ParsingException;
import pl.ksitarski.quickparse.parsers.TypeParser;

import java.util.List;

public class StringParser implements TypeParser<String> {
    @Override
    public String parserName() {
        return "String";
    }

    @Override
    public String parse(String s, List<String> args) throws ParsingException {
        return s;
    }
}
