package my.project.agrim;

/**
 * Created by Vivek on 11-06-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registercrop_edit extends AppCompatActivity implements View.OnClickListener, HttpResponse, AdapterView.OnItemSelectedListener {

    ArrayList<String> single_crop_details, farm_address_from_db, farm_ID_from_DB = null;
    ArrayList<String> cities = null;
    String selected_farm_address, selected_farm_ID =null;
    // components of array: id, crop name, grade, rate, volume, address, farmer name, rating, delivery city;
    TextView name, crop_id;
    EditText grade, quantity, rate;
    Spinner delivery_city1,delivery_city2;
    Spinner farm_id_to_select;
    Button submit;
    String farmerid, Transaction_Add_or_Update = null;
    Integer DB_which_process=0;
    Integer DB_city_process=0;
    /* 0 = no value
    * 1 = Get Farm Details from DB
    * 2 = Add/Update the DB with Crop details entered by user*/
    String farm_id=null;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    String City_var=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_crop_new_edit);

        Intent geti = getIntent();
        single_crop_details = new ArrayList<String>();
        single_crop_details = (ArrayList<String>) geti.getSerializableExtra("Data");


        Transaction_Add_or_Update = single_crop_details.get(0).toString();
        farmerid = single_crop_details.get(1).toString();


        crop_id=(TextView)findViewById(R.id.Farm_Crop_ID);
        name = (TextView) findViewById(R.id.Farm_Crop);
        grade = (EditText)findViewById(R.id.Farm_Grade);
        quantity = (EditText)findViewById(R.id.Farm_Volume);
        rate = (EditText)findViewById(R.id.Farm_crop_Rate);

        delivery_city1 = (Spinner) findViewById(R.id.Farm_city_del1);
        delivery_city1.setOnItemSelectedListener(this);

        delivery_city2 = (Spinner)findViewById(R.id.Farm_city_del2);
        delivery_city2.setOnItemSelectedListener(this);

        farm_id_to_select= (Spinner)findViewById(R.id.Farm_ID_To_Select);
        farm_id_to_select.setOnItemSelectedListener(this);

        cities = new ArrayList<String>();
        City_var = "Gurgaon";


//        City_List cl = new City_List(this);
//
//
//        cities.addAll(cl.getcitylist());


//        ArrayAdapter<CharSequence> city1 = ArrayAdapter.createFromResource(this, R.array.City_list, R.layout.support_simple_spinner_dropdown_item);
//        delivery_city1.setAdapter(city1);
//
//
//        ArrayAdapter<CharSequence> city2 = ArrayAdapter.createFromResource(this, R.array.City_list, R.layout.support_simple_spinner_dropdown_item);
//        delivery_city2.setAdapter(city2);

        if (Transaction_Add_or_Update.contains("Update"))
        {
            crop_id.setText(single_crop_details.get(2));
            name.setText(single_crop_details.get(5));
            grade.setText(single_crop_details.get(6));
            rate.setText(single_crop_details.get(7));
            quantity.setText(single_crop_details.get(8));
//            delivery_city1.setText(single_crop_details.get(12));
//            delivery_city2.setText(single_crop_details.get(13));
        }
//        else
//        {name.setFocusable(true);}
        //* Send farmer ID to fetch details of the current Farms registered by the farmer *//
        farm_address_from_db = new ArrayList<String>();
        farm_ID_from_DB = new ArrayList<String>();


        getexisting_farmaddress_details();

        getcitydetails();

        manage_further_flow();


    }


    public void manage_further_flow()
    {
        submit = (Button)findViewById(R.id.Register);
        submit.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

        if (parent.getId()==R.id.Farm_ID_To_Select)
        {
            selected_farm_ID = farm_id_to_select.getSelectedItem().toString();
            selected_farm_address = farm_address_from_db.get(position);
        }
    }


    public void getexisting_farmaddress_details()
    {
        JSONArray jfarm_array = new JSONArray();
        JSONObject jfarm_obj = new JSONObject();
        try {
            jfarm_obj.put("ID",farmerid);
            jfarm_array.put(jfarm_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_which_process=1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetFarmdetails.php";
        json_array_data = jfarm_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }


    public void push_data_to_cloud (String url, String jsondata)
    {

        if (DB_which_process==1)
        {
            AsyncHttpRequest farmdata = new AsyncHttpRequest(this, url, "GetFarmdetails", jsondata, this);
            farmdata.execute();

        }
        else if(DB_which_process==2)
        {
            AsyncHttpRequest crop_data_update = new AsyncHttpRequest(this, url, "UpdateCropetails", jsondata, this);
            crop_data_update.execute();

        }
        else if (DB_city_process==1)
        {
            AsyncHttpRequest city_data = new AsyncHttpRequest(this, url, "getcity", jsondata, this);
            city_data.execute();

        }


    }


    @Override
    public void onClick(View v)
    {
        String cn = name.getText().toString();
        String cg = grade.getText().toString();
        String cq = quantity.getText().toString();
        String cr = rate.getText().toString();
        String cdel1 = delivery_city1.getSelectedItem().toString();
        String cdel2 = delivery_city2.getSelectedItem().toString();

        int i = 0;

        if (cn.isEmpty())
        {name.setError("Please Enter crop name");}
        else {i=i+1;}

        if (cg.isEmpty())
        {grade.setError("Please Enter crop grade");}
        else {i=i+1;}

        if (cq.isEmpty())
        {quantity.setError("Please Enter Volume Crop available for sales");}
        else {i=i+1;}

        if (cr.isEmpty())
        {rate.setError("Please Enter Selling Rate of the crop");}
        else {i=i+1;}

        if (cdel1.isEmpty())
        {rate.setError("Please Enter City name for Delivery");}
        else {i=i+1;}

        if (cdel2.isEmpty())
        {rate.setError("Please Enter City name for Delivery");}
        else {i=i+1;}

        if (i==6) {

            JSONArray jarray_db = new JSONArray();
            JSONObject jobject_db = new JSONObject();
            try {

                String Unique_ID = null;
                if (Transaction_Add_or_Update.contains("Add"))
                {
                    Unique_ID = "Crop_" + cn.toString() + "_"+ farm_id + "_" + cg ;
                    jobject_db.put("C_Rating", " ");

                }
                else {
                    Unique_ID = single_crop_details.get(2).toString();
                    if (single_crop_details.get(9).toString().isEmpty())
                    {
                        jobject_db.put("C_Rating"," ");
                    }
                    else {jobject_db.put("C_Rating", single_crop_details.get(9).toString());}
                }

                jobject_db.put("OPERATION",Transaction_Add_or_Update);
                jobject_db.put("C_CropID", Unique_ID.toString());
                jobject_db.put("C_Farmer_ID",single_crop_details.get(1).toString());
                jobject_db.put("C_Farmer_Name",single_crop_details.get(3).toString());
                jobject_db.put("C_Farmer_ContactNum",single_crop_details.get(4).toString());
                jobject_db.put("C_CropName", cn.toString());
                jobject_db.put("C_Grade", cg);
                jobject_db.put("C_Rate", cr);
                jobject_db.put("C_Quantity", cq);
                jobject_db.put("C_FarmID",selected_farm_ID);
//                jobject_db.put("C_FarmID","abcd");
                jobject_db.put("C_Address", selected_farm_address);
//                jobject_db.put("C_Address","abcd");
                jobject_db.put("C_Delivery_City1", cdel1);
                jobject_db.put("C_Delivery_City2", cdel2);
                jobject_db.put("C_Agrim_Service_Percent",single_crop_details.get(14).toString());

                jarray_db.put(jobject_db);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DB_which_process=2;
            url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/AddUpdateCropDetails.php";
            json_array_data = jarray_db.toString();
            push_data_to_cloud(url_sendnumtodb, json_array_data);
        }

    }



    @Override
    public void getResponse(String serverResponse, String responseType)
    {

        if(DB_which_process==2) {
            try {

                JSONArray jadb = new JSONArray(serverResponse);
                JSONObject jodb = new JSONObject();
                jodb = jadb.getJSONObject(0);
                String s = jodb.getString("Status");

                if (s.equals("Success"))
                {
                    Intent i1 = new Intent();
                    i1.putExtra("Data", "Success");
                    if(Transaction_Add_or_Update=="Update")
                    {setResult(1, i1);}
                    else if(Transaction_Add_or_Update=="Add")
                    {setResult(2, i1);}
                    this.finish();
                } else {
  //                  push_data_to_cloud(url_sendnumtodb, json_array_data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(DB_which_process==1)
        {
            try
            {
                JSONArray ja = new JSONArray(serverResponse);
                JSONObject jo = new JSONObject();
                jo = ja.getJSONObject(0);
                String s = jo.getString("Status");

                if (s.equals("Success"))
                {

                    int i=0;
                    jo = ja.getJSONObject(1);
                    JSONArray ja1 = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    ja1 = jo.getJSONArray("Data");
                    jo1 = new JSONObject();

                    farm_ID_from_DB.add("Select -->");
                    farm_address_from_db.add("Select");

                    for (i=0;i<ja1.length();i++)
                    {
                        jo1 = ja1.getJSONObject(i);
                        String farm_id = jo1.getString("FarmID");
                        String plotnum = jo1.getString("PlotNum");
                        String streetname = jo1.getString("StreetName");
                        String village = jo1.getString("Village");
                        String district = jo1.getString("District");
                        String state = jo1.getString("State");

                        String farm_address_local = plotnum+", "+streetname+", "+village+", "+district+", "+state;
                        farm_address_from_db.add(farm_address_local);
                        farm_ID_from_DB.add(farm_id);

                    }
                }
                else
                {
                    getexisting_farmaddress_details();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                }
            } catch(Exception e){e.printStackTrace();}

            if (farm_ID_from_DB.size()> 0) {
                manage_farmid_adaptor();
            }
        }

        if (DB_city_process==1)
        {
            cities = new ArrayList<String>();
            cities.add("Select-->");
            try
            {
                JSONArray ja = new JSONArray(serverResponse);
                JSONObject jo = new JSONObject();
                jo = ja.getJSONObject(0);
                String s = jo.getString("Status");

                if (s.equals("Success"))
                {

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
                } else {getcitydetails();}
            } catch(Exception e) {e.printStackTrace();}

            if (cities.size()> 0) {
                manage_city_adaptor();
            }

        }
    }


    public void manage_farmid_adaptor() {

        ArrayAdapter<String> farm = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,farm_ID_from_DB);
        farm.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        farm_id_to_select.setAdapter(farm);

    }

    public void manage_city_adaptor()
    {
//        Toast.makeText(this,"Cities:"+cities,Toast.LENGTH_LONG).show();
        ArrayAdapter<String> city = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,cities);
        city.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        delivery_city1.setAdapter(city);
        delivery_city2.setAdapter(city);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getcitydetails() {
        JSONArray jcity_array = new JSONArray();
        JSONObject jcity_obj = new JSONObject();
        try {
            jcity_obj.put("City", City_var);
            jcity_array.put(jcity_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_city_process=1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetCityList.php";
        json_array_data = jcity_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

}