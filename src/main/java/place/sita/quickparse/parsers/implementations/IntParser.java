package place.sita.quickparse.parsers.implementations;

import place.sita.architecture.PublicApi;
import place.sita.quickparse.exc.ParsingException;
import place.sita.quickparse.parsers.TypeParser;

import java.util.List;

@PublicApi
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
            throw new ParsingException("Could not parse \"" + s + "\" to Integer.", e);
        }
    }
}
