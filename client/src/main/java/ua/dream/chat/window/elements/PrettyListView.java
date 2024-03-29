package ua.dream.chat.window.elements;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

import java.util.Set;

public class PrettyListView<T> extends ListView<T> {

    private ScrollBar vBar = new ScrollBar();
    private ScrollBar hBar = new ScrollBar();

    public PrettyListView() {
        super();

        skinProperty().addListener(it -> {
            // first bind, then add new scrollbars, otherwise the new bars will be found
            bindScrollBars();
            getChildren().addAll(vBar, hBar);
        });

        getStyleClass().add("pretty-list-view");

        heightProperty().addListener((obs, oldValue, newValue) -> {
            vBar.setVisible(getHeight() < getFixedCellSize() * getItems().size());
        });

        vBar.setManaged(false);
        vBar.setOrientation(Orientation.VERTICAL);
        vBar.getStyleClass().add("pretty-scroll-bar");

        hBar.setManaged(false);
        hBar.setOrientation(Orientation.HORIZONTAL);
        hBar.getStyleClass().add("pretty-scroll-bar");
        hBar.visibleProperty().bind(hBar.visibleAmountProperty().greaterThan(0));

        setFixedCellSize(58d);
    }

    private void bindScrollBars() {
        final Set<Node> nodes = lookupAll("VirtualScrollBar");
        for (Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) node;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    bindScrollBars(vBar, bar);
                } else if (bar.getOrientation().equals(Orientation.HORIZONTAL)) {
                    bindScrollBars(hBar, bar);
                }
            }
        }
    }

    private void bindScrollBars(ScrollBar scrollBarA, ScrollBar scrollBarB) {
        scrollBarA.valueProperty().bindBidirectional(scrollBarB.valueProperty());
        scrollBarA.minProperty().bindBidirectional(scrollBarB.minProperty());
        scrollBarA.maxProperty().bindBidirectional(scrollBarB.maxProperty());
        scrollBarA.visibleAmountProperty().bindBidirectional(scrollBarB.visibleAmountProperty());
        scrollBarA.unitIncrementProperty().bindBidirectional(scrollBarB.unitIncrementProperty());
        scrollBarA.blockIncrementProperty().bindBidirectional(scrollBarB.blockIncrementProperty());
        ;
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();

        Insets insets = getInsets();
        double w = getWidth();
        double h = getHeight();

        final double prefWidth = vBar.prefWidth(-1);
        final double prefHeight = hBar.prefHeight(-1);
        vBar.resizeRelocate(w - prefWidth - insets.getRight(), insets.getTop(), prefWidth, h - insets.getTop() - insets.getBottom());
        hBar.resizeRelocate(insets.getLeft(), h - prefHeight - insets.getBottom(), w - insets.getLeft() - insets.getRight(), prefHeight);
    }

}
