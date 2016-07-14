// IStockAidlInterface.aidl
package com.jaysen.wallstreet;

// Declare any non-default types here with import statements

interface IStockAidlInterface {
    List<Message> getData();
    void setOnUpdateListener();
}
