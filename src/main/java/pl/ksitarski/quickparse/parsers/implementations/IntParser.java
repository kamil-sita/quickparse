package pl.ksitarski.quickparse.parsers.implementations;

import pl.ksitarski.quickparse.exc.ParsingException;
import pl.ksitarski.quickparse.parsers.TypeParser;

import java.util.List;


public class IntParser implements TypeParser<Integer> {

    @Override
    public String parserName() {
        return "int";
    }

    @Override
    public Integer parse(String s, List<String> args) throws ParsingException {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            throw new ParsingException();
        }
    }
}
