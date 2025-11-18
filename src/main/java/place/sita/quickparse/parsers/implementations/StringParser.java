package place.sita.quickparse.parsers.implementations;

import place.sita.architecture.PublicApi;
import place.sita.quickparse.exc.ParsingException;
import place.sita.quickparse.parsers.TypeParser;

import java.util.List;

@PublicApi
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
