package karbanovich.fit.bstu.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ContactsActivity extends AppCompatActivity {

    ListView contactsList;
    ArrayList<Contact> contacts;
    CustomListAdapter customListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        binding();
    }

    private void binding() {
        contactsList = (ListView) findViewById(R.id.contactsList);
        contacts = JSONHelper.getContactsJSON(this);
        customListAdapter = new CustomListAdapter(this, contacts);
        contactsList.setAdapter(customListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {return false;}

            @Override
            public boolean onQueryTextChange(String s) {
                customListAdapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search_view)
            return true;

        return super.onOptionsItemSelected(item);
    }

    public class CustomListAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Contact> contacts;
        private ArrayList<Contact> filteredContacts;
        private Context context;


        public CustomListAdapter(Context context, ArrayList<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
            this.filteredContacts = contacts;
        }

        @Override
        public int getCount() {return filteredContacts.size();}

        @Override
        public Object getItem(int i) {return null;}

        @Override
        public long getItemId(int i) {return 0;}

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = getLayoutInflater().inflate(R.layout.contacts_item, null);

            TextView itemNameSurname = (TextView) view.findViewById(R.id.itemNameSurname);
            TextView itemPhoneNumber = (TextView) view.findViewById(R.id.itemPhoneNumber);
            TextView itemBirthday = (TextView) view.findViewById(R.id.itemBirthday);

            itemNameSurname.setText(filteredContacts.get(position).getName() + " " + filteredContacts.get(position).getSurname());
            itemPhoneNumber.setText(filteredContacts.get(position).getPhoneNumber());
            itemBirthday.setText("День рождения: " + filteredContacts.get(position).getBirthday());

            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();

                    if(charSequence == null || charSequence.length() == 0) {
                        filterResults.count = contacts.size();
                        filterResults.values = contacts;
                    } else {
                        String searchStr = charSequence.toString().toLowerCase(Locale.ROOT);
                        ArrayList<Contact> resultData = new ArrayList<>();

                        for(Contact item : contacts) {
                            if((item.getName() + " " + item.getSurname()).toLowerCase(Locale.ROOT).contains(searchStr)
                                    || item.getPhoneNumber().toLowerCase(Locale.ROOT).contains(searchStr))
                                resultData.add(item);

                            filterResults.count = resultData.size();
                            filterResults.values = resultData;
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filteredContacts = (ArrayList<Contact>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }
}