package place.sita.quickparse;

import place.sita.architecture.PublicApi;

@PublicApi
public interface QuickParser<ResultT> {

	ResultT parse(String input);

}
