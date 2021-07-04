// IPCInterface.aidl
package me.android.ipc;

// Declare any non-default types here with import statements
import android.graphics.Bitmap;
import me.android.ipc.Data;

interface IPCInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     * List<T> - T is basic types, Charsequence and Parcelable
     * Map<K,V> - K-V is the same case with T.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);


    Bitmap getImage(String uri);

    Data getData(String id);
}
