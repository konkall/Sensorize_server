package Controllers.Tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import com.anap.second.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * The Tab 1 controller initializes all the gui of tab 1.
 */
public class Tab1Controller {

    //TABLE VIEW AND DATA
    private final static int rowsPerPage = 13;
    @FXML
    private ObservableList<ObservableList> data;
    @FXML
    private TableView tableview = new TableView();
    @FXML
    private Pagination paginationtable;
    @FXML
    private TextField MobileDevice1;
    @FXML
    private TextField MobileDevice2;
    @FXML
    private TextField SensorType;
    @FXML
    private TextField SensorValues;
    @FXML
    private TextField Longitude;
    @FXML
    private TextField Latitude;
    @FXML
    private TextField Date;
    @FXML
    private TextField Time2;
    @FXML
    private TextField Time1;
    @FXML
    private TextField ConfirmedCollision;
    @FXML
    private Button add;
    @FXML
    private Button refreshbutton;
    @FXML
    private Button deletebutton;

    private String style;


    /**
     * Connects to the database and creates dynamically the database table.
     * It also takes the arguments for the search button.
     */
//CONNECTION DATABASE
    @FXML
    public void buildData() {
        Connection c;
        data = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER

            String SQL;
            SQL = search_database(dynamic_query());
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                    row.add(rs.getString(i));

                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            tableview.setItems(data);
            paginationtable.setCurrentPageIndex(0);
            paginationtable.setPageCount((data.size() / rowsPerPage + 1));
            paginationtable.setPageFactory(this::createPage);


            //FINALLY ADDED TO TableView


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

    }


    /**
     * Create the pagination of tableview.
     *
     * @param pageIndex the page index
     * @return the node
     */
    public Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        tableview.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        tableview.prefHeightProperty();
        tableview.minHeightProperty();
        tableview.maxHeightProperty();
        tableview.setFixedCellSize(35);


        return new BorderPane(tableview);
    }


    /**
     * Implements the search of the gui. Takes the query and runs it in the database.
     *
     * @param dynamic_query the dynamic query
     * @return the string
     */
    public String search_database(String dynamic_query) {


        if (!MobileDevice1.getText().isEmpty() || !MobileDevice2.getText().isEmpty() || !SensorType.getText().isEmpty() ||
                !SensorValues.getText().isEmpty() || !Date.getText().isEmpty() || !Time1.getText().isEmpty() || !Time2.getText().isEmpty()
                || !Latitude.getText().isEmpty() || !ConfirmedCollision.getText().isEmpty()) {

            System.out.println("...Return dynamic query.....");
            String sql = "SELECT * FROM collisions WHERE " + dynamic_query + ";";
            return sql;

        } else {

            System.out.println("return all!!! ");
            String sql = "SELECT * FROM collisions;";
            return sql;
        }
    }


    /**
     * It creates the correct queries to run on the database.
     *
     * @return the string
     */
    public String dynamic_query() {


        String dynamic_query = "";

        if (!MobileDevice1.getText().isEmpty()) {
            dynamic_query = dynamic_query + " UUID=" + "'" + MobileDevice1.getText() + "'" + "and";

        }

        if (!MobileDevice2.getText().isEmpty()) {
            dynamic_query = dynamic_query + " UUID=" + "'" + MobileDevice2.getText() + "'" + "and";

        }

        if (!SensorType.getText().isEmpty()) {
            dynamic_query = dynamic_query + " sensor_type LIKE " + "'%" + SensorType.getText() + "%'" + "and";

        }

        if (!SensorValues.getText().isEmpty()) {
            dynamic_query = dynamic_query + " sensor_values=" + "'%" + SensorValues.getText() + "%'" + "and";

        }

        if (!Time1.getText().isEmpty()) {
            dynamic_query = dynamic_query + " time=" + "'" + Time1.getText() + "'" + "and";

        }

        if (!Time2.getText().isEmpty()) {
            dynamic_query = dynamic_query + " time=" + "'" + Time2.getText() + "'" + "and";

        }

        if (!Latitude.getText().isEmpty()) {
            dynamic_query = dynamic_query + " latitude=" + "'" + Latitude.getText() + "'" + "and";

        }

        if (!Longitude.getText().isEmpty()) {
            dynamic_query = dynamic_query + " longitude=" + "'" + Longitude.getText() + "'" + "and";

        }

        if (!ConfirmedCollision.getText().isEmpty()) {
            dynamic_query = dynamic_query + " confirmed_collision=" + "'" + ConfirmedCollision.getText() + "'" + "and";

        }

        if (dynamic_query.endsWith("and")) {
            dynamic_query = dynamic_query + "remove";
        }

        dynamic_query = dynamic_query.replace("andremove", "");
        System.out.println("------------------>" + dynamic_query);
        return dynamic_query;
    }


    /**
     * It controls the refresh button with event handler. It refreshes the database.
     */
    public void RefreshButton() {
        refreshbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Connection c;
                data = FXCollections.observableArrayList();
                try {
                    c = DBConnect.connect();
                    //SQL FOR SELECTING ALL OF CUSTOMER

                    String SQL;
                    SQL = "SELECT * FROM collisions";
                    //ResultSet
                    ResultSet rs = c.createStatement().executeQuery(SQL);

                    /**********************************
                     * TABLE COLUMN ADDED DYNAMICALLY *
                     **********************************/
                    for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                        //We are using non property style for making dynamic table
                        final int j = i;
                        TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                        col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                                return new SimpleStringProperty(param.getValue().get(j).toString());
                            }
                        });

                        tableview.getColumns().addAll(col);
                        //System.out.println("Column [" + i + "] ");
                    }

                    /********************************
                     * Data added to ObservableList *
                     ********************************/
                    while (rs.next()) {
                        //Iterate Row
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            //Iterate Column
                            row.add(rs.getString(i));

                        }
                        System.out.println("Row [1] added " + row);
                        data.add(row);

                    }

                    //FINALLY ADDED TO TableView
                    tableview.setItems(data);
                    paginationtable.setCurrentPageIndex(0);
                    paginationtable.setPageCount((data.size() / rowsPerPage + 1));
                    paginationtable.setPageFactory(Tab1Controller.this::createPage);


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error on Building Data");
                }

            }


        });

    }

    /*public void DeleteButton() {
        deletebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                data = FXCollections.observableArrayList();
                try {
                    c = Main.db.SQL DBConnect.connect();
                    //SQL FOR SELECTING ALL OF CUSTOMER

                    String SQL;
                    SQL = "DELETE FROM collisions";
                    ResultSet rs = c.createStatement().executeQuery(SQL);



                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error on Building Data");

                }
            }
        });
    }*/
}