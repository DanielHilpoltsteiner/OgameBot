package units.research;

import comp.Research;

/**
 *
 */
public enum ResearchTypes {
    ENERGY_TECH,
    LASER_TECH,
    ION_TECH,
    PLASMA_TECH,
    COMBUSTION_ENGINE,
    IMPULSE_ENGINGE,
    HYPERSPACE_DRIVE,
    SPIONAGE_TECH,
    ASTROPHYSICS,
    INTERGALACTIC_RESEARCHNETWORK,
    GRAVITON_RESEARCH,
    WEAPON_TECH,
    SHIP_PLATING,
    SHIELD_TECH;

    public Research create() {
        return new Research();
    }
}
