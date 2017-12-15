package ogamebot.units.building;

import javafx.beans.binding.NumberBinding;
import ogamebot.units.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class Buildings extends Container<Building, BuildingType> {
    private NumberBinding usedFields;

    private Buildings(Collection<BuildingType> types) {
        types.forEach(type -> {
            final Building value = type.create();
            if (usedFields == null) {
                usedFields = value.counterProperty().add(0);
            } else {
                usedFields = usedFields.add(value.counterProperty());
            }
            put(type, value);
        });
    }

    private Buildings(List<Building> buildings) {
        buildings.forEach(type -> {
            if (usedFields == null) {
                usedFields = type.counterProperty().add(0);
            } else {
                usedFields = usedFields.add(type.counterProperty());
            }
            put(type.getType(), type);
        });
    }

    public static Buildings planetBuildings() {
        BuildingType[] buildingTypes = getPlanetTypes();
        return new Buildings(Arrays.asList(buildingTypes));
    }

    public static Buildings planetBuildings(Collection<Building> collection) {
        return getBuildings(collection, Arrays.asList(getPlanetTypes()));
    }

    private static BuildingType[] getPlanetTypes() {
        return new BuildingType[]{
                BuildingType.METALMINE,
                BuildingType.CRYSTALMINE,
                BuildingType.DEUTSYNTH,
                BuildingType.SOLARPLANT,
                BuildingType.FUSIONPLANT,
                BuildingType.METALSTORAGE,
                BuildingType.CRYSTALSTORAGE,
                BuildingType.DEUTSTORAGE,
                BuildingType.ROBOFACTORY,
                BuildingType.SPACESHIPSHIPYARD,
                BuildingType.RESEARCHLAB,
                BuildingType.ALLIANCEDEPOT,
                BuildingType.ROCKETSILO,
                BuildingType.NANOFACTORY,
                BuildingType.TERRAFORMER,
                BuildingType.SPACEDOCK,
        };
    }

    public static Buildings moonBuildings() {
        BuildingType[] buildingTypes = getMoonTypes();
        return new Buildings(Arrays.asList(buildingTypes));
    }

    public static Buildings moonBuildings(Collection<Building> collection) {
        final List<BuildingType> allowedTypes = Arrays.asList(getMoonTypes());
        return getBuildings(collection, allowedTypes);
    }

    private static Buildings getBuildings(Collection<Building> collection, List<BuildingType> allowedTypes) {
        List<BuildingType> allowed = new ArrayList<>(allowedTypes);
        for (Building building : collection) {
            //should not throw anything, BuildingType implements UnitType<Building>
            if (!allowedTypes.contains(building.getType())) {
                throw new IllegalArgumentException();
            }
            allowed.remove(building.getType());
        }
        final List<Building> buildings = new ArrayList<>(collection);

        if (!allowed.isEmpty()) {
            allowed.forEach(type -> buildings.add(type.create()));
        }
        return new Buildings(buildings);
    }

    private static BuildingType[] getMoonTypes() {
        return new BuildingType[]{
                BuildingType.METALSTORAGE,
                BuildingType.CRYSTALSTORAGE,
                BuildingType.DEUTSTORAGE,
                BuildingType.ROBOFACTORY,
                BuildingType.MOONBASE,
                BuildingType.SENSORPHALANX,
                BuildingType.JUMPG_GATE,
                BuildingType.SPACESHIPSHIPYARD,
        };
    }

    public Number getUsedFields() {
        return usedFields.intValue();
    }

    public NumberBinding usedFieldsProperty() {
        return usedFields;
    }

}
