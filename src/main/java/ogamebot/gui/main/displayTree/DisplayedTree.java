package ogamebot.gui.main.displayTree;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.TreeItem;
import ogamebot.comp.Player;
import ogamebot.comp.Universe;

import java.util.*;

/**
 *
 */
public class DisplayedTree {
    private TreeItem<TreeAble> root = new TreeItem<>();
    private Map<Universe, TreeItem<TreeAble>> uniWrapperMap = new TreeMap<>(Comparator.comparing(TreeAble::getName));
    private Map<Player, TreeItem<TreeAble>> playerWrapperMap = new TreeMap<>(Comparator.comparing(TreeAble::getName));


    public void add(Universe universe) {
        if (!uniWrapperMap.containsKey(universe)) {
            final TreeItem<TreeAble> item = new TreeItem<>(universe);

            item.setExpanded(true);
            uniWrapperMap.put(universe, item);

            final ObservableSet<Player> players = universe.getPlayers();

            players.addListener((SetChangeListener<? super Player>) observable -> {
                if (observable.getSet().isEmpty()) {
                    root.getChildren().remove(item);
                } else {
                    players.forEach(this::add);
                    root.getChildren().add(item);
                }
            });
            if (!players.isEmpty()) {
                players.forEach(this::add);
                root.getChildren().add(item);
            }
        }
    }

    public void add(Player player) {
        System.out.println("adding");
        final Universe universe = player.getUniverse();
        if (!uniWrapperMap.containsKey(universe)) {
            add(universe);
        }
        if (!playerWrapperMap.containsKey(player)) {
            final TreeItem<TreeAble> item = new TreeItem<>(player);

            uniWrapperMap.get(universe).getChildren().add(item);
            playerWrapperMap.put(player, item);

            universe.addPlayer(player);
        }
    }

    private void remove(Universe universe) {
        final TreeItem<TreeAble> item = uniWrapperMap.remove(universe);
        if (item != null) {
            root.getChildren().remove(item);
        }
    }

    private void remove(Player player) {
        final TreeItem<TreeAble> item = playerWrapperMap.remove(player);
        if (item != null) {
            final Universe universe = (player).getUniverse();
            uniWrapperMap.get(universe).getChildren().remove(item);
        }
    }

    public void remove(TreeAble able) {
        if (able instanceof Player) {
            remove((Player) able);
        } else if (able instanceof Universe) {
            remove((Universe) able);
        }
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(playerWrapperMap.keySet());
    }

    public List<Universe> getUniverses() {
        return new ArrayList<>(uniWrapperMap.keySet());
    }

    public TreeItem<TreeAble> getRoot() {
        return root;
    }
}
