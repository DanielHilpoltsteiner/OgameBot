package ogamebot.gui;

import javafx.beans.property.Property;
import tools.Condition;

import java.util.Collection;
import java.util.function.Function;

/**
 *
 */
public class ChoiceItem<E> extends BasicItem<E> {

    private final Collection<E> choices;

    public ChoiceItem(String name, String desc, Property<E> property, Collection<E> choices, String cat, Function<Object, E> converter) {
        super(name, desc, property, cat, converter);
        Condition.check().nonNull(choices);
        this.choices = choices;
    }

    public Collection<E> getChoices() {
        return choices;
    }
}
