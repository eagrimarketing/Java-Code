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

public class RegisterFarm extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, HttpResponse {

    String farmer_id, farmer_name, farmer_contact = null;
    String occupation = null;
    ArrayList<Details_Farm> farm_details = new ArrayList<Details_Farm>();
    FloatingActionButton fab_add_farm;

    ListView lv;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_farm_listview);


        Intent geti = getIntent();

        fab_add_farm = (FloatingActionButton)findViewById(R.id.fab_add_farm);

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        farmer_id = sp.getString("ID", null);
        occupation = sp.getString("Occupation", null);
        farmer_name = sp.getString("FName", null) + " " + sp.getString("LName", null);
        farmer_contact = sp.getString("MPhone", null);

        if (occupation.contentEquals("Farmer")) {
        } else {
            Toast.makeText(this, "Only Farmer can edit the Farm List" + occupation, Toast.LENGTH_SHORT).show();
            this.finish();
        }

//* Send farmer ID to fetch details of the current Farms registered by the farmer *//

        getexisting_farm_details();

//* Dispay listview of current farms registered by the farmer on his phone*//

//        lv=(ListView)findViewById(R.id.farmer_farm_list);
//        Farmer_farm_adaptor aa_df = new Farmer_farm_adaptor(this, R.layout.register_farm_customview, farm_details);
////        ArrayAdapter<Details_Farm> aa_df = new Farmer_farm_adaptor();
//        lv.setAdapter(aa_df);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        fab_add_farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> farm_edit_intent3 = new ArrayList<String>();
                farm_edit_intent3.add("Add");
                farm_edit_intent3.add(" ");
                farm_edit_intent3.add(farmer_id);
                farm_edit_intent3.add(" ");
                farm_edit_intent3.add(" ");
                farm_edit_intent3.add(" ");
                farm_edit_intent3.add(" ");
                farm_edit_intent3.add(" ");
                farm_edit_intent3.add(farmer_name);
                farm_edit_intent3.add(farmer_contact);

                        Intent newcrop1 = new Intent(getApplicationContext(), Registerfarm_edit.class);
                        newcrop1.putStringArrayListExtra("Data", farm_edit_intent3);
                        startActivityForResult(newcrop1, 2);



            }
        });

    }

    @Override
    public void onClick(View v) {

    }


    public void getexisting_farm_details() {
        JSONArray jfarmer_array = new JSONArray();
        JSONObject jfarmer_object = new JSONObject();
        try {
            jfarmer_object.put("ID", farmer_id);
            jfarmer_array.put(jfarmer_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetFarmdetails.php";
        json_array_data = jfarmer_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void push_data_to_cloud(String url, String jsondata) {
//        if(ConnectionDetector.isNetworkAvailable(this))
//        {
        AsyncHttpRequest farmdata = new AsyncHttpRequest(this, url, "GetFarmdetails", jsondata, this);
        farmdata.execute();


//        }
//        else {
//        Toast.makeText(this, "Trying Internet Connection", Toast.LENGTH_LONG).show();
//        }

    }

    @Override
    public void getResponse(String serverResponse, String responseType) {

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
                jo1 = new JSONObject();
                for (i = 0; i < ja1.length(); i++) {
                    jo1 = ja1.getJSONObject(i);
                    String farm_id = jo1.getString("FarmID");
                    String farmerid = jo1.getString("FarmerID");
                    String farmer_name = jo1.getString("FarmerName");
                    String farmer_contact = jo1.getString("MobileNum");
                    String plotnum = jo1.getString("PlotNum");
                    String streetname = jo1.getString("StreetName");
                    String village = jo1.getString("Village");
                    String district = jo1.getString("District");
                    String state = jo1.getString("State");

                    Details_Farm df = new Details_Farm(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
                    df.setDF_Farm_ID(farm_id);
                    df.setDF_Farmer_ID(farmerid);
                    df.setDF_Farmer_Name(farmer_name);
                    df.setDF_Farmer_Contact(farmer_contact);
                    df.setDF_Plotnum(plotnum);
                    df.setDF_Streetname(streetname);
                    df.setDF_Village(village);
                    df.setDF_District(district);
                    df.setDF_State(state);

                    farm_details.add(df);

                }

            } else {
                getexisting_farm_details();

//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Toast.makeText(this, "array size" + farm_details.size(), Toast.LENGTH_LONG).show();
        if (farm_details.size() > 0) {
            manage_farm_adaptor();
        }

    }

    public void manage_farm_adaptor() {

        lv = (ListView) findViewById(R.id.farmer_farm_list);
        Farmer_farm_adaptor aa_df = new Farmer_farm_adaptor(this, R.layout.register_farm_customview, farm_details);
        ////        ArrayAdapter<Details_Farm> aa_df = new Farmer_farm_adaptor();
        lv.setAdapter(aa_df);


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegisterFarm Page", // TODO: Define a title for the content shown.
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
                "RegisterFarm Page", // TODO: Define a title for the content shown.
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


    private class Farmer_farm_adaptor extends ArrayAdapter<Details_Farm> {

        ArrayList<Details_Farm> farm_data = new ArrayList<Details_Farm>();
        private LayoutInflater mInflater;

        public Farmer_farm_adaptor(Context context, int resource, ArrayList<Details_Farm> objects) {
            super(context, resource, objects);
            this.farm_data=objects;
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return farm_data.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);
//                View v = convertView;
            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.register_farm_customview, null);
            }

            Details_Farm df = farm_data.get(position);
//            setContentView(R.layout.register_farm_customview);


//            if (df!=null) {
                TextView farmid = (TextView)convertView.findViewById(R.id.farm_data_farm_id);
                TextView plotnum = (TextView)convertView.findViewById(R.id.farm_data_plot_num);
                TextView streetname = (TextView)convertView.findViewById(R.id.farm_data_street_name);
                TextView village = (TextView)convertView.findViewById(R.id.farm_data_village);
                TextView district = (TextView)convertView.findViewById(R.id.farm_data_district);
                TextView state = (TextView)convertView.findViewById(R.id.farm_data_state);

                farmid.setText(df.getDF_Farm_ID());

                plotnum.setText(df.getDF_Plotnum());
                streetname.setText(df.getDF_Streetname());
                village.setText(df.getDF_Village());
                district.setText(df.getDF_District());
                state.setText(df.getDF_State());
//            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(),"inside click",Toast.LENGTH_LONG).show();
                    ArrayList<String> farm_edit_intent = new ArrayList<String>();
                    farm_edit_intent.add("Update");
                    farm_edit_intent.add(farm_details.get(position).getDF_Farm_ID());

                    if (farm_details.get(position).getDF_Farmer_ID().isEmpty()) {
                        farm_edit_intent.add(farmer_id);
                    } else {
                        farm_edit_intent.add(farm_details.get(position).getDF_Farmer_ID());
                    }

                    farm_edit_intent.add(farm_details.get(position).getDF_Plotnum());
                    farm_edit_intent.add(farm_details.get(position).getDF_Streetname());
                    farm_edit_intent.add(farm_details.get(position).getDF_Village());
                    farm_edit_intent.add(farm_details.get(position).getDF_District());
                    farm_edit_intent.add(farm_details.get(position).getDF_State());
                    if (farm_details.get(position).getDF_Farmer_Name().isEmpty()) {
                        farm_edit_intent.add(farmer_name);
                    } else {
                        farm_edit_intent.add(farm_details.get(position).getDF_Farmer_Name());
                    }
                    if (farm_details.get(position).getDF_Farmer_Contact().isEmpty()) {
                        farm_edit_intent.add(farmer_contact);
                    } else {
                        farm_edit_intent.add(farm_details.get(position).getDF_Farmer_Contact());
                    }


                    Intent i = new Intent(getApplicationContext(), Registerfarm_edit.class);
                    i.putStringArrayListExtra("Data", farm_edit_intent);
                    startActivityForResult(i, 1);

                }
            });

            return convertView;


        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                farm_details.clear();
                getexisting_farm_details();
                break;
            case 2:
                this.finish();
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////        ArrayList<String> farm_edit_intent = new ArrayList<String>();
//////        Toast.makeText(this,"To update the Shop "+farm_details.get(position).getDF_Farm_ID(),Toast.LENGTH_SHORT).show();
////        farm_edit_intent.add("Update");
////        farm_edit_intent.add(farm_details.get(position).getDF_Farm_ID());
////
////        if (farm_details.get(position).getDF_Farmer_ID().isEmpty()) {
////            farm_edit_intent.add(farmer_id);
////        } else {
////            farm_edit_intent.add(farm_details.get(position).getDF_Farmer_ID());
////        }
////
////        farm_edit_intent.add(farm_details.get(position).getDF_Plotnum());
////        farm_edit_intent.add(farm_details.get(position).getDF_Streetname());
////        farm_edit_intent.add(farm_details.get(position).getDF_Village());
////        farm_edit_intent.add(farm_details.get(position).getDF_District());
////        farm_edit_intent.add(farm_details.get(position).getDF_State());
////        if (farm_details.get(position).getDF_Farmer_Name().isEmpty()) {
////            farm_edit_intent.add(farmer_name);
////        } else {
////            farm_edit_intent.add(farm_details.get(position).getDF_Farmer_Name());
////        }
////        if (farm_details.get(position).getDF_Farmer_Contact().isEmpty()) {
////            farm_edit_intent.add(farmer_contact);
////        } else {
////            farm_edit_intent.add(farm_details.get(position).getDF_Farmer_Contact());
////        }
////
////
////        Intent i = new Intent(getApplicationContext(), Registerfarm_edit.class);
////        i.putStringArrayListExtra("Data", farm_edit_intent);
////        startActivityForResult(i, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registerfarm_listview, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ArrayList<String> farm_edit_intent2 = new ArrayList<String>();
        farm_edit_intent2.add("Add");
        farm_edit_intent2.add(" ");
        farm_edit_intent2.add(farmer_id);
        farm_edit_intent2.add(" ");
        farm_edit_intent2.add(" ");
        farm_edit_intent2.add(" ");
        farm_edit_intent2.add(" ");
        farm_edit_intent2.add(" ");
        farm_edit_intent2.add(farmer_name);
        farm_edit_intent2.add(farmer_contact);

        switch (item.getItemId()) {
            case R.id.farm_action_add:
                Intent newcrop = new Intent(getApplicationContext(), Registerfarm_edit.class);
                newcrop.putStringArrayListExtra("Data", farm_edit_intent2);
                startActivityForResult(newcrop, 2);
                break;

        }
        return super.onOptionsItemSelected(item);

    }
}
