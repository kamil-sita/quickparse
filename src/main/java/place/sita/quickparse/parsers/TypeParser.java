package place.sita.quickparse.parsers;

import place.sita.architecture.PublicApi;
import place.sita.quickparse.exc.ParsingException;

import java.util.List;

/**
 * Represents a parser for QuickParse.
 */
@PublicApi
public interface TypeParser<TYPE> {

    /**
     * Returns name of the parser - names of the types of the variables from QuickParse pattern will be matched against
     * those names.
     */
    String parserName();

    /**
     * Parses given string into given type.
     * @throws ParsingException thrown when given string is not parseable to given type.
     */
    TYPE parse(String s, List<String> args) throws ParsingException;
}
