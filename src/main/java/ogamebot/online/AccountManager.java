package ogamebot.online;

import ogamebot.comp.Player;
import ogamebot.comp.Universe;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class AccountManager {
    private static AccountManager manager = new AccountManager();

    private Map<Player, AccountAccess> accessMap = new TreeMap<>(getComparator());

    public static AccountManager getInstance() {
        return manager;
    }

    public void add(Player player, String mail, String password, Universe universe) throws IOException {
        final AccountAccess access = new AccountAccess(universe);
        access.update(player, mail, password);
        accessMap.put(player, access);
    }

    public Player createPlayer(String mail, String password, Universe universe) throws IOException {
        final AccountAccess access = new AccountAccess(universe);
        final Player player = access.getPlayer(mail, password);

        if (player != null) {
            accessMap.put(player, access);
        }

        return player;
    }

    public void finish() {
        accessMap.values().forEach(AccountAccess::logOut);
    }

    public void update() throws IOException, AccountException {
        for (Map.Entry<Player, AccountAccess> entry : accessMap.entrySet()) {
            entry.getValue().update(entry.getKey());
        }
    }

    private Comparator<Player> getComparator() {
        return Comparator.comparing(Player::getName).thenComparing(Player::getUniverse);
    }
}
