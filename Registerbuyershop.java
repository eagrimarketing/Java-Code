package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vivek on 19-03-2017.
 */
public class Registerbuyershop extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    String buyer_id, buyer_name, buyer_contact = null;
    String occupation = null;
    ArrayList<Details_Shop> shop_details = new ArrayList<Details_Shop>();
    ListView lv;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    FloatingActionButton fab_add_shop;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_buyershop_listview);


        Intent geti = getIntent();

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        buyer_id = sp.getString("ID", null);
        occupation = sp.getString("Occupation", null);
        buyer_name = sp.getString("FName", null) + " " + sp.getString("LName", null);
        buyer_contact = sp.getString("MPhone", null);
        fab_add_shop = (FloatingActionButton)findViewById(R.id.fab_add_shop);

        if (occupation.contentEquals("Buyer/Store Owner")) {
        } else {
            Toast.makeText(this, "Only Buyer can edit the Shop List" + occupation, Toast.LENGTH_SHORT).show();
            this.finish();
        }

//* Send farmer ID to fetch details of the current Farms registered by the farmer *//

        getexisting_shop_details();

        fab_add_shop.setOnClickListener(new View.OnClickListener()
        {

        @Override
        public void onClick(View v)
        {
            ArrayList<String> shop_edit_intent2 = new ArrayList<String>();
            shop_edit_intent2.add("Add");
            shop_edit_intent2.add(" ");
            shop_edit_intent2.add(buyer_id);
            shop_edit_intent2.add(" ");
            shop_edit_intent2.add(" ");
            shop_edit_intent2.add(" ");
            shop_edit_intent2.add(" ");
            shop_edit_intent2.add(" ");
            shop_edit_intent2.add(buyer_name);
            shop_edit_intent2.add(buyer_contact);

                    Intent newcrop = new Intent(getApplicationContext(), Registerbuyershop_edit.class);
                    newcrop.putStringArrayListExtra("Data", shop_edit_intent2);
                    startActivityForResult(newcrop, 2);

        }
        }
        );

//* Dispay listview of current farms registered by the farmer on his phone*//

//
//        lv = (ListView) findViewById(R.id.buyer_shop_list);
//        ArrayAdapter<Details_Shop> aa_ds = new Buyer_shop_adaptor();
//        lv.setAdapter(aa_ds);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {

    }


    public void getexisting_shop_details() {
        JSONArray jbuyer_array = new JSONArray();
        JSONObject jbuyer_object = new JSONObject();
        try {
            jbuyer_object.put("ID", buyer_id);
            jbuyer_object.put("City","NA");
            jbuyer_array.put(jbuyer_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetBuyerShopDetails.php";
        json_array_data = jbuyer_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void push_data_to_cloud(String url, String jsondata)
    {
        AsyncHttpRequest buyershopdata = new AsyncHttpRequest(this, url, "GetBuyerShopdetails", jsondata, this);
        buyershopdata.execute();


    }


    @Override
    public void getResponse(String serverResponse, String responseType) {

        if (serverResponse.contentEquals("256[]"))
        {
            Toast.makeText(this,"No Response from Sever",Toast.LENGTH_LONG).show();
        }
        else {
            try {
                JSONArray ja = new JSONArray(serverResponse);
                JSONObject jo = new JSONObject();
                jo = ja.getJSONObject(0);
                String s = jo.getString("Status");

                if (s.equals("Success")) {

                    int i = 0;
                    jo = ja.getJSONObject(1);
                    JSONArray ja1 = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    ja1 = jo.getJSONArray("Data");

                    for (i = 0; i < ja1.length(); i++) {
                        jo1 = ja1.getJSONObject(i);
                        String shop_id = jo1.getString("ShopID");
                        String buyerid = jo1.getString("BuyerID");
                        String buyer_name = jo1.getString("BuyerName");
                        String buyer_contact = jo1.getString("MobileNum");
                        String plotnum = jo1.getString("PlotNum");
                        String streetname = jo1.getString("StreetName");
                        String city = jo1.getString("City");
                        String district = jo1.getString("District");
                        String state = jo1.getString("State");

                        Details_Shop ds = new Details_Shop(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
                        ds.setDS_Shop_ID(shop_id);
                        ds.setDS_Buyer_ID(buyerid);
                        ds.setDS_Buyer_Name(buyer_name);
                        ds.setDS_Buyer_Contact(buyer_contact);
                        ds.setDS_Plotnum(plotnum);
                        ds.setDS_Streetname(streetname);
                        ds.setDS_City(city);
                        ds.setDS_District(district);
                        ds.setDS_State(state);

                        shop_details.add(ds);
                    }
                } else {
                    getexisting_shop_details();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (shop_details.size() > 0) {
            manage_shop_adaptor();
        }


    }

    public void manage_shop_adaptor() {

        lv = (ListView) findViewById(R.id.buyer_shop_list);
        Buyer_shop_adaptor aa_ds = new Buyer_shop_adaptor(this, R.layout.register_buyershop_customview, shop_details);

        lv.setAdapter(aa_ds);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Registerbuyershop Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://my.project.agrim/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Registerbuyershop Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://my.project.agrim/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class Buyer_shop_adaptor extends ArrayAdapter<Details_Shop> {
// Class will display list of all shops registered by the buyer till the moment

        ArrayList<Details_Shop> shop_data = new ArrayList<Details_Shop>();
        private LayoutInflater mInflater;

        public Buyer_shop_adaptor(Context context, int resource, ArrayList<Details_Shop> objects) {
            super(context, resource, objects);
            this.shop_data = objects;
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return shop_data.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);

            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.register_buyershop_customview, null);
            }

            Details_Shop ds = shop_data.get(position);

            TextView shopid = (TextView) convertView.findViewById(R.id.buyershop_data_buyershop_id);
            TextView plotnum = (TextView)convertView.findViewById(R.id.buyershop_data_plot_num);
            TextView streetname = (TextView)convertView.findViewById(R.id.buyershop_data_street_name);
            TextView city = (TextView)convertView.findViewById(R.id.buyershop_data_city);
            TextView district =(TextView)convertView.findViewById(R.id.buyershop_data_district);
            TextView state = (TextView)convertView.findViewById(R.id.buyershop_data_state);

            shopid.setText(ds.getDS_Shop_ID());
            plotnum.setText(ds.getDS_Plotnum());
            streetname.setText(ds.getDS_Streetname());
            city.setText(ds.getDS_City());
            district.setText(ds.getDS_District());
            state.setText(ds.getDS_State());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> shop_edit_intent = new ArrayList<String>();

                    shop_edit_intent.add("Update");
                    shop_edit_intent.add(shop_details.get(position).getDS_Shop_ID());

                    if (shop_data.get(position).getDS_Buyer_ID().isEmpty()) {
                        shop_edit_intent.add(buyer_id);
                    } else {
                        shop_edit_intent.add(shop_details.get(position).getDS_Buyer_ID());
                    }
                    shop_edit_intent.add(shop_details.get(position).getDS_Plotnum());
                    shop_edit_intent.add(shop_details.get(position).getDS_Streetname());
                    shop_edit_intent.add(shop_details.get(position).getDS_City());
                    shop_edit_intent.add(shop_details.get(position).getDS_District());
                    shop_edit_intent.add(shop_details.get(position).getDS_State());
                    if (shop_details.get(position).getDS_Buyer_Name().isEmpty()) {
                        shop_edit_intent.add(buyer_name);
                    } else {
                        shop_edit_intent.add(shop_details.get(position).getDS_Buyer_Name());
                    }
                    if (shop_details.get(position).getDS_Buyer_Contact().isEmpty()) {
                        shop_edit_intent.add(buyer_contact);
                    } else {
                        shop_edit_intent.add(shop_details.get(position).getDS_Buyer_Contact());
                    }


                    Intent i = new Intent(getApplicationContext(), Registerbuyershop_edit.class);
                    i.putStringArrayListExtra("Data", shop_edit_intent);
                    startActivityForResult(i, 1);


                }
            });


            return convertView;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                this.finish();
                break;
            case 2:
                this.finish();
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registerbuyershop_listview, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ArrayList<String> shop_edit_intent2 = new ArrayList<String>();
        shop_edit_intent2.add("Add");
        shop_edit_intent2.add(" ");
        shop_edit_intent2.add(buyer_id);
        shop_edit_intent2.add(" ");
        shop_edit_intent2.add(" ");
        shop_edit_intent2.add(" ");
        shop_edit_intent2.add(" ");
        shop_edit_intent2.add(" ");
        shop_edit_intent2.add(buyer_name);
        shop_edit_intent2.add(buyer_contact);

        switch (item.getItemId()) {
            case R.id.buyershop_action_add:
                Intent newcrop = new Intent(getApplicationContext(), Registerbuyershop_edit.class);
                newcrop.putStringArrayListExtra("Data", shop_edit_intent2);
                startActivityForResult(newcrop, 2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
