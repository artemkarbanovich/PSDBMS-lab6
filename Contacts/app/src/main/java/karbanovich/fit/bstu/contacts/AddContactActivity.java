package karbanovich.fit.bstu.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.time.LocalDate;
import java.util.ArrayList;


public class AddContactActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText phoneNumber;
    private DatePicker birthday;
    private TextView birthdayText;
    private boolean validationStatus;
    private Toast notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        bindingView();
        validation();
    }

    public void add(View view) {
        validationStatus = true;
        callView();

        if(!validationStatus) {
            Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_LONG).show();
            return;
        }

        String name = this.name.getText().toString();
        String surname = this.surname.getText().toString();
        String phoneNumber = this.phoneNumber.getText().toString();
        int year = this.birthday.getYear();
        int month = this.birthday.getMonth() + 1;
        int day = this.birthday.getDayOfMonth();

        String birthday = year + ".";

        if(String.valueOf(month).length() == 1) {
            birthday += "0" + month + ".";
        } else birthday += month + ".";

        if(String.valueOf(day).length() == 1) {
            birthday += "0" + day;
        } else birthday += day;


        try {
            Contact contact = new Contact(name, surname, phoneNumber, birthday);
            ArrayList<Contact> contacts = JSONHelper.getContactsJSON(this);

            contacts.add(contact);
            JSONHelper.saveContactsJSON(this, contacts);

            notification = Toast.makeText(this, "Контакст успешно добавлен", Toast.LENGTH_LONG);
        } catch(Exception e ) {
            notification = Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_LONG);
        } finally {
            notification.show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void validation() {
        name.addTextChangedListener(new ValidationHelper(name) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.length() < 2 || text.length() > 25) {
                    textView.setError("Длина должна быть от 2 до 25");
                    validationStatus = false;
                }
                if(text.trim().length() == 0) {
                    textView.setError("Не может быть пустая строка");
                    validationStatus = false;
                }
            }
        });
        surname.addTextChangedListener(new ValidationHelper(surname) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.length() < 2 || text.length() > 25) {
                    textView.setError("Длина должна быть от 2 до 25");
                    validationStatus = false;
                }
                if(text.trim().length() == 0) {
                    textView.setError("Не может быть пустая строка");
                    validationStatus = false;
                }
            }
        });
        phoneNumber.addTextChangedListener(new ValidationHelper(phoneNumber) {
            @Override
            public void validate(TextView textView, String text) {
                if(!text.matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
                    textView.setError("Неверный формат телефона");
                    validationStatus = false;
                }
            }
        });
        birthday.setOnDateChangedListener((datePicker, i, i1, i2) -> {
            LocalDate date = LocalDate.of(i, i1 + 1, i2);

            if(date.plusYears(14).isAfter(LocalDate.now())) {
                birthdayText.setError("Вы должны быть старше 14");
                birthdayText.requestFocus();
                validationStatus = false;
            } else birthdayText.setError(null);
        });
    }

    private void bindingView() {
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        birthday = (DatePicker) findViewById(R.id.birthday);
        birthdayText = (TextView) findViewById(R.id.birthdayText);
    }

    private void callView() {
        name.setText(name.getText().toString());
        surname.setText(surname.getText().toString());
        phoneNumber.setText(phoneNumber.getText().toString());
        birthday.updateDate(birthday.getYear(), birthday.getMonth(), birthday.getDayOfMonth() + 1);
        birthday.updateDate(birthday.getYear(), birthday.getMonth(), birthday.getDayOfMonth() - 1);
    }
}