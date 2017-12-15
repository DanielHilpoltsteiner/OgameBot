package ogamebot.gui.fleet;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ogamebot.gui.DataHolder;
import ogamebot.units.warfare.ShipType;
import ogamebot.units.warfare.Ships;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FleetSetter {
    private List<BooleanBinding> bindings = new ArrayList<>();


    public static FleetSetter set(FleetControl control) {
        final FleetSetter setter = new FleetSetter();

        for (ShipType type : ShipType.values()) {
            try {
                final String abbreviation = type.getAbbrevation();

                final Field value = FleetControl.class.getDeclaredField(abbreviation + "Value");
                final Field image = FleetControl.class.getDeclaredField(abbreviation + "Value");

                value.setAccessible(true);
                final Object f = value.get(control);
                image.setAccessible(true);
                final Object i = image.get(control);

                if (f instanceof TextField && i instanceof ImageView) {
                    TextField field = (TextField) f;
                    ImageView view = (ImageView) i;

                    DataHolder.getInstance().currentBodyProperty().addListener((observable, oldValue, newValue) -> {
                        setter.bindings.clear();
                        if (newValue != null) {
                            final Ships ship = newValue.getShip(type);


                            final BooleanBinding zero = ship.counterProperty().isEqualTo(0);
                            field.disableProperty().bind(zero);

                            zero.addListener((observable1, oldValue1, newValue1) -> {
                                if (newValue1) {
                                    view.setImage(new Image(setter.getClass().getResource("img/ships/" + abbreviation + "Available.jpg").toExternalForm()));
                                } else {
                                    view.setImage(new Image(setter.getClass().getResource("img/ships/" + abbreviation + "Unavailable.jpg").toExternalForm()));
                                }
                            });

                            setter.bindings.add(zero);
                        }
                    });
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
        return setter;
    }
}
