package Controllers;

import javafx.fxml.FXML;
import Controllers.Tabs.Tab1Controller;
import Controllers.Tabs.Tab2Controller;

/**
 * The Main controller controls the two controllers of the tabs.
 */
public class MainController {
    /**
     * The Tab 1 controller.
     */
    @FXML Tab1Controller tab1Controller;
    /**
     * The Tab 2 controller.
     */
    @FXML Tab2Controller tab2Controller;

    /**
     * Controls the refresh button of the tab 1 controller.
     */
    @FXML public void initialize() {
        tab1Controller.RefreshButton();
    }

}
