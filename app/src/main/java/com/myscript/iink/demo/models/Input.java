package com.myscript.iink.demo.models;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

public class Input implements Parcelable {
    public String name;
    public String type;
    public RectF mRectF;

    public String value = "";

    public Input(String name, String type, RectF recfF) {
        this.name = name;
        this.type = type;
        this.mRectF = recfF;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeParcelable(this.mRectF, flags);
        dest.writeString(this.value);
    }

    protected Input(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.mRectF = in.readParcelable(RectF.class.getClassLoader());
        this.value = in.readString();
    }

    public static final Creator<Input> CREATOR = new Creator<Input>() {
        @Override
        public Input createFromParcel(Parcel source) {
            return new Input(source);
        }

        @Override
        public Input[] newArray(int size) {
            return new Input[size];
        }
    };
}
