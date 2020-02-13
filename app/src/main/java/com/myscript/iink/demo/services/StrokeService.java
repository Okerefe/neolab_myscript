package com.myscript.iink.demo.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.myscript.iink.Configuration;
import com.myscript.iink.ContentPackage;
import com.myscript.iink.ContentPart;
import com.myscript.iink.Editor;
import com.myscript.iink.Engine;
import com.myscript.iink.MimeType;
import com.myscript.iink.PointerEvent;
import com.myscript.iink.PointerEventType;
import com.myscript.iink.PointerType;
import com.myscript.iink.Renderer;
import com.myscript.iink.demo.IInkApplication;
import com.myscript.iink.demo.database.DataSource;
import com.myscript.iink.demo.models.Document;
import com.myscript.iink.demo.models.Input;
import com.myscript.iink.demo.utils.MyScriptEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import kr.neolab.sdk.ink.structure.Stroke;

import static com.myscript.iink.PointerType.PEN;

public class StrokeService extends IntentService {

    public static final String TAG = "strokeService";
    public static final String STROKE_SERVICE_MESSAGE = "strokeServiceMessage";
    public static final String STROKE_SERVICE_PAYLOAD= "strokeServicePayload";
    public static final String CONTAINERS = "containers";
    public static final String EMAIL_CONFIRM = "confirmemail";
    public static final String DOCUMENT= "document";
    public static final String STROKE_DOC_RESPONSE = "strokedocresponse";
    public static final String STROKE_DOC_COMPLETE_STATUS = "completedstatus";
    public static final String PROCESS_COMPLETE = "complete";
    public static final String PROCESS_NOT_COMPLETE = "notcomplete";

    private Engine engine;
    private Editor editor;
    private DisplayMetrics displayMetrics;
    private ContentPackage contentPackage;
    private ContentPart contentPart;
    private static final String language = "en_US";
    private static final String packageName = "package.iink";
    private static String partType = "Text";
    public Context c;
    DataSource mDataSource;
    MimeType mimeType = MimeType.TEXT;

    public StrokeService() {
        super("MyService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Document doc = (Document) intent.getParcelableExtra(DOCUMENT);
        Toast toast = Toast.makeText(this, "STROKE SERVICE...", Toast.LENGTH_LONG);
        toast.show();
        Log.d(TAG,"STROKE SERVICE....>>>>");

//        engine = IInkApplication.getEngine();
//        // Configure recognition
//        Configuration conf = engine.getConfiguration();
//        String confDir = "zip://" + getPackageCodePath() + "!/assets/conf";
//        Log.d(TAG, "&&&&&&&&&&&PACKAGE CODE PATH!!!" + getPackageCodePath());
//        conf.setStringArray("configuration-manager.search-path", new String[]{confDir});
//        conf.setString("content-package.temp-folder", getFilesDir().getPath() + File.separator + "tmp");
//        conf.setString("lang", language);
//        // Configure the engine to disable guides (recommended)
//        conf.setBoolean("text.guides.enable", false);
//
//        // Create a renderer with a null render target
//        displayMetrics = getResources().getDisplayMetrics();
//        //DPI Values for Neo A4 Paper
//        Renderer renderer = engine.createRenderer(11, 11, null);
//
//        // Create the editor
//        editor = engine.createEditor(renderer);
//
//        // The editor requires a font metrics provider and a view size *before* calling setPart()
//        editor.setFontMetricsProvider(null);

        //Width and Height for Neo Paper
        switch (doc.orientation){
            case Document.ORIENTATION_PORTRAIT:
//                editor.setViewSize(92, 118);
            break;
            case Document.ORIENTATION_LANDSCAPE:
//                editor.setViewSize(118, 92);
            break;
        }

        mDataSource = new DataSource(this);
//        try {
//            // Create a new package
//            contentPackage = engine.createPackage(packageName);
//
//            // Create a new part
//            contentPart = contentPackage.createPart(partType);
//        } catch (IOException e) {
//            Log.e(TAG, "Failed to open package \"" + packageName + "\"", e);
//        } catch (IllegalArgumentException e) {
//            Log.e(TAG, "Failed to open package \"" + packageName + "\"", e);
//        }
//        editor.setPart(contentPart);

        for(int i=0; i<doc.getInputSize(); i++) {
            // Associate editor with the new part

            Input current = doc.getInput(i);
            ArrayList<Stroke> strokes = mDataSource.getStrokes(current.mRectF);
            if(strokes.size() < 1){
                continue;
            }
            ArrayList<PointerEvent> arrayListPointerEvents = null;
            arrayListPointerEvents = new ArrayList<PointerEvent>();
            PointerType mPointerType = PEN;
            int id = 1;
            for (int counter = 0; counter < strokes.size(); counter++) {
                Stroke s = strokes.get(counter);
//                If Pointer does not receive an UP, MOVE and DOWN movement it will give errors
//                So for Strokes with less than 3 Dots we create this pointers
                if(s.size() < 3){
                    Log.d(TAG, "<<<<< LESS THAN 3");
                    for(int d = 0; d< 3; d++){
                        PointerEvent pointerEvent = new PointerEvent();
                        pointerEvent.pointerType = mPointerType;
                        pointerEvent.pointerId = id;
                        // We assign either DOWN,UP Or MOVE Pointer Event Types to each pointer depending on the Dots
                        if (d == 0) {
                            pointerEvent.eventType = PointerEventType.DOWN;
                            Log.d(TAG, "PenDown");
                        } else if (d == 2) {
                            pointerEvent.eventType = PointerEventType.UP;
                            Log.d(TAG, "PenUp");
                        } else {
                            pointerEvent.eventType = PointerEventType.MOVE;
                            Log.d(TAG, "PenMove");
                        }
                        if(doc.orientation ==  Document.ORIENTATION_PORTRAIT) {
                            pointerEvent.x = s.get(0).x;
                            pointerEvent.y = s.get(0).y;

                            pointerEvent.t = s.get(0).timestamp;
                            pointerEvent.f = s.get(0).pressure;

                        }
                        if(doc.orientation ==  Document.ORIENTATION_LANDSCAPE) {
                            pointerEvent.x = (130 - (s.get(0).y));
                            pointerEvent.y = s.get(0).x;

                            pointerEvent.t = s.get(0).timestamp;
                            pointerEvent.f = s.get(0).pressure;

                        }
                        arrayListPointerEvents.add(pointerEvent);
                        ++id;
                    }

                } else {

                    for (int a = 0; a < s.size(); a++) {
                        Log.d(TAG, "New Dot");
                        PointerEvent pointerEvent = new PointerEvent();
                        pointerEvent.pointerType = mPointerType;
                        pointerEvent.pointerId = id;
                        // We assign either DOWN,UP Or MOVE Pointer Event Types to each pointer depending on the Dots
                        if (a == 0) {
                            pointerEvent.eventType = PointerEventType.DOWN;
                            Log.d(TAG, "PenDown");
                        } else if (a == (s.size() - 1)) {
                            pointerEvent.eventType = PointerEventType.UP;
                            Log.d(TAG, "PenUp");
                        } else {
                            pointerEvent.eventType = PointerEventType.MOVE;
                            Log.d(TAG, "PenMove");
                        }
//                    Switch Statement to check Orientation and adjust x and y axis accordingly

                        if(doc.orientation ==  Document.ORIENTATION_PORTRAIT) {
                            pointerEvent.x = s.get(a).x;
                            pointerEvent.y = s.get(a).y;

                            pointerEvent.t = s.get(a).timestamp;
                            pointerEvent.f = s.get(a).pressure;
                            Log.d(TAG, "Portrait");

                        }
                        if(doc.orientation ==  Document.ORIENTATION_LANDSCAPE) {
                            pointerEvent.x = (130 - (s.get(a).y));
                            pointerEvent.y = s.get(a).x;

                            pointerEvent.t = s.get(a).timestamp;
                            pointerEvent.f = s.get(a).pressure;
                            Log.d(TAG, "Landscape");

                        }

                        arrayListPointerEvents.add(pointerEvent);
//                        ++id;
                    }
                }
            }
            PointerEvent[] arrayPointerEvents = arrayListPointerEvents.toArray(new PointerEvent[arrayListPointerEvents.size()]);
            // Feed the editor

//            TextEngine.getText(arrayPointerEvents,);
            File file = new File(c.getExternalFilesDir(null), File.separator + current.name + mimeType.getFileExtensions());
            current.value = MyScriptEngine.textEngine(this,doc,arrayPointerEvents,file);
//            editor.pointerEvents(arrayPointerEvents, false);
            Log.d(TAG, "Reached Here 1");
//            editor.waitForIdle();
            Log.d(TAG, "Reached Here 2");
//            StringBuilder readFile = new StringBuilder();
//            readFile.append("");
//            try {
////            Export File and Read The Contents Of the File then Delete the File
//                editor.export_(null, file.getAbsolutePath(), mimeType, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.d(TAG, "There Were Some Errors ");
//            }

//            Scanner myReader = null;
//            try {
//                myReader = new Scanner(file);
//                while (myReader.hasNextLine()) {
//                    readFile.append(myReader.nextLine());
//                }
//                myReader.close();
////            Delete File
//                file.delete();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//            editor.clear();
            Log.d(TAG, "SENDING DOCUMENT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + current.value);
            Log.d(TAG, "Reached Here 1");

            Intent messageIntent = new Intent(STROKE_SERVICE_MESSAGE);
            messageIntent.putExtra(STROKE_DOC_RESPONSE, doc);
            messageIntent.putExtra(STROKE_DOC_COMPLETE_STATUS, PROCESS_NOT_COMPLETE);
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
        }
        Log.d(TAG, "Reached Final");
        mDataSource.close();
        Intent messageIntent = new Intent(STROKE_SERVICE_MESSAGE);
        messageIntent.putExtra(STROKE_DOC_RESPONSE, doc);
        messageIntent.putExtra(STROKE_DOC_COMPLETE_STATUS, PROCESS_COMPLETE);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
//        contentPart.close();
//        contentPackage.close();
//        try {
//            engine.deletePackage(packageName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDataSource = new DataSource(this);
        c=this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}