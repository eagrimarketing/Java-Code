package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vivek on 19-03-2017.
 */
public class Crop_ordering extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String user_id, user_name, user_address, user_contact, user_occupation, farm_id_for_data_to_intent, farm_lat, farm_lng =null;
    String city_selected_by_user, Shop_selected_by_user,address_shop_selected_user, latitude_shop_selected_user, longitude_shop_selected_user =null;
    ArrayList<Details_Product> crop_details=null;
    ArrayList<String> data_thru_intent = null;
    Integer array_position_data_Intent_final = 0;
    Integer Qty_Remaining_via_intent = 0;
    String CropID_via_intent = null;
    Integer Process_via_intent = 0;
    /* CROP - to get current crop details for selected city
     * FARM - to get Farm details e.g. latitude, longitude for selected crop for order placement*/
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_ordering_listview);

        Intent i = getIntent();

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        user_id = sp.getString("ID", null);
        user_occupation = sp.getString("Occupation",null);
        user_name=sp.getString("FName",null)+" "+sp.getString("LName",null);
        user_address=sp.getString("Address",null);
        user_contact=sp.getString("MPhone",null);

//        Toast.makeText(this, "Occupation: "+user_occupation, Toast.LENGTH_SHORT).show();

        crop_details = new ArrayList<Details_Product>();

        if(user_occupation.contentEquals("Buyer/Store Owner"))
        {
            Intent i11 = new Intent(getApplicationContext(),Crop_ordering_Select_City.class);
            startActivityForResult(i11,11);

        }
        else
        {
            Toast.makeText(this, "Only Buyer can place order ", Toast.LENGTH_SHORT).show();
            this.finish();
        }




//        getcrops_for_cityselected();

        //* Dispay listview of crops for sale in the city selected by user*//



    }

    public void getcrops_for_cityselected()
    {
        JSONArray jcrop_array = new JSONArray();
        JSONObject jcrop_object = new JSONObject();
        try {
            jcrop_object.put("City", city_selected_by_user);
            jcrop_array.put(jcrop_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "CROP";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetCropdetailsforOrder.php";
        json_array_data = jcrop_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    public void Update_crop_quantity_remaining()
    {
        JSONArray jqty_array = new JSONArray();
        JSONObject jqty_object = new JSONObject();
        try {
            jqty_object.put("CropID",CropID_via_intent);
            jqty_object.put("QTY", Qty_Remaining_via_intent);
            jqty_array.put(jqty_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "QTY";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateCropQuantitypostOrderplacement.php";
        json_array_data = jqty_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest cropdata = new AsyncHttpRequest(this, url, "GetCropdetailsfororder", jsondata, this);
        cropdata.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType)
    {

        if (serverResponse.contentEquals("256[]"))
        {
            Toast.makeText(this,"Pls Check Internet Connectivity and try again",Toast.LENGTH_LONG).show();
        }
        else {
            if (DB_Query_Table == "CROP") {
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
                            String prod_id = jo1.getString("C_CropID");
                            String farmer_id = jo1.getString("C_Farmer_ID");
                            String farmer_name = jo1.getString("C_Farmer_Name");
                            String farmer_contact_num = jo1.getString("C_Farmer_ContactNum");
                            String prod_name = jo1.getString("C_CropName");
                            String grade = jo1.getString("C_Grade");
                            String rate = jo1.getString("C_Rate");
                            String qty = jo1.getString("C_Quantity");
                            String prod_rating = jo1.getString("C_Rating");
                            String farm_id = jo1.getString("C_FarmID");
                            String farmaddress = jo1.getString("C_Address");
                            String deliverycity1 = jo1.getString("C_Delivery_City1");
                            String deliverycity2 = jo1.getString("C_Delivery_City2");
                            String village = jo1.getString("Village");
                            Double charges = jo1.getDouble("C_Agrim_Service_Percent");

                            Integer qty_calc = Integer.parseInt(qty);
                            if (qty_calc>0)
                            {
                                Details_Product dp = new Details_Product(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", 0.0);
                                dp.setDP_Prod_ID(prod_id);
                                dp.setDP_FarmerID(farmer_id);
                                dp.setDP_FarmerName(farmer_name);
                                dp.setDP_Farmer_ContactNum(farmer_contact_num);
                                dp.setDP_ProdName(prod_name);
                                dp.setDP_Grade(grade);
                                dp.setDP_Rate(rate);
                                dp.setDP_Qty(qty);
                                dp.setDP_Prod_Rating(prod_rating);
                                dp.setDP_FarmID(farm_id);
                                dp.setDP_FarmAddress(farmaddress);
                                dp.setDP_Delivery_City1(deliverycity1);
                                dp.setDP_Delivery_City2(deliverycity2);
                                dp.setDP_Village(village);
                                dp.setDP_Agrim_Charges(charges);

                                crop_details.add(dp);
                            }
                        }
                    } else {
                        getcrops_for_cityselected();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (crop_details.size() > 0) {
                    manage_crop_adaptor();
                }

            }  /* Data extract logic from Crop table completes here*/
                else if (DB_Query_Table == "FARM")
            {
                try {
                    JSONArray ja = new JSONArray(serverResponse);
                    JSONObject jo = new JSONObject();
                    jo = ja.getJSONObject(0);
                    String s = jo.getString("Status");

                    if (s.equals("Success")) {
                        jo = ja.getJSONObject(1);
                        JSONArray ja1 = new JSONArray();
                        JSONObject jo1 = new JSONObject();
                        ja1 = jo.getJSONArray("Data");
                        jo1 = ja1.getJSONObject(0);
                        farm_lat = jo1.getString("Latitude");
                        farm_lng = jo1.getString("Longitude");
                        Prepare_Data_Call_Final_Code(array_position_data_Intent_final);

                    } else {
                        getlat_lng_farm(farm_id_for_data_to_intent);
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (DB_Query_Table == "QTY")
            {
                try {
                    JSONArray ja = new JSONArray(serverResponse);
                    JSONObject jo = new JSONObject();
                    jo = ja.getJSONObject(0);
                    String s = jo.getString("Status");

                    if (s.equals("Success")) {
                        Toast.makeText(this,"Crop Quantity Updated",Toast.LENGTH_LONG).show();
                        DB_Query_Table = " ";
                        crop_details.clear();
                        getcrops_for_cityselected();
                    } else
                    {
                        Update_crop_quantity_remaining();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void manage_crop_adaptor()
    {

        lv = (ListView) findViewById(R.id.crop_ordering_list);
        Crop_List_Adaptor cla = new Crop_List_Adaptor(this, R.layout.crop_ordering_customview, crop_details);
        lv.setAdapter(cla);
//        if (Process_via_intent == 1)
//        {
//            Process_via_intent = 0;
//            Update_crop_quantity_remaining();
//        }
    }

    private class Crop_List_Adaptor extends ArrayAdapter<Details_Product>
    {
// Class will display list of all products available for delivery in the city selected

        ArrayList<Details_Product> crop_data = new ArrayList<Details_Product>();
        private LayoutInflater mInflater;

        public Crop_List_Adaptor(Context C, int resource, ArrayList<Details_Product> objects)
        {
            super(C, resource, objects);
            this.crop_data= objects;
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {return super.getCount();}

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
//            return super.getView(position, convertView, parent);

            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.crop_ordering_customview, null);
            }

            Details_Product dpp = crop_data.get(position);
            TextView cropname = (TextView)convertView.findViewById(R.id.c_crop_order_crop_name);
            TextView cropgrade = (TextView) convertView.findViewById(R.id.c_crop_order_crop_grade);
            TextView croprate = (TextView) convertView.findViewById(R.id.c_crop_order_crop_rate);
            TextView cropvol = (TextView) convertView.findViewById(R.id.c_crop_order_crop_qty);
            TextView farmername = (TextView)convertView.findViewById(R.id.c_crop_order_crop_farmername);
            TextView croprating = (TextView)convertView.findViewById(R.id.c_crop_order_crop_rating);

            cropname.setText(dpp.getDP_ProdName());
            cropgrade.setText(dpp.getDP_Grade());
            croprate.setText(dpp.getDP_Rate());
            cropvol.setText(dpp.getDP_Qty());
            farmername.setText(dpp.getDP_FarmerName());
            croprating.setText(dpp.getDP_Prod_Rating());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    farm_id_for_data_to_intent = crop_details.get(position).getDP_FarmID();
                    getlat_lng_farm(farm_id_for_data_to_intent);
                    array_position_data_Intent_final = position;

                }
            });


            return convertView;

        }

    }


    public void Prepare_Data_Call_Final_Code(int p)
    {

        data_thru_intent = new ArrayList<String>();


        data_thru_intent.add(crop_details.get(p).getDP_Prod_ID());
        data_thru_intent.add(crop_details.get(p).getDP_FarmerID());
        data_thru_intent.add(user_id);
        data_thru_intent.add(crop_details.get(p).getDP_ProdName());
        data_thru_intent.add(crop_details.get(p).getDP_Grade());
        data_thru_intent.add(crop_details.get(p).getDP_Rate());
        data_thru_intent.add(crop_details.get(p).getDP_Prod_Rating());
        data_thru_intent.add(crop_details.get(p).getDP_FarmerName());
        data_thru_intent.add(crop_details.get(p).getDP_Farmer_ContactNum());
        data_thru_intent.add(crop_details.get(p).getDP_FarmID());
        data_thru_intent.add(crop_details.get(p).getDP_FarmAddress());
        data_thru_intent.add(farm_lat);
        data_thru_intent.add(farm_lng);
        data_thru_intent.add(user_name);
        data_thru_intent.add(user_contact);
        data_thru_intent.add(Shop_selected_by_user);
        data_thru_intent.add(address_shop_selected_user);
        data_thru_intent.add(latitude_shop_selected_user);
        data_thru_intent.add(longitude_shop_selected_user);
        data_thru_intent.add(city_selected_by_user);
        data_thru_intent.add(crop_details.get(p).getDP_Village());
        data_thru_intent.add(crop_details.get(p).getDP_Agrim_Charges().toString());
        data_thru_intent.add(crop_details.get(p).getDP_Qty().toString());

        Intent ifinal = new Intent(getApplicationContext(),Crop_ordering_final_stage.class);
        ifinal.putStringArrayListExtra("Data",data_thru_intent);
        startActivityForResult(ifinal,22);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<String> data_thru_intent2 = new ArrayList<String>();
        ArrayList<String> qty_intent = new ArrayList<String>();
        if (requestCode==11)
        {
            if(resultCode==RESULT_OK)
            {
                this.finish();
            }
            else
            {
                data_thru_intent2 = data.getStringArrayListExtra("Data");
                city_selected_by_user = data_thru_intent2.get(0);
                Shop_selected_by_user = data_thru_intent2.get(1);
                address_shop_selected_user = data_thru_intent2.get(2);
                latitude_shop_selected_user = data_thru_intent2.get(3);
                longitude_shop_selected_user = data_thru_intent2.get(4);
//                Toast.makeText(this, "User selected city/shop Option: " + city_selected_by_user + Shop_selected_by_user, Toast.LENGTH_SHORT).show();
                getcrops_for_cityselected();
            }
        }
        else if(requestCode==22)
//        else
        {
            qty_intent = data.getStringArrayListExtra("Data");
            CropID_via_intent = qty_intent.get(0);
            Qty_Remaining_via_intent = Integer.parseInt(qty_intent.get(1));
//            Process_via_intent = 1;
            Update_crop_quantity_remaining();
//            getcrops_for_cityselected();

        }

    }

    public void getlat_lng_farm(String farmid)
    {
        JSONArray jfarm_array = new JSONArray();
        JSONObject jfarm_object = new JSONObject();
        try {
            jfarm_object.put("FARMID", farmid);
            jfarm_array.put(jfarm_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "FARM";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetFarmdetailsfororder.php";
        json_array_data = jfarm_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }



}
