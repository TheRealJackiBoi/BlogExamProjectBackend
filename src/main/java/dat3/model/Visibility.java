package dat3.model;

public enum Visibility {
    PUBLIC("public"),
    PRIVATE("private"),
    FRIENDS("friends"),
    ARCHIVED("archived");

    private final String visibility;

    Visibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return visibility;
    }
}
