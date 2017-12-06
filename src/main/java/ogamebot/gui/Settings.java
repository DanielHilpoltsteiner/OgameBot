package ogamebot.gui;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.Editors;

import java.util.Set;

/**
 *
 */
public class Settings extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        PropertySheet sheet = new PropertySheet();
        sheet.setPropertyEditorFactory(item -> {

            if (item instanceof ChoiceItem) {
                return Editors.createChoiceEditor(item, ((ChoiceItem<?>) item).getChoices());
            } else if (String.class.isAssignableFrom(item.getType())) {
                return Editors.createTextEditor(item);
            } else if (Number.class.isAssignableFrom(item.getType())) {
                return Editors.createNumericEditor(item);
            } else if (Boolean.class.isAssignableFrom(item.getType())) {
                return Editors.createCheckEditor(item);
            } else {
                return null;
            }
        });


        sheet.getItems().add(new BasicItem<>("hi", "my description", new SimpleStringProperty(""), "my category", String::valueOf));
        sheet.getItems().add(new BasicItem<>("hi", "my description", new SimpleDoubleProperty(), "my category", o -> Double.parseDouble(String.valueOf(o))));
        sheet.getItems().add(new BasicItem<>("hi", "my description", new SimpleIntegerProperty(), "my category", o -> Integer.parseInt(String.valueOf(o))));
        sheet.getItems().add(new BasicItem<>("hi", "my description", new SimpleBooleanProperty(), "my category", o -> Boolean.parseBoolean(String.valueOf(o))));
        sheet.getItems().add(new BasicItem<>("hi", "my description", new SimpleStringProperty(""), "my category", String::valueOf));
        sheet.getItems().add(new ChoiceItem<>("hi", "my description", new SimpleStringProperty(""), Set.of("hi", "bye"), "my category", String::valueOf));

        root.getChildren().add(sheet);
        primaryStage.setTitle("OgameBot");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.show();

        // TODO: 10.11.2017 removal does not function properly
    }


}
