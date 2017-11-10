package gui.propertySheet;

import javafx.scene.Node;
import javafx.scene.control.Control;

/**
 *
 */
class NodeWrapper {
    private Control editor;
    private Node descriptor;


    NodeWrapper(Control editor, Node descriptor) {
        this.editor = editor;
        this.descriptor = descriptor;
    }

    public Node getDescriptor() {
        return descriptor;
    }

    public Control getEditor() {
        return editor;
    }
}
