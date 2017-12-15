package ogamebot.gui.dialogs;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ogamebot.gui.GuiTools;

/**
 *
 */
abstract class AddDialog<R> extends Dialog<R> {

    ButtonBar.ButtonData finish;
    Node finishButton;

    AddDialog() {
        init();
    }

    private void init() {
        final DialogPane dialogPane = getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
        finish = ButtonBar.ButtonData.OK_DONE;
        final ButtonType fertig = new ButtonType("Fertig", finish);

        dialogPane.getButtonTypes().add(fertig);
        dialogPane.getButtonTypes().add(new ButtonType("Schließen", ButtonBar.ButtonData.CANCEL_CLOSE));

        finishButton = getDialogPane().lookupButton(fertig);
        finishButton.setDisable(true);
    }

    void addContent() {
        Pane pane = getContent();
        pane.getStyleClass().add("container-grid");
        getDialogPane().setContent(pane);
    }

    int setToGrid(String text, Node node, GridPane pane, int index) {
        Text desc = new Text(text);
        pane.addRow(index, desc, node);
        return ++index;
    }

    TextField getIntegerField(int maxChars) {
        TextField field = new TextField();
        field.setTextFormatter(GuiTools.getTextFormatter(maxChars));
        return field;
    }

    TextField getDoubleField(int maxChars) {
        TextField field = new TextField();
        field.setTextFormatter(GuiTools.getTextFormatter(maxChars));
        return field;
    }

    abstract Pane getContent();
}
