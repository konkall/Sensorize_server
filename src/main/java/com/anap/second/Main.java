package com.anap.second;


import database.SqlConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 *The main class. It starts the Publisher and the Subscriber and the gui.
 */
public class Main extends Application   {
    /**
     * New instance for sql connection
     */
    public static SqlConnection db = new SqlConnection();

    /**
     * Loads the Main.fxml for the gui
     */
    @Override
    public void start(Stage primaryStage)  {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/main/java/Views/Main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    } catch(Exception e) {
        e.printStackTrace();
    }
    }

    /**
     * The main function.Starts the two threads (publisher and subscriber), and the gui.
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String[] args) throws SQLException {

    MsgQueue obj = new MsgQueue();
    SubscriberThread sub = new SubscriberThread("SubThread", obj);
    PublisherThread pub = new PublisherThread("PubThread" , obj);




       sub.start();
       pub.start();
       launch(args);

    }




}



