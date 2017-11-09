package units;

/**
 *
 */
public interface UnitType<E> {
    String getName();

    E create();
}
