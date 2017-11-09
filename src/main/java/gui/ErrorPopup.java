package gui;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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

        double x = node.getScene().getWindow().getX();
        double y = node.getScene().getWindow().getY();

        Point2D point2D = node.localToScene(0, 0);

        double anchorX = point2D.getX() + x;
        double anchorY = point2D.getY() + y;

        double prefWidth = pane.getPrefWidth();
        double prefHeight = pane.getPrefHeight();
        popup.show(node, anchorX - prefWidth, anchorY - prefHeight);
    }
}
