package ogamebot.gui.fleet;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ogamebot.gui.DataHolder;
import ogamebot.units.astroObjects.Moon;
import ogamebot.units.astroObjects.Planet;
import org.controlsfx.control.SegmentedButton;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class FleetControl implements Initializable {

    @FXML
    private ScrollPane fleetControlContainer;

    @FXML
    private Text targetName;

    @FXML
    private ToggleButton planetTarget;

    @FXML
    private ToggleButton moonTarget;

    @FXML
    private ToggleButton debrisTarget;

    @FXML
    private TextField galaxy;

    @FXML
    private TextField system;

    @FXML
    private TextField position;

    @FXML
    private ComboBox<?> ownTargets;

    @FXML
    private ComboBox<?> lastTargets;

    @FXML
    private ToggleButton transportMission;

    @FXML
    private ToggleButton attackMIssion;

    @FXML
    private ToggleButton stationMission;

    @FXML
    private ToggleButton spionageMission;

    @FXML
    private ToggleButton breakDownMission;

    @FXML
    private ToggleButton colonizeMission;

    @FXML
    private ToggleButton destroyMission;

    @FXML
    private ToggleButton aksMission;

    @FXML
    private ToggleButton expeditionMission;

    @FXML
    private ToggleButton stopByMission;

    @FXML
    private ImageView bodyImage;

    @FXML
    private Text ownGalaxy;

    @FXML
    private Text ownSystem;

    @FXML
    private Text ownPosition;

    @FXML
    private Text distance;

    @FXML
    private Text flightTime;

    @FXML
    private Text fuelConsumption;

    @FXML
    private SegmentedButton speedFactorGroup;

    @FXML
    private TextField transportMetField;

    @FXML
    private TextField transportCrysField;

    @FXML
    private TextField transportDeutField;

    @FXML
    private ProgressBar capacityBar;

    @FXML
    private Text capacityDescriptor;

    @FXML
    private Text speed;

    @FXML
    private HBox holdTime;

    @FXML
    private ImageView kjImage;

    @FXML
    private TextField kJValue;

    @FXML
    private Button sendFleetBtn;

    @FXML
    private ImageView sJImage;

    @FXML
    private TextField sJValue;

    @FXML
    private ImageView xerImage;

    @FXML
    private TextField xerValue;

    @FXML
    private TextField ssValue;

    @FXML
    private ImageView ssImage;

    @FXML
    private ImageView kTImage;

    @FXML
    private TextField kTValue;

    @FXML
    private ImageView gTImage;

    @FXML
    private TextField gTValue;

    @FXML
    private ImageView koloImage;

    @FXML
    private TextField koloValue;

    @FXML
    private ImageView sxerImage;

    @FXML
    private TextField sxerValue;

    @FXML
    private ImageView bomberImage;

    @FXML
    private TextField bomberValue;

    @FXML
    private ImageView zerrImage;

    @FXML
    private TextField zerrValue;

    @FXML
    private ImageView ripImage;

    @FXML
    private TextField ripValue;

    @FXML
    private ImageView recImage;

    @FXML
    private TextField recValue;

    @FXML
    private ImageView spioImage;

    @FXML
    private TextField spioValue;

    private FleetSetter set;

    @FXML
    void allCrys() {

    }

    @FXML
    void allDeut() {

    }

    @FXML
    void allFleet() {

    }

    @FXML
    void allMet() {

    }

    @FXML
    void lastFleet() {

    }

    @FXML
    void noCrys() {

    }

    @FXML
    void noDeut() {

    }

    @FXML
    void noFleet() {

    }

    @FXML
    void noMet() {

    }

    @FXML
    void sendFleet() {

    }

    @FXML
    void showApi() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initGui();
    }

    private void initGui() {
        fleetControlContainer.disableProperty().bind(DataHolder.getInstance().currentBodyProperty().isNull());

        DataHolder.getInstance().currentBodyProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                bodyImage.setImage(null);
            } else {
                if (newValue instanceof Moon) {
                    bodyImage.setImage(new Image(getClass().getResource("/img/moonIconSelected.png").toExternalForm()));
                    bodyImage.setFitHeight(20);
                    bodyImage.setPreserveRatio(true);

                } else if (newValue instanceof Planet) {

                    bodyImage.setImage(new Image(getClass().getResource("/img/planetIconSelected.png").toExternalForm()));
                    bodyImage.setFitHeight(20);
                    bodyImage.setPreserveRatio(true);
                }
            }
        });
        set = FleetSetter.set(this);
    }
}
