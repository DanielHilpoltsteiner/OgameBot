package ogamebot.gui;

import afester.javafx.svg.SvgLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ogamebot.comp.Player;
import ogamebot.comp.Universe;
import ogamebot.concurrent.OnStartTask;
import ogamebot.data.DataManager;
import ogamebot.gui.dialogs.AddPlanet;
import ogamebot.gui.dialogs.AddPlayer;
import ogamebot.gui.dialogs.AddUniverse;
import ogamebot.gui.dialogs.LoginDialog;
import ogamebot.gui.main.displayTree.DisplayedTree;
import ogamebot.gui.main.displayTree.TreeAble;
import ogamebot.online.AccountManager;
import ogamebot.units.astroObjects.CelestialBody;
import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.Planet;
import org.controlsfx.control.HiddenSidesPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 */
public class Main extends Application implements Initializable {

    @FXML
    private ListView<CelestialBody> celestialBodyList;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button addUniverseBtn;

    @FXML
    private Button addPlanetBtn;

    @FXML
    private Button addPlayerBtn;

    @FXML
    private VBox root;

    @FXML
    private TreeView<TreeAble> uniPlayerTree;

    @FXML
    private Text universeName;

    @FXML
    private Text playerName;

    @FXML
    private Text pointsValue;

    @FXML
    private TabPane playerContent;

    @FXML
    private HiddenSidesPane hiddenUniTree;

    private AccountManager manager = AccountManager.getInstance();
    private DisplayedTree tree = new DisplayedTree();

    @Override
    public void start(Stage primaryStage) throws Exception {
        OnStartTask.start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("OgameBot");
        final Scene value = new Scene(root);
        primaryStage.setScene(value);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataHolder.getInstance().addPlayerListener((observable, oldValue, newValue) -> loadPlayer(newValue));
        loadContent();
        setPaneListener(root);
        readyTree();
        initGui();
    }

    @FXML
    void addUniverse() {
        Dialog<Universe> dialog = new AddUniverse();
        final Optional<Universe> optionalUniverse = dialog.showAndWait();
        optionalUniverse.ifPresent(tree::add);
    }

    @FXML
    void addPlayer() {
        final List<Universe> universes = tree.getUniverses();
        Dialog<Player> dialog = new AddPlayer(universes);

        final Optional<Player> optionalPlayer = dialog.showAndWait();
        optionalPlayer.ifPresent(tree::add);
    }

    @FXML
    void addPlanet() {
        final List<Player> players = tree.getPlayers();

        Dialog<Planet> dialog = new AddPlanet(players);

        final Optional<Planet> optionalPlanet = dialog.showAndWait();
        optionalPlanet.ifPresent(planet -> {
            final Player player = planet.getPlayer();
            player.getPlanets().add(planet);
        });
    }

    @FXML
    void saveData() {
        DataManager.save();
    }

    @FXML
    void openSettings() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        try {
            Parent root = loader.load();
            Stage settingsStage = new Stage();
            settingsStage.initOwner(this.root.getScene().getWindow());

            settingsStage.setTitle("Einstellungen");
            settingsStage.setScene(new Scene(root));
            settingsStage.sizeToScene();
            settingsStage.show();
        } catch (IOException e) {
            // TODO: 09.11.2017 show error window
            e.printStackTrace();
        }

    }

    private void readyTree() {
        Platform.runLater(() -> {
            final List<Universe> universes = OnStartTask.get();
//            final List<Universe> universes = DataManager.getStubs();
            universes.forEach(tree::add);
            uniPlayerTree.setRoot(tree.getRoot());
            readyTreeSelection();

            checkOnlinePlayer(universes);
        });


        uniPlayerTree.setCellFactory(param -> new TreeCell<>() {
            @Override
            protected void updateItem(TreeAble item, boolean empty) {
                super.updateItem(item, empty);


                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item.getName());
                }
            }
        });

        uniPlayerTree.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                final TreeItem<TreeAble> item = uniPlayerTree.getSelectionModel().getSelectedItem();
                if (item != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                    if (item.getValue() instanceof Player) {
                        alert.setContentText("Sind sie sich sicher das sie dieses Universum mit allen Spielern entfernen wollen?");
                    } else if (item.getValue() instanceof Universe) {
                        alert.setContentText("Sind sie sich sicher das sie diesen Spieler entfernen wollen?");
                    }
                    final Optional<ButtonType> optional = alert.showAndWait();
                    optional.ifPresent(buttonType -> {
                        if (buttonType == ButtonType.OK) {
                            tree.remove(item.getValue());
                        }
                    });
                }
                event.consume();
            }
        });
    }

    private void checkOnlinePlayer(List<Universe> universes) {
        final List<Player> onlinePlayers = universes.stream().flatMap(universe -> universe.getPlayers().stream()).filter(Player::isOnlinePlayer).collect(Collectors.toList());
        onlinePlayers.forEach(player -> {
            final LoginDialog dialog = new LoginDialog();
            dialog.showAndWait();
        });
    }

    private void loadPlayer(Player newValue) {
        if (newValue != null) {
            playerName.textProperty().bind(newValue.nameProperty());
            universeName.textProperty().bind(newValue.getUniverse().nameProperty());

            pointsValue.textProperty().bind(newValue.pointsProperty().asString());
        } else {
            playerName.setText("N/A");
            universeName.setText("N/A");
            pointsValue.setText("0");
        }
    }

    private void initGui() {
        final Group universe = new SvgLoader().loadSvg(getClass().getResourceAsStream("/img/universeIcon.svg"));
        final Group player = new SvgLoader().loadSvg(getClass().getResourceAsStream("/img/playerIcon.svg"));

        //missing rx element
//        final Group planet = new SvgLoader().loadSvg(getClass().getResourceAsStream("/img/planetIcon.svg"));

        addUniverseBtn.setGraphic(universe);
        addPlayerBtn.setGraphic(player);
        addPlanetBtn.setGraphic(new ImageView(getClass().getResource("/img/planetIcon.png").toExternalForm()));

        celestialBodyList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(CelestialBody item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {

                    ImageView icon = null;
                    if (item instanceof Planet) {
                        icon = new ImageView(getClass().getResource("/img/planetIconSelected.png").toExternalForm());
                    } else if (item instanceof Moon) {
                        icon = new ImageView(getClass().getResource("/img/moonIconSelected.png").toExternalForm());
                    }
                    setGraphic(icon);
                    setText(item.getName());
                }
            }
        });

        celestialBodyList.
                getSelectionModel().
                selectedItemProperty().
                addListener((observable, oldValue, newValue) -> DataHolder.getInstance().setCurrentBody(newValue));
    }

    private void readyTreeSelection() {
        uniPlayerTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isLeaf() && newValue.getValue() instanceof Player) {
                final Player currentPlayer = (Player) newValue.getValue();
                celestialBodyList.setItems(currentPlayer.getCelestialBodies());
                DataHolder.getInstance().setCurrentPlayer(currentPlayer);
            }
        });

        uniPlayerTree.getSelectionModel().selectFirst();
        TreeItem<TreeAble> selectedItem = uniPlayerTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            ObservableList<TreeItem<TreeAble>> children = selectedItem.getChildren();

            if (!children.isEmpty()) {
                uniPlayerTree.getSelectionModel().select(children.get(0));
            }
        }
    }

    private void setPaneListener(Parent root) {
        for (Node node : root.getChildrenUnmodifiable()) {
            node.setOnMouseClicked(event -> node.requestFocus());

            if (node instanceof Parent) {
                setPaneListener((Parent) node);
            }
        }
    }

    private void loadContent() {
        FXMLLoader buildLoader = new FXMLLoader(getClass().getResource("/fxml/build.fxml"));
        FXMLLoader overViewLoader = new FXMLLoader(getClass().getResource("/fxml/overview.fxml"));
        FXMLLoader fleetLoader = new FXMLLoader(getClass().getResource("/fxml/fleet.fxml"));
        FXMLLoader galaxyViewLoader = new FXMLLoader(getClass().getResource("/fxml/galaxyView.fxml"));
        FXMLLoader fightSimulatorLoader = new FXMLLoader(getClass().getResource("/fxml/fightSimulator.fxml"));
        FXMLLoader newsLoader = new FXMLLoader(getClass().getResource("/fxml/news.fxml"));
        FXMLLoader allianceLoader = new FXMLLoader(getClass().getResource("/fxml/alliance.fxml"));
        try {
            playerContent.getTabs().add(new Tab("Übersicht", overViewLoader.load()));
            playerContent.getTabs().add(new Tab("Ausbauen", buildLoader.load()));
            playerContent.getTabs().add(new Tab("Flotte", fleetLoader.load()));
            playerContent.getTabs().add(new Tab("Nachrichten", newsLoader.load()));
            playerContent.getTabs().add(new Tab("Galaxie", galaxyViewLoader.load()));
            playerContent.getTabs().add(new Tab("Kampfsimulator", fightSimulatorLoader.load()));
            playerContent.getTabs().add(new Tab("Kampfsimulator", allianceLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
