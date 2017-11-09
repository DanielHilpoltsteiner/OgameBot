package units.astroObjects;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import units.Building.Building;
import units.Building.MoonBuilding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class Moon implements CelestialBody {
    private int maxT;
    private int fields;
    private String name;
    private NumberBinding usedFields;

    private Planet planet;
    private Map<MoonBuilding, Building> buildingMap = new HashMap<>();

    @Override
    public String getName() {
        return name;
    }

    public Building getBuilding(MoonBuilding type) {
        Building building = buildingMap.get(type);

        if (building == null) {
            building = type.create();

            if (usedFields == null) {
                usedFields = Bindings.add(0, building.levelProperty());
            } else {
                usedFields.add(building.levelProperty());
            }

            buildingMap.put(type, building);
        }

        return building;
    }

    void setPlanet(Planet planet) {
        Objects.requireNonNull(planet);
        this.planet = planet;
    }
}
