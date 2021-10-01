package karbanovich.fit.bstu.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addContact(View view) {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    public void contacts(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }
}