package ogamebot.gui.dialogs;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ogamebot.comp.Universe;
import ogamebot.comp.UniverseBuilder;

/**
 *
 */
public class AddUniverse extends AddDialog<Universe> {

    private TextField defToTf;
    private TextField fleetToTf;
    private TextField name;
    private TextField flightSpeed;
    private TextField economySpeed;
    private CheckBox donut;
    private CheckBox rapidfire;
    private TextField bonusFields;

    public AddUniverse() {
        init();
    }

    private void init() {
        addContent();
        addListener(finishButton);

        setOnShowing(event -> name.requestFocus());

        setResultConverter(param -> {
            if (param.getButtonData() == finish) {
                final double defToTf = Double.parseDouble(this.defToTf.getText());
                final double fleetToTf = Double.parseDouble(this.fleetToTf.getText());
                final double flightSpeed = Double.parseDouble(this.flightSpeed.getText());
                final double economySpeed = Double.parseDouble(this.economySpeed.getText());
                final boolean donut = Boolean.parseBoolean(this.donut.getText());
                final boolean rapidFire = Boolean.parseBoolean(this.rapidfire.getText());
                final int bonusFields = Integer.parseInt(this.bonusFields.getText());
                final String name = this.name.getText();

                return new UniverseBuilder().setDefTf(defToTf).setFleetTf(fleetToTf).setFlightSpeed(flightSpeed).setEconomySpeed(economySpeed).setDonut(donut).setRapidFire(rapidFire).setBonusFields(bonusFields).setName(name).createUniverse();
            }
            return null;
        });
    }

    private void addListener(Node finishButton) {
        finishButton.disableProperty().bind(
                defToTf.textProperty().isEmpty()
                        .or(fleetToTf.textProperty().isEmpty())
                        .or(flightSpeed.textProperty().isEmpty())
                        .or(economySpeed.textProperty().isEmpty())
                        .or(bonusFields.textProperty().isEmpty())
                        .or(name.textProperty().isEmpty()));
    }

    @Override
    GridPane getContent() {
        final GridPane pane = new GridPane();

        name = new TextField();
        defToTf = getIntegerField(3);
        fleetToTf = getIntegerField(3);
        flightSpeed = getIntegerField(3);
        economySpeed = getIntegerField(3);
        donut = new CheckBox();
        rapidfire = new CheckBox();
        bonusFields = getIntegerField(2);

        int i = 0;
        i = setToGrid("Name", name, pane, i);
        i = setToGrid("Def in Tf", defToTf, pane, i);
        i = setToGrid("Flotte in Tf", fleetToTf, pane, i);
        i = setToGrid("Fluggeschwindigkeit", flightSpeed, pane, i);
        i = setToGrid("Wirtschaftsgeschwindigkeit", economySpeed, pane, i);
        i = setToGrid("Donut", donut, pane, i);
        i = setToGrid("Rapidfire", rapidfire, pane, i);
        setToGrid("Bonusfelder", bonusFields, pane, i);
        return pane;
    }
}
