package place.sita.quickparse.builder;

import place.sita.architecture.PublicApi;
import place.sita.quickparse.*;
import place.sita.quickparse.templateparser.NamedValue;
import place.sita.quickparse.templateparser.ValueExtractor;

import java.util.List;
import java.util.Objects;

@PublicApi
public class QuickParserBuilder {

	private Config config = Config.defaults();
	private CompiledTemplate compiledTemplate;

	private QuickParserBuilder() {

	}

	public static QuickParserBuilder builder() {
		return new QuickParserBuilder();
	}

	public QuickParserBuilder withConfig(Config config) {
		Objects.requireNonNull(config);
		this.config = config;
		return this;
	}

	public QuickParserBuilder withCompiledTemplate(CompiledTemplate compiledTemplate) {
		Objects.requireNonNull(compiledTemplate);
		this.compiledTemplate = compiledTemplate;
		return this;
	}

	public QuickParserBuilder withTemplate(Template template) {
		Objects.requireNonNull(template);
		this.compiledTemplate = QuickParse.compileTemplate(template);
		return this;
	}

	public QuickParserBuilder withTemplate(String template) {
		Objects.requireNonNull(template);
		this.compiledTemplate = QuickParse.compileTemplate(template);
		return this;
	}

	public <ResultT> QuickParser<ResultT> createParser(QuickParseResultMapper<ResultT> resultMapper) {
		Objects.requireNonNull(resultMapper);
		Objects.requireNonNull(compiledTemplate);

		return new QuickParser<ResultT>() {
			@Override
			public ResultT parse(String input) {
				List<NamedValue> values = ValueExtractor.extractValues(compiledTemplate, input, config);
				return resultMapper.map(values);
			}
		};
	}

}
