package com.giovannisaberon.simplestories;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ImageJson {

    private Context context;

    public ImageJson(Context context){
        this.context = context;
    }

    public String loadJSONFromAsset(String filename) throws IOException {
        String json = "there is nothing";
        AssetManager am = context.getAssets();

        try {
            InputStream is = am.open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "error";
        }
        Log.i("json bible: ", json);
        return json;
    }

    public JSONObject getJsonObject(String file) throws JSONException {
        JSONObject obj = new JSONObject(file);
        return obj;
    }


    public JSONArray getSlides(String  filename, String key) throws JSONException, IOException {
        String jsonstring = this.loadJSONFromAsset(filename);
        JSONObject jsonObject = new JSONObject(jsonstring);
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        Log.i("json array: ", jsonArray.toString());

        return jsonArray;
    }
}
