package place.sita.quickparse.templateparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import place.sita.quickparse.QuickParse;
import place.sita.quickparse.exc.TemplateException;

import static org.junit.jupiter.api.Assertions.*;


public class CompilerTest {

    @Test
    void should_compileCorrectTemplate_when_withNoNamesNorTypesSingleSign() {
        CompiledTemplate template = Assertions.assertDoesNotThrow(() -> QuickParse.compileTemplate(" $ "));
        assertEquals(1, template.getGroups().size());
        validateGroup(template, 0, null, null);
        validateRegex(template, "\\Q \\E(.*)\\Q \\E");
    }

    @Test
    void should_compileCorrectTemplate_when_withNoNamesNorTypes() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("$ $"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, null, null);
        validateGroup(template, 1, null, null);
        validateRegex(template, "(.*)\\Q \\E(.*)");
    }


    @Test
    void should_compileCorrectTemplate_when_withNamesAndNoTypes() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("${template1}$ ${template2}$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "template1", null);
        validateGroup(template, 1, "template2", null);
        validateRegex(template, "(.*)\\Q \\E(.*)");
    }

    @Test
    void should_compileCorrectTemplate_when_withDuplicateNamesAndNoTypes() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("${template1}$ ${template1}$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "template1", null);
        validateGroup(template, 1, "template1", null);
        validateRegex(template, "(.*)\\Q \\E(.*)");
    }

    @Test
    void should_compileCorrectTemplate_when_withNamesAndNoTypesAndComplexUnicodeCharacter() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("${template1}$ \uD867\uDE3D ${template1}$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "template1", null);
        validateGroup(template, 1, "template1", null);
        validateRegex(template, "(.*)\\Q \uD867\uDE3D \\E(.*)");
    }

    @Test
    void should_compileCorrectTemplate_when_withNamesAndTypes() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("$(int){template1}$ $(int2){template2}$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "template1", "int");
        validateGroup(template, 1, "template2", "int2");
        validateRegex(template, "(.*)\\Q \\E(.*)");
    }

    @Test
    void should_compileCorrectTemplate_when_withEmptyNamesAndTypes() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("$(){}$ $(){}$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "", "");
        validateGroup(template, 1, "", "");
        validateRegex(template, "(.*)\\Q \\E(.*)");
    }

    @Test
    void should_compileCorrectTemplate_when_withNamesAndTypesAndComplexUnicodeCharacter() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("$(int){template1}$ \uD867\uDE3D $(int2){template1}$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "template1", "int");
        validateGroup(template, 1, "template1", "int2");
        validateRegex(template, "(.*)\\Q \uD867\uDE3D \\E(.*)");
    }

    @Test
    void should_compileCorrectTemplate_when_withNamesAndTypesAndComplexUnicodeCharacterAndEscapedCharacter() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("$(int){template1}$ \uD867\uDE3D $(int2){template1}$ $$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "template1", "int");
        validateGroup(template, 1, "template1", "int2");
        validateRegex(template, "(.*)\\Q \uD867\uDE3D \\E(.*)\\Q $\\E");
    }

    @Test
    void should_compileCorrectTemplate_when_withNamesAndTypesAndComplexUnicodeCharacterAndEscapedCharacterAndRegexInside() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("$(int){template1}$ \uD867\uDE3D $(int2){template1}$ (.*) $$"));
        assertEquals(2, template.getGroups().size());
        validateGroup(template, 0, "template1", "int");
        validateGroup(template, 1, "template1", "int2");
        validateRegex(template, "(.*)\\Q \uD867\uDE3D \\E(.*)\\Q (.*) $\\E");
    }

    @Test
    void should_compileCorrectTemplate_when_withNamesAndTypesAndComplexUnicodeCharacterAndEscapedCharacterAndRegexInsideQuote() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate("$ (.*) \\E $$"));
        assertEquals(1, template.getGroups().size());
        validateGroup(template, 0, null, null);
        validateRegex(template, "(.*)\\Q (.*) \\E\\\\E\\Q $\\E");
    }

    @Test
    void should_compileCorrectTemplate_when_withNamesAndTypesAndComplexUnicodeCharacterAndEscapedCharacterAndMultipleSignsNextToEachOtherEscaped() {
        CompiledTemplate template = assertDoesNotThrow(() -> QuickParse.compileTemplate(" $$$ "));
        assertEquals(1, template.getGroups().size());
        validateGroup(template, 0, null, null);
        validateRegex(template, "\\Q $\\E(.*)\\Q \\E");
    }

    @Test
    void should_throwException_when_templateInvalidNoGroup() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate("123132"));
    }

    @Test
    void should_throwException_when_templateInvalidNoText() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate("$"));
    }

    @Test
    void should_throwException_when_templateInvalidNameStarted() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate("${name"));
    }

    @Test
    void should_throwException_when_templateInvalidTypeNameStarted() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate("$(name"));
    }

    @Test
    void should_throwException_when_templateInvalidTwoNames() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate("${name}{name2}"));
    }

    @Test
    void should_throwException_when_templateInvalidTwoTypeNames() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate("$(name)(name2)"));
    }

    @Test
    void should_throwException_when_templateInvalidPossibleMultipleMatches() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate(" $(){}$$(){}$ "));
    }

    @Test
    void should_throwException_when_complexGroupNotEndedBySign() {
        assertThrows(TemplateException.class, () -> QuickParse.compileTemplate(" $(abc){abc} "));
    }

    private void validateGroup(CompiledTemplate template, int index, String name, String type) {
        assertEquals(name, template.getGroups().get(index).getName());
        assertEquals(type, template.getGroups().get(index).getType());
    }

    private void validateRegex(CompiledTemplate template, String regex) {
        assertEquals(regex, template.getRegex().toString());
    }
    
}
