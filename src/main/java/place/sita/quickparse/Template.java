package place.sita.quickparse;

import place.sita.architecture.PublicApi;

import java.util.ArrayList;
import java.util.List;

@PublicApi
public class Template {

    private final List<TemplateElement> elements;

    public Template(List<TemplateElement> elements) {
        this.elements = elements;
    }

    public Template add(Template other) {
        List<TemplateElement> combined = new ArrayList<>(elements);
        combined.addAll(other.elements);
        return new Template(combined);
    }

    public List<TemplateElement> getElements() {
        return elements;
    }
}
