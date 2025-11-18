package place.sita.quickparse.templateparser;

import java.util.List;

class CaptureGroup {

    private final String name;
    private final String type;
    private final List<String> args;

    public CaptureGroup(String name, String type, List<String> args) {
        this.name = name;
        this.type = type;
        this.args = args;
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
}
