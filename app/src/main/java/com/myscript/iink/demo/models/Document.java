package com.myscript.iink.demo.models;

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Document implements Parcelable {
    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_LANDSCAPE = 2;
    private int id;
    private ArrayList<Input> inputList;
//    private Input[] inputList;
//    private Input[] inputList = [new Input("dd", "ss", new RectF(38.97f,95.04f,42.75f,122.34f)),];
    private RectF doneRectF;
    public int orientation;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Input getInputList(int i) {
        return inputList.get(i);
//        return Array.get(inputList, i);//  inputList[];
    }

//    public void setInputList(ArrayList<Input> inputList) {
//        this.inputList = inputList;
//    }

    public String getDone() {
        return doneRectF.toString();
    }

    public void setDone(RectF done) {
        this.doneRectF = done;
    }

    public int getInputSize() {
        return inputList.size();
//        return inputList.length();
    }

    public Input getInput(int i) {
        return inputList.get(i);
//        return (Input) Array.get(inputList, i);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeList(this.inputList);
        dest.writeParcelable(this.doneRectF, flags);
        dest.writeInt(this.orientation);
    }

    public Document() {
    }

    protected Document(Parcel in) {
        this.id = in.readInt();
        this.inputList = new ArrayList<Input>();
        in.readList(this.inputList, Input.class.getClassLoader());
        this.doneRectF = in.readParcelable(RectF.class.getClassLoader());
        this.orientation = in.readInt();
    }

    public static final Creator<Document> CREATOR = new Creator<Document>() {
        @Override
        public Document createFromParcel(Parcel source) {
            return new Document(source);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };
}
