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
public class ViewOrderlist_Buyer_Pending_Action extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Buyer_ID, Occupation, Buyer_name, Buyer_contact, Intent_Agent_ID, Intent_Farmer_ID = null;
    String Order_Status_for_Buyer_Accept_Reject = "3B";
    String Order_Status_Option_Buyer_Accept = "Buyer Accepted";
    String Order_Status_Option_Buyer_Reject = "Buyer Rejected";
    ArrayList<Details_Order> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;

    Integer Reprocess_Status = 0;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_buyer_pending_action);


        order_status_edit_intent = new ArrayList<String>();
        Intent ione = getIntent();
        order_status_edit_intent = ione.getStringArrayListExtra("Data");
        Intent_Agent_ID = order_status_edit_intent.get(1);
        Intent_Farmer_ID = order_status_edit_intent.get(2);

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        Buyer_ID = sp.getString("ID", null);
        Occupation = sp.getString("Occupation",null);
        Buyer_name=sp.getString("FNAME",null)+" "+sp.getString("LNAME",null);
        Buyer_contact=sp.getString("MPhone",null);

        if (Occupation.contentEquals("Buyer/Store Owner"))
        {}
        else
        {
            Toast.makeText(this, "Only Buyer can view these details"+Occupation , Toast.LENGTH_SHORT).show();
            this.finish();
        }

        Order_Array = new ArrayList<Details_Order>();
        getorderlist_pending_buyer_action();


    }

    public void getorderlist_pending_buyer_action(){

        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Buyer_ID);
            jorder_object.put("Occupation",Occupation);
            jorder_object.put("O_Order_Status",Order_Status_for_Buyer_Accept_Reject);
            jorder_object.put("O_ID_1",Intent_Agent_ID);
            jorder_object.put("O_ID_2",Intent_Farmer_ID);
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderdetails.php";
        json_array_data = jorder_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "GetOrderdetailsforbuyer", jsondata, this);
        orderdata.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType) {


        if (serverResponse.contentEquals("256[]"))
        {
            Toast.makeText(this,"No Orders Pending for Your Acceptance",Toast.LENGTH_LONG).show();
            if (Reprocess_Status == 1)
            {
                Reprocess_Status = 0;
                manage_order_adaptor();
                this.finish();
            }
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
                        String order_id = jo1.getString("O_Order_ID");
                        String crop_id = jo1.getString("O_Crop_ID");
                        String crop_name = jo1.getString("O_CropName");
                        String order_status = jo1.getString("O_Order_Status");
                        String order_disp_status = jo1.getString("O_Order_Display_Status");
                        String order_qty = jo1.getString("O_Ordered_Quantity");
                        String order_cost = jo1.getString("O_Order_Value");
                        String farmer_id = jo1.getString("O_Farmer_ID");
                        String farmer_name = jo1.getString("O_Farmer_Name");
                        String farmer_contactnum = jo1.getString("O_Farmer_ContactNum");
                        String farmer_farm_address = jo1.getString("O_Farm_Address");
                        String buyer_id = jo1.getString("O_Buyer_ID");
                        String buyer_name = jo1.getString("O_Buyer_Name");
                        String buyer_contact_num = jo1.getString("O_Buyer_ContactNum");
                        String buyer_shop_address = jo1.getString("O_Buyer_Shop_Address");
                        String agent_id = jo1.getString("O_Agent_ID");
                        String agent_name = jo1.getString("O_Agent_Name");
                        String agent_contact = jo1.getString("O_Agent_Contact");

                        Details_Order dorder = new Details_Order(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
                        dorder.setOrder_Order_ID(order_id);
                        dorder.setOrder_Crop_ID(crop_id);
                        dorder.setOrder_CropName(crop_name);
                        dorder.setOrder_Order_Status(order_status);
                        dorder.setOrder_Display_Stage(order_disp_status);
                        dorder.setOrder_Ordered_Quantity(order_qty);
                        dorder.setOrder_Order_Value(order_cost);
                        dorder.setOrder_Farmer_ID(farmer_id);
                        dorder.setOrder_Farmer_Name(farmer_name);
                        dorder.setOrder_Farmer_ContactNum(farmer_contactnum);
                        dorder.setOrder_Farm_Address(farmer_farm_address);
                        dorder.setOrder_Buyer_ID(buyer_id);
                        dorder.setOrder_Buyer_Name(buyer_name);
                        dorder.setOrder_Buyer_ContactNum(buyer_contact_num);
                        dorder.setOrder_Buyer_Shop_Address(buyer_shop_address);
                        dorder.setOrder_Agent_ID(agent_id);
                        dorder.setOrder_Agent_Name(agent_name);
                        dorder.setOrder_Agent_Contact_Num(agent_contact);

                        Order_Array.add(dorder);
                    }
                } else {
                    getorderlist_pending_buyer_action();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Order_Array.size() > 0) {
            manage_order_adaptor();
        }

    }

    @Override
    public void onClick(View v) {

    }

    public void manage_order_adaptor()
    {
        lv = (ListView)findViewById(R.id.orders_buyer_action_pending);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_buyer_pending_action_customview, Order_Array);
        lv.setAdapter(oa);
    }


    private class Orders_Adaptor extends ArrayAdapter<Details_Order>
    {
        ArrayList<Details_Order> order_data = new ArrayList<Details_Order>();
        private LayoutInflater mInflater;

        public Orders_Adaptor(Context context, int resource, ArrayList<Details_Order> object)
        {
            super(context,resource,object);
            this.order_data = object;
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {return super.getCount();}


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);

            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.vieworderlist_buyer_pending_action_customview,null);
            }

            final Details_Order dto = order_data.get(position);

            TextView cropname = (TextView)convertView.findViewById(R.id.b_cropname);
            TextView order_status = (TextView)convertView.findViewById(R.id.b_status);
            TextView cropvol = (TextView) convertView.findViewById(R.id.b_cropvol);
            TextView cropcost = (TextView)convertView.findViewById(R.id.b_cost);
            TextView farmername = (TextView)convertView.findViewById(R.id.b_farmername);
            TextView farmercontact = (TextView)convertView.findViewById(R.id.b_farmer_contact);
            TextView agentname = (TextView)convertView.findViewById(R.id.b_agentname);
            TextView agentcontact = (TextView)convertView.findViewById(R.id.b_agent_contact);

            cropname.setText(dto.getOrder_CropName());
            order_status.setText(dto.getOrder_Display_Stage());
            cropvol.setText(dto.getOrder_Ordered_Quantity());
            cropcost.setText(dto.getOrder_Order_Value());
            farmername.setText(dto.getOrder_Farmer_Name());
            farmercontact.setText(dto.getOrder_Farmer_ContactNum());
            agentname.setText(dto.getOrder_Agent_Name());
            agentcontact.setText(dto.getOrder_Agent_Contact_Num());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    order_status_edit_intent = new ArrayList<String>();

                    order_status_edit_intent.add("Update");
                    order_status_edit_intent.add(dto.getOrder_Order_ID());

                    Intent i = new Intent(getApplicationContext(),ViewOrderlist_Buyer_Pending_Action_Update.class);
                    i.putStringArrayListExtra("Data",order_status_edit_intent);
                    startActivityForResult(i,1);


                }
            });

            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
//                String ss = data.getStringExtra("Data");
                Order_Array.clear();
                Reprocess_Status = 1;
                getorderlist_pending_buyer_action();
//                Intent i1 = new Intent(this,ViewOrderlist_Buyer_Order_Stage2_Groupview.class);
//                setResult(1, i1);
//                this.finish();
                break;
            case 2:
                this.finish();
                break;
        }
    }

}
