package pl.ksitarski.quickparse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.ksitarski.quickparse.exc.TemplateException;
import pl.ksitarski.quickparse.exc.TemplateMismatchException;

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
    public void should_returnOneElementList_when_parsing_case0() {
        List<Object> objects = QuickParse.parseToList(" $ ", " a ");
        assertEquals(1, objects.size());
        assertEquals("a", objects.get(0));
    }

    @Test
    public void should_returnOneElementList_when_parsing_case1() {
        List<Object> objects = QuickParse.parseToList(" $ ", " 1 ");
        assertEquals(1, objects.size());
        assertEquals("1", objects.get(0));
    }

    @Test
    public void should_returnOneElementList_when_parsing_case2() {
        List<Object> objects = QuickParse.parseToList(" $(int)$ ", " 1 ");
        assertEquals(1, objects.size());
        assertEquals(1, objects.get(0));
    }

    @Test
    public void should_returnOneIntElementList_when_parsingWithKnownType() {
        assertThrows(TemplateException.class, () -> QuickParse.parseToList("$(int)$", "1"));
    }

    @Test
    public void should_returnOneIntElementList_when_parsingWithKnownTypeAndOneEscapedUnexpected() {
        assertThrows(TemplateException.class, () -> QuickParse.parseToList("$$ $(int)$$(int)$", "$ 12"));
    }

    @Test
    public void should_returnOneIntElementList_when_parsingWithKnownTypeAndOneEscaped_case0() {
        List<Object> objects = QuickParse.parseToList("$$ $(int)$$$ $(int)$", "$ 1$ 2");
        assertEquals(2, objects.size());
        assertEquals(1, objects.get(0));
        assertEquals(2, objects.get(1));
    }

    @Test
    public void should_returnOneIntElementList_when_parsingWithKnownTypeAndOneEscaped_case1() {
        List<Object> objects = QuickParse.parseToList("$(int)$ $(int)$", "1 2");
        assertEquals(2, objects.size());
        assertEquals(1, objects.get(0));
        assertEquals(2, objects.get(1));
    }

    @Test
    public void should_returnOneIntElementList_when_parsingWithKnownTypeAndOneEscaped_case2() {
        List<Object> objects = QuickParse.parseToList("$$ $(int)$ $(int)$", "$ 1 2");
        assertEquals(2, objects.size());
        assertEquals(1, objects.get(0));
        assertEquals(2, objects.get(1));
    }

    @Test
    public void should_returnOneIntElementList_when_parsingWithKnownTypeAndOneEscaped_case3() {
        List<Object> objects = QuickParse.parseToList("$$ $(int)$ $(int)$ $$ $(int){a1}$ ${a2}(int)$ ${}$ ${}$ $ a", "$ 1 2 $ 4 3 1 2 3 a");
        assertEquals(7, objects.size());

        assertEquals(1, objects.get(0));
        assertEquals(2, objects.get(1));
        assertEquals(4, objects.get(2));
        assertEquals(3, objects.get(3));
        assertEquals("1", objects.get(4));
        assertEquals("2", objects.get(5));
        assertEquals("3", objects.get(6));
    }

    @Test
    public void should_parseCorrectly_when_confusingTagsBehaviour_case0() {
        Map<String, Object> objects = QuickParse.parseToMap("$$ $(int)$ $(int)$", "$ 1 2", true);
        assertEquals(1, objects.size());
        List<Object> objList = (List<Object>) objects.get(null);
        assertNotNull(objList);
        assertEquals(objList.size(), 2);
        assertEquals(objList.get(0), 1);
        assertEquals(objList.get(1), 2);
    }

    @Test
    public void should_parseCorrectly_when_confusingTagsBehaviour_case1() {
        Map<String, Object> objects = QuickParse.parseToMap("$$ $(int){}$ $(int){}$", "$ 1 2", true);
        assertEquals(1, objects.size());
        List<Object> objList = (List<Object>) objects.get("");
        assertNotNull(objList);
        assertEquals(objList.size(), 2);
        assertEquals(objList.get(0), 1);
        assertEquals(objList.get(1), 2);
    }

    @Test
    public void should_parseCorrectly_when_confusingTagsBehaviour_case2() {
        Map<String, Object> objects = QuickParse.parseToMap("$ $ (.*) \\E $$", "$ $ (.*) \\E $", true);
        assertEquals(1, objects.size());
        List<Object> objList = (List<Object>) objects.get(null);
        assertNotNull(objList);
        assertEquals(objList.size(), 2);
        assertEquals("$", objList.get(0));
        assertEquals("$", objList.get(1));
    }


}
