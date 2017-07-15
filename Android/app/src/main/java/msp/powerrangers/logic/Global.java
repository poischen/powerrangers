package msp.powerrangers.logic;

import android.util.Log;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

/**
 * provides some methods, used from several logic classes
 */

public final class Global {

    public final String PATH = "path";
    public final String FILE = "file";

    public Global(){

    }

    public static HashMap<String, String> splitPath(String url){
        HashMap<String, String> hashmap = new HashMap<String, String>();

        hashmap.put("path", "test");
        hashmap.put("file", "test");

        return hashmap;
    }

    public static String getThumbUrl (String stringUrl) {
        try {
            //URI sourceUrl = new URI("https://firebasestorage.googleapis.com" + stringUrl);
            //File file = new File(stringUrl);
            //String path = file.getPath();
            //String fileAndExtension = file.getName();

            String file = stringUrl.substring(stringUrl.lastIndexOf('/')+1, stringUrl.length());
            String path = stringUrl.substring(0, stringUrl.lastIndexOf('/')+1);

            String StringThumbUrl = path + "thumb_" + file;
            return StringThumbUrl;
        } catch (Exception e){
            Log.v("Global", "stringUrl: " + stringUrl);
            return stringUrl;
        }
    }

}
