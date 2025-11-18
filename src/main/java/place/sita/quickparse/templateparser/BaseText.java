package place.sita.quickparse.templateparser;

import place.sita.architecture.PrivateApi;
import place.sita.quickparse.TemplateElement;

import java.util.regex.Pattern;

@PrivateApi
class BaseText implements TemplateElement {
    private final String text;

    public BaseText(String text) {
        this.text = text;
    }

    @Override
    public String asRegex() {
        return Pattern.quote(text);
    }

    public String getText() {
        return text;
    }
}
