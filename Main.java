package com.topbloc.codechallenge;

import com.topbloc.codechallenge.db.DatabaseManager;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.connect();
        // Don't change this - required for GET and POST requests with the header 'content-type'

        options("/*",
                (req, res) -> {
                    res.header("Access-Control-Allow-Headers", "content-type");
                    res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                    return "OK";
                });

        // Don't change - if required you can reset your database by hitting this endpoint at localhost:4567/reset
        get("/reset", (req, res) -> {
            DatabaseManager.resetDatabase();
            return "OK";
        });

        //TODO: Add your routes here. a couple of examples are below

        get("/items", (req, res) -> DatabaseManager.getItems());
        get("/itemInventory", (req, res) -> DatabaseManager.getItemInventory());
        get("/itemOutOfStock", (req, res) -> DatabaseManager.getItemOutOfStock());
        get("/itemOverStock", (req, res) -> DatabaseManager.getItemOverStock());
        get("/itemStockUnder35", (req, res) -> DatabaseManager.getItemStockUnder35());
        get("/itemFromId", (req, res) -> {
            String id = req.queryParams("itemId");
            return DatabaseManager.getItemFromId(id);
        });
        get("/allDistributors", (req, res) -> DatabaseManager.getAllDistributors());
        get("/distributorItemsFromId", (req, res) -> {
            String distId = req.queryParams("distId");
            return DatabaseManager.getDistributorItemsFromId(distId);
        });
        get("/itemsFromSameDistributor", (req, res) -> {
            String itemId = req.queryParams("itemId");
            return DatabaseManager.getItemsFromSameDistributor(itemId);
        });

        post("/addIntoDatabase", (req, res) -> {
            String itemId = req.queryParams("itemId");
            String name = req.queryParams("name");
            return DatabaseManager.addIntoDatabase(itemId, name);
        });
        post("/addIntoInventory", (req, res) -> {
            String item = req.queryParams("itemId");
            String stock = req.queryParams("stock");
            String capacity = req.queryParams("capacity");
            return DatabaseManager.addIntoInventory(item, stock, capacity);
        });
        put("/modifyInventory", (req, res) -> {
            String item = req.queryParams("itemId");
            String stock = req.queryParams("stock");
            String capacity = req.queryParams("capacity");
            return DatabaseManager.modifyInventory(item, stock, capacity);
        });
        post("/addDistributor", (req, res) -> {
            String name = req.queryParams("name");
            String id = req.queryParams("distId");
            return DatabaseManager.addDistributor(name, id);
        });
        post("/addToDistCatalog", (req, res) -> {
            String distId = req.queryParams("distId");
            String itemId = req.queryParams("itemId");
            String cost = req.queryParams("cost");
            return DatabaseManager.addToDistCatalog(distId, itemId, cost);
        });
        put("/modifyDistItemPrice", (req, res) -> {
            String distId = req.queryParams("distId");
            String itemId = req.queryParams("itemId");
            String cost = req.queryParams("cost");
            return DatabaseManager.modifyDistItemCost(distId, itemId, cost);
        });
        get("/getCheapestRestock", (req, res) -> {
            String itemId = req.queryParams("itemId");
            String quantity = req.queryParams("quantity");
            return DatabaseManager.getCheapestRestock(itemId, quantity);
        });
        delete("/deleteFromInventory", (req, res) -> {
            String itemId = req.queryParams("itemId");
            return DatabaseManager.deleteFromInventory(itemId);
        });
        delete("/deleteDistributor", (req, res) -> {
            String distId = req.queryParams("distId");
            return DatabaseManager.deleteDistributor(distId);
        });

        get("/inventory", (req, res) -> DatabaseManager.getInventory());
        get("/version", (req, res) -> "TopBloc Code Challenge v1.0");
    }
}

