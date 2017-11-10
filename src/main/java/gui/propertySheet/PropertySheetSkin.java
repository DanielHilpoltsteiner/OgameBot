package gui.propertySheet;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class PropertySheetSkin extends SkinBase<PropertySheet> {
    private Category UNASSIGNED_CAT = new Category("Allgemeine Einstellungen", true);

    private BorderPane root;
    private ListView<Category> categoryListView = new ListView<>();
    private VBox subCategoryContainer = new VBox();
    private VBox propertiesContainer = new VBox();
    private GridPane propertySheet = new GridPane();

    private MapProperty<Category, ObservableSet<SubCategory>> categoryMap = new SimpleMapProperty<>(FXCollections.observableHashMap());

    private Map<SubCategory, TitledPane> catTitlePaneMap = new HashMap<>();
    private Map<SubCategory, ObservableList<Item>> itemMap = new HashMap<>();

    private ListProperty<SubCategory> subCategoriesForDisplay = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<Item> items;

    private BooleanProperty hasCategories = new SimpleBooleanProperty();
    private Map<Item, NodeWrapper> subCatNodeMap = new HashMap<>();
    //display listView on the root
    private ChangeListener<Boolean> hasCategoryListener = (observable, oldValue, newValue) -> {
        if (newValue) {
            // TODO: 10.11.2017 display leftover properties on propertyContainer
            root.setLeft(categoryListView);
        } else {
            root.setLeft(null);
        }
    };
    //adds a categoryListView left to the propertyContainer if the first category is added
    //adds/removes keys of this map as items of the categoryListView
    private MapChangeListener<? super Category, ? super ObservableSet<SubCategory>> categoryListener = observable -> {
        if (observable.getMap().keySet().stream().allMatch(Category::isInvisible)) {
            hasCategories.set(false);
            subCategoriesForDisplay.clear();
            subCategoriesForDisplay.addAll(categoryMap.get(UNASSIGNED_CAT));
        } else {
            if (observable.wasAdded()) {
                if (!categoryListView.getItems().contains(UNASSIGNED_CAT) && categoryMap.containsKey(UNASSIGNED_CAT)) {
                    categoryListView.getItems().add(UNASSIGNED_CAT);
                }
                hasCategories.set(true);
                categoryListView.getItems().add(observable.getKey());

                if (categoryListView.getSelectionModel().isEmpty()) {
                    categoryListView.getSelectionModel().selectFirst();
                }
            } else {
                if (observable.getMap().isEmpty()) {
                    hasCategories.set(false);
                } else {
                    categoryListView.getItems().remove(observable.getKey());
                }
            }
        }
    };
    private ListChangeListener<? super SubCategory> subCategoryListener = c -> {
        if (c.next()) {
            if (c.wasAdded()) {
                c.getAddedSubList().forEach(subCat -> {

                    if (!subCat.isInvisible()) {
                        TitledPane value;

                        if (!catTitlePaneMap.containsKey(subCat)) {

                            value = getTitledPane(subCat);
                            catTitlePaneMap.put(subCat, value);
                        } else {
                            System.out.println("contained");
                            value = catTitlePaneMap.get(subCat);
                        }

                        subCategoryContainer.getChildren().add(value);
                        layoutItems((GridPane) value.getContent(), itemMap.get(subCat));
                    } else {
                        layoutItems(propertySheet, itemMap.get(subCat));
                    }
                });
            } else if (c.wasRemoved()) {
                c.getRemoved().
                        stream().
                        flatMap(subCat -> itemMap.get(subCat).stream()).
                        forEach(item -> {
                            SubCategory subCat = item.getSubCategory();

                            Node descriptor = subCatNodeMap.get(item).getDescriptor();
                            Control editor = subCatNodeMap.get(item).getEditor();

                            if (subCat.isInvisible()) {
                                propertySheet.getChildren().remove(editor);
                                propertySheet.getChildren().remove(descriptor);
                            } else {
                                Node content = catTitlePaneMap.get(subCat).getContent();

                                subCategoryContainer.getChildren().remove(catTitlePaneMap.get(subCat));
                                ((GridPane) content).getChildren().remove(editor);
                                ((GridPane) content).getChildren().remove(descriptor);
                            }
                        });
            }
        }
    };

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected PropertySheetSkin(PropertySheet control) {
        super(control);

        addListener();
        init();
        initItems(control);
        root.setTop(new Label("Einstellungen"));

        root.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> root.requestFocus());
        getChildren().add(root);
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return root.minWidth(height);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return root.minHeight(width);
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return root.maxWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return root.maxHeight(width);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return root.prefWidth(height);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return root.prefHeight(width);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        root.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
    }

    private void displayItems() {
    }

    private void addListener() {
        hasCategories.addListener(hasCategoryListener);

        categoryMap.addListener(categoryListener);

        //if a category was selected: display mapped subCategories
        categoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableSet<SubCategory> subCategories = categoryMap.get(newValue);
                subCategoriesForDisplay.clear();
                subCategoriesForDisplay.addAll(subCategories);
            }
        });

        subCategoriesForDisplay.addListener(subCategoryListener);
    }

    private void initItems(PropertySheet control) {
        items = new SimpleListProperty<>(control.getItems());

        items.addListener((ListChangeListener<? super Item>) c -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    addItems(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    removeItems(c.getRemoved());
                }
            }
        });
        addItems(control.getItems());
    }

    private void init() {
        root = new BorderPane();

        root.getStyleClass().add("setting-container");
        categoryListView.getStyleClass().add("category-list-view");
        propertiesContainer.getStyleClass().add("properties-container");
        subCategoryContainer.getStyleClass().add("sub-category-container");
        propertySheet.getStyleClass().add("leftover-property-grid");

        categoryListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        root.setCenter(propertiesContainer);
        propertiesContainer.getChildren().add(subCategoryContainer);
        propertiesContainer.getChildren().add(propertySheet);
    }

    private void display() {

    }

    private void addItems(List<? extends Item> items) {
        Map<SubCategory, ObservableList<Item>> collect = items.
                stream().
                collect(Collectors.groupingBy(Item::getSubCategory, Collectors.toCollection(FXCollections::observableArrayList)));


        //every category with null or an empty String as name value will be invisible to the listView
        Map<Category, List<SubCategory>> catMap = collect.
                keySet().
                stream().
                collect(Collectors.groupingBy(sub -> {
                    String name = sub.getCategory().getName();
                    if (name == null || name.isEmpty()) {
                        sub.setCategory(UNASSIGNED_CAT);
                        return UNASSIGNED_CAT;
                    }
                    return sub.getCategory();
                }));


        this.itemMap.putAll(collect);
        System.out.println(collect.keySet());
        catMap.keySet().forEach(s -> addCategory(catMap.get(s), s));
    }

    private void addCategory(List<SubCategory> subCategories, Category category) {
        if (categoryMap.containsKey(category)) {
            categoryMap.get(category).addAll(subCategories);
        } else {
            ObservableSet<SubCategory> set = FXCollections.observableSet();
            set.addAll(subCategories);
            categoryMap.put(category, set);
        }
    }

    private void removeItems(List<? extends Item> items) {

    }

    private TitledPane getTitledPane(SubCategory subCategory) {
        TitledPane pane = new TitledPane();
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("sub-category-grid");

        pane.setContent(gridPane);
        pane.setText(subCategory.getSubCategory());
        pane.getStyleClass().add("sub-category");
        return pane;
    }

    private Control getEditor() {
        return new TextField();
    }

    private void layoutItems(GridPane pane, List<Item> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            NodeWrapper nodeWrapper = subCatNodeMap.get(item);

            int rowCount = pane.getRowCount();
            System.out.println(rowCount);

            if (rowCount != 0) {
                rowCount = rowCount + i;
            }

            System.out.println(items);

            if (nodeWrapper == null) {
                Text text = new Text(item.getDescription());
                Control textField = getEditor();

                pane.add(text, 0, rowCount);
                pane.add(textField, 1, rowCount);

                subCatNodeMap.put(item, new NodeWrapper(textField, text));
            } else {
                Node descriptor = nodeWrapper.getDescriptor();
                Control editor = nodeWrapper.getEditor();

                if (!pane.getChildren().contains(descriptor) && !pane.getChildren().contains(editor)) {
                    pane.add(descriptor, 0, rowCount);
                    pane.add(editor, 1, rowCount);
                }
            }
        }
    }
}
