package gui.propertySheet;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import tools.Condition;

import java.util.Arrays;

/**
 *
 */
public class PropertySheet extends Control {
    private final ObservableList<Item> items = FXCollections.observableArrayList();

    public PropertySheet() {
        setFocusTraversable(true);
    }

    public void addItem(Property<?> property, String descr, String category) {
        addItem(new Item(property, descr, category, "sub"));
    }

    public void addItem(Item item) {
        Condition.check().nonNull(item);
        items.add(item);
    }

    public void addItems(Item... items) {
        Condition.check().nonNull((Object[]) items);
        this.items.addAll(Arrays.asList(items));
    }

    public void removeItem(Item item) {
        Condition.check().nonNull(item);
        this.items.remove(item);
    }

    public void setSubCategoryMode() {
        // TODO: 10.11.2017 switch subCategory representations, from titledPane to tabs etc
    }

    public void setCategoryMode() {
        // TODO: 10.11.2017 switch category representations, from titledPane to tabs etc
    }

    @Override
    public String getUserAgentStylesheet() {
        return "css/setting.css";
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PropertySheetSkin(this);
    }

    ObservableList<Item> getItems() {
        return items;
    }
}
