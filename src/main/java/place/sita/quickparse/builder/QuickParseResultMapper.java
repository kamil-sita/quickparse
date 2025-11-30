package place.sita.quickparse.builder;

import place.sita.quickparse.templateparser.NamedValue;

import java.util.List;

public interface QuickParseResultMapper<ResultT> {

	ResultT map(List<NamedValue> namedValues);

}
