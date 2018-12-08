package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Crop_ordering_Select_City extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, HttpResponse {

    ArrayList<String> shop_for_spinner, shop_address, shop_latitude, shop_longitude, data_to_intent, shopid_for_spinner = null;
    ArrayList<String> city_array = null;
    Spinner Select_city, Select_shop;
    String city_selected_from_spinner, shop_selected_from_spinner, address_shop_selected_by_user, latitude_shop_selected_by_user, longitude_shop_selected_by_user = " ";
    Button submit;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    private boolean isSpinnerInitial = true;
    Integer DB_city_process = 0;
    Integer DB_crop_process = 0;
//    ArrayList<String> City_for_spinner = null;
//    String City1 = "NewDelhi";
//    String City2 = "Gurgaon";
//    String City3 = "Noida";
//    String City4 = "Gr Noida";
//    String City5 = "Mumbai";
//    String City6 = "Bhopal";
//    String City7 = "Jaipur";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_ordering_select_city);

        Intent i = getIntent();


//        Toast.makeText(this, "inside Select city code:", Toast.LENGTH_LONG).show();

//        City_for_spinner = new ArrayList<String>();
//        City_for_spinner.add(City1);
//        City_for_spinner.add(City2);
//        City_for_spinner.add(City3);
//        City_for_spinner.add(City4);
//        City_for_spinner.add(City5);

        Select_city = (Spinner)findViewById(R.id.select_city_spinner);
        Select_city.setOnItemSelectedListener(this);

//        ArrayAdapter<CharSequence> sc = ArrayAdapter.createFromResource(this, R.array.City_list, R.layout.support_simple_spinner_dropdown_item);
//        Select_city.setAdapter(sc);

        Select_shop=(Spinner)findViewById(R.id.select_shop_spinner);
        Select_shop.setOnItemSelectedListener(this);

//        Toast.makeText(this, "Select city: Spinner setup done", Toast.LENGTH_LONG).show();

        city_array = new ArrayList<String>();
        shop_for_spinner = new ArrayList<String>();
        shop_address = new ArrayList<String>();
        shop_latitude = new ArrayList<String>();
        shop_longitude = new ArrayList<String>();
        shopid_for_spinner = new ArrayList<String>();
        data_to_intent = new ArrayList<String>();
        data_to_intent.add("NA");



//        ArrayAdapter<String> sc = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, City_for_spinner);
//        sc.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        Select_city.setAdapter(sc);


//        Toast.makeText(this, "Shops for Buyer received fro DB", Toast.LENGTH_LONG).show();

//        ArrayAdapter<String> ss = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, shop_for_spinner);
//        ss.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        Select_shop.setAdapter(ss);

        getcityedetails();

        submit = (Button)findViewById(R.id.Register);
        submit.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {

        Intent i1 = new Intent();
        i1.putExtra("Data", "NA");
        setResult(RESULT_OK, i1);

        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if (isSpinnerInitial)
        {
            isSpinnerInitial=false;
        }
        else
        {
            if (spinner.getId() == R.id.select_city_spinner) {
                city_selected_from_spinner = Select_city.getSelectedItem().toString();
                if (shop_for_spinner != null)
                {
                    shop_for_spinner.clear();
                    shop_address.clear();
                    shop_latitude.clear();
                    shop_longitude.clear();
                    shopid_for_spinner.clear();
                    manage_shop_adaptor();

                    shop_selected_from_spinner = null;
                    address_shop_selected_by_user = null;
                    latitude_shop_selected_by_user = null;
                    longitude_shop_selected_by_user = null;

                }
                getShops_from_DB();
            }
        }

        if(shop_for_spinner.size()>0)
        {
            if(spinner.getId()==R.id.select_shop_spinner)
            {
                shop_selected_from_spinner = Select_shop.getSelectedItem().toString();
                address_shop_selected_by_user = shop_address.get(position);
                latitude_shop_selected_by_user = shop_latitude.get(position);
                longitude_shop_selected_by_user = shop_longitude.get(position);
            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        city_selected_from_spinner="NA";
    }

    @Override
    public void onClick(View v) {

//        Toast.makeText(this, "City/Shop: " + city_selected_from_spinner + "/" + shop_selected_from_spinner, Toast.LENGTH_LONG).show();
        data_to_intent = new ArrayList<String>();


        if (city_selected_from_spinner !=null && !city_selected_from_spinner.isEmpty())
        {
            if (shop_selected_from_spinner != null && !shop_selected_from_spinner.isEmpty())
            {
                data_to_intent.add(city_selected_from_spinner);
                data_to_intent.add(shop_selected_from_spinner);
                data_to_intent.add(address_shop_selected_by_user);
                data_to_intent.add(latitude_shop_selected_by_user);
                data_to_intent.add(longitude_shop_selected_by_user);

                Intent i1 = new Intent(this, Crop_ordering.class);
                i1.putExtra("Data", data_to_intent);
                setResult(11, i1);
                this.finish();

            }
            else {
                data_to_intent.add("NA");
                data_to_intent.add("NA");
                data_to_intent.add("NA");
                data_to_intent.add("NA");
                data_to_intent.add("NA");
                Toast.makeText(this,"Please Select City & Shop",Toast.LENGTH_LONG).show();}
        }
        else {
            data_to_intent.add("NA");
            data_to_intent.add("NA");
            data_to_intent.add("NA");
            data_to_intent.add("NA");
            data_to_intent.add("NA");
            Toast.makeText(this,"Please Select City & Shop",Toast.LENGTH_LONG).show();}

    }

    public void getShops_from_DB()
    {
        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        String buyer_id = sp.getString("ID", null);

        JSONArray jshop_array = new JSONArray();
        JSONObject jshop_object = new JSONObject();
        try {
            jshop_object.put("ID", buyer_id);
            jshop_object.put("City",city_selected_from_spinner);
            jshop_array.put(jshop_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_crop_process = 1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetBuyerShopDetails.php";
        json_array_data = jshop_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }


    private void getcityedetails() {
        JSONArray jcity_array = new JSONArray();
        JSONObject jcity_obj = new JSONObject();
        try {
            jcity_obj.put("City", "NA");
            jcity_array.put(jcity_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_city_process = 1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetCityList.php";
        json_array_data = jcity_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }


    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest buyershopdata = new AsyncHttpRequest(this, url, "GetBuyerShopdetails", jsondata, this);
        buyershopdata.execute();

    }


    @Override
    public void getResponse(String serverResponse, String responseType)
    {
        if (serverResponse.contentEquals("256[]"))
        {
            Toast.makeText(this,"Pls Try Again with Correct Inputs",Toast.LENGTH_LONG).show();
        }
        else {
            if (serverResponse.contentEquals("[null]")) {
                Toast.makeText(this, "No Detail received from Server", Toast.LENGTH_LONG).show();
            } else {
                if (DB_crop_process == 1) {
                    shop_for_spinner = new ArrayList<String>();
                    shop_address = new ArrayList<String>();
                    shop_latitude = new ArrayList<String>();
                    shop_longitude = new ArrayList<String>();
                    shopid_for_spinner = new ArrayList<String>();

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

                            shop_for_spinner.add("Select-->");
                            shopid_for_spinner.add("Select-->");
                            shop_address.add("Select-->");
                            shop_latitude.add("Select-->");
                            shop_longitude.add("Select-->");
                            for (i = 0; i < ja1.length(); i++) {
                                jo1 = ja1.getJSONObject(i);
                                String shop_id = jo1.getString("ShopID");
                                String plotnum = jo1.getString("PlotNum");
                                String streetName = jo1.getString("StreetName");
                                String city = jo1.getString("City");
                                String district = jo1.getString("District");
                                String state = jo1.getString("State");
                                String latitude = jo1.getString("Latitude");
                                String longitude = jo1.getString("Longitude");

                                String tmp_shop_address = plotnum + ", " + streetName + ", " + city + ", " + district + ", " + state;
                                String tmp_shop_for_spinner = shop_id + ":" + tmp_shop_address;
                                shop_for_spinner.add(tmp_shop_for_spinner);
                                shopid_for_spinner.add(shop_id);
                                shop_address.add(tmp_shop_address);
                                shop_latitude.add(latitude);
                                shop_longitude.add(longitude);

                            }
                            DB_crop_process = 0;
                        }
                        else {getShops_from_DB();}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (shop_for_spinner.size()>0)
                    {manage_shop_adaptor();}
                }

                if (DB_city_process == 1) {
                    city_array = new ArrayList<String>();

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

                            city_array.add("Select-->");
                            for (i = 0; i < ja1.length(); i++) {
                                jo1 = ja1.getJSONObject(i);
                                String city = jo1.getString("City");

                                city_array.add(city);
                            }
                            DB_city_process = 0;
                        }
                        else {getcityedetails();}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (city_array.size()>0)
                    {manage_city_adaptor();}
                }

            }
        }

    }


    public void manage_shop_adaptor() {

        ArrayAdapter<String> ss = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, shop_for_spinner);
        ss.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Select_shop.setAdapter(ss);

    }

    public void manage_city_adaptor() {

        ArrayAdapter<String> cc = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, city_array);
        cc.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Select_city.setAdapter(cc);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
