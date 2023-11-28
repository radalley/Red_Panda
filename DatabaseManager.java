package com.topbloc.codechallenge.db;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatabaseManager {
    private static final String jdbcPrefix = "jdbc:sqlite:";
    private static final String dbName = "challenge.db";
    private static String connectionString;
    private static Connection conn;

    static {
        File dbFile = new File(dbName);
        connectionString = jdbcPrefix + dbFile.getAbsolutePath();
    }

    public static void connect() {
        try {
            Connection connection = DriverManager.getConnection(connectionString);
            System.out.println("Connection to SQLite has been established.");
            conn = connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // Schema function to reset the database if needed - do not change
    public static void resetDatabase() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            dbFile.delete();
        }
        connectionString = jdbcPrefix + dbFile.getAbsolutePath();
        connect();
        applySchema();
        seedDatabase();
    }

    // Schema function to reset the database if needed - do not change
    private static void applySchema() {
        String itemsSql = "CREATE TABLE IF NOT EXISTS items (\n"
                + "id integer PRIMARY KEY,\n"
                + "name text NOT NULL UNIQUE\n"
                + ");";
        String inventorySql = "CREATE TABLE IF NOT EXISTS inventory (\n"
                + "id integer PRIMARY KEY,\n"
                + "item integer NOT NULL UNIQUE references items(id) ON DELETE CASCADE,\n"
                + "stock integer NOT NULL,\n"
                + "capacity integer NOT NULL\n"
                + ");";
        String distributorSql = "CREATE TABLE IF NOT EXISTS distributors (\n"
                + "id integer PRIMARY KEY,\n"
                + "name text NOT NULL UNIQUE\n"
                + ");";
        String distributorPricesSql = "CREATE TABLE IF NOT EXISTS distributor_prices (\n"
                + "id integer PRIMARY KEY,\n"
                + "distributor integer NOT NULL references distributors(id) ON DELETE CASCADE,\n"
                + "item integer NOT NULL references items(id) ON DELETE CASCADE,\n"
                + "cost float NOT NULL\n" +
                ");";

        try {
            System.out.println("Applying schema");
            conn.createStatement().execute(itemsSql);
            conn.createStatement().execute(inventorySql);
            conn.createStatement().execute(distributorSql);
            conn.createStatement().execute(distributorPricesSql);
            System.out.println("Schema applied");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Schema function to reset the database if needed - do not change
    private static void seedDatabase() {
        String itemsSql = "INSERT INTO items (id, name) VALUES (1, 'Licorice'), (2, 'Good & Plenty'),\n"
            + "(3, 'Smarties'), (4, 'Tootsie Rolls'), (5, 'Necco Wafers'), (6, 'Wax Cola Bottles'), (7, 'Circus Peanuts'), (8, 'Candy Corn'),\n"
            + "(9, 'Twix'), (10, 'Snickers'), (11, 'M&Ms'), (12, 'Skittles'), (13, 'Starburst'), (14, 'Butterfinger'), (15, 'Peach Rings'), (16, 'Gummy Bears'), (17, 'Sour Patch Kids')";
        String inventorySql = "INSERT INTO inventory (item, stock, capacity) VALUES\n"
                + "(1, 27, 25), (2, 0, 20), (3, 15, 25), (4, 30, 50), (5, 14, 15), (6, 8, 10), (7, 10, 10), (8, 30, 40), (9, 17, 70), (10, 43, 65),\n" +
                "(11, 32, 55), (12, 25, 45), (13, 8, 45), (14, 10, 60), (15, 20, 30), (16, 15, 35), (17, 14, 60)";
        String distributorSql = "INSERT INTO distributors (id, name) VALUES (1, 'Candy Corp'), (2, 'The Sweet Suite'), (3, 'Dentists Hate Us')";
        String distributorPricesSql = "INSERT INTO distributor_prices (distributor, item, cost) VALUES \n" +
                "(1, 1, 0.81), (1, 2, 0.46), (1, 3, 0.89), (1, 4, 0.45), (2, 2, 0.18), (2, 3, 0.54), (2, 4, 0.67), (2, 5, 0.25), (2, 6, 0.35), (2, 7, 0.23), (2, 8, 0.41), (2, 9, 0.54),\n" +
                "(2, 10, 0.25), (2, 11, 0.52), (2, 12, 0.07), (2, 13, 0.77), (2, 14, 0.93), (2, 15, 0.11), (2, 16, 0.42), (3, 10, 0.47), (3, 11, 0.84), (3, 12, 0.15), (3, 13, 0.07), (3, 14, 0.97),\n" +
                "(3, 15, 0.39), (3, 16, 0.91), (3, 17, 0.85)";

        try {
            System.out.println("Seeding database");
            conn.createStatement().execute(itemsSql);
            conn.createStatement().execute(inventorySql);
            conn.createStatement().execute(distributorSql);
            conn.createStatement().execute(distributorPricesSql);
            System.out.println("Database seeded");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Helper methods to convert ResultSet to JSON - change if desired, but should not be required
    private static JSONArray convertResultSetToJson(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<String> colNames = IntStream.range(0, columns)
                .mapToObj(i -> {
                    try {
                        return md.getColumnName(i + 1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        JSONArray jsonArray = new JSONArray();
        while (rs.next()) {
            jsonArray.add(convertRowToJson(rs, colNames));
        }
        return jsonArray;
    }

    private static JSONObject convertRowToJson(ResultSet rs, List<String> colNames) throws SQLException {
        JSONObject obj = new JSONObject();
        for (String colName : colNames) {
            obj.put(colName, rs.getObject(colName));
        }
        return obj;
    }

    // Controller functions - add your routes here. getItems is provided as an example
    public static JSONArray getItems() {
        String sql = "SELECT * FROM items";
//        System.out.println(sql);
        try {
            ResultSet set = conn.createStatement().executeQuery(sql);
            System.out.println("returning convertresultset");
            return convertResultSetToJson(set);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("returning null");
            return null;
        }
    }

    public static JSONArray getInventory() {
        String sql = "SELECT * FROM inventory";
//        System.out.println(sql);
        try {
            ResultSet set = conn.createStatement().executeQuery(sql);
            System.out.println("returning convertresultset");
            return convertResultSetToJson(set);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("returning null");
            return null;
        }
    }

    public static JSONArray getItemInventory() {
        String sql = "SELECT name, items.id, stock, capacity FROM inventory JOIN items On items.id = inventory.item";
//        System.out.println(sql);
        try {
            ResultSet set = conn.createStatement().executeQuery(sql);
            System.out.println("returning convertresultset");
            return convertResultSetToJson(set);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("returning null");
            return null;
        }
    }

    public static JSONArray getItemOutOfStock() {
        String sql = "SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where stock = 0";
//        System.out.println(sql);
        try {
            ResultSet set = conn.createStatement().executeQuery(sql);
            System.out.println("returning convertresultset");
            return convertResultSetToJson(set);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("returning null");
            return null;
        }
    }

    public static JSONArray getItemOverStock() {
        String sql = "SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where stock > capacity";
//        System.out.println(sql);
        try {
            ResultSet set = conn.createStatement().executeQuery(sql);
            System.out.println("returning convertresultset");
            return convertResultSetToJson(set);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("returning null");
            return null;
        }
    }

    public static JSONArray getItemStockUnder35() {
        String sql = "SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where stock < capacity * .35";
//        System.out.println(sql);
        try {
            ResultSet set = conn.createStatement().executeQuery(sql);
            System.out.println("returning convertresultset");
            return convertResultSetToJson(set);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("returning null");
            return null;
        }
    }

    public static JSONArray getItemFromId(String itemId) {
        if(itemId != null && !itemId.isEmpty())  {
            String sql = "SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where items.id = " + itemId;
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        System.out.println("ERROR: Please provide itemId");
        return null;
    }

    public static JSONArray getAllDistributors() {
        String sql = "SELECT * FROM distributors";
//        System.out.println(sql);
        try {
            ResultSet set = conn.createStatement().executeQuery(sql);
            System.out.println("returning convertresultset");
            return convertResultSetToJson(set);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("returning null");
            return null;
        }
    }

    public static JSONArray getDistributorItemsFromId(String distId) {
        if(distId != null && !distId.isEmpty()) {
            String sql = "SELECT  items.name AS item_name, items.id AS item_id, cost  FROM (SELECT name, distributors.id, item, cost FROM distributors JOIN distributor_prices ON distributors.id = distributor WHERE distributors.id = " + distId + ") JOIN items where item = items.id";
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        System.out.println("ERROR: Please provide distId");
        return null;
    }

    public static JSONArray getItemsFromSameDistributor(String itemId) {
        if(itemId != null && !itemId.isEmpty()) {
            String sql = "select name AS dist_name, item_id, cost from (select name, distributors.id AS dist_id1, item AS item_id, cost from distributors join distributor_prices on distributors.id = distributor) join (Select distributor AS dist_id2 from distributor_prices join items ON distributor_prices.item = items.id and items.id = " + itemId + ") on dist_id1 = dist_id2";
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        System.out.println("ERROR: Please provide itemId");
        return null;
    }

    public static JSONArray addIntoDatabase(String postId, String postName) {
        if(postId != null && !postId.isEmpty() && postName != null && postName.isEmpty()) {
            String sql = "insert into items (id,name) Values (\"" + postId + "\",\"" + postName + "\")";
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        else {
        System.out.println("ERROR: Please provide itemId and name");
        return null;
        }
    }

    public static JSONArray addIntoInventory(String postItem, String postStock, String postCapacity) {
        if(postItem != null && !postItem.isEmpty() && postStock != null && !postStock.isEmpty() && postCapacity != null && postCapacity != null) {
            String sql = "insert into inventory (item, stock, capacity) Values (\"" + postItem + "\",\"" + postStock + "\",\"" + postCapacity + "\")";
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        else {
            System.out.println("ERROR: Please provide itemId, stock, and capacity");
            return null;
        }
    }

    public static JSONArray modifyInventory(String postItem, String postStock, String postCapacity) {
        if(postItem != null && !postItem.isEmpty()) {
            if(postStock != null && !postStock.isEmpty() || postCapacity != null && !postCapacity.isEmpty()) {
                String sql = "update inventory set";
                if (postStock != null && !postStock.isEmpty()) //has stock
                {
                    //System.out.println("entered, has stock");
                    sql += " stock = " + postStock;
                }
                if (postStock != null && !postStock.isEmpty() && postCapacity != null && !postCapacity.isEmpty()) //has both, needs a comma to maintain sql compatibility
                {
                    sql += ",";
                }
                if (postCapacity != null && !postCapacity.isEmpty()) //has capacity
                {
                    //System.out.println("entered, has stock of capacity");
                    sql += " capacity = " + postCapacity;
                }
                //add tail where statement
                sql += " where item = " + postItem;
                //ORIGINAL QUERY: String sql = "update inventory set stock = " + postStock + ", capacity = " + postCapacity + " where item = " + postItem;
//                System.out.println(sql);
                try {
                    ResultSet set = conn.createStatement().executeQuery(sql);
                    System.out.println("returning convertresultset");
                    return convertResultSetToJson(set);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    System.out.println("returning null on item1");
                    return null;
                }
            }
            System.out.println("ERROR: Please provide stock and/or capacity");
            return null;
        }

        System.out.println("ERROR: Please provide itemId and stock or capacity");
        return null;

    }

    public static JSONArray addDistributor(String postName, String postID) {
        if(postName != null && !postName.isEmpty() && postID != null && !postID.isEmpty()) {
            String sql = "insert into distributors (name, id) Values (\"" + postName + "\",\"" + postID + "\")";
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        else {
            System.out.println("ERROR: Please provide name and distId");
            return null;
        }
    }

    public static JSONArray addToDistCatalog(String distId, String itemId, String cost) {
        if(distId != null && !distId.isEmpty() && itemId != null && !itemId.isEmpty() && cost != null && !cost.isEmpty()) {
            String sql = "insert into distributor_prices (distributor, item, cost) Values (\"" + distId + "\",\"" + itemId + "\",\"" + cost + "\")";
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        else {
            System.out.println("ERROR: Please provide distId, itemId, and cost");
            return null;
        }
    }

    public static JSONArray modifyDistItemCost(String putDistId, String putItemId, String putCost) {
        if(putDistId != null && !putDistId.isEmpty() && putItemId != null && !putItemId.isEmpty() && putCost != null && !putCost.isEmpty()) {

            String sql = "update distributor_prices set cost = " + putCost + " where item = " + putItemId + " and distributor = " + putDistId;
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        System.out.println("ERROR: Please provide distId, iteimId, and cost values");
        return null;

    }

    public static JSONArray getCheapestRestock(String itemId, String quantity) {
        if(itemId != null && !itemId.isEmpty() && quantity != null && !quantity.isEmpty()) {
            String sql = "select distributor, item, cost*" +quantity + " as cost from distributor_prices where item = " + itemId + " order by cost limit 1";
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item1");
                return null;
            }
        }
        else {
            System.out.println("ERROR: Please provide itemId and quantity");
            return null;
        }

    }

    public static JSONArray deleteFromInventory(String itemId) {
        if(itemId != null && !itemId.isEmpty()) {
            String sql = "delete from inventory where item = " + itemId;
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item");
                return null;
            }
        }
        System.out.println("ERROR: Please provide itemId");
        return null;
    }

    public static JSONArray deleteDistributor(String distId) {
        if(distId != null && !distId.isEmpty()) {
            String sql = "delete from distributors where id = " + distId;
//            System.out.println(sql);
            try {
                ResultSet set = conn.createStatement().executeQuery(sql);
                System.out.println("returning convertresultset");
                return convertResultSetToJson(set);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("returning null on item");
                return null;
            }
        }
        System.out.println("ERROR: Please provide distId");
        return null;
    }







}
