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
 * Created by Vivek on 09-07-2017.
 */
public class ViewOrderlist_Agent_Info_Buyer_Pending_Payment extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Agent_ID, Occupation, Agent_name, Agent_contact, Intent_Buyer_ID = null;
    String Order_Status_for_Agent_Info_Buyer_Payment  = "3BC";
    String Order_Status_for_Agent_Info_Buyer_Pending_Acceptance  = "3B";
    ArrayList<Details_Order> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_info_order_closed);

        order_status_edit_intent = new ArrayList<String>();
        Intent ii = getIntent();
        order_status_edit_intent = ii.getStringArrayListExtra("Data");
        Intent_Buyer_ID = order_status_edit_intent.get(1);

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
//            setResult(2, i1);
            this.finish();
        }

        Order_Array = new ArrayList<Details_Order>();

        getclosed_orderlist_agent_view();

//        lv = (ListView)findViewById(R.id.orders_agent_closed_list);
//        ArrayAdapter<Details_Order> aa = new Orders_Adaptor();
//        lv.setAdapter(aa);

    }

    public void getclosed_orderlist_agent_view(){

        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Agent_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Agent_Info_Buyer_Payment);
            jorder_object.put("O_Order_Status2",Order_Status_for_Agent_Info_Buyer_Pending_Acceptance);
            jorder_object.put("O_Buyer_ID",Intent_Buyer_ID );
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderpendingbuyeracceptanceforagent.php";
        json_array_data = jorder_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "GetOrderdetailsforBuyer", jsondata, this);
        orderdata.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType) {

        if (serverResponse.contentEquals("256[]"))
        {
            Toast.makeText(this,"No Payment pending from Buyer",Toast.LENGTH_LONG).show();
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
                        String buyer_id = jo1.getString("O_Buyer_ID");
                        String buyer_name = jo1.getString("O_Buyer_Name");
                        String buyer_contact_num = jo1.getString("O_Buyer_ContactNum");
                        String buyer_shop_address = jo1.getString("O_Buyer_Shop_Address");
                        String farmer_id = jo1.getString("O_Farmer_ID");
                        String farmer_name = jo1.getString("O_Farmer_Name");
                        String farmer_contact_num = jo1.getString("O_Farmer_ContactNum");
                        String farmer_farm_address = jo1.getString("O_Farm_Address");

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
                    getclosed_orderlist_agent_view();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }


    public void manage_order_adaptor()
    {
        lv = (ListView)findViewById(R.id.orders_agent_closed_list);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_agent_info_order_closed_customview, Order_Array);
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
        public View getView(int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);

            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.vieworderlist_agent_info_order_closed_customview,null);
            }

            final Details_Order dto = order_data.get(position);

            TextView cropname = (TextView)convertView.findViewById(R.id.a_cl_cropname);
            TextView order_status = (TextView)convertView.findViewById(R.id.a_cl_status);
            TextView cropvol = (TextView) convertView.findViewById(R.id.a_cl_cropvol);
            TextView croporderdate = (TextView)convertView.findViewById(R.id.a_cl_date);
            TextView farmername = (TextView)convertView.findViewById(R.id.a_cl_farmername);
            TextView farmercontact = (TextView)convertView.findViewById(R.id.a_cl_farmer_contact);
            TextView farmaddress = (TextView)convertView.findViewById(R.id.a_cl_farm_address);
            TextView buyername = (TextView)convertView.findViewById(R.id.a_cl_buyername);
            TextView buyercontact = (TextView)convertView.findViewById(R.id.a_cl_buyer_contact);
            TextView shopaddress = (TextView)convertView.findViewById(R.id.a_cl_shop_address);

            cropname.setText(dto.getOrder_CropName());
            order_status.setText(dto.getOrder_Display_Stage());
            cropvol.setText(dto.getOrder_Ordered_Quantity());
            croporderdate.setText(dto.getOrder_Order_Date());
            farmername.setText(dto.getOrder_Farmer_Name());
            farmercontact.setText(dto.getOrder_Farmer_ContactNum());
            farmaddress.setText(dto.getOrder_Farm_Address());
            buyername.setText(dto.getOrder_Buyer_Name());
            buyercontact.setText(dto.getOrder_Buyer_ContactNum());
            shopaddress.setText(dto.getOrder_Buyer_Shop_Address());


            return convertView;
        }
    }


}
