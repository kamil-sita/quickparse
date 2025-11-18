package place.sita.quickparse;

public class NamedValue {
    private final Object value;
    private final String name;

    public NamedValue(Object value, String name) {
        this.value = value;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
