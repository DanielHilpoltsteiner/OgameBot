package ogamebot.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import ogamebot.comp.Player;
import ogamebot.units.astroObjects.CelestialBody;
import tools.Condition;

/**
 *
 */
public class DataHolder {
    private static DataHolder ourInstance = new DataHolder();
    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
    private ObjectProperty<CelestialBody> currentBody = new SimpleObjectProperty<>();

    private DataHolder() {
        if (ourInstance != null) {
            throw new IllegalStateException();
        }
    }

    public static DataHolder getInstance() {
        return ourInstance;
    }

    public void addPlayerListener(InvalidationListener listener) {
        currentPlayer.addListener(listener);
    }

    public void addPlayerListener(ChangeListener<Player> changeListener) {
        currentPlayer.addListener(changeListener);
    }

    public Player getCurrentPlayer() {
        return currentPlayer.get();
    }

    public void setCurrentPlayer(Player player) {
        Condition.check().nonNull(player);
        currentPlayer.set(player);
    }

    public CelestialBody getCurrentBody() {
        return currentBody.get();
    }

    public void setCurrentBody(CelestialBody currentBody) {
        this.currentBody.set(currentBody);
    }

    public ReadOnlyObjectProperty<CelestialBody> currentBodyProperty() {
        return currentBody;
    }
}
