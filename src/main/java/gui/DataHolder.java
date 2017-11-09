package gui;

import comp.Player;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import tools.Condition;

/**
 *
 */
public class DataHolder {
    private static DataHolder ourInstance = new DataHolder();
    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();

    private DataHolder() {
        if (ourInstance != null) {
            throw new IllegalStateException();
        }
    }

    public static DataHolder getInstance() {
        return ourInstance;
    }

    public void addListener(InvalidationListener listener) {
        currentPlayer.addListener(listener);
    }

    public void addListener(ChangeListener<Player> changeListener) {
        currentPlayer.addListener(changeListener);
    }

    public Player getCurrentPlayer() {
        return currentPlayer.get();
    }

    public void setCurrentPlayer(Player player) {
        Condition.check().nonNull(player);
        currentPlayer.set(player);
    }
}
