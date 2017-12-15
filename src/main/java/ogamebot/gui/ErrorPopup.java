package ogamebot.gui;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import tools.Condition;

/**
 *
 */
public class ErrorPopup {
    private final Node node;
    private final String msg;

    public ErrorPopup(Node node, String msg) {
        this.msg = msg;
        Condition.check().nonNull(node, msg).notEmpty(msg);
        this.node = node;
    }

    public void showError() {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        Pane pane = new Pane();

        pane.setPrefSize(200, 200);

        Color fill = new Color(0.2157, 0.1098, 0.3922, 1);
        CornerRadii radii = new CornerRadii(0);
        Insets insets = new Insets(0);

        BackgroundFill backgroundFill = new BackgroundFill(fill, radii, insets);
        Background value = new Background(backgroundFill);

        pane.getChildren().add(new Text(msg));
        pane.setBackground(value);
        popup.getContent().add(pane);

        final Bounds boundsInLocal = node.getBoundsInLocal();
        final Bounds bounds = node.localToScreen(boundsInLocal);

        final double anchorY = bounds.getMinY() - bounds.getHeight();
        final double anchorX = bounds.getMaxX();
        popup.show(node, anchorX, anchorY);
    }
}
