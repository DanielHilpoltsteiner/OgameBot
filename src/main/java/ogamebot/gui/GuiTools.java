package ogamebot.gui;

import javafx.scene.control.TextFormatter;

/**
 *
 */
public abstract class GuiTools {
    public static TextFormatter<String> getTextFormatter(int maxChars) {
        return new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (!c.getControlNewText().matches("[0-9]*")) {
                    c.setText(c.getControlText());
                }
                if (c.getControlNewText().isEmpty()) {
                    c.setText("0");
                    // TODO: 09.11.2017 do sth better
                }
                if (newLength > maxChars) {
                    c.setText(c.getControlText());
                    c.setRange(0, maxChars);
                }
            }
            return c;
        });
    }
}
