package place.sita.quickparse.builder.mappers;

import place.sita.quickparse.builder.QuickParseResultMapper;
import place.sita.quickparse.templateparser.NamedValue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ToObjectListMapper implements QuickParseResultMapper<List<Object>> {
	@Override
	public List<Object> map(List<NamedValue> namedValues) {
		Objects.requireNonNull(namedValues);

		return namedValues
			.stream()
			.map(NamedValue::getValue)
			.collect(Collectors.toList());
	}
}
