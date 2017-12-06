package ogamebot.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ogamebot.comp.Player;
import ogamebot.comp.UpgradeAble;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.PlanetBuilding;
import ogamebot.units.research.ResearchFields;
import ogamebot.units.warfare.DefenceType;
import ogamebot.units.warfare.ShipType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 *
 */
public class Build implements Initializable {
    @FXML
    private ListView<Planet> planetList;

    @FXML
    private TabPane tabPane;

    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initGui();
        loadData();
    }

    private void loadTab(String text, UnitType[] values, BiFunction<CelestialBody, UnitType, UpgradeAble> upgradeAbleBiFunction) {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/expand.fxml"));
        try {
            ScrollPane pane = loader.load();
            Tab tab = new Tab(text, pane);
            tabPane.getTabs().add(tab);

            Expand expand = loader.getController();
            expand.load(values, planetList.getSelectionModel().selectedItemProperty(), upgradeAbleBiFunction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGui() {
        loadTab("Gebäude", PlanetBuilding.values(), CelestialBody::getBuilding);
        loadTab("Forschung", ResearchFields.values(), (body, type) -> body.getPlayer().getResearch((ResearchFields) type));

        final UnitType[] machineTypes = Stream.of(DefenceType.values(), ShipType.values())
                .flatMap(Stream::of)
                .toArray(UnitType[]::new);

        loadTab("Schiffe/Verteidigung", machineTypes, (body, type) -> {
            if (type instanceof ShipType) {
                return body.getShip(type);
            } else if (type instanceof DefenceType) {
                return body.getDefence(type);
            }
            return null;
        });

        planetList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Planet item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void loadData() {
        currentPlayer.addListener((observable, oldValue, newValue) -> loadPlayer(newValue));
        currentPlayer.setValue(DataHolder.getInstance().getCurrentPlayer());
        DataHolder.getInstance().addListener((observable, oldValue, newValue) -> currentPlayer.setValue(newValue));
    }

    private void loadPlayer(Player player) {
        if (player != null) {
            planetList.setItems(player.getPlanets());

            if (!planetList.getItems().isEmpty()) {
                planetList.getSelectionModel().selectFirst();
            }
        }
    }

}
