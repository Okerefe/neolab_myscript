package com.myscript.iink.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
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
import com.myscript.iink.demo.database.DataSource;
import com.myscript.iink.demo.models.Document;
import com.myscript.iink.demo.models.Input;
import com.myscript.iink.demo.models.MultiRowDocument;
import com.myscript.iink.demo.models.TestDoc;
import com.myscript.iink.demo.models.WebResponse;
import com.myscript.iink.demo.services.MyService;
import com.myscript.iink.demo.services.StrokeService;
import com.myscript.iink.demo.utils.RequestPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import kr.neolab.sdk.ink.structure.Stroke;

import static com.myscript.iink.PointerType.PEN;
import static com.myscript.iink.demo.services.StrokeService.PROCESS_COMPLETE;
import static com.myscript.iink.demo.services.StrokeService.STROKE_DOC_COMPLETE_STATUS;

public class PopulateField extends AppCompatActivity {
    private Engine engine;
    private Editor editor;
    private DisplayMetrics displayMetrics;
    private ContentPackage contentPackage;
    private ContentPart contentPart;
    private static final String language = "en_US";
    private static final String packageName = "package.iink";
    private static String partType = "Text";
    public Context c;
    private static final String TAG = "ListStrokes";


    DataSource mDataSource;
    MimeType mimeType = MimeType.TEXT;

    private BroadcastReceiver docBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Document doc =
                    intent.getParcelableExtra(StrokeService.STROKE_DOC_RESPONSE);
            if(intent.getStringExtra(StrokeService.STROKE_DOC_COMPLETE_STATUS).equals(StrokeService.PROCESS_COMPLETE)){
                Toast toast = Toast.makeText(PopulateField.this, "ALL DATA LOADED", Toast.LENGTH_SHORT);
                toast.show();

            } else {
                Toast toast = Toast.makeText(PopulateField.this, "DATA Still Loading", Toast.LENGTH_SHORT);
                toast.show();

            }
            for(int i=0; i<doc.getInputSize(); i++) {
                Input mInput = doc.getInput(i);
                int resID = getResources().getIdentifier(mInput.name, "id", getPackageName());
                EditText mEdit = findViewById(resID);
                mEdit.setText(mInput.value);
            }
        }
    };


    Map<String, RectF> manifestDetails = new HashMap<String,RectF>();

    ArrayList<Map<String, RectF>> mapArrayList = new ArrayList<Map<String, RectF>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate_field);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        c=this;

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(docBroadcastReceiver,
                        new IntentFilter(StrokeService.STROKE_SERVICE_MESSAGE));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast toast = Toast.makeText(PopulateField.this, "Loading Data.......", Toast.LENGTH_LONG);
//                toast.show();
                Log.d(TAG,"POPULATEFIELDFUNCTION>>>>");
                loadDetails();
//                uploadResult();
            }
        });
    }



    private void loadDetails() {
        Gson gson = new Gson();
        Document doc = gson.fromJson(TestDoc.testDoc, Document.class);
        Intent intent = new Intent(this, StrokeService.class);
        intent.putExtra(StrokeService.DOCUMENT, doc);
        startService(intent);
//        engine = IInkApplication.getEngine();
//        // Configure recognition
//        Configuration conf = engine.getConfiguration();
//        String confDir = "zip://" + getPackageCodePath() + "!/assets/conf";
//
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
//
//        //Width and Height for Neo Paper
////        editor.setViewSize(92, 118);
//        editor.setViewSize(118, 92);
//
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
//        // Associate editor with the new part
//        editor.setPart(contentPart);
//        mDataSource = new DataSource(this);
//
//        StringBuilder readFile = new StringBuilder();
//        readFile.append("");
//        for(int i=0; i<mapArrayList.size(); i++) {
//            Map<String, RectF> currentMap = mapArrayList.get(i);
//            // Do something with the value
//            // Loop through each of the Form Value and their coordinates and send to MyScript through Datasource
//            for(Map.Entry<String,RectF> entry : currentMap.entrySet()) {
////                "{\"name\":\"first_name\",\"type\":\"text\",\"rectF\":{'left':32,'top':32,'right':32,'bottom':32}}," +
////                readFile.append(
////                );
//                Log.d(TAG,"\"{\"name\":\"" + entry.getKey() + "\",\"type\":\"text\",\"rectF\":{'left':"+entry.getValue().left+",'top':"+entry.getValue().top+",'right':"+entry.getValue().right+",'bottom':"+entry.getValue().bottom+"}},\" +\n");
//                Log.d(TAG,entry.getKey());
//                int resID = getResources().getIdentifier(entry.getKey(), "id", getPackageName());
//                EditText mEdit = findViewById(resID);
////                String text = mDataSource.getValue(entry.getValue(),editor,c,entry.getKey());
//                String text = "";
//                if(!text.equals("")){
//                    mEdit.setText(text);
//                } else{
//                    mEdit.setText("Value Not found");
//                }
//            }
//        }
//
//        editor.setPart(null);
//        contentPart.close();
//        contentPackage.close();
//        try {
//            engine.deletePackage(packageName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        engine = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void fill(Document doc) {
        for(int i=0; i<doc.getInputSize(); i++) {
            Input mInput = doc.getInput(i);
            int resID = getResources().getIdentifier(mInput.name, "id", getPackageName());
            EditText mEdit = findViewById(resID);
            mEdit.setText(mInput.value);
        }
    }
    public void uploadResult(){
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint("https://test.twinpaper.com/upload/");
        requestPackage.setRequestParams("doc_type", "receipt");

        // Create Post Form Parameters
        for (Map.Entry<String,RectF> entry : manifestDetails.entrySet()) {
            Log.d(TAG,entry.getKey());
            int resID = getResources().getIdentifier(entry.getKey(), "id", getPackageName());
            EditText mEdit = findViewById(resID);
            requestPackage.setPostParams(entry.getKey(), mEdit.getText().toString());
        }


        requestPackage.setMethod("POST");
        Log.i(TAG, "Creating intent service");
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
        startService(intent);
    }
}