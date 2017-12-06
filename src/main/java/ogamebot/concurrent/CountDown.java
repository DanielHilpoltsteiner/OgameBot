package ogamebot.concurrent;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 */
public class CountDown {
    private static CountDown countDown = new CountDown();
    private final Timeline timeline;

    private List<ObjectProperty<ZonedDateTime>> instants = new ArrayList<>();
    private List<DurationLimit> countDownList = new ArrayList<>();
    private List<DurationLimit> countUpList = new ArrayList<>();

    private boolean init = false;

    private CountDown() {
        if (countDown != null) {
            throw new IllegalStateException();
        }
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> refresh()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void shutDown() {
        countDown.timeline.stop();
    }

    public static CountDown getWorker() {
        if (!countDown.init) {
            countDown.init = true;
        }
        return countDown;
    }

    public void addCountDown(ObjectProperty<Duration> duration, Duration limit) {
        countDownList.add(new DurationLimit(duration, limit));
    }

    public void addCountUp(ObjectProperty<Duration> duration) {
        countUpList.add(new DurationLimit(duration, Duration.ofDays(999999999)));
    }

    public void refreshInstant(ObjectProperty<ZonedDateTime> instantProperty) {
        instants.add(instantProperty);
    }

    public void remove(ObjectProperty<ZonedDateTime> instantProperty) {
        instants.remove(instantProperty);
    }

    private void refresh() {
        instants.forEach(property -> property.set(property.get().plusSeconds(1)));
        refreshDuration((Duration::minusSeconds), countDownList);
        refreshDuration((Duration::plusSeconds), countUpList);
    }

    private void refreshDuration(BiFunction<Duration, Integer, Duration> function, List<DurationLimit> limits) {
        for (DurationLimit limit : new ArrayList<>(limits)) {
            final Duration value = function.apply(limit.duration.get(), 1);
            if (value.equals(limit.limit)) {
                limits.remove(limit);
            }
            limit.duration.set(value);
        }
    }

    private class DurationLimit {
        private final ObjectProperty<Duration> duration;
        private final Duration limit;

        DurationLimit(ObjectProperty<Duration> duration, Duration limit) {
            this.duration = duration;
            this.limit = limit;
        }
    }
}
