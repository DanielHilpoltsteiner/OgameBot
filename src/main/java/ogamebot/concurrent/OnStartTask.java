package ogamebot.concurrent;

import ogamebot.comp.Universe;
import ogamebot.data.DataManager;
import ogamebot.online.OgameServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class OnStartTask implements Callable<List<Universe>> {
    private static OnStartTask task = new OnStartTask();
    private ExecutorService service = Executors.newFixedThreadPool(3);
    private Future<List<Universe>> submit;

    private OnStartTask() {
        if (task != null) {
            throw new IllegalStateException();
        }
    }

    public static void start() {
        if (task.service.isShutdown()) {
            throw new IllegalStateException("Task can be executed only once");
        }
        task.submit = task.service.submit(task);
    }

    public static List<Universe> get() {
        if (task.submit == null) {
            throw new IllegalStateException();
        }
        try {
            final List<Universe> universes = task.submit.get();
            if (universes != null) {
                task.service.shutdown();
            }
            return universes;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Universe> call() throws Exception {
        final Future<List<Universe>> onlineServer = service.submit(OgameServer::getServer);
        final Future<List<Universe>> savedServer = service.submit(DataManager::get);

        final List<Universe> server = onlineServer.get();
        final List<Universe> universes = savedServer.get();

        final ArrayList<Universe> copySavedServer = new ArrayList<>(universes);
        copySavedServer.removeAll(server);

        if (!copySavedServer.isEmpty()) {
            //todo 11.12.2017 issue warning to application for possible deleted server
        }

        server.removeAll(universes);
        universes.addAll(server);
        server.forEach(DataManager::add);

        return universes;
    }
}
