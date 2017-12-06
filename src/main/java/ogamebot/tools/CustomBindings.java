package ogamebot.tools;

import javafx.beans.property.Property;

/**
 *
 */
public class CustomBindings {
    public static void bindBidirectional(Property<Number> numberProperty, Property<Boolean> booleanProperty) {
        booleanProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                numberProperty.setValue(1);
            } else {
                numberProperty.setValue(0);
            }
        });

        numberProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                booleanProperty.setValue(Boolean.TRUE);
            } else {
                booleanProperty.setValue(Boolean.FALSE);
            }
        });
    }
}
