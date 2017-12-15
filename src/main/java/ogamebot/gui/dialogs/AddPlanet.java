package ogamebot.gui.dialogs;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ogamebot.comp.Player;
import ogamebot.comp.Position;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.astroObjects.PlanetBuilder;

import java.util.List;

/**
 *
 */
public class AddPlanet extends AddDialog<Planet> {

    private final List<Player> players;
    private TextField planetPosition;
    private TextField solarSystem;
    private TextField galaxy;
    private TextField fields;
    private TextField maxT;
    private TextField name;
    private ComboBox<Player> playerComboBox;

    public AddPlanet(List<Player> players) {
        this.players = players;
        init();
    }

    private void init() {
        addContent();
        addListener();

        setResultConverter(param -> {
            if (param.getButtonData() == finish) {
                final int position = Integer.parseInt(planetPosition.getText());
                final int solarSystem = Integer.parseInt(this.solarSystem.getText());
                final int galaxy = Integer.parseInt(this.galaxy.getText());
                final int fields = Integer.parseInt(this.fields.getText());
                final int maxT = Integer.parseInt(this.maxT.getText());
                final String name = this.name.getText();
                final Player player = playerComboBox.getSelectionModel().getSelectedItem();

                return new PlanetBuilder(name)
                        .setPlayer(player)
                        .setFields(fields)
                        .setMaxT(maxT)
                        .setPosition(new Position(galaxy, solarSystem, position))
                        .createPlanet();
            } else {
                return null;
            }
        });
    }

    private void addListener() {
        finishButton.disableProperty().bind(
                planetPosition.textProperty().isEmpty()
                        .or(solarSystem.textProperty().isEmpty())
                        .or(galaxy.textProperty().isEmpty())
                        .or(fields.textProperty().isEmpty())
                        .or(maxT.textProperty().isEmpty())
                        .or(name.textProperty().isEmpty())
                        .or(playerComboBox.getSelectionModel().selectedItemProperty().isNull()));
    }


    @Override
    GridPane getContent() {
        GridPane pane = new GridPane();

        playerComboBox = new ComboBox<>();
        playerComboBox.setItems(FXCollections.observableArrayList(players));
        playerComboBox.setCellFactory(param -> getListCell());
        playerComboBox.setButtonCell(getListCell());

        name = new TextField();
        maxT = getIntegerField(3);
        fields = getIntegerField(2);
        galaxy = getIntegerField(1);
        solarSystem = getIntegerField(3);
        planetPosition = getIntegerField(2);

        int i = 0;
        i = setToGrid("Spieler", playerComboBox, pane, i);
        i = setToGrid("Name", name, pane, i);
        i = setToGrid("Maximale Temperatur", maxT, pane, i);
        i = setToGrid("Basisfelder", fields, pane, i);
        i = setToGrid("Galaxie", galaxy, pane, i);
        i = setToGrid("Sonnensystem", solarSystem, pane, i);
        setToGrid("Planet", planetPosition, pane, i);
        return pane;
    }

    private ListCell<Player> getListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getName() + " (" + item.getUniverse().getName() + ")");
                    setGraphic(null);
                }
            }
        };
    }
}
