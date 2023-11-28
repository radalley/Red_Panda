# Red_Panda
Created for Backend Coding Challenge for Job Interview with TopBloc

# Hello Team at TopBloc! 
I am Riley Dalley. I am extremely excited to have the opportunity to present my current knowledge through this coding challenge. Here you will find my finished files, and within this readme document, all my thoughts and notes as I completed and tested this challenge.

I look forward to hearing back and would love to engage further about this project to see what comments you may have. Without further ado, let's begin!

# Programs Used
- IntelliJ IDEA 2023.2.5 - Acted as my IDE where I could operate on the code
- SQLiteStudio (3.4.4) - Acted as a wonderful testing group to try out queries before bringing them into code
- Postman - Acted as my API frontend where I could interact and view outputting data

# Initial Thoughts 
  - The data looks fun and a great challenge to bring to code (Plus I get to work with candy so that's always great)
  - The initial code presented provides a really strong scaffolding to work on. I can tell immediately how this project draws on a user's knowledge of API requests, SQL, and Java
  - Choosing my SQL IDE was interesting. This was my first time trying SQLite Studio and found it to be a nice change of pace from previous programs.
  - Notes will include the Postman request made as well as the SQL  query sent to receive outputting data
  My main goal, display my knowledge, prove myself, and have some fun doing so
    
# The Challenge Itself
  GET routes
    Inventory routes that return the following:
      All items in your inventory, including the item name, ID, amount in stock, and total capacity
        - Postman: get localhost:4567/itemInventory
        - SQL: SELECT name, items.id, stock, capacity FROM inventory JOIN items On items.id = inventory.item
        
      All items in your inventory that are currently out of stock, including the item name, ID, amount in stock, and total capacity
        - For this step, I adjusted the database for testing. changes are as follows:
          - Licorice stock set from 22 to 27 to test overcapacity. Chosen due to it being the highest stocked item by percentile
          - Good and Plenty stock set from 4 to 0 to test out-of-stock functionality. Chosen due to being the lowest stocked item by percentile
        - Postman: get localhost:4567/itemOutOfStock
        - SQL: SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where stock = 0
        
      All items in your inventory that are currently overstocked, including the item name, ID, amount in stock, and total capacity
        - Postman: get localhost:4567/itemOverStock
        - SQL: SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where stock > capacity
        
      All items in your inventory that are currently low on stock (<35%), including the item name, ID, amount in stock, and total capacity
        - Postman: get localhost:4567/itemStockUnder35
        - SQL: SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where stock < capacity * .35
        
      A dynamic route that, when given an ID, returns the item name, ID, amount in stock, and total capacity of that item
        - Postman: get localhost:4567/itemFromId?itemId=3
        - SQL: SELECT name, items.id, stock, capacity FROM items JOIN inventory On items.id = inventory.item Where items.id = 3
        
    Distributor routes that return the following:
      All distributors, including the id and name
        - Postman: get localhost:4567/allDistributors
        - SQL: SELECT * FROM distributors
        
      A dynamic route that, given a distributors ID, returns the items distributed by a given distributor, including the item name, ID, and cost
        - Postman: post localhost:4567/distributorItemsFromId?distId=2
        - SQL: SELECT  items.name AS item_name, items.id AS item_id, cost  FROM (SELECT name, distributors.id, item, cost FROM distributors JOIN distributor_prices ON distributors.id = distributor WHERE distributors.id = 2) JOIN items where item = items.id
      
      A dynamic route that, given an item ID, returns all offerings from all distributors for that item, including the distributor name, ID, and cost
POST/PUT/DELETE routes
  - Postman: post localhost:4567/itemsFromSameDistributor?itemId=4
  - SQL: select name AS dist_name, item_id, cost from (select name, distributors.id AS dist_id1, item AS item_id, cost from distributors join distributor_prices on distributors.id = distributor) join (Select distributor AS dist_id2 from distributor_prices join items ON distributor_prices.item = items.id and items.id = 4) on dist_id1 = dist_id2

  Routes that allow you to:
    Add a new item to the database
      - Postman: post localhost:4567/addIntoDatabase?itemId=18&name=Sour Gummy Worms
      - SQL: insert into items (id,name) Values ("18","Sour Gummy Worms")
      - Confirmed in Postman using: get localhost:4567/items
      - My partner loves sour gummy worms, this is for her
      
    Add a new item to your inventory
      - Postman: post localhost:4567/addIntoInventory?itemId=18&stock=10&capacity=35
      - SQL: insert into inventory (item, stock, capacity) Values ("18","10","35")
      - Confirmed in Postman using: get localhost:4567/inventory
    
    Modify an existing item in your inventory 
      - (itemId, stock, capacity)
      - Just stock
        - Before value: 1, 27, 25
        - Postman: put localhost:4567/modifyInventory?itemId=1&stock=10
        - SQL: update inventory set stock = 10 where item = 1
        - New Value: 1, 15, 25
      - Just capacity
        - Before Value: 2, 0, 20
        - Postman: put localhost:4567/modifyInventory?itemId=2&capacity=30
        - SQL: update inventory set capacity = 30 where item = 2
        - New Value: 2, 0, 30
      - Both stock and capacity
        - Postman: put localhost:4567/modifyInventory?itemId=3&stock=40&capacity=45
        - SQL: update inventory set stock = 40, capacity = 45 where item = 3
      - Confirmed in Postman using: get localhost:4567/inventory
        
    Add a distributor
      - Postman: post localhost:4567/addDistributor?name=Dalleys Sweets&distId=4
      - SQL: insert into distributors (name, id) Values ("Dalleys Sweets","4")
      - Confirmed in Postman using: get localhost:4567/allDistributors
    
    Add items to a distributor's catalog (including the cost)
      - Postman: post localhost:4567/addToDistCatalog?distId=1&itemId=7&cost=.21
      - SQL: insert into distributor_prices (distributor, item, cost) Values ("1","7",".21")
      - Confirmed in Postman using: get localhost:4567/distributorItemsFromId?distId=1
      
    Modify the price of an item in a distributor's catalog
      - Before value: distId 1, itemId 3, cost .45
      - Postman: put localhost:4567/modifyDistItemPrice?distId=1&itemId=3&cost=.85
      - SQL: update distributor_prices set cost = .85 where item = 3 and distributor = 1
      - Confirmed in Postman using: get localhost:4567/distributorItemsFromId?distId=1
      - new value: distId 1, itemId 3, cost .85
      
    Get the cheapest price for restocking an item at a given quantity from all distributors
      - Postman: get localhost:4567/getCheapestRestock?itemId=3&quantity=10
      - SQL: select distributor, item, cost*10 as cost from distributor_prices where item = 3 order by cost limit 1
      - Note: decided to multiply the cost by the desired quantity, sort the table, and pull the cheapest value that would therefore be in row 1

    Delete an existing item from your inventory
      - Postman: localhost:4567/deleteFromInventory?itemId=5
      - SQL: delete from inventory where item = 5
      - Confirmed in Postman using: get localhost:4567/inventory
      
    Delete an existing distributor given their ID
      - Postman: localhost:4567/deleteDistributor?distId=2
      - SQL: delete from distributors where id = 2
      - Confirmed in Postman using: get localhost:4567/allDistributors

# Additional Notes
  - added inventory get call to view inventory table directly
  - added error handling for input calls. In the case of a null or missing variable, the system will now ask you to input again. 
  - Some values return float rounding errors from SQL
  - Some SQL queries detect the column ID as ambiguous, they have been changed to distributor.id to solve said issue
  -  Some debugging System.out calls by myself have been commented out and left in, most allow viewing of a called SQL query
  -  Red Pandas' tails are about 12" to 20" long, as such they sleep on them often for warmth and comfort

# THANK YOU! In Conclusion
Lastly, thank you so much for this opportunity! I cannot state enough how much enjoyed working on this project. I found it to be heavily engaging and I truly believe you will be able to see the hard work put in. 

I look forward to hearing back from you soon! 
  Riley Dalley
