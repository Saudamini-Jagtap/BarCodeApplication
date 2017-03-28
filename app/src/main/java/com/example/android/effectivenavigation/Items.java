package com.example.android.effectivenavigation;

/**
 * Created by sjaltran on 30/6/16.
 */
public class Items {

    //private variables
    int _id;
    String _name;
    String _barcode;
    String _category;
    String _price;

    // Empty constructor
    public Items(){
    }
    // constructor
    public Items(int id, String _name, String _barcode, String _category, String _price){
        this._id = id;
        this._name = _name;
        this._barcode = _barcode;
        this._category = _category;
        this._price = _price;
    }

    // constructor
    public Items(String _name, String _barcode, String _category, String _price){
        this._name = _name;
        this._barcode = _barcode;
        this._category = _category;
        this._price = _price;
    }

    public Items(String _name, String _barcode, String _category){
        this._name = _name;
        this._barcode = _barcode;
        this._category = _category;
    }
    public Items(String _name){
        this._name = _name;
    }

    public Items(String _name, String _barcode) {
        this._name = _name;
        this._barcode = _barcode;
        //this._category = Category.getCategory();
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting barcode number
    public String getBarcode(){
        return this._barcode;
    }

    // setting barcode number
    public void setBarcode(String barcode){
        this._barcode = barcode;
    }

    // getting category number
    public String getCategory(){
        return this._category;
    }

    // setting category number
    public void setCategory(String category){
        this._category = category;
    }

    // getting price number
    public String getPrice(){
        return this._price;
    }

    // setting price number
    public void setPrice(String price){
        this._price = price;
    }
}
