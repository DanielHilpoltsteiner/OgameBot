package units.warfare;

/**
 *
 */
public enum Rockets {
    INTERCEPTOR_MISSILE("Abfangrakete"),
    INTERPLANETARY_MISSILE("Interplanetarrakete"),;

    private final String name;

    Rockets(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
