package ogamebot.comp;

import java.util.List;

/**
 *
 */
public interface GameEntity {
    String getText();

    List<GameEntity> getChildren();
}
