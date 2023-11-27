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
            String id = req.queryParams("id");
            return DatabaseManager.getItemFromId(id);
        });
        get("/allDistributors", (req, res) -> DatabaseManager.getAllDistributors());
        get("/DistributorItemsFromId", (req, res) -> {
            String id = req.queryParams("id");
            return DatabaseManager.getDistributorItemsFromId(id);
        });
        get("/ItemsFromSameDistributor", (req, res) -> {
            String id = req.queryParams("id");
            return DatabaseManager.getItemsFromSameDistributor(id);
        });


//        get("/inventory", (req, res) -> DatabaseManager.getInventory());
//        get("/distributors", (req, res) -> DatabaseManager.getDistributors());
        get("/version", (req, res) -> "TopBloc Code Challenge v1.001");
    }
}

