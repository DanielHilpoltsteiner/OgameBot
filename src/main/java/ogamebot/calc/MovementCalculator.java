package ogamebot.calc;

import ogamebot.comp.Position;
import ogamebot.comp.Reachable;
import ogamebot.comp.Universe;
import ogamebot.units.warfare.Fleet;

/**
 *
 */
public class MovementCalculator {


    /**
     * Calculate the time of movement between the current owner of the fleet
     * to the specified goal position.
     *
     * @param fleet fleet to move
     * @param goal  goal to fly to
     * @return the duration in seconds
     */
    public double moveFleet(Fleet fleet, Reachable goal) {
        final Position startPosition = fleet.getOwner().getPosition();

        final Universe universe = fleet.getOwner().getPlayer().getUniverse();
        final double flightSpeed = universe.getFlightSpeed();
        final int maxSpeed = fleet.getMaxSpeed();

        return calcMovingTime(goal, startPosition, universe, flightSpeed, maxSpeed);
    }

    private double calcMovingTime(Reachable goal, Position startPosition, Universe universe, double flightSpeed, int maxSpeed) {
        double flightSpeedFactor = 3500d / flightSpeed;
        double maxSpeedFactor = 10d / maxSpeed;

        double distance = calcDistance(startPosition, universe, goal.getPosition());
        double distanceFactor = distance * maxSpeedFactor;
        distanceFactor = Math.sqrt(distanceFactor);

        double speed = flightSpeedFactor * distanceFactor;
        return speed + 10;
    }

    private int calcDistance(Position startPosition, Universe universe, Position goalPosition) {
        int distance;

        final int startGalaxy = startPosition.getGalaxy();
        final int goalGalaxy = goalPosition.getGalaxy();

        if (startGalaxy != goalGalaxy) {
            distance = galaxyDistance(universe, startGalaxy, goalGalaxy);
            distance *= 20_000;
        } else {
            final int startSolar = startPosition.getSolarSystem();
            final int endSolar = goalPosition.getSolarSystem();

            if ((startSolar != endSolar)) {
                distance = solarDistance(universe, startSolar, endSolar);
                distance *= 95;
                distance += 2_700;

            } else {
                final int startSystem = startPosition.getSystemPosition();
                final int endSystem = goalPosition.getSystemPosition();

                if (startSystem != endSystem) {
                    distance = systemDistance(startSystem, endSystem);
                    distance *= 5;
                    distance += 1_000;
                } else {
                    distance = 5;
                }
            }
        }

        return distance;
    }

    private int solarDistance(Universe universe, int start, int end) {
        int distance;

        final int normalDistance = Math.abs(start - end);
        if (universe.isDonut()) {
            final int systems_max = universe.getSystems_max();

            int donutDistance = calcDonut(start, end, systems_max);
            distance = Math.min(normalDistance, donutDistance);
        } else {
            distance = normalDistance;
        }

        return distance;
    }

    private int calcDonut(int start, int end, int max) {
        if (max < start || max < end) {
            throw new IllegalArgumentException();
        }

        final double half = max / 2d;

        int first = start;
        int second = end;

        if (start > half) {
            first = max - first;
        }

        if (end > half) {
            second = max - second;
        }
        final int distance = Math.abs(first - second);

        if (distance > half) {
            throw new ArithmeticException();
        }
        return distance;
    }

    private int systemDistance(int start, int end) {
        return Math.abs(start - end);
    }

    private int galaxyDistance(Universe universe, int start, int end) {
        int distance;

        final int normalDistance = Math.abs(start - end);

        if (universe.isDonut()) {
            final int galaxy_max = universe.getGalaxies_max();

            int donutDistance = calcDonut(start, end, galaxy_max);
            distance = Math.min(normalDistance, donutDistance);
        } else {
            distance = normalDistance;
        }

        return distance;
    }
}
