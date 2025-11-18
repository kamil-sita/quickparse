package place.sita.quickparse;

import place.sita.architecture.PublicApi;

@PublicApi // Users should NOT implement this interface
public interface TemplateElement {
    String asRegex();
}
