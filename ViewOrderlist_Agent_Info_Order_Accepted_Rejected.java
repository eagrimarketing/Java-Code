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
public class ViewOrderlist_Agent_Info_Order_Accepted_Rejected extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Agent_ID, Occupation, Agent_name, Agent_contact = null;
    String Order_Status_for_Agent_Info_Rejection1 = "3BB";
    String Order_Status_for_Agent_Info_Rejection1_Closure = "3BBZ1";
    String Order_Status_for_Agent_Info_Rejection2 = "3BBZ1";
    String Order_Status_for_Agent_Info_Rejection2_Closure = "3BBZ2";
//    String Order_Status_for_Agent_Info_Rejection3 = "3BA";
//    String Order_Status_for_Agent_Info_Rejection3_Closure = "3BAZ1";
//    String Order_Status_for_Agent_Info_Rejection4 = "3BAZ1";
//    String Order_Status_for_Agent_Info_Rejection4_Closure = "3BAZ2";
    String Order_Status_Option_Agent_Informed = "Agent Informed";
    String Order_Workflow_Closed_Status = "C";
    ArrayList<Details_Order> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    JSONArray jupdate_status_array = new JSONArray();

    String Control_DB_Access = null;   /* R=Read DB for Listview, U=update Order status  */
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_info_order_rejected);

        Intent ii = getIntent();
        String Text_From_Intent_Option_Selection = ii.getStringExtra("Data");

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

        Control_DB_Access="R";
        getrejected_orderlist_agent_view();

//        lv = (ListView)findViewById(R.id.orders_agent_rejected_list);
//        ArrayAdapter<Details_Order> aa = new Orders_Adaptor();
//        lv.setAdapter(aa);
//
//        update_order_status_agent_informed();

    }

    public void getrejected_orderlist_agent_view(){

        Control_DB_Access = "R";
        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("O_Agent_ID", Agent_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Agent_Info_Rejection1);
            jorder_object.put("O_Order_Status2",Order_Status_for_Agent_Info_Rejection2);
            jorder_object.put("O_Order_Status3"," ");
            jorder_object.put("O_Order_Status4"," ");
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderacceptedrejectedforagent.php";
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

        try
        {
            JSONArray ja = new JSONArray(serverResponse);
            JSONObject jo = new JSONObject();
            jo = ja.getJSONObject(0);
            String s = jo.getString("Status");

            if (Control_DB_Access.contains("R"))
            {
                if (s.equals("Success")) {

                    int i=0;
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

                        Details_Order dorder = new Details_Order(" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," ");
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

                        JSONObject jupdate_status_object = new JSONObject();
                        jupdate_status_object.put("O_Order_ID", order_id);
                        jupdate_status_object.put("O_Order_Display_Status", Order_Status_Option_Agent_Informed);
                        jupdate_status_object.put("O_Order_Workflow_Status", Order_Workflow_Closed_Status);

                        if (order_status.equals(Order_Status_for_Agent_Info_Rejection1)) {
                            jupdate_status_object.put("O_Order_Status", Order_Status_for_Agent_Info_Rejection1_Closure);
                        } else if (order_status.equals(Order_Status_for_Agent_Info_Rejection2)) {
                            jupdate_status_object.put("O_Order_Status", Order_Status_for_Agent_Info_Rejection2_Closure);
                        }
                        jupdate_status_array.put(jupdate_status_object);
                    }
                }
                if (Order_Array.size()>0)
                {
                    update_order_status_agent_informed();
                    manage_order_adaptor();
                }

            }
            else if (Control_DB_Access.contains("U"))
            {
                if (s.equals("Success")) {
//                    Intent i1 = new Intent(getApplicationContext(), ViewOrderlist_Agent_Optionselection.class);
//                    i1.putExtra("Data", "Success");
//                    setResult(2, i1);
//                    this.finish();
                }
            }
        } catch(Exception e){e.printStackTrace();}

    }


    public void update_order_status_agent_informed()
    {
        Control_DB_Access="U";
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateCancelledOrder.php";
        json_array_data = jupdate_status_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void manage_order_adaptor()
    {
        lv = (ListView)findViewById(R.id.orders_agent_rejected_list);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_agent_info_order_rejected_customview, Order_Array);
        lv.setAdapter(oa);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        public View getView(int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);

            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.vieworderlist_agent_info_order_rejected_customview,null);
            }

            final Details_Order dto = order_data.get(position);

            TextView cropname = (TextView)convertView.findViewById(R.id.a_r_cropname);
            TextView order_status = (TextView)convertView.findViewById(R.id.a_r_status);
            TextView cropvol = (TextView) convertView.findViewById(R.id.a_r_cropvol);
            TextView croporderdate = (TextView)convertView.findViewById(R.id.a_r_date);
            TextView farmername = (TextView)convertView.findViewById(R.id.a_r_farmername);
            TextView farmercontact = (TextView)convertView.findViewById(R.id.a_r_farmer_contact);
            TextView farmaddress = (TextView)convertView.findViewById(R.id.a_r_farm_address);
            TextView buyername = (TextView)convertView.findViewById(R.id.a_r_buyername);
            TextView buyercontact = (TextView)convertView.findViewById(R.id.a_r_buyer_contact);
            TextView shopaddress = (TextView)convertView.findViewById(R.id.a_r_shop_address);


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
