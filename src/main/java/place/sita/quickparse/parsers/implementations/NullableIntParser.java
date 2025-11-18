package place.sita.quickparse.parsers.implementations;


import place.sita.quickparse.exc.ParsingException;
import place.sita.quickparse.parsers.TypeParser;

import java.util.List;

public class NullableIntParser implements TypeParser<Integer> {

    @Override
    public String parserName() {
        return "Integer";
    }

    @Override
    public Integer parse(String s, List<String> args) throws ParsingException {
        if ("null".equalsIgnoreCase(s)) {
            return null;
        }

        return new IntParser().parse(s, args);
    }
}
