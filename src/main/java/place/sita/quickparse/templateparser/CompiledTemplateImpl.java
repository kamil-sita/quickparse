package place.sita.quickparse.templateparser;

import place.sita.architecture.PrivateApi;
import place.sita.quickparse.CompiledTemplate;

import java.util.List;
import java.util.regex.Pattern;

@PrivateApi
class CompiledTemplateImpl implements CompiledTemplate {

    private final List<CaptureGroup> groups;
    private final Pattern regex;

	CompiledTemplateImpl(List<CaptureGroup> groups, Pattern regex) {
        this.groups = groups;
        this.regex = regex;
    }

	List<CaptureGroup> getGroups() {
        return groups;
    }

	Pattern getRegex() {
        return regex;
    }
}
