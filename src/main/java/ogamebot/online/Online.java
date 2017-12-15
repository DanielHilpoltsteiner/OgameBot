package ogamebot.online;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Online {
    private static Online module = new Online();
    private volatile boolean online = false;
    private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private boolean init = false;

    private Online() {
        if (module != null) {
            throw new IllegalStateException();
        }
        startPing();
    }

    public static boolean isOnline() {
        if (!module.init) {
            module.ping();
            module.init = true;
        }
        return module.online;
    }

    private void startPing() {
        scheduledExecutor.scheduleAtFixedRate(this::ping, 15, 15, TimeUnit.SECONDS);
    }

    private void ping() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://de.ogame.gameforge.com/").openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            online = 200 <= responseCode && responseCode <= 399;
        } catch (IOException exception) {
            online = false;
        }
    }
}
