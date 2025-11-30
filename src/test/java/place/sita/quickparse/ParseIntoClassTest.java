package place.sita.quickparse;

import org.junit.jupiter.api.Test;
import place.sita.quickparse.exc.TemplateException;
import place.sita.quickparse.exc.TemplateMismatchException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParseIntoClassTest {

    @Test
    public void should_assignPositiveIntegerToClassFromSimpleTemplate() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("abc ${val}(int)$", "abc 1", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(1, simpleIntegerClass.getVal());
    }

    @Test
    public void should_assignNegativeIntegerToClassFromSimpleTemplate() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("abc ${val}(int)$", "abc -1", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(-1, simpleIntegerClass.getVal());
    }

    @Test
    public void should_assignTwoIntegersToClassFromSimpleTemplate() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("${val}(int)$, ${val2}(int)$", "-1, 2", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(-1, simpleIntegerClass.getVal());
        assertEquals(2, simpleIntegerClass.getVal2());
    }

    @Test
    public void should_assignTwoIntegersToClassFromSimpleTemplateWithCharactersAfterTheLastCaptureGroup() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("${val}(int)$, ${val2}(int)$, 3", "-1, 2, 3", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(-1, simpleIntegerClass.getVal());
        assertEquals(2, simpleIntegerClass.getVal2());
    }

    @Test
    public void should_assignTwoIntegersToClassFromSimpleTemplateWithUncommonChar() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("${val}(int)$, ą${val2}(int)$", "-1, ą2", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(-1, simpleIntegerClass.getVal());
        assertEquals(2, simpleIntegerClass.getVal2());
    }

    @Test
    public void should_assignTwoIntegersToClassFromSimpleTemplateWithUnicodeChar() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("${val}(int)$, ☕${val2}(int)$", "-1, ☕2", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(-1, simpleIntegerClass.getVal());
        assertEquals(2, simpleIntegerClass.getVal2());
    }

    @Test
    public void should_assignTwoIntegersToClassFromSimpleTemplateWithBiggerUnicodeChar() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("${val}(int)$, \uD867\uDE3D${val2}(int)$", "-1, \uD867\uDE3D2", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(-1, simpleIntegerClass.getVal());
        assertEquals(2, simpleIntegerClass.getVal2());
    }

    @Test
    public void should_throwException_when_parsingInvalidTemplate() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        assertThrows(TemplateException.class, () -> QuickParse.parseToObject("${val", "-1", simpleIntegerClass, SimpleIntegerClass.class));
    }

    @Test
    public void should_throwException_when_parsingInvalidNoParametersDueToEscapedCharactersTemplate() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        assertThrows(TemplateMismatchException.class, () -> QuickParse.parseToObject("$${val}$", "-1", simpleIntegerClass, SimpleIntegerClass.class));
    }

    @Test
    public void should_assignTwoIntegersToClassFromSimpleTemplateWithCharactersAfterTheLastCaptureGroupAndIntParsing() {
        SimpleIntegerClass simpleIntegerClass = new SimpleIntegerClass();

        QuickParse.parseToObject("$(int){val}$, $(int){val2}$, 3", "-1, 2, 3", simpleIntegerClass, SimpleIntegerClass.class);

        assertEquals(-1, simpleIntegerClass.getVal());
        assertEquals(2, simpleIntegerClass.getVal2());
    }

	@Test
	public void should_acceptCompiledTemplate() {
		CompiledTemplate compiledTemplate = QuickParse.compileTemplate("abc ${val}(int)$");

		SimpleIntegerClass obj1 = QuickParse.parseToObject(compiledTemplate, "abc 1", new SimpleIntegerClass(), SimpleIntegerClass.class);

		assertEquals(1, obj1.getVal());

		SimpleIntegerClass obj2 = QuickParse.parseToObject(compiledTemplate, "abc 2", new SimpleIntegerClass(), SimpleIntegerClass.class);

		assertEquals(2, obj2.getVal());
	}

}
