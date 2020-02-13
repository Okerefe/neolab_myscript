package com.myscript.iink.demo.utils;

import android.content.Context;
import android.util.Log;

import com.myscript.iink.Configuration;
import com.myscript.iink.ContentPackage;
import com.myscript.iink.ContentPart;
import com.myscript.iink.Editor;
import com.myscript.iink.Engine;
import com.myscript.iink.MimeType;
import com.myscript.iink.PointerEvent;
import com.myscript.iink.Renderer;
import com.myscript.iink.demo.IInkApplication;
import com.myscript.iink.demo.models.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MyScriptEngine {

    private static final String TAG = "MyScriptEngine";

    public static String textEngine(Context c, Document doc, PointerEvent[] arrayPointerEvents,File file) {
        Engine engine;
        Editor editor;
        String language = "en_US";
        String packageName = "package.iink";
        String partType = "Text";
        ContentPart contentPart = null;
        ContentPackage contentPackage = null;
        MimeType mimeType = MimeType.TEXT;

        engine = IInkApplication.getEngine();
        // Configure recognition
        Configuration conf = engine.getConfiguration();
        String confDir = "zip://" + c.getPackageCodePath() + "!/assets/conf";
        Log.d(TAG, "&&&&&&&&&&&PACKAGE CODE PATH!!!" + c.getPackageCodePath());
        conf.setStringArray("configuration-manager.search-path", new String[]{confDir});
        conf.setString("content-package.temp-folder", c.getFilesDir().getPath() + File.separator + "tmp");
        conf.setString("lang", language);
        // Configure the engine to disable guides (recommended)
        conf.setBoolean("text.guides.enable", false);

        Renderer renderer = engine.createRenderer(11, 11, null);
        editor = engine.createEditor(renderer);

        // The editor requires a font metrics provider and a view size *before* calling setPart()
        editor.setFontMetricsProvider(null);
//        editor.setPart(contentPart);
        switch (doc.orientation){
            case Document.ORIENTATION_PORTRAIT:
                editor.setViewSize(92, 118);
                break;
            case Document.ORIENTATION_LANDSCAPE:
                editor.setViewSize(118, 92);
                break;
        }

        try {
            // Create a new package
            contentPackage = engine.createPackage(packageName);

            // Create a new part
            contentPart = contentPackage.createPart(partType);
        } catch (IOException e) {
            Log.d(TAG, "FAILED<<<<<<<<<>>>>>>>>>>??????????????");
            Log.e(TAG, "Failed to open package \"" + packageName + "\"", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Failed to open package \"" + packageName + "\"", e);
            Log.d(TAG, "FAILED<<<<<<<<<>>>>>>>>>>??????????????");
        }
        editor.setPart(contentPart);
        editor.pointerEvents(arrayPointerEvents, false);
        Log.d(TAG, "Reached Function IIDLE");

        editor.waitForIdle();
        Log.d(TAG, "PASSED Function IIDLE");
        try {
//            Export File and Read The Contents Of the File then Delete the File
            editor.export_(null, file.getAbsolutePath(), mimeType, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "There Were Some Errors ");
        }
        StringBuilder readFile = new StringBuilder();
        readFile.append("");
        Scanner myReader = null;
        try {
            myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                readFile.append(myReader.nextLine());
            }
            myReader.close();
//            Delete File
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        editor.clear();
        editor.setPart(null);

        contentPart.close();
        contentPackage.close();
        try {
            engine.deletePackage(packageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        engine = null;
        Log.d(TAG, "Reached Function Final");
        return readFile.toString();
    }
}
