package karbanovich.fit.bstu.contacts;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSONHelper {
    private static final String FILE_NAME = "contacts.json";

    public static ArrayList<Contact> getContactsJSON(Context context) {
        if(!isExist(context))
            return new ArrayList<>();

        Gson gson = new Gson();
        File fileInternal = new File(context.getFilesDir(), FILE_NAME);

        try {
            FileInputStream fisInternal = new FileInputStream(fileInternal);
            InputStreamReader isrInternal = new InputStreamReader(fisInternal);

            DataItems contacts = gson.fromJson(isrInternal, DataItems.class);

            fisInternal.close();
            isrInternal.close();
            return contacts.getContacts();
        } catch (Exception e) { }

        return new ArrayList<>();
    }

    public static void saveContactsJSON(Context context, ArrayList<Contact> contacts) {
        File fileInternal = new File(context.getFilesDir(), FILE_NAME);
        File fileExternal = new File(context.getExternalFilesDir(null), FILE_NAME);

        Gson gson = new Gson();
        DataItems dataItems = new DataItems();

        dataItems.setContacts(contacts);
        String jsonStr = gson.toJson(dataItems);

        try {
            FileOutputStream fosInternal = new FileOutputStream(fileInternal, false);
            FileOutputStream fosExternal = new FileOutputStream(fileExternal, false);

            fosInternal.write(jsonStr.getBytes());
            fosExternal.write(jsonStr.getBytes());

            fosInternal.close();
            fosExternal.close();
        } catch (Exception e) { }
    }

    private static boolean isExist(Context context) {
        File fileInternal = new File(context.getFilesDir(), FILE_NAME);
        File fileExternal = new File(context.getExternalFilesDir(null), FILE_NAME);
        return fileInternal.exists() && fileExternal.exists();
    }

    private static class DataItems {
        private ArrayList<Contact> contacts;

        public ArrayList<Contact> getContacts() {return contacts;}
        public void setContacts(ArrayList<Contact> contacts) {this.contacts = contacts;}
    }
}
