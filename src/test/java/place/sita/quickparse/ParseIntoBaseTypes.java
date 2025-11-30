package place.sita.quickparse;

import org.junit.jupiter.api.Test;
import place.sita.quickparse.exc.TemplateException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParseIntoBaseTypes {

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
		assertEquals(2, objList.size());
		assertEquals(1, objList.get(0));
		assertEquals(2, objList.get(1));
	}

	@Test
	public void should_parseCorrectly_when_confusingTagsBehaviour_case1() {
		Map<String, Object> objects = QuickParse.parseToMap("$$ $(int){}$ $(int){}$", "$ 1 2", true);
		assertEquals(1, objects.size());
		List<Object> objList = (List<Object>) objects.get("");
		assertNotNull(objList);
		assertEquals(2, objList.size());
		assertEquals(1, objList.get(0));
		assertEquals(2, objList.get(1));
	}

	@Test
	public void should_parseCorrectly_when_confusingTagsBehaviour_case2() {
		Map<String, Object> objects = QuickParse.parseToMap("$ $ (.*) \\E $$", "$ $ (.*) \\E $", true);
		assertEquals(1, objects.size());
		List<Object> objList = (List<Object>) objects.get(null);
		assertNotNull(objList);
		assertEquals(2, objList.size());
		assertEquals("$", objList.get(0));
		assertEquals("$", objList.get(1));
	}


}
