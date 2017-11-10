package gui.propertySheet;

import javafx.beans.property.Property;

/**
 *
 */
public class Item {
    private Property<?> value;
    private String description;
    private SubCategory subCategory;

    public Item(Property<?> property, String description, String category, String subCategory) {
        this.value = property;
        this.description = description;
        this.subCategory = SubCategory.createSubCategory(subCategory, category);
    }

    public Object getValue() {
        return value.getValue();
    }

    public Property<?> valueProperty() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    SubCategory getSubCategory() {
        return subCategory;
    }

    void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return "Item{" +
                "value=" + value +
                ", description='" + description + '\'' +
                ", subCategory=" + subCategory +
                '}';
    }
}
