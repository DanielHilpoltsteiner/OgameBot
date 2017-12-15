package ogamebot.comp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import ogamebot.units.UnitType;

/**
 *
 */
public interface UpgradeAble {
    IntegerProperty counterProperty();

    int getCounter();

    UnitType<?> getType();

    String getName();

    ReadOnlyStringProperty nameProperty();

    Cost getCost();
}
