package ogamebot.gui.dialogs;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ogamebot.comp.Player;
import ogamebot.comp.PlayerBuilder;
import ogamebot.comp.Universe;
import ogamebot.online.AccountManager;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class AddPlayer extends AddDialog<Player> {

    private final List<Universe> universes;
    private TextField name;
    private ComboBox<Universe> universe;
    private TextField darkMatter;
    private TextField highscore;
    private TextField points;
    private CheckBox isOnlinePlayer;
    private int row;
    private BorderPane contentPane;
    private GridPane offlinePane;
    private GridPane onlinePane;
    private TextField mail;
    private PasswordField passwordField;
    private BooleanBinding binding;

    public AddPlayer(List<Universe> universes) {
        this.universes = universes;
        init();
    }

    private void init() {
        addContent();
        addListener();

        setResultConverter(param -> {
            if (param.getButtonData() == finish) {

                if (isOnlinePlayer.isSelected()) {
                    final String mail = this.mail.getText();
                    String password = passwordField.getText();
                    try {
                        return AccountManager.getInstance().createPlayer(mail, password, universe.getSelectionModel().getSelectedItem());
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Login failed");
                        alert.show();
                        e.printStackTrace();
                    }
                } else {
                    final Universe universe = this.universe.getSelectionModel().getSelectedItem();
                    final String name = this.name.getText();
                    final String darkMatter = this.darkMatter.getText();
                    final String highScore = highscore.getText();
                    final String points = this.points.getText();

                    final PlayerBuilder builder = new PlayerBuilder().setName(name).setUniverse(universe);

                    if (!darkMatter.isEmpty()) {
                        builder.setDarkMatter(Integer.parseInt(darkMatter));
                    }

                    if (!points.isEmpty()) {
                        builder.setPoints(Integer.parseInt(points));
                    }
                    if (!highScore.isEmpty()) {
                        builder.setHighscore(Integer.parseInt(highScore));
                    }

                    return builder.createPlayer();
                }
            }
            return null;
        });
    }

    private void addListener() {
        binding = universe.getSelectionModel()
                .selectedItemProperty().isNull()
                .or(name.textProperty().isEmpty());

        finishButton.disableProperty().bind(binding);

        isOnlinePlayer.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {

                if (onlinePane == null) {
                    onlinePane = new GridPane();
                    this.mail = new TextField();
                    this.passwordField = new PasswordField();

                    setToGrid("E-Mail", mail, onlinePane, 0);
                    setToGrid("Passwort", passwordField, onlinePane, 1);

                }
                contentPane.setCenter(onlinePane);

                finishButton.disableProperty().unbind();
                finishButton.disableProperty().bind(
                        mail.textProperty().isEmpty()
                                .or(passwordField.textProperty().isEmpty())
                                .or(universe.getSelectionModel().selectedItemProperty().isNull()));

            } else {
                finishButton.disableProperty().unbind();
                finishButton.disableProperty().bind(binding);

                contentPane.setCenter(offlinePane);
            }
        });
    }

    @Override
    Pane getContent() {
        contentPane = new BorderPane();
        offlinePane = new GridPane();


        name = new TextField();
        points = getIntegerField(20);
        highscore = getIntegerField(20);
        darkMatter = getIntegerField(20);


        row = 0;
        row = setToGrid("Spielername", name, offlinePane, row);
        row = setToGrid("Punkte", points, offlinePane, row);
        row = setToGrid("Highscore", highscore, offlinePane, row);
        row = setToGrid("Dunkle Materie", darkMatter, offlinePane, row);

        contentPane.setCenter(offlinePane);
        setTop();
        setBottom();
        return contentPane;
    }

    private void setBottom() {
        HBox bottom = new HBox();
        contentPane.setBottom(bottom);
        isOnlinePlayer = new CheckBox();

        bottom.getChildren().add(new Text("Online-Spieler"));
        bottom.getChildren().add(isOnlinePlayer);
        bottom.setSpacing(5);
    }

    private void setTop() {
        HBox top = new HBox();
        top.setSpacing(5);

        top.getChildren().add(new Text("Universum"));
        universe = new ComboBox<>(FXCollections.observableArrayList(universes));
        universe.setCellFactory(param -> getListCell());
        universe.setButtonCell(getListCell());
        top.getChildren().add(universe);

        contentPane.setTop(top);
    }

    private ListCell<Universe> getListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Universe item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getName());
                }
            }
        };
    }
}
