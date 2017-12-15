package ogamebot.calc;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;

import java.util.WeakHashMap;

/**
 *
 */
public class CustomBindings {
    //todo does this make sense? i don't think so, due to the changes in the hashcode of properties, 'need' to do an alternative
    private static WeakHashMap<Property<Number>, ChangeListener<Number>> numberListener = new WeakHashMap<>();
    private static WeakHashMap<Property<Boolean>, ChangeListener<Boolean>> booleanListener = new WeakHashMap<>();


    /**
     * A pseudo binding through listening.
     *
     * @param numberProperty
     * @param booleanProperty
     */
    public static void bindBidirectional(Property<Number> numberProperty, Property<Boolean> booleanProperty) {
        final ChangeListener<Boolean> booleanChangeListener = (observable, oldValue, newValue) -> {
            if (newValue) {
                numberProperty.setValue(1);
            } else {
                numberProperty.setValue(0);
            }
        };
        booleanProperty.addListener(booleanChangeListener);
        booleanListener.put(booleanProperty, booleanChangeListener);

        final ChangeListener<Number> numberChangeListener = (observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                booleanProperty.setValue(Boolean.TRUE);
            } else {
                booleanProperty.setValue(Boolean.FALSE);
            }
        };
        numberProperty.addListener(numberChangeListener);
        numberListener.put(numberProperty, numberChangeListener);
    }

    public static void unbindBidirectional(Property<Number> numberProperty, Property<Boolean> booleanProperty) {
        final ChangeListener<Number> numberChangeListener = numberListener.get(numberProperty);
        if (numberChangeListener != null) {
            numberProperty.removeListener(numberChangeListener);
        }

        final ChangeListener<Boolean> booleanChangeListener = booleanListener.get(booleanProperty);

        if (booleanChangeListener != null) {
            booleanProperty.removeListener(booleanChangeListener);
        }
    }
}
