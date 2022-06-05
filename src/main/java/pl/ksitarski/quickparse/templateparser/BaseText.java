package pl.ksitarski.quickparse.templateparser;

import java.util.regex.Pattern;

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
