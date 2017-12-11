package IRPact_modellierung.IO.SQLiteHandling;

import IRPact_modellierung.agents.consumerAgents.ConsumerAgentGroup;
import IRPact_modellierung.products.Product;

import java.util.*;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO think about how to structure DB communication

/**
 * Created by sim on 10/11/16.
 */
public class SQLiteHandler {

    private static final Logger fooLog = LogManager.getLogger("debugConsoleLogger");


    private String dataDirectory;

    public SQLiteHandler(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void writeAdoptionPatternToDb(int year, String scenarioPrefix, Map<ConsumerAgentGroup, Map<Product, Integer>> adoptersPerCAGperProduct) {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dataDirectory + "/" + scenarioPrefix + "_year"+ String.valueOf(year) + ".db");
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();

            String tableName = (scenarioPrefix+String.valueOf(year)).toUpperCase();

            fooLog.debug("Attempting to create table {}", tableName);

            String tableCreationStatement = "CREATE TABLE "+tableName+
                   // "(ENTRYID INT PRIMARY KEY     NOT NULL," +
                    " (CONSUMERGROUP STRING     NOT NULL," +
                    " PRODUCTNAME STRING     NOT NULL," +
                    " NOADOPTERS INT     NOT NULL)";
            stmt.executeUpdate(tableCreationStatement);



            int entryID = 0;
            fooLog.info("Attempting to fill data base");
            for (ConsumerAgentGroup currentCag : adoptersPerCAGperProduct.keySet()) {
                for(Product currentProduct : adoptersPerCAGperProduct.get(currentCag).keySet()) {
                    String dataEntryStatement = "INSERT INTO " + tableName + " (CONSUMERGROUP, PRODUCTNAME, NOADOPTERS) " +
                            "VALUES (" +
                            /* entryID + ",*/ "\'" + currentCag.getGroupName() + "\',\'" + currentProduct.getName() + "\'," + adoptersPerCAGperProduct.get(currentCag).get(currentProduct) + ");";
                    fooLog.debug("Attempting to write {} to dataBase", dataEntryStatement);
                    stmt.executeUpdate(dataEntryStatement);
                    entryID++;
                }
            }
            stmt.close();
            c.commit();
        } catch (SQLException sqle) {
            System.err.println(sqle.getClass().getName() + ": " + sqle.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException cnfe){
            System.err.println(cnfe.getClass().getName() + ": " + cnfe.getMessage());
            System.exit(0);
        }
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

