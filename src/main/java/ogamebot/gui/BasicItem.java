package ogamebot.gui;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.PropertySheet;
import tools.Condition;

import java.util.Optional;
import java.util.function.Function;

/**
 *
 */
public class BasicItem<E> implements PropertySheet.Item {

    private final String cat;
    private final String name;
    private final String desc;
    private final Property<E> property;
    private final Function<Object, E> converter;

    public BasicItem(String name, String desc, Property<E> property, String cat, Function<Object, E> converter) {
        this.converter = converter;
        Condition.check().nonNull(name, property, property.getValue());
        this.cat = cat;
        this.name = name;
        this.desc = desc;
        this.property = property;
    }

    @Override
    public Class<?> getType() {
        return property.getValue().getClass();
    }

    @Override
    public String getCategory() {
        return cat;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public Object getValue() {
        return property.getValue();
    }

    @Override
    public void setValue(Object value) {
        if (converter != null) {
            property.setValue(converter.apply(value));
        } else {
            property.setValue((E) value);
        }
    }

    @Override
    public Optional<ObservableValue<?>> getObservableValue() {
        return Optional.of(property);
    }
}
