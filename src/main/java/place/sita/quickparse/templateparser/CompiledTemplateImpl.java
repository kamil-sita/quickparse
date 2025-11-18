package place.sita.quickparse.templateparser;

import place.sita.quickparse.CompiledTemplate;

import java.util.List;
import java.util.regex.Pattern;

public class CompiledTemplateImpl implements CompiledTemplate {

    private final List<CaptureGroup> groups;
    private final Pattern regex;

    public CompiledTemplateImpl(List<CaptureGroup> groups, Pattern regex) {
        this.groups = groups;
        this.regex = regex;
    }

    public List<CaptureGroup> getGroups() {
        return groups;
    }

    public Pattern getRegex() {
        return regex;
    }
}
