package tools;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 */
public class Condition {
    private static final Condition instance = new Condition();

    private Condition() {
        if (instance != null) {
            throw new IllegalStateException();
        }
    }

    public static Condition check() {
        return instance;
    }

    public Condition greaterEqualThan(int i, int... ints) {
        if (Arrays.stream(ints).anyMatch(value -> value < i)) {
            throw new IllegalArgumentException();
        }
        return this;
    }

    public Condition nonNull(Object... objects) {
        if (Arrays.stream(objects).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException();
        }
        return this;
    }

    public Condition notEmpty(String... strings) {
        if (Arrays.stream(strings).anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException();
        }
        return this;
    }

    public Condition positive(int... ints) {
        greaterEqualThan(0, ints);
        return this;
    }
}
