package com.example.packup.data;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.packup.constants.MyConstants;
import com.example.packup.database.RoomDB;
import com.example.packup.models.Items;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData extends Application {
    RoomDB database;
    String category;
    Context context;

    public static final String LAST_VERSION = "LAST_VERSION";
    public static final int NEW_VERSION = 3;

    public AppData(RoomDB database) {
        this.database = database;
    }

    public AppData(RoomDB database, Context context) {
        this.database = database;
        this.context = context;
    }

    public List<Items> getBasicData() {
        category = "Basic Needs";
        List<Items> basicItem = new ArrayList<>();
        basicItem.add(new Items("Visa", category, false));
        basicItem.add(new Items("Passport", category, false));
        basicItem.add(new Items("Driving License", category, false));
        basicItem.add(new Items("Identity Card", category, false));
        basicItem.add(new Items("Water Bottle", category, false));
        basicItem.add(new Items("Notebook", category, false));
        basicItem.add(new Items("Pen", category, false));
        basicItem.add(new Items("Toothbrush", category, false));
        basicItem.add(new Items("Soap", category, false));
        basicItem.add(new Items("Towel", category, false));
        basicItem.add(new Items("Mobile Charger", category, false));
        basicItem.add(new Items("Sunglasses", category, false));
        basicItem.add(new Items("Shoes", category, false));
        basicItem.add(new Items("Backpack", category, false));
        basicItem.add(new Items("Headphones", category, false));
        basicItem.add(new Items("Snacks", category, false));
        return basicItem;
    }

    public List<Items> getFoodData() {
        String []data = {"Snack", "Sandwich", "Juice", "Tea Bags", "Coffee", "Water", "Chips", "Baby Food"};
        return prepareItemsList(MyConstants.FOOD_CAMEL_CASE, data);
    }

    public List<Items> getPersonalNeeds() {
        String[] data = {
                "Toothbrush",
                "Toothpaste",
                "Shampoo",
                "Soap",
                "Deodorant",
                "Hair Brush",
                "Face Cream",
                "Razor",
                "Lip Balm",
                "Hand Cream",
                "Shaving Gel",
                "Nail Clipper",
                "Sunscreen",
                "Perfume",
                "Tissues",
                "Body Lotion"
        };
        return prepareItemsList(MyConstants.PERSONAL_CARE_CAMEL_CASE, data);
    }

    public List<Items> getTechnologyData() {
        String []data = {"Mobile Phone", "Phone Charger", "E-book Reader", "Camera", "Camera Charger", "Mini Speakers", "Laptop", "Keyboard", "Laptop Charger", "Mouse"};
        return prepareItemsList(MyConstants.TECHNOLOGY_CAMEL_CASE, data);
    }

    public List<Items> getClothingData() {
        String []data = {"Stockings", "T-Shirts", "Trousers", "Night pants", "Garments", "Casual Dresses", "Evening Dresses", "Jacket", "Hoodie", "Shorts", "Hat", "Scarf", "Casual Shoes", "Raincoats", "Formal Dress", "Suit"};
        return prepareItemsList(MyConstants.CLOTHING_CAMEL_CASE, data);
    }

    public List<Items> getBabyNeeds() {
        String []data = {"Snapsuit", "Outfits", "Baby socks", "Jumpsuit", "Baby hat", "Baby Bottles", "Baby Diaper", "Detergent", "Brest Pump", "Baby soap", "Baby Shampoo", "Stroller", "Baby Carrier"};
        return prepareItemsList(MyConstants.BABY_NEEDS_CAMEL_CASE, data);
    }

    public List<Items> getHealthCareData() {
        String[] data = {"Bandages", "Antiseptic Cream", "Pain Reliever", "Cough Syrup", "Thermometer", "First Aid Kit", "Disinfectant Wipes", "Hand Sanitizer", "Face Mask", "Surgical Gloves", "Inhaler", "Vitamins", "Eye Drops", "Blood Pressure Monitor", "Medical Tape", "Gauze Pads"};
        return prepareItemsList(MyConstants.HEALTH_CARE_CAMEL_CASE, data);
    }

    public List<Items> getCarSuppliesData() {
        String[] data = {
                "Tire Inflator",
                "Jumper Cables",
                "Spare Tire",
                "Engine Oil",
                "Car Jack",
                "Windshield Washer Fluid",
                "Car Battery Charger",
                "Wrench Set",
                "Air Freshener",
                "Car Cover",
                "Seat Covers",
                "Car Vacuum Cleaner",
                "GPS Device",
                "Car Cleaning Cloth",
                "Emergency Road Kit",
                "Fuel Can"
        };
        return prepareItemsList(MyConstants.CAR_SUPPLIES_CAMEL_CASE, data);
    }

    public List<Items> getBeachSuppliesDat() {
        String[] data = {"Sea Glasses", "Sea Bed", "Beach Ball", "Wears", "Beach Umbrella", "Swim Fins", "Beach Bags", "Beach Towel"};
        return prepareItemsList(MyConstants.BEACH_SUPPLIES_CAMEL_CASE, data);
    }


    public  List<Items> getNeedsData() {
        String[] data = {"Laundry Bags", "Luggage Tags", "Travel Bag", "Clothing Kit", "Recycle Bags"};
        return prepareItemsList(MyConstants.NEEDS_CAMEL_CASE, data);
    }

    public List<Items> prepareItemsList(String category, String []data) {
        List<String> list = Arrays.asList(data);
        List<Items> dataList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            dataList.add(new Items(list.get(i), category, false));
        }
        return dataList;
    }

    public List<List<Items>> getAllData() {
        List<List<Items>> listOfAllItems = new ArrayList<>();
        listOfAllItems.clear();
        listOfAllItems.add(getBasicData());
        listOfAllItems.add(getClothingData());
        listOfAllItems.add(getPersonalNeeds());
        listOfAllItems.add(getBabyNeeds());
        listOfAllItems.add(getHealthCareData());
        listOfAllItems.add(getTechnologyData());
        listOfAllItems.add(getFoodData());
        listOfAllItems.add(getBeachSuppliesDat());
        listOfAllItems.add(getCarSuppliesData());
        listOfAllItems.add(getNeedsData());

        return listOfAllItems;
    }

    public void persistAllData() {
        List<List<Items>> listOfAllItems = getAllData();
        for (List<Items> list: listOfAllItems) {
            for(Items items:list) {
                database.mainDao().saveItems(items);
            }
        }

        System.out.println("Data Added!");
    }


    public void persistDataByCategory(String category, Boolean onlyDel) {
        try {
            List<Items> list = deleteAndGetListByCategory(category, onlyDel);
            if(!onlyDel) {
                for (Items item : list) {
                    database.mainDao().saveItems(item);
                }
                Toast.makeText(context, category + " Reset Successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, category + " Reset Successfully.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong! :(", Toast.LENGTH_SHORT).show();
        }
    }



    private List<Items> deleteAndGetListByCategory(String category, Boolean onlyDel) {
        if(onlyDel) {
            database.mainDao().deleteAllByCategoryAndAddedBy(category, MyConstants.SYSTEM_SMALL);
        } else {
            database.mainDao().deleteAllByCategory(category);
        }

        switch (category) {
            case MyConstants.BASIC_NEEDS_CAMEL_CASE:
                return getBasicData();


            case MyConstants.CLOTHING_CAMEL_CASE:
                return getClothingData();

            case MyConstants.PERSONAL_CARE_CAMEL_CASE:
                return getPersonalNeeds();

            case MyConstants.BEACH_SUPPLIES_CAMEL_CASE:
                return getBeachSuppliesDat();


            case MyConstants.BABY_NEEDS_CAMEL_CASE:
                return getBabyNeeds();

            case MyConstants.HEALTH_CARE_CAMEL_CASE:
                return getHealthCareData();

            case MyConstants.TECHNOLOGY_CAMEL_CASE:
                return getTechnologyData();

            case MyConstants.FOOD_CAMEL_CASE:
                return getFoodData();


            case MyConstants.CAR_SUPPLIES_CAMEL_CASE:
                return getCarSuppliesData();

            case MyConstants.NEEDS_CAMEL_CASE:
                return getNeedsData();
            default:
                return new ArrayList<>();
        }
    }
}
