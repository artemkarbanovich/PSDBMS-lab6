package karbanovich.fit.bstu.birthdays;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class JSONHelper {
    private static final String FILE_NAME = "contacts.json";


    public static ArrayList<Contact> getContactsJSON(Context context) {
        if(!isExist(context))
            return new ArrayList<>();

        Gson gson = new Gson();
        File fileExternal = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/karbanovich.fit.bstu.contacts/files", FILE_NAME);

        try {
            FileInputStream fisExternal = new FileInputStream(fileExternal);
            InputStreamReader isrExternal = new InputStreamReader(fisExternal);

            DataItems contacts = gson.fromJson(isrExternal, DataItems.class);

            fisExternal.close();
            isrExternal.close();
            return contacts.getContacts();
        } catch (Exception e) { }

        return new ArrayList<>();
    }

    private static boolean isExist(Context context) {
        File fileExternal = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/karbanovich.fit.bstu.contacts/files", FILE_NAME);
        return fileExternal.exists();
    }

    public static Date lastModifiedFile() {
        File fileExternal = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/karbanovich.fit.bstu.contacts/files", FILE_NAME);
        Date lastModified = new Date(fileExternal.lastModified());
        return lastModified;
    }

    private static class DataItems {
        private ArrayList<Contact> contacts;

        public ArrayList<Contact> getContacts() {return contacts;}
        public void setContacts(ArrayList<Contact> contacts) {this.contacts = contacts;}
    }
}
