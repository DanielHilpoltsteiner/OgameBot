package ogamebot.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ogamebot.comp.GameEntity;
import ogamebot.comp.Player;
import ogamebot.comp.Universe;
import ogamebot.units.astroObjects.PlanetBuilder;
import tools.JsonAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 *
 */
public class Main extends Application implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private VBox root;

    @FXML
    private TreeView<GameEntity> uniPlayerTree;

    @FXML
    private Text universeName;

    @FXML
    private Text playerName;

    @FXML
    private Text pointsValue;

    @FXML
    private TabPane playerContent;


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("OgameBot");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataHolder.getInstance().addListener((observable, oldValue, newValue) -> loadPlayer(newValue));
        loadContent();
        setPaneListener(root);
        readyTree();
    }

    private GameEntity entityRoot = new GameEntity() {
        List<GameEntity> children = getEntities();

        @Override
        public String getText() {
            return "ROOT";
        }

        @Override
        public List<GameEntity> getChildren() {
            return children;
        }
    };

    @FXML
    void saveData() {
        Gson gson = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(Set.class, new JsonAdapter<>()).
                registerTypeAdapter(Map.class, new JsonAdapter<>()).
                registerTypeAdapter(IntegerProperty.class, new JsonAdapter<>()).
                registerTypeAdapter(ObjectProperty.class, new JsonAdapter<>()).
                registerTypeAdapter(StringProperty.class, new JsonAdapter<>()).
                registerTypeAdapter(DoubleProperty.class, new JsonAdapter<>()).
                create();

        if (entityRoot != null) {
            // TODO: 09.11.2017 get new gson for java 9?
            // TODO: 09.11.2017 error for linked treemap internal??
            String json = gson.toJson(getEntities());
            System.out.println(json);
            gson.fromJson(json, Universe[].class);

        }
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

    @FXML
    void openSettings() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main.fxml"));
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

        TreeItem<GameEntity> root = new TreeItem<>(entityRoot);
        resolveTree(entityRoot, root);
        uniPlayerTree.setRoot(root);

        readyTreeSelection();

        uniPlayerTree.setCellFactory(param -> new TreeCell<>() {
            @Override
            protected void updateItem(GameEntity item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getText());
                }
            }
        });
    }

    private void readyTreeSelection() {
        uniPlayerTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isLeaf() && newValue.getValue() instanceof Player) {
                DataHolder.getInstance().setCurrentPlayer((Player) newValue.getValue());
            }
        });

        uniPlayerTree.getSelectionModel().selectFirst();
        TreeItem<GameEntity> selectedItem = uniPlayerTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            ObservableList<TreeItem<GameEntity>> children = selectedItem.getChildren();

            if (!children.isEmpty()) {
                uniPlayerTree.getSelectionModel().select(children.get(0));
            }
        }
    }

    private List<GameEntity> getEntities() {
        Universe value = new Universe(0.1, 0.7, 2, 3, true, true, 25, "orion");
        Player w = new Player("w", 0, 0, 0, value);
        Player f = new Player("f", 0, 0, 0, value);
        Player a = new Player("a", 0, 0, 0, value);
        Player s = new Player("s", 0, 0, 0, value);
        Player d = new Player("d", 0, 0, 0, value);
        Player player1 = new Player("!", 0, 0, 0, value);
        value.addPlayer(w);
        value.addPlayer(a);
        value.addPlayer(s);
        value.addPlayer(d);
        value.addPlayer(f);
        value.addPlayer(player1);
        player1.getPlanets().add(new PlanetBuilder("franz").setPlayer(player1).createPlanet());

        List<GameEntity> entities = new ArrayList<>();
        entities.add(value);
        return entities;
    }

    private void resolveTree(GameEntity entity, TreeItem<GameEntity> root) {
        entity.getChildren().forEach(gameEntity -> {
            TreeItem<GameEntity> item = new TreeItem<>(gameEntity);
            root.getChildren().add(item);
            resolveTree(gameEntity, item);
        });
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
        FXMLLoader buildLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/build.fxml"));
        FXMLLoader overViewLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/overview.fxml"));
        try {
            playerContent.getTabs().add(new Tab("Übersicht", overViewLoader.load()));
            playerContent.getTabs().add(new Tab("Ausbauen", buildLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
