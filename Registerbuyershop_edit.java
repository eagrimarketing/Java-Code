package my.project.agrim;

/**
 * Created by Vivek on 23-06-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Registerbuyershop_edit extends AppCompatActivity implements View.OnClickListener, HttpResponse, AdapterView.OnItemSelectedListener {

    TextView buyer_shop_id;
    EditText buyershop_plotnum, buyershop_street;
    Button submit, validateaddr;
    Spinner buyershop_city, buyershop_district, buyershop_state;
    String temp_address, transaction_type_add_update, selected_city, selected_district, selected_state =null;
    ArrayList<String> shop_intent_input = null;
    Latlng ll;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    String City_var=null;
    ArrayList<String> cities = null;
    Integer DB_city_process = 0;
    Integer DB_buyer_process = 0;
    Integer DB_district_process = 0;
    Integer DB_state_process = 0;
    Integer DB_location_Complete = 0;
    Integer DB_addr_process = 0;
    Integer DB_cds_process = 0;
    ArrayList<String> district_array = null;
    ArrayList<String> state_array= null;
    String API_KEY = "AIzaSyDRjdX5IyR1-ZJp7AmWIFETHxfM2R6FSrI";
    ArrayList<Latlng> latitude_longitude = null;
    String result = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_buyershop_new_edit);

        Intent i = getIntent();
        shop_intent_input = i.getStringArrayListExtra("Data");
        transaction_type_add_update = shop_intent_input.get(0);

        buyer_shop_id = (TextView) findViewById(R.id.Buyer_ShopId);
        buyershop_plotnum = (EditText)findViewById(R.id.Buyer_Plot_num);
        buyershop_street = (EditText)findViewById(R.id.Buyer_StreetName);

        buyershop_city= (Spinner) findViewById(R.id.Buyer_City);
        buyershop_city.setOnItemSelectedListener(this);


        buyershop_district= (Spinner) findViewById(R.id.Buyer_District);
        buyershop_district.setOnItemSelectedListener(this);

        buyershop_state= (Spinner) findViewById(R.id.Buyer_State);
        buyershop_state.setOnItemSelectedListener(this);

        cities = new ArrayList<String>();
        City_var = "NA";

        if (transaction_type_add_update.contains("Update"))
        {
            buyer_shop_id.setText(shop_intent_input.get(1));
            buyershop_plotnum.setText(shop_intent_input.get(3));
            buyershop_street.setText(shop_intent_input.get(4));
        }
//        else
//        {
//            buyershop_plotnum.setFocusable(true);
//        }

//        ArrayAdapter<CharSequence> city = ArrayAdapter.createFromResource(this, R.array.City_list, R.layout.support_simple_spinner_dropdown_item);
//        buyershop_city.setAdapter(city);

        ArrayList<String> district_array = new ArrayList<String>();
        ArrayList<String> state_array= new ArrayList<String>();
//        ArrayList<Latlng> Latitude_Longitude = new ArrayList<Latlng>();
        latitude_longitude = new ArrayList<Latlng>();

//        getcitydetails();
//
//        getdistrictdetails();

        getstatedetails();

        manage_further_flow();


    }


    public void manage_further_flow()
    {
        submit=(Button)findViewById(R.id.Register);
        validateaddr = (Button)findViewById(R.id.Validateshopaddress);
        validateaddr.setOnClickListener(this);
        submit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String bpn = buyershop_plotnum.getText().toString();
        String bstr = buyershop_street.getText().toString();
        String bc = selected_city;
        String bd = selected_district;
        String bstt = selected_state;


        int i = 0;

        if (bpn.isEmpty())
        {buyershop_plotnum.setError("Please Enter Plot Number");}
        else {i=i+1;}

        if (bstr.isEmpty())
        {buyershop_street.setError("Please Enter Street Name/number");}
        else {i=i+1;}

        if (bc.isEmpty())
        {buyershop_street.setError("Please Enter City ");}
        else {i=i+1;}

        if (bd.isEmpty())
        {buyershop_street.setError("Please Enter District Name");}
        else {i=i+1;}

        if (bstt.isEmpty())
        {buyershop_street.setError("Please Enter State name");}
        else {i=i+1;}

        if (i==5 && result.contentEquals("1")) {

            switch (v.getId()) {
                case R.id.Validateshopaddress:
                    temp_address = bpn + ", " + bstr + ", " + bc + ", " + bd + ", " + bstt;
//                    getlatlong(temp_address);
                    getLocationFromAddress(this, temp_address);
                    break;

                case R.id.Register: {
                    JSONArray jarray = new JSONArray();
                    JSONObject jobject = new JSONObject();
                    try {

                        String Unique_ID = null;
                        if (transaction_type_add_update.contains("Add")) {
                            Unique_ID = "SHOP_" + shop_intent_input.get(2).toString() + "_" + bpn + "_" + bc;
                        } else {
                            Unique_ID = shop_intent_input.get(1);
                        }

                        jobject.put("OPERATION", transaction_type_add_update);
                        jobject.put("ShopID", Unique_ID);
                        jobject.put("BuyerID", shop_intent_input.get(2));
                        jobject.put("BuyerName", shop_intent_input.get(8));
                        jobject.put("MobileNum", shop_intent_input.get(9));
                        jobject.put("PlotNum", bpn);
                        jobject.put("StreetName", bstr);
                        jobject.put("City", bc);
                        jobject.put("District", bd);
                        jobject.put("State", bstt);
                        if (latitude_longitude == null)
                        {
                            jobject.put("Latitude", 0.0);
                            jobject.put("Longitude", 0.0);
                        }
                        else
                            if (latitude_longitude.size()==0)
                            {
                                jobject.put("Latitude", 0.0);
                                jobject.put("Longitude", 0.0);
                            }
                        else {
                            jobject.put("Latitude", latitude_longitude.get(0).getLatitude());
                            jobject.put("Longitude", latitude_longitude.get(0).getLongitude());
                        }

                        jarray.put(jobject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DB_buyer_process = 1;
                    url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/AddUpdateBuyerShopDetails.php";
                    json_array_data = jarray.toString();
                    push_data_to_cloud(url_sendnumtodb, json_array_data);
                }
                break;
            }

            }


    }

    private void getlatlong(String address) {
        JSONArray jaddr_array = new JSONArray();
        JSONObject jaddr_obj = new JSONObject();
        try {
            jaddr_obj.put("ADDRESS", address);
            jaddr_obj.put("KEY",API_KEY);
            jaddr_array.put(jaddr_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_addr_process = 1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/FindLatLng.php";
        json_array_data = jaddr_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }


    private void getcitydetails() {
        JSONArray jcity_array = new JSONArray();
        JSONObject jcity_obj = new JSONObject();
        try {
            jcity_obj.put("City", City_var);
            jcity_obj.put("District", selected_district);
            jcity_obj.put("State", selected_state);
            jcity_array.put(jcity_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_city_process=1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetCityListforDistrict.php";
        json_array_data = jcity_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    private void getdistrictdetails() {
        JSONArray jdist_array = new JSONArray();
        JSONObject jdist_obj = new JSONObject();
        try {
            jdist_obj.put("District", "NA");
            jdist_obj.put("State",selected_state);
            jdist_array.put(jdist_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_district_process=1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetDistrictListforState.php";
        json_array_data = jdist_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }


    private void getstatedetails() {
        JSONArray jstate_array = new JSONArray();
        JSONObject jstate_obj = new JSONObject();
        try {
            jstate_obj.put("State", "NA");
            jstate_array.put(jstate_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_state_process =1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetStateList.php";
        json_array_data = jstate_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }


    private void validatecds() {

        JSONArray jcds_array = new JSONArray();
        JSONObject jcds_obj = new JSONObject();
        try {
            jcds_obj.put("State", selected_state);
            jcds_obj.put("District", selected_district);
            jcds_obj.put("City", selected_city);
            jcds_array.put(jcds_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_cds_process =1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/Validatecds.php";
        json_array_data = jcds_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }


    public void push_data_to_cloud (String url, String jsondata)
    {

        AsyncHttpRequest shopupdate_hr = new AsyncHttpRequest(this,url,"AddUpdateShopdetails",jsondata,this);
        shopupdate_hr.execute();


    }

    @Override
    public void getResponse(String serverResponse, String responseType)
    {
        if (serverResponse.contentEquals("256[]"))
        {
            Toast.makeText(this,"No Address Details received from Server",Toast.LENGTH_LONG).show();
        }
        else {
            if (serverResponse.contentEquals("[null]")) {
                Toast.makeText(this, "No Address Details received from Server", Toast.LENGTH_LONG).show();
            } else {
                if (DB_buyer_process == 1) {
                    try {
                        JSONArray ja = new JSONArray(serverResponse);
                        JSONObject jo = new JSONObject();
                        jo = ja.getJSONObject(0);
                        String s = jo.getString("Status");

                        if (s.equals("Success")) {
                            Intent i1 = new Intent();
                            i1.putExtra("Data", "Success");
                            if (transaction_type_add_update == "Update") {
                                setResult(1, i1);
                            } else if (transaction_type_add_update == "Add") {
                                setResult(2, i1);
                            }
                            this.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (DB_city_process == 1) {
                    cities = new ArrayList<String>();
                    cities.add("Select-->");
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
                                String city_name = jo1.getString("City");

                                cities.add(city_name);
                            }
                        } else {
                            getcitydetails();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (cities.size() > 0) {
                        manage_city_adaptor();
                    }
                    DB_city_process = 0;
                }
                if (DB_district_process == 1) {
                    district_array = new ArrayList<String>();
                    district_array.add("Select-->");
                    try {
                        JSONArray ja_dist = new JSONArray(serverResponse);
                        JSONObject jo_dist = new JSONObject();
                        jo_dist = ja_dist.getJSONObject(0);
                        String s = jo_dist.getString("Status");

                        if (s.equals("Success")) {

                            int i = 0;
                            jo_dist = ja_dist.getJSONObject(1);
                            JSONArray ja1_dist = new JSONArray();
                            JSONObject jo1_dist = new JSONObject();
                            ja1_dist = jo_dist.getJSONArray("Data");
                            jo1_dist = new JSONObject();

                            for (i = 0; i < ja1_dist.length(); i++) {
                                jo1_dist = ja1_dist.getJSONObject(i);
                                String dist_name = jo1_dist.getString("District");

                                district_array.add(dist_name);
                            }
                            DB_district_process = 0;
                        } else {
                            getdistrictdetails();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (district_array.size() > 0) {
                        manage_dist_adaptor();
                    }
                }
                if (DB_state_process == 1) {
                    state_array = new ArrayList<String>();
                    state_array.add("Select-->");
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
                                String state_name = jo1.getString("State");

                                state_array.add(state_name);
                            }
                            DB_state_process = 0;
                        } else {
                            getdistrictdetails();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (state_array.size() > 0) {
                        manage_state_adaptor();
                    }
                }

                if (DB_addr_process == 1) {
                    latitude_longitude = new ArrayList<Latlng>();
                    try {
                        JSONArray ja_addr = new JSONArray(serverResponse);
                        JSONObject jo_addr = new JSONObject();
                        jo_addr = ja_addr.getJSONObject(0);
                        String s = jo_addr.getString("Status");

                        if (s.equals("Success")) {
                            int i = 0;
                            jo_addr = ja_addr.getJSONObject(1);
                            JSONArray ja1_addr = new JSONArray();
                            JSONObject jo1_addr = new JSONObject();
                            ja1_addr = jo_addr.getJSONArray("Data");
                            jo1_addr = new JSONObject();

                            for (i = 0; i < ja1_addr.length(); i++) {
                                jo1_addr = ja1_addr.getJSONObject(i);
                                Double latitude = jo1_addr.getDouble("lat");
                                Double longitude = jo1_addr.getDouble("lng");
                                String postalcode = jo1_addr.getString("postal_code");

                                Latlng coord = new Latlng(0.0, 0.0);
                                coord.setLatitude(latitude);
                                coord.setLongitude(longitude);

                                latitude_longitude.add(coord);
                                DB_location_Complete = 1;

                            }
                            DB_addr_process = 0;
                        } else {
                            getlatlong(temp_address);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (DB_cds_process == 1) {
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
                                result = jo1.getString("CDS");

                            }
                            DB_cds_process = 0;
                        } else {
                            validatecds();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void manage_city_adaptor()
    {
        ArrayAdapter<String> city = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,cities);
        city.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        buyershop_city.setAdapter(city);
    }

    public void manage_dist_adaptor()
    {
        ArrayAdapter<String> dist = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,district_array);
        dist.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        buyershop_district.setAdapter(dist);
    }

    public void manage_state_adaptor()
    {
        ArrayAdapter<String> stt = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,state_array);
        stt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        buyershop_state.setAdapter(stt);
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getId()==R.id.Buyer_State)
        {
            selected_state=buyershop_state.getSelectedItem().toString();
            if (district_array != null)
            {district_array.clear();}
            if (cities != null)
            {cities.clear();}
            getdistrictdetails();
        }
        if (parent.getId()==R.id.Buyer_District)
        {
            selected_district=buyershop_district.getSelectedItem().toString();
            if (cities != null)
            {cities.clear();}
            getcitydetails();
        }
        if (parent.getId()==R.id.Buyer_City)
        {
            selected_city = buyershop_city.getSelectedItem().toString();
            if (selected_city.contains("Select"))
            {}
            else
            {
                validatecds();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        Latlng coord = new Latlng(0.0, 0.0);
        coord.setLatitude(p1.latitude);
        coord.setLongitude(p1.longitude);

        latitude_longitude.add(coord);

        return p1;
    }

}
