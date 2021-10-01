package karbanovich.fit.bstu.birthdays;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ListView contactsList;
    private ArrayList<Contact> filteredContacts;
    private ArrayList<Contact> contacts;
    private CustomListAdapter customListAdapter;
    private DatePicker date;
    private Date lastModified;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        verifyStoragePermissions(this);
        binding();
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void binding() {
        date = (DatePicker) findViewById(R.id.date);
        contactsList = (ListView) findViewById(R.id.contactsList);
        contacts = JSONHelper.getContactsJSON(this);
        lastModified = JSONHelper.lastModifiedFile();
        filteredContacts = new ArrayList<>();
        customListAdapter = new CustomListAdapter(this, filteredContacts);
        contactsList.setAdapter(customListAdapter);
        date.setOnDateChangedListener((datePicker, i, i1, i2) -> {
            customListAdapter.updateContactsList();
        });
    }

    private void setFilteredContacts() {
        if(lastModified.before(JSONHelper.lastModifiedFile())) {
            contacts = JSONHelper.getContactsJSON(this);
            lastModified = JSONHelper.lastModifiedFile();
        }

        int year = this.date.getYear();
        int month = this.date.getMonth() + 1;
        int day = this.date.getDayOfMonth();

        String birthday = year + ".";

        if(String.valueOf(month).length() == 1) {
            birthday += "0" + month + ".";
        } else birthday += month + ".";

        if(String.valueOf(day).length() == 1) {
            birthday += "0" + day;
        } else birthday += day;

        final String date = birthday;

        filteredContacts = new ArrayList<>();
        filteredContacts = (ArrayList<Contact>) contacts.stream()
                .filter(c -> c.getBirthday().equals(date)).collect(Collectors.toList());
    }

    public class CustomListAdapter extends BaseAdapter {

        private ArrayList<Contact> contacts;
        private Context context;


        public CustomListAdapter(Context context, ArrayList<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
        }

        @Override
        public int getCount() {return contacts.size();}

        @Override
        public Object getItem(int i) {return null;}

        @Override
        public long getItemId(int i) {return 0;}

        public void updateContactsList() {
            setFilteredContacts();
            contacts.clear();
            contacts.addAll(filteredContacts);
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = getLayoutInflater().inflate(R.layout.contacts_item, null);

            TextView itemNameSurname = (TextView) view.findViewById(R.id.itemNameSurname);
            TextView itemPhoneNumber = (TextView) view.findViewById(R.id.itemPhoneNumber);
            TextView itemBirthday = (TextView) view.findViewById(R.id.itemBirthday);

            itemNameSurname.setText(contacts.get(position).getName() + " " + contacts.get(position).getSurname());
            itemPhoneNumber.setText(contacts.get(position).getPhoneNumber());
            itemBirthday.setText("День рождения: " + contacts.get(position).getBirthday());

            return view;
        }
    }
}