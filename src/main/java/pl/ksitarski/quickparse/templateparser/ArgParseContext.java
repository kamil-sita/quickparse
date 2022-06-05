package pl.ksitarski.quickparse.templateparser;

import java.util.List;

class ArgParseContext {
    private String type;
    private String name;
    private List<String> args;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
