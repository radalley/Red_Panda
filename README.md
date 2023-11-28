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
  - This is definitely gonna make me hungry
 
  #The Challenge Itself
  GET routes
    Inventory routes that return the following:
      All items in your inventory, including the item name, ID, amount in stock, and total capacity
      All items in your inventory that are currently out of stock, including the item name, ID, amount in stock, and total capacity
      All items in your inventory that are currently overstocked, including the item name, ID, amount in stock, and total capacity
      All items in your inventory that are currently low on stock (<35%), including the item name, ID, amount in stock, and total capacity
      A dynamic route that, when given an ID, returns the item name, ID, amount in stock, and total capacity of that item
    Distributor routes that return the following:
      All distributors, including the id and name
      A dynamic route that, given a distributors ID, returns the items distributed by a given distributor, including the item name, ID, and cost
      A dynamic route that, given an item ID, returns all offerings from all distributors for that item, including the distributor name, ID, and cost
POST/PUT/DELETE routes
  Routes that allow you to:
    Add a new item to the database
    Add a new item to your inventory
    Modify an existing item in your inventory
    Add a distributor
    Add items to a distributor's catalog (including the cost)
    Modify the price of an item in a distributor's catalog
    Get the cheapest price for restocking an item at a given quantity from all distributors
    Delete an existing item from your inventory
    Delete an existing distributor given their ID
