package my.project.agrim;

/**
 * Created by Vivek on 18-06-2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Locale;

public class Registerfarm_edit extends AppCompatActivity implements View.OnClickListener, HttpResponse, AdapterView.OnItemSelectedListener {

    TextView farm_disp_farmid;
    EditText farm_plotnum, farm_street;
    Spinner farm_district, farm_state, farm_village;
    Button submit, validateaddr;
    String temp_address = null;
    ArrayList<String> farm_intent_input = null;
    String transaction_type_add_update = null;
    Latlng ll;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    ArrayList<String> district_array = null;
    ArrayList<String> state_array = null;
    ArrayList<String> village_array = null;
    ArrayList<Latlng> latitude_longitude = null;
    Integer DB_dist_process = 0;
    Integer DB_state_process = 0;
    Integer DB_vil_process = 0;
    Integer DB_farm_update_process = 0;
    Integer DB_addr_process = 0;
    Integer DB_location_Complete = 0;
    Integer DB_vds_process = 0;
    String selected_district, selected_state, selected_village = null;
    LocationManager lm;
    double x = 0;
    double y = 0;
    Geocoder geocoder;
    Location location;
    String getlat;
    String getlang;
    List<Address> addresses;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    String API_KEY = "AIzaSyDRdhJq4uW6Eip4niSg26IhNTiT_1JtDqE";
    Integer maxresult = 5;
    String result = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_farm_new_edit);

        Intent i = getIntent();
        farm_intent_input = i.getStringArrayListExtra("Data");
        transaction_type_add_update = farm_intent_input.get(0);

        farm_disp_farmid = (TextView) findViewById(R.id.farm_data_farm_id);
        farm_plotnum = (EditText) findViewById(R.id.farm_data_plot_num);
        farm_street = (EditText) findViewById(R.id.farm_data_street_name);

        farm_village = (Spinner) findViewById(R.id.farm_data_village_name);
        farm_village.setOnItemSelectedListener(this);
        farm_district = (Spinner) findViewById(R.id.farm_data_district);
        farm_district.setOnItemSelectedListener(this);
        farm_state = (Spinner) findViewById(R.id.farm_data_state);
        farm_state.setOnItemSelectedListener(this);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        ll = new Latlng(0.0, 0.0);
        if (transaction_type_add_update.contains("Update")) {
            farm_disp_farmid.setText(farm_intent_input.get(1));
            farm_plotnum.setText(farm_intent_input.get(3));
            farm_street.setText(farm_intent_input.get(4));
//            farm_village.setText(farm_intent_input.get(5));
//            farm_district.setText(farm_intent_input.get(6));
//            farm_state.setText(farm_intent_input.get(7));
        } else {
            farm_plotnum.setFocusable(true);
        }

        ArrayList<String> district_array = new ArrayList<String>();
        ArrayList<String> state_array = new ArrayList<String>();
//        ArrayList<Latlng> latitude_longitude = new ArrayList<Latlng>();
        latitude_longitude = new ArrayList<Latlng>();

//        getvillagedetails();
//
//        getdistrictdetails();

        getstatedetails();

        submit = (Button) findViewById(R.id.Register);
        validateaddr = (Button) findViewById(R.id.Validateaddress);
        validateaddr.setOnClickListener(this);
        submit.setOnClickListener(this);

    }


    private void getvillagedetails() {
        JSONArray jvil_array = new JSONArray();
        JSONObject jvil_obj = new JSONObject();
        try {
            jvil_obj.put("Village", "NA");
            jvil_obj.put("District",selected_district);
            jvil_obj.put("State",selected_state);
            jvil_array.put(jvil_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_vil_process = 1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetVillageListforDistrict_vds.php";
        json_array_data = jvil_array.toString();
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
        DB_dist_process = 1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetDistrictListforState_vds.php";
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
        DB_state_process = 1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetStateList.php";
        json_array_data = jstate_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }


    @Override
    public void onClick(View v) {

        String fpn = farm_plotnum.getText().toString();
        String fstr = farm_street.getText().toString();
        String fv = selected_village;
        String fd = selected_district;
        String fstt = selected_state;

        int i = 0;

        if (fpn.isEmpty()) {
            farm_plotnum.setError("Please Enter Plot Number");
        } else {
            i = i + 1;
        }

        if (fstr.isEmpty()) {
            farm_street.setError("Please Enter Street Name/number");
        } else {
            i = i + 1;
        }

        if (fv.isEmpty()) {
            farm_street.setError("Please Enter Village Name");
        } else {
            i = i + 1;
        }

        if (fd.isEmpty()) {
            farm_street.setError("Please Enter District Name");
        } else {
            i = i + 1;
        }

        if (fstt.isEmpty()) {
            farm_street.setError("Please Enter State name");
        } else {
            i = i + 1;
        }

        /* Identify Latitude and Longitude of the Farm to be saved in DB  */
        if (i == 5 && result.contentEquals("1")) {

            switch (v.getId()) {
                case R.id.Validateaddress:
                    temp_address = fpn + ", " + fstr + ", " + fv + ", " + fd + ", " + fstt;
//                    getlatlong(temp_address);
                    getLocationFromAddress(this, temp_address);

//                    Geocoder gc = new Geocoder(this);
//                    try {
//                        addresses = gc.getFromLocationName(temp_address,maxresult);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(this,"Lat "+addresses.get(0).getLongitude(),Toast.LENGTH_LONG).show();


                    break;
                case R.id.Register: {
                    JSONArray jarray = new JSONArray();
                    JSONObject jobj = new JSONObject();
                    try {

                        String Unique_ID = null;
                        if (transaction_type_add_update.contains("Add")) {
                            Unique_ID = "FARM_" + farm_intent_input.get(2).toString() + "_" + fpn + "_" + fv;
                        } else {
                            Unique_ID = farm_intent_input.get(1).toString();
                        }

                        jobj.put("OPERATION", transaction_type_add_update);
                        jobj.put("FARMID", Unique_ID);
                        jobj.put("FarmerID", farm_intent_input.get(2).toString());
                        jobj.put("FarmerName", farm_intent_input.get(8).toString());
                        jobj.put("MobileNum", farm_intent_input.get(9).toString());
                        jobj.put("PlotNum", fpn);
                        jobj.put("StreetName", fstr);
                        jobj.put("Village", fv);
                        jobj.put("District", fd);
                        jobj.put("State", fstt);
                        if (latitude_longitude == null)
                        {
                            jobj.put("Latitude", 0.0);
                            jobj.put("Longitude", 0.0);
                        }else
                        if (latitude_longitude.size()==0)
                        {
                            jobj.put("Latitude", 0.0);
                            jobj.put("Longitude", 0.0);
                        }
                        else
                        {
                            jobj.put("Latitude", latitude_longitude.get(0).getLatitude());
                            jobj.put("Longitude", latitude_longitude.get(0).getLongitude());
                        }

                        jarray.put(jobj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DB_farm_update_process = 1;
                    url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/AddUpdateFarmDetails.php";
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


    private void validatevds() {

        JSONArray jvds_array = new JSONArray();
        JSONObject jvds_obj = new JSONObject();
        try {
            jvds_obj.put("State", selected_state);
            jvds_obj.put("District", selected_district);
            jvds_obj.put("Village", selected_village);
            jvds_array.put(jvds_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_vds_process =1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/Validatevds.php";
        json_array_data = jvds_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }



    public void push_data_to_cloud (String url, String jsondata)
    {
        if (DB_farm_update_process==1) {
            AsyncHttpRequest farmupdate_hr = new AsyncHttpRequest(this,url,"AddUpdateFarmdetails",jsondata,this);
            farmupdate_hr.execute();}

        if (DB_dist_process==1)
        {
            AsyncHttpRequest dist_all = new AsyncHttpRequest(this,url,"GetDistrictdetails",jsondata,this);
            dist_all.execute();}

        if (DB_state_process==1)
        {
            AsyncHttpRequest state_all = new AsyncHttpRequest(this,url,"GetStatedetails",jsondata,this);
            state_all.execute();}

        if (DB_vil_process==1)
        {
            AsyncHttpRequest village_all = new AsyncHttpRequest(this,url,"GetVillagedetails",jsondata,this);
            village_all.execute();
        }
        if (DB_addr_process==1)
        {
            AsyncHttpRequest address_all = new AsyncHttpRequest(this,url,"Getaddressdetails",jsondata,this);
            address_all.execute();
        }
        if (DB_vds_process==1)
        {
            AsyncHttpRequest address_all = new AsyncHttpRequest(this,url,"Validate combinations",jsondata,this);
            address_all.execute();
        }

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
                if (DB_farm_update_process == 1) {
                    try {
                        JSONArray ja = new JSONArray(serverResponse);
                        JSONObject jo = new JSONObject();
                        jo = ja.getJSONObject(0);
                        String s = jo.getString("Status");
                        if (s.equals("Success")) {
                            Intent i1 = new Intent();
                            i1.putExtra("Data", "Success");
                            if (transaction_type_add_update.contains("Update")) {
                                setResult(1, i1);
                            } else if (transaction_type_add_update.contains("Add")) {
                                setResult(2, i1);
                            }
                            this.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (DB_vil_process == 1) {
                    village_array = new ArrayList<String>();

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

                            village_array.add("Select-->");

                            for (i = 0; i < ja1_dist.length(); i++) {
                                jo1_dist = ja1_dist.getJSONObject(i);
                                String village_name = jo1_dist.getString("Village");

                                village_array.add(village_name);
                            }
                            DB_vil_process = 0;
                        } else {
                            getvillagedetails();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (village_array.size() > 0) {
                        manage_vil_adaptor();
                    }
                }

                if (DB_dist_process == 1) {
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
                            DB_dist_process = 0;
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
                            getstatedetails();
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
                if (DB_vds_process == 1) {
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
                                result = jo1.getString("VDS");

                            }
                            DB_vds_process = 0;
                        } else {
                            validatevds();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }


    public void manage_vil_adaptor()
    {
        ArrayAdapter<String> vil = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,village_array);
        vil.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        farm_village.setAdapter(vil);
    }

    public void manage_dist_adaptor()
    {
        ArrayAdapter<String> dist = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,district_array);
        dist.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        farm_district.setAdapter(dist);
    }

    public void manage_state_adaptor()
    {
        ArrayAdapter<String> stt = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,state_array);
        stt.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        farm_state.setAdapter(stt);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId()==R.id.farm_data_state)
        {
            selected_state = farm_state.getSelectedItem().toString();
            if (district_array != null)
            {district_array.clear();}
            if (village_array != null)
            {village_array.clear();}
            getdistrictdetails();
        }
        if (parent.getId()==R.id.farm_data_district)
        {
            selected_district = farm_district.getSelectedItem().toString();
            if (village_array != null)
            {village_array.clear();}
            getvillagedetails();
        }
        if (parent.getId()==R.id.farm_data_village_name)
        {
            selected_village = farm_village.getSelectedItem().toString();
            if (selected_village.contains("Select"))
            {}
            else
            {
                validatevds();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public class MyLocationListner implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location) {
            x = location.getLatitude();
            y = location.getLongitude();
            Toast.makeText(Registerfarm_edit.this,x+", "+y,Toast.LENGTH_LONG).show();

            try {
                geocoder = new Geocoder(Registerfarm_edit.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x,y,1);
                StringBuilder sb = new StringBuilder();
                if (geocoder.isPresent())
                {
                    Address returnaddress = addresses.get(0);
                    String locality = returnaddress.getLocality();
                    String country = returnaddress.getCountryName();
                    String region_code = returnaddress.getCountryCode();
                    String postalcode = returnaddress.getPostalCode();

                    sb.append(locality + "");
                    sb.append(country+""+region_code+"");
                    sb.append(postalcode+"");
                    Toast.makeText(Registerfarm_edit.this,sb,Toast.LENGTH_LONG).show();
                }
            }catch (IOException e)
            {Log.e("Geocoder issue",e.getMessage());}

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public void getlocationpermission(){

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mLocationPermissionGranted = true;
        }else
        {
            ActivityCompat.requestPermissions(Registerfarm_edit.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionGranted = false;
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mLocationPermissionGranted = true;
                }
            }
        }
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
