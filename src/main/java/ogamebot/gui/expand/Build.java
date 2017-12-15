package ogamebot.gui.expand;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import ogamebot.comp.Player;
import ogamebot.comp.UpgradeAble;
import ogamebot.units.UnitType;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Planet;
import ogamebot.units.building.BuildingType;
import ogamebot.units.research.ResearchField;
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

    @FXML
    private VBox expansionContainer;

    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initGui();
    }

    private void initGui() {
        Expansionmode.Normal.change(this);
    }

    @FXML
    void getDynamicExpander() {
        Expansionmode.Dynamic.change(this);
    }

    @FXML
    void getNormalExpander() {
        Expansionmode.Normal.change(this);
    }

    private enum Expansionmode {
        Normal {
            @Override
            void change(Build build) {
                if (!build.expansionContainer.getChildren().contains(normal)) {
                    build.expansionContainer.getChildren().remove(dynamic);
                    if (normal == null) {
                        loadTabPane();
                    }
                    build.expansionContainer.getChildren().add(normal);
                }
            }

            private void loadTabPane() {
                normal = new TabPane();

                loadTab("Gebäude", BuildingType.values(), (CelestialBody::getBuilding));
                loadTab("Forschung", ResearchField.values(), (body, type) -> body.getPlayer().getResearch(type));

                final UnitType[] machineTypes = Stream.of(DefenceType.values(), ShipType.values())
                        .flatMap(Stream::of)
                        .toArray(UnitType[]::new);

                /*loadTab("Schiffe/Verteidigung", new UnitType[0], (body, type) -> {
                 *//*if (type instanceof ShipType) {
                        return body.getShip(type);
                    } else if (type instanceof DefenceType) {
                        return body.getDefence(type);
                    }*//*
                    return null;
                });*/
                Expand.ready();
            }

            private <E extends UpgradeAble, R extends UnitType<E>> void loadTab(String text, R[] values, BiFunction<CelestialBody, R, E> upgradeAbleBiFunction) {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/expand.fxml"));
                try {
                    ScrollPane pane = loader.load();
                    Tab tab = new Tab(text, pane);
                    normal.getTabs().add(tab);

                    Expand<E, R> expand = loader.getController();
                    expand.load(values, upgradeAbleBiFunction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },
        Dynamic {
            @Override
            void change(Build build) {
                if (!build.expansionContainer.getChildren().contains(dynamic)) {
                    build.expansionContainer.getChildren().remove(normal);
                    if (dynamic == null) {
                        loadDynamic();
                    }
                    build.expansionContainer.getChildren().add(dynamic);
                }
            }

            private void loadDynamic() {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dynamicExpand.fxml"));
                try {
                    dynamic = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        private static TabPane normal;
        private static VBox dynamic;

        abstract void change(Build build);
    }


    private void loadPlayer(Player player) {
    }

}
