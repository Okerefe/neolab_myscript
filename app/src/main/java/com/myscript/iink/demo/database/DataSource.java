package com.myscript.iink.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.myscript.iink.Editor;
import com.myscript.iink.MimeType;
import com.myscript.iink.PointerEvent;
import com.myscript.iink.PointerEventType;
import com.myscript.iink.PointerType;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import kr.neolab.sdk.ink.structure.Dot;
import kr.neolab.sdk.ink.structure.Stroke;

import static com.myscript.iink.PointerType.PEN;


public class DataSource {
    private static final String TAG = "DataSource";
    MimeType mimeType = MimeType.TEXT;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mDbHelper;

    public DataSource(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    //    Datasource Function to Insert Strokes into database
    public long insertStroke(Stroke stroke) {

        ArrayList<Dot> dotArray = stroke.getDots();

        Uri strokeUri = null;
        final int N = dotArray.size();
        if (N == 0)
            return 0;

        ContentValues values = new ContentValues();

        StringBuilder str
                = new StringBuilder();
        int a = 1;
        str.append("[");
//        Convert the Dots Of Each Stroke to A Json String Object before Insertion into the Database
//        Dots will be converted from the Json String to theDot Objects using tools like Gson on Retrieval
//        Dots could also be converted to blob format for efficiency purpose. though Strings are being used here

        for (Dot d : dotArray) {
//            If Statement is to enable us add commas to each Json Array Object except the first one
            if (a != 1) {
                str.append(",{\"x\":" + d.x + ",\"y\":" + d.y + ",\"timestamp\":" + d.timestamp +
                        ",\"pressure\":" + d.pressure + ",\"sectionId\":" + d.sectionId + ",\"ownerId\":" + d.ownerId +
                        ",\"noteId\":" + d.noteId + ",\"pageId\":" + d.pageId + ",\"doteType\":" + d.dotType + "}");
            } else {
                str.append("{\"x\":" + d.x + ",\"y\":" + d.y + ",\"timestamp\":" + d.timestamp +
                        ",\"pressure\":" + d.pressure + ",\"sectionId\":" + d.sectionId + ",\"dotType\":" + d.dotType + "}");

            }
            a++;
        }
        str.append("]");

        values.put(StrokesTable.SECTION_ID, stroke.sectionId);
        values.put(StrokesTable.OWNER_ID, stroke.ownerId);
        values.put(StrokesTable.NOTE_ID, stroke.noteId);
        values.put(StrokesTable.PAGE_ID, stroke.pageId);
        values.put(StrokesTable.COLOR, stroke.color);
        values.put(StrokesTable.THICKNESS, stroke.thickness);
        values.put(StrokesTable.TIME_START, stroke.timeStampStart);
        values.put(StrokesTable.TIME_END, stroke.timeStampEnd);
        values.put(StrokesTable.DOTS, str.toString());
        values.put(StrokesTable.DOT_COUNT, dotArray.size());

        Log.d(TAG, "Inserting STROKE INTO DATABASE");
        return mDatabase.insert(StrokesTable.TABLE_STROKES, null, values);
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


//    Function to encode stroke from the Database
    private Stroke getStroke(Cursor cursor) {
        Stroke mStroke = new Stroke(cursor.getInt(cursor.getColumnIndex(StrokesTable.SECTION_ID)),
                cursor.getInt(cursor.getColumnIndex(StrokesTable.OWNER_ID)),
                cursor.getInt(cursor.getColumnIndex(StrokesTable.NOTE_ID)),
                cursor.getInt(cursor.getColumnIndex(StrokesTable.PAGE_ID)),
                cursor.getInt(cursor.getColumnIndex(StrokesTable.COLOR)), 0);


        mStroke.thickness = cursor.getInt(cursor.getColumnIndex(StrokesTable.COLOR));
        mStroke.timeStampStart = cursor.getInt(cursor.getColumnIndex(StrokesTable.TIME_START));
        mStroke.timeStampEnd = cursor.getInt(cursor.getColumnIndex(StrokesTable.TIME_END));

        String dots = cursor.getString(cursor.getColumnIndex(StrokesTable.DOTS));

        Gson gson = new Gson();
        Dot[] f = gson.fromJson(dots, Dot[].class);
        for (Dot d : f) {
            mStroke.add(d);
        }
        return mStroke;
    }

    public ArrayList<Stroke> getStrokes(RectF rectF) {
        ArrayList<Stroke> strokes = new ArrayList<Stroke>();
        Cursor cursor = null;
        cursor = mDatabase.query(StrokesTable.TABLE_STROKES, StrokesTable.ALL_COLUMNS,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Stroke s = getStroke(cursor);
            // check if stroke is in this Rectf
            if (s.contains(rectF)) {
                strokes.add(s);
            }
        }

        return strokes;
    }


//    the Get Value deals with looping through the stroke table and finding the rights stroke and..
//    Sending them to the myscript editor to get the text value
    public String getValue(RectF mRectF, Editor editor, Context c, String fileName) {
//        Define Cursor and get all Strokes From Database
        Cursor cursor = null;
        cursor = mDatabase.query(StrokesTable.TABLE_STROKES, StrokesTable.ALL_COLUMNS,
                null, null, null, null, null);

//        The Engine Editor Accepts PointerArray so we will frist create an Arraylist then after Looping Extensively
//        Through The Database, we will convert the ArrayList to an Array and feed the Editor
        ArrayList<PointerEvent> arrayListPointerEvents = new ArrayList<PointerEvent>();
        PointerType mPointerType = PEN;
        int id = 1;
        while (cursor.moveToNext()) {
            Stroke s = getStroke(cursor);
            // check if stroke is in this Rectf
            if (s.contains(mRectF)) {
                for (int i = 0; i < s.size(); i++) {
                    Log.d(TAG, "New Dot");
                    int l = s.size() - 1;
                    PointerEvent pointerEvent = new PointerEvent();
                    pointerEvent.pointerType = mPointerType;
                    pointerEvent.pointerId = id;

                    // We assign either DOWN,UP Or MOVE Pointer Event Types to each pointer depending on the Dots
                    if (i == 0) {
                        pointerEvent.eventType = PointerEventType.DOWN;
                        Log.d(TAG, "PenDown");
                    } else if (i == l) {
                        pointerEvent.eventType = PointerEventType.UP;
                        Log.d(TAG, "PenUp");
                    } else {
                        pointerEvent.eventType = PointerEventType.MOVE;
                        Log.d(TAG, "PenMove");
                    }
                    pointerEvent.x = 130-(s.get(i).y);
                    pointerEvent.y = s.get(i).x;

                    pointerEvent.t = s.get(i).timestamp;
                    pointerEvent.f = s.get(i).pressure;
                    arrayListPointerEvents.add(pointerEvent);
                    ++id;
                }
                Log.d(TAG, "End Of Stroke");

//                We Assume The Stoke wont be used again after being worked on so we delete it from the database to avoid over population of it
                int strokeId = cursor.getInt(cursor.getColumnIndex(StrokesTable._ID));
//                mDatabase.delete(StrokesTable.TABLE_STROKES, StrokesTable._ID + "=" + strokeId, null);
            }
        }

        PointerEvent[] arrayPointerEvents = arrayListPointerEvents.toArray(new PointerEvent[arrayListPointerEvents.size()]);
        // Feed the editor
        editor.pointerEvents(arrayPointerEvents, false);
        File file = new File(c.getExternalFilesDir(null), File.separator + fileName + mimeType.getFileExtensions());
        editor.waitForIdle();
        StringBuilder readFile = new StringBuilder();
        readFile.append("");
        try {
//            Export File and Read The Contents Of the File then Delete the File
            editor.export_(null, file.getAbsolutePath(), mimeType, null);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                readFile.append(myReader.nextLine());
            }
            myReader.close();
//            Delete File
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Clear Editor After Use
        editor.clear();
        return readFile.toString();

    }
}