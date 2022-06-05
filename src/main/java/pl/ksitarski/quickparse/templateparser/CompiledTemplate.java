package pl.ksitarski.quickparse.templateparser;

import java.util.List;
import java.util.regex.Pattern;

public class CompiledTemplate {

    private final List<CaptureGroup> groups;
    private final Pattern regex;

    CompiledTemplate(List<CaptureGroup> groups, Pattern regex) {
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
