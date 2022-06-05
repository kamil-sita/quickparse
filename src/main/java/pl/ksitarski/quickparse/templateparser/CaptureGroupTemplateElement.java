package pl.ksitarski.quickparse.templateparser;

import java.util.List;

class CaptureGroupTemplateElement implements TemplateElement {
    private final String name;
    private final String type;
    private final List<String> args;

    public CaptureGroupTemplateElement(ArgParseContext argParseContext) {
        this.name = argParseContext.getName();
        this.type = argParseContext.getType();
        this.args = argParseContext.getArgs();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String asRegex() {
        return "(.*)";
    }
}
