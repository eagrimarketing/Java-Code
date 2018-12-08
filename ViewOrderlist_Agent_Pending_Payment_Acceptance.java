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
public class ViewOrderlist_Agent_Pending_Payment_Acceptance extends AppCompatActivity implements View.OnClickListener, HttpResponse{

    ListView lv;
    String Agent_ID, Occupation, Agent_name, Agent_contact = null;
    String Order_Status_for_Agent_Pending_Payment_Acceptance = "3BA";
    String Order_Status_Option_Agent_Accepted = "Agent Delivered";
    String Order_Status_Option_Agent_Rejected = "Agent Cancelled";
    ArrayList<Details_Order> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    Integer Reprocess_status = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_pending_delivery);

        Intent i = getIntent();
        String Text_From_Intent_Option_Selection = i.getStringExtra("Data");

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        Agent_ID = sp.getString("ID", null);
        Occupation = sp.getString("Occupation",null);
        Agent_name=sp.getString("FNAME",null)+" "+sp.getString("LNAME",null);
        Agent_contact=sp.getString("MPhone",null);

        if (Occupation.contentEquals("Agent"))
        {}
        else
        {
            Toast.makeText(this, "Only Agent can view these details", Toast.LENGTH_SHORT).show();
//            Intent i1 = new Intent(getApplicationContext(), ViewOrderlist_Agent_Optionselection.class);
//            i1.putExtra("Data", "Failure");
//            setResult(1, i1);
            this.finish();
        }

        Order_Array = new ArrayList<Details_Order>();
        getorderlist_pending_agent_delivery();

//        this.finish();

    }

    public void getorderlist_pending_agent_delivery(){

        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Agent_ID);
            jorder_object.put("Occupation",Occupation);
            jorder_object.put("O_Order_Status",Order_Status_for_Agent_Pending_Payment_Acceptance);
            jorder_object.put("O_ID_1"," ");
            jorder_object.put("O_ID_2"," ");
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
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "GetOrderdetailsforagent", jsondata, this);
        orderdata.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType) {


        if (serverResponse.contentEquals("256[]"))
        {
         Toast.makeText(this,"No Payment Pending Acceptance",Toast.LENGTH_LONG).show();
            if (Reprocess_status==1)
            {manage_order_adaptor();
            this.finish();}
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
                        String farmer_contact_num = jo1.getString("O_Farmer_ContactNum");
                        String farmer_farm_address = jo1.getString("O_Farm_Address");
                        String buyer_id = jo1.getString("O_Buyer_ID");
                        String buyer_name = jo1.getString("O_Buyer_Name");
                        String buyer_contact_num = jo1.getString("O_Buyer_ContactNum");
                        String buyer_shop_address = jo1.getString("O_Buyer_Shop_Address");

                        Details_Order dorder = new Details_Order(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
                        dorder.setOrder_Order_ID(order_id);
                        dorder.setOrder_Crop_ID(crop_id);
                        dorder.setOrder_CropName(crop_name);
                        dorder.setOrder_Order_Status(order_status);
                        dorder.setOrder_Display_Stage(order_disp_status);
                        dorder.setOrder_Ordered_Quantity(order_qty);
                        dorder.setOrder_Order_Value(order_cost);
                        dorder.setOrder_Buyer_ID(buyer_id);
                        dorder.setOrder_Buyer_Name(buyer_name);
                        dorder.setOrder_Buyer_ContactNum(buyer_contact_num);
                        dorder.setOrder_Buyer_Shop_Address(buyer_shop_address);
                        dorder.setOrder_Farmer_ID(farmer_id);
                        dorder.setOrder_Farmer_Name(farmer_name);
                        dorder.setOrder_Farmer_ContactNum(farmer_contact_num);
                        dorder.setOrder_Farm_Address(farmer_farm_address);

                        Order_Array.add(dorder);
                    }
                } else {
                    getorderlist_pending_agent_delivery();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Order_Array.size() > 0) {
                manage_order_adaptor();
            }

        }
    }

    public void manage_order_adaptor()
    {
        lv = (ListView)findViewById(R.id.orders_agent_delivery_pending);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_agent_pending_delivery_customview, Order_Array);
        lv.setAdapter(oa);

    }



    @Override
    public void onClick(View v) {

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
                convertView = mInflater.inflate(R.layout.vieworderlist_agent_pending_delivery_customview,null);
            }

            final Details_Order dto = order_data.get(position);

            TextView cropname = (TextView)convertView.findViewById(R.id.a_d_cropname);
            TextView order_status = (TextView)convertView.findViewById(R.id.a_d_status);
            TextView cropvol = (TextView) convertView.findViewById(R.id.a_d_cropvol);
            TextView cropcost = (TextView)convertView.findViewById(R.id.a_d_price);
            TextView farmername = (TextView)convertView.findViewById(R.id.a_d_farmername);
            TextView farmercontact = (TextView)convertView.findViewById(R.id.a_d_farmer_contact);
            TextView farmaddress= (TextView)convertView.findViewById(R.id.a_d_farm_address);
            TextView buyername = (TextView)convertView.findViewById(R.id.a_d_buyername);
            TextView buyerercontact = (TextView)convertView.findViewById(R.id.a_d_buyer_contact);
            TextView shopaddress = (TextView)convertView.findViewById(R.id.a_d_shop_address);

            cropname.setText(Order_Array.get(position).getOrder_CropName());
            order_status.setText(Order_Array.get(position).getOrder_Display_Stage());
            cropvol.setText(Order_Array.get(position).getOrder_Ordered_Quantity());
            cropcost.setText(Order_Array.get(position).getOrder_Order_Value());
            farmername.setText(Order_Array.get(position).getOrder_Farmer_Name());
            farmercontact.setText(Order_Array.get(position).getOrder_Farmer_ContactNum());
            farmaddress.setText(Order_Array.get(position).getOrder_Farm_Address());
            buyername.setText(Order_Array.get(position).getOrder_Buyer_Name());
            buyerercontact.setText(Order_Array.get(position).getOrder_Buyer_ContactNum());
            shopaddress.setText(Order_Array.get(position).getOrder_Buyer_Shop_Address());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    order_status_edit_intent = new ArrayList<String>();

                    order_status_edit_intent.add("Update");
                    order_status_edit_intent.add(dto.getOrder_Order_ID());
                    order_status_edit_intent.add(Order_Status_Option_Agent_Accepted);
                    order_status_edit_intent.add(Order_Status_Option_Agent_Rejected);

                    Intent i = new Intent(getApplicationContext(),ViewOrderlist_Agent_Pending_Payment_Acceptance_Update.class);
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
                Reprocess_status = 1;
                getorderlist_pending_agent_delivery();
                break;
            case 2:
                this.finish();
                break;
        }
    }

}
