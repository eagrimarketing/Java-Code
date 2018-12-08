package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterCrop extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    EditText cropname, cropvolume, fieldplotnum, locality, village, district, state, deliverycity1, deliverycity2;
    Button submit;
    String farmer_id, farmer_name, farmer_address, farmer_contact=null;
    String occupation = null;
    String farmer="Farmer";
    ArrayList<Details_Product> prod_details = null;
    ListView lv;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    String fn_add = "Add";
    FloatingActionButton fab_add_crop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_crop_listview);

        Intent geti = getIntent();

        prod_details = new ArrayList<Details_Product>();
        fab_add_crop = (FloatingActionButton)findViewById(R.id.fab_add_crop);

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        farmer_id = sp.getString("ID", null);
        occupation = sp.getString("Occupation",null);
        farmer_name=sp.getString("FName",null)+" "+sp.getString("LName",null);
        farmer_address=sp.getString("Address",null);
        farmer_contact=sp.getString("MPhone",null);

        if(occupation.contentEquals("Farmer"))
        {}
        else
        {
            this.finish();
        }

//* Send farmer ID to fetch details of the current products registered by the farmer *//

        getexisting_product_details();

//* Dispay listview of current products registered by the farmer on his phone*//


//        lv=(ListView)findViewById(R.id.farmer_crop_list);
//        ArrayAdapter<Details_Product> aa = new Farmercrop_adaptor();
//        lv.setAdapter(aa);

        fab_add_crop.setOnClickListener(new View.OnClickListener()
        {

             @Override
             public void onClick(View v)
             {
                 ArrayList<String> crop_edit_intent3 = new ArrayList<String>();
                 crop_edit_intent3.add("Add");
                 crop_edit_intent3.add(farmer_id);
                 crop_edit_intent3.add(" ");
                 crop_edit_intent3.add(farmer_name);
                 crop_edit_intent3.add(farmer_contact);

                         Intent newshop = new Intent(getApplicationContext(),Registercrop_New.class);
                         newshop.putStringArrayListExtra("Data",crop_edit_intent3);
                         startActivityForResult(newshop,2);

              }
        }
        );


    }



    public void getexisting_product_details()
    {

        JSONArray jfarmer_array = new JSONArray();
        JSONObject jfarmer_object = new JSONObject();
        try {
            jfarmer_object.put("ID", farmer_id);
            jfarmer_array.put(jfarmer_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetCropdetails.php";
        json_array_data = jfarmer_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void push_data_to_cloud (String url, String jsondata)
    {

        AsyncHttpRequest cropdata = new AsyncHttpRequest(this, url, "GetCropdetails", jsondata, this);
        cropdata.execute();


    }


    @Override
    public void getResponse(String serverResponse, String responseType)
    {
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

                for (i=0;i<ja1.length();i++)
                {
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
                    Double charges = jo1.getDouble("C_Agrim_Service_Percent");
                    String village = "NA";

                    Details_Product dp = new Details_Product(" "," "," "," "," "," "," "," "," "," "," "," "," "," ",0.0);
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

                    prod_details.add(dp);
                }
            }
            else
            {
                getexisting_product_details();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
            }
        } catch(Exception e){e.printStackTrace();}

        if (prod_details.size() > 0) {
            manage_crop_adaptor();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public void manage_crop_adaptor() {

        lv = (ListView) findViewById(R.id.farmer_crop_list);
        Farmer_crop_adaptor aa_cd = new Farmer_crop_adaptor(this, R.layout.register_crop_customview, prod_details);

        lv.setAdapter(aa_cd);

    }

    @Override
    public void onClick(View v) {

    }

//
//    @Override
//    public void onClick(View v) {
//
//    }

    private class Farmer_crop_adaptor extends ArrayAdapter<Details_Product>
    {
// Class will display list of all products registered by the farmer till the moment

            ArrayList<Details_Product> crop_data = new ArrayList<Details_Product>();
            private LayoutInflater mInflater;

        public Farmer_crop_adaptor(Context context, int resource, ArrayList<Details_Product> objects) {
            super(context, resource, objects);
            this.crop_data= objects;
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);

            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.register_crop_customview, null);
            }

            Details_Product dpp = crop_data.get(position);

//            TextView cropid = (TextView)convertView.findViewById(R.id.c_crop_data_cropid);
            TextView cropname = (TextView)convertView.findViewById(R.id.c_crop_data_crop_name);
            TextView cropgrade = (TextView) convertView.findViewById(R.id.c_crop_data_grade);
            TextView croprate = (TextView) convertView.findViewById(R.id.c_crop_data_rate);
            TextView cropvol = (TextView) convertView.findViewById(R.id.c_crop_data_vol);
            TextView croploc = (TextView)convertView.findViewById(R.id.c_crop_data_location);
//            TextView farmername = (TextView)convertView.findViewById(R.id.c_crop_data_farmername);
            TextView croprating = (TextView)convertView.findViewById(R.id.c_crop_data_rating);
            TextView delicity1 = (TextView)convertView.findViewById(R.id.c_crop_data_delvcity1);
            TextView delicity2 = (TextView)convertView.findViewById(R.id.c_crop_data_delvcity2);

//            cropid.setText(dpp.getDP_Prod_ID());
            cropname.setText(dpp.getDP_ProdName());
            cropgrade.setText(dpp.getDP_Grade());
            croprate.setText(dpp.getDP_Rate());
            cropvol.setText(dpp.getDP_Qty());
            croploc.setText(dpp.getDP_FarmAddress());
//            farmername.setText(dpp.getDP_FarmerName());
            croprating.setText(dpp.getDP_Prod_Rating());
            delicity1.setText(dpp.getDP_Delivery_City1());
            delicity2.setText(dpp.getDP_Delivery_City2());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<String> crop_edit_intent= new ArrayList<String>();

                    crop_edit_intent.add("Update");

                    if(prod_details.get(position).getDP_FarmerID().isEmpty())
                    {crop_edit_intent.add(farmer_id);}
                    else {crop_edit_intent.add(prod_details.get(position).getDP_FarmerID());}

                    crop_edit_intent.add(prod_details.get(position).getDP_Prod_ID());

                    if(prod_details.get(position).getDP_FarmerName().isEmpty())
                    {crop_edit_intent.add(farmer_name);}
                    else {crop_edit_intent.add(prod_details.get(position).getDP_FarmerName());}

                    if(prod_details.get(position).getDP_Farmer_ContactNum().isEmpty())
                    {crop_edit_intent.add(farmer_contact);}
                    else {crop_edit_intent.add(prod_details.get(position).getDP_Farmer_ContactNum());}

                    crop_edit_intent.add(prod_details.get(position).getDP_ProdName());
                    crop_edit_intent.add(prod_details.get(position).getDP_Grade());
                    crop_edit_intent.add(prod_details.get(position).getDP_Rate());
                    crop_edit_intent.add(prod_details.get(position).getDP_Qty());
                    crop_edit_intent.add(prod_details.get(position).getDP_Prod_Rating());
                    crop_edit_intent.add(prod_details.get(position).getDP_FarmID());

                    if(prod_details.get(position).getDP_FarmAddress().isEmpty())
                    {
                        crop_edit_intent.add(farmer_address);
                    }
                    else
                    {
                        crop_edit_intent.add(prod_details.get(position).getDP_FarmAddress());
                    }

                    crop_edit_intent.add(prod_details.get(position).getDP_Delivery_City1());
                    crop_edit_intent.add(prod_details.get(position).getDP_Delivery_City2());
                    crop_edit_intent.add(prod_details.get(position).getDP_Agrim_Charges().toString());

                    Intent i = new Intent(getApplicationContext(),Registercrop_edit.class);
                    i.putStringArrayListExtra("Data",crop_edit_intent);
                    startActivityForResult(i,1);

                }
            });


            return convertView;

        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
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
        getMenuInflater().inflate(R.menu.menu_registercrop_listview,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<String> crop_edit_intent2 = new ArrayList<String>();
        crop_edit_intent2.add("Add");
        crop_edit_intent2.add(farmer_id);
        crop_edit_intent2.add(" ");
        crop_edit_intent2.add(farmer_name);
        crop_edit_intent2.add(farmer_contact);


        switch (item.getItemId())
        {
            case R.id.action_add:

                Intent newshop = new Intent(getApplicationContext(),Registercrop_New.class);
                newshop.putStringArrayListExtra("Data",crop_edit_intent2);
                startActivityForResult(newshop,2);
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}
