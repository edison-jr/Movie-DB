package com.edison.android.tools.media;


/**
 * Created by jeziel on 10/03/17.
 */
public interface MediaWriter<T> {

    void writeString(String name, String value);
    void writeInt(String name, int value);
    void writeDouble(String name, double value);
    void writeBoolean(String name, boolean value);
    void writeLong(String name, long value);

    T media();

}
