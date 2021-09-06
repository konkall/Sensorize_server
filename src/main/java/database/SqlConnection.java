package database;


import java.sql.*;

/**
 * The Sql connection class
 */
public class SqlConnection {

    /**
     * Initalizes the connection
     */
    public Connection con = null;
    /**
     * Initializes the statement
     */
    public Statement stmt = null;

    /**
     * It connects to the database.
     */
    public void sqlConnect() {
        try {

            String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
            String MYSQL_URL = "jdbc:mysql://localhost:3306/sensorize";
            final String USER = "root";
            final String PASS = "overdose93";

            Class.forName(MYSQL_DRIVER);
            System.out.println("Loading....");
            con = DriverManager.getConnection(MYSQL_URL, USER, PASS);
            System.out.println("Connected to the database....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create tables of the database.
     */
    public void Create_Table(){
            try {
                stmt = con.createStatement();
                String sql = "CREATE TABLE collisions"
                        + "(UUID VARCHAR(25),"
                        + "sensor_type VARCHAR(100),"
                        + "sensor_values VARCHAR(100),"
                        + "date VARCHAR(25),"
                        + "time VARCHAR(25),"
                        + "latitude VARCHAR(25),"
                        + "longitude VARCHAR(25),"
                        + "confirmed_collision VARCHAR(5))";
                stmt.executeUpdate(sql);
                System.out.println("Table has been created.");
                stmt.close();

            } catch (SQLException ex) {
                System.out.println("SQLException:\n" + ex.toString());
                ex.printStackTrace();
            }
        }

    /**
     * It inserts values in the database.
     *
     * @param UUID          the uuid
     * @param Sensor_Type   the sensor type
     * @param Sensor_Values the sensor values
     * @param Date          the date
     * @param Time          the time
     * @param Latitude      the latitude
     * @param Longitude     the longitude
     */
    public void table_insert(String UUID, String Sensor_Type, String Sensor_Values,
                      String Date, String Time, String Latitude, String Longitude) {
        try {
            //INSERT INTO TABLE_NAME VALUES (value1,value2,value3,...valueN);

            stmt = con.createStatement();

            System.out.println("Inserting into table.....");
            String sql = "INSERT INTO collisions(UUID, sensor_type, sensor_values, " +
                    "date, time, latitude, longitude, confirmed_collision)"
                    + " VALUES ('"+ UUID + "','" + Sensor_Type + "','" + Sensor_Values + "','"
                    + Date + "','" + Time + "','" + Latitude + "','" + Longitude + "', 'NO');";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println("SQLException:\n" + ex.toString());
            ex.printStackTrace();
        }
    }

    /**
     * This disconnects from the database.
     *
     * @throws SQLException the sql exception
     */

//    public void TableExists() throws SQLException {
//
//        DatabaseMetaData meta = con.getMetaData();
//        ResultSet res = meta.getTables(null, null, "collisions",
//                new String[] {"TABLE"});
//        while (res.next()) {
//            System.out.println(
//                    "  "+res.getString("TABLE_CAT")
//                            + ", "+res.getString("TABLE_NAME")
//                            + ", "+res.getString("TABLE_TYPE")
//                            + ", "+res.getString("REMARKS"));
//        }
//    }
    public void sqlDisconnect() throws SQLException {
        System.out.println("Disconnecting from database .....");
        con.close();
        System.out.println("Disconnected from database .....");
    }


}
