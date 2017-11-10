package gui;

import gui.propertySheet.Item;
import gui.propertySheet.PropertySheet;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 */
public class Settings extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        PropertySheet sheet = new PropertySheet();
        root.getChildren().add(sheet);
        sheet.addItem(new Item(new SimpleIntegerProperty(), "Beschreibung", "Dauer", "sub"));
        sheet.addItem(new Item(new SimpleIntegerProperty(), "Beschreibung", "Dauer", "sub"));
        sheet.addItem(new Item(new SimpleIntegerProperty(), "Beschreibung", "Dauer", "sub"));
        sheet.addItem(new Item(new SimpleIntegerProperty(), "Beschreibung", "Dauer", "sub"));
        sheet.addItem(new Item(new SimpleIntegerProperty(), "Beschreibung", "Meins", "sub"));
        Item item = new Item(new SimpleIntegerProperty(), "Name", "Dauer", "sub");
        sheet.addItem(item);

        primaryStage.setTitle("OgameBot");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.show();

        // TODO: 10.11.2017 removal does not function properly
        sheet.removeItem(item);
    }


}
