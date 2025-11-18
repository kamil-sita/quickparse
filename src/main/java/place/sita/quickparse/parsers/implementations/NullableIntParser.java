package place.sita.quickparse.parsers.implementations;


import place.sita.architecture.PublicApi;
import place.sita.quickparse.exc.ParsingException;
import place.sita.quickparse.parsers.TypeParser;

import java.util.List;

@PublicApi
public class NullableIntParser implements TypeParser<Integer> {

	private final IntParser underlying = new IntParser();

    @Override
    public String parserName() {
        return "Integer";
    }

    @Override
    public Integer parse(String s, List<String> args) throws ParsingException {
        if ("null".equalsIgnoreCase(s)) {
            return null;
        }

        return underlying.parse(s, args);
    }
}
