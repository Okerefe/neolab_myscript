package com.myscript.iink.demo.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomBluetoothDevice implements Parcelable {
    private String macAddress, name;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.macAddress);
        dest.writeString(this.name);
    }

    public CustomBluetoothDevice() {
    }

    protected CustomBluetoothDevice(Parcel in) {
        this.macAddress = in.readString();
        this.name = in.readString();
    }




    public static final Creator<CustomBluetoothDevice> CREATOR = new Creator<CustomBluetoothDevice>() {
        @Override
        public CustomBluetoothDevice createFromParcel(Parcel source) {
            return new CustomBluetoothDevice(source);
        }

        @Override
        public CustomBluetoothDevice[] newArray(int size) {
            return new CustomBluetoothDevice[size];
        }
    };
}

