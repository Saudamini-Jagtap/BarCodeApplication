package com.example.android.effectivenavigation;

/**
 * Created by sjaltran on 11/7/16.
 */
public class Category{
    //private variables
    int _id;
    String _name;

    // Empty constructor
    public Category(){

    }
    // constructor
    public Category(int _id, String _name) {
        this._id = _id;
        this._name = _name;
    }
    // constructor
    public Category (String _name){
        this._name = _name;
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
}
