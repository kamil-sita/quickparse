package place.sita.quickparse.templateparser;

import java.util.ArrayList;
import java.util.List;
public class PreCompiledTemplate {

    private final List<TemplateElement> groups;

    public PreCompiledTemplate(List<TemplateElement> groups) {
        this.groups = groups;
    }

    public PreCompiledTemplate add(PreCompiledTemplate other) {
        List<TemplateElement> combined = new ArrayList<>(groups);
        combined.addAll(other.groups);
        return new PreCompiledTemplate(combined);
    }

    List<TemplateElement> getGroups() {
        return groups;
    }
}
