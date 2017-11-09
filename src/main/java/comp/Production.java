package comp;

import units.Building.production.mines.CrystalMine;
import units.Building.production.mines.DeutSynth;
import units.Building.production.mines.MetalMine;

/**
 *
 */
public class Production {
    private final MetalMine metalMine;
    private final CrystalMine crystalMine;
    private final DeutSynth deutSynth;

    public Production(MetalMine metalMine, CrystalMine crystalMine, DeutSynth deutSynth) {
        this.metalMine = metalMine;
        this.crystalMine = crystalMine;
        this.deutSynth = deutSynth;
    }

    public int getMetProd() {
        return 0;
    }

    public int getCrysProd() {
        return 0;
    }

    public int getDeutProd() {
        return 0;
    }
}
