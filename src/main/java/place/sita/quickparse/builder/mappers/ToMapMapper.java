package place.sita.quickparse.builder.mappers;

import place.sita.quickparse.builder.QuickParseResultMapper;
import place.sita.quickparse.exc.AssignmentException;
import place.sita.quickparse.templateparser.NamedValue;

import java.util.*;

public class ToMapMapper implements QuickParseResultMapper<Map<String, Object>> {

	private final boolean duplicatesAllowed;

	public ToMapMapper(boolean duplicatesAllowed) {
		this.duplicatesAllowed = duplicatesAllowed;
	}

	@Override
	public Map<String, Object> map(List<NamedValue> namedValues) {
		Objects.requireNonNull(namedValues);

		Set<String> namesAssigned = new HashSet<>();
		Set<String> listExists = new HashSet<>();
		Map<String, Object> objects = new HashMap<>();

		for (NamedValue namedValue : namedValues) {
			Object value = namedValue.getValue();
			String name = namedValue.getName();

			if (namesAssigned.contains(name)) {
				if (!duplicatesAllowed) {
					throw new AssignmentException("Second assignment to name \"" + name + "\"");
				} else {
					if (listExists.contains(name)) {
						List objList = (List) objects.get(name);
						objList.add(value);
					} else {
						Object obj = objects.get(name);
						List objList = new ArrayList();
						objList.add(obj);
						objList.add(value);
						listExists.add(name);
						objects.put(name, objList);
					}
				}
			} else {
				objects.put(name, value);
				namesAssigned.add(name);
			}
		}

		return objects;
	}
}
