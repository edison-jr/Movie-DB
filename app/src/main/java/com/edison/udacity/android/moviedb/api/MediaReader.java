package com.edison.udacity.android.moviedb.api;


/**
 * Created by jeziel on 10/03/17.
 */
public interface MediaReader {

    String readString(String name, String defaultValue);
    int readInt(String name, int defaultValue);
    double readDouble(String name, double defaultValue);
    boolean readBoolean(String name, boolean defaultValue);
    long readLong(String name, long defaultValue);

}
