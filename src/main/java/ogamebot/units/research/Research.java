package ogamebot.units.research;

import gorgon.external.DataAccess;
import gorgon.external.GorgonEntry;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ogamebot.comp.Cost;
import ogamebot.comp.UpgradeAble;
import ogamebot.data.daos.ResearchDao;

/**
 *
 */
@DataAccess(ResearchDao.class)
public class Research implements UpgradeAble, GorgonEntry {
    private final ResearchField fields;
    private final ReadOnlyStringProperty name;
    private IntegerProperty level = new SimpleIntegerProperty();


    public Research(ResearchField field) {
        this.fields = field;
        name = new SimpleStringProperty(field.getName());
    }

    @Override
    public Cost getCost() {
        return fields.getCost(getCounter());
    }

    @Override
    public IntegerProperty counterProperty() {
        return level;
    }

    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    @Override
    public int getCounter() {
        return level.get();
    }

    public static Research create(ResearchField field, Integer level) {
        final Research research = new Research(field);
        research.counterProperty().set(level);
        return research;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Research research = (Research) o;

        return getName().equals(research.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public ResearchField getType() {
        return fields;
    }

    @Override
    public String toString() {
        return "Research{" +
                "name=" + name.get() +
                ", level=" + level.get() +
                '}';
    }

    @Override
    public int compareTo(GorgonEntry gorgonEntry) {
        if (gorgonEntry == null) return -1;
        if (gorgonEntry == this) return 0;
        if (gorgonEntry.getClass() != getClass()) return -1;

        Research research = (Research) gorgonEntry;
        return getName().compareTo(research.getName());
    }
}
