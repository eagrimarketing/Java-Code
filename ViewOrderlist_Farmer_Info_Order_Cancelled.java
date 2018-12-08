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
public class ViewOrderlist_Farmer_Info_Order_Cancelled extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Farmer_ID, Occupation, Farmer_name, Farmer_contact = null;
    String Order_Status_for_Farmer_Info_Rejection1 = "3A";
    String Order_Status_for_Farmer_Info_Rejection1_Closure = "3AZ1";
    String Order_Closure_Statement_Rejection1 = "Agent Cancelled, Farmer Informed";
    String Order_Status_for_Farmer_Info_Rejection11 = "3AZ1";
    String Order_Status_for_Farmer_Info_Rejection11_Closure = "3AZ1";
    String Order_Closure_Statement_Rejection11 = "Agent Cancelled, Farmer Informed";
    String Order_Status_for_Farmer_Info_Rejection12 = "3AZ2";
    String Order_Status_for_Farmer_Info_Rejection12_Closure = "3AZF";
    String Order_Closure_Statement_Rejection12 = "Agent Cancelled, All Informed";

//    String Order_Status_for_Farmer_Info_Rejection2 = "3AZ1";
//    String Order_Status_for_Farmer_Info_Rejection2_Closure = "3AZ2";
    String Order_Status_for_Farmer_Info_Rejection3 = "3BB";
    String Order_Status_for_Farmer_Info_Rejection3_Closure = "3BBZ1";
    String Order_Closure_Statement_Rejection3 = "Buyer Rejected, Farmer Informed";
    String Order_Status_for_Farmer_Info_Rejection4 = "3BBZ1";
    String Order_Status_for_Farmer_Info_Rejection4_Closure = "3BBZ2";
    String Order_Closure_Statement_Rejection4 = "Buyer Rejected, All Informed";

    String Order_Status_Option_Farmer_Informed = "Farmer Informed";
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
        setContentView(R.layout.vieworderlist_farmer_info_order_cancelled);

        Intent ii = getIntent();
        String Text_From_Intent_Option_Selection = ii.getStringExtra("Data");

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        Farmer_ID = sp.getString("ID", null);
        Occupation = sp.getString("Occupation",null);
        Farmer_name=sp.getString("FNAME",null)+" "+sp.getString("LNAME",null);
        Farmer_contact=sp.getString("MPhone",null);

        if (Occupation.contentEquals("Farmer"))
        {}
        else
        {
            Toast.makeText(this, "Only Farmer can view these details", Toast.LENGTH_SHORT).show();
//            Intent i1 = new Intent();
//            i1.putExtra("Data", "Failure");
//            setResult(2, i1);
            this.finish();
        }
        Control_DB_Access="R";
        Order_Array = new ArrayList<Details_Order>();

        getcancelled_orderlist_farmer_view();

//        lv = (ListView)findViewById(R.id.orders_farmer_cancelled_list);
//        ArrayAdapter<Details_Order> aa = new Orders_Adaptor();
//        lv.setAdapter(aa);
//
//        update_order_status_farmer_informed();

    }

    public void getcancelled_orderlist_farmer_view(){

        Control_DB_Access = "R";
        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Farmer_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Farmer_Info_Rejection1);
            jorder_object.put("O_Order_Status2",Order_Status_for_Farmer_Info_Rejection3);
            jorder_object.put("O_Order_Status3",Order_Status_for_Farmer_Info_Rejection4);
            jorder_object.put("O_Order_Status4",Order_Status_for_Farmer_Info_Rejection11);
            jorder_object.put("O_Order_Status5",Order_Status_for_Farmer_Info_Rejection12);
            jorder_object.put("O_Order_Status6"," ");
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrdercancelledforfarmer.php";
        json_array_data = jorder_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    public  void update_order_status_farmer_informed()
    {
        Control_DB_Access="U";
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateCancelledOrder.php";
        json_array_data = jupdate_status_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "GetOrderdetailsforBuyer", jsondata, this);
        orderdata.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType) {

        if (serverResponse.contains("256[]"))
        {Toast.makeText(this,"No Cancelled Orders",Toast.LENGTH_LONG).show();}
        else {
            try {
                JSONArray ja = new JSONArray(serverResponse);
                JSONObject jo = new JSONObject();
                jo = ja.getJSONObject(0);
                String s = jo.getString("Status");

                if (Control_DB_Access.contains("R")) {
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
                            String order_date = jo1.getString("O_Order_Date");
                            String buyer_id = jo1.getString("O_Buyer_ID");
                            String buyer_name = jo1.getString("O_Buyer_Name");
                            String buyer_contact_num = jo1.getString("O_Buyer_ContactNum");
                            String agent_id = jo1.getString("O_Agent_ID");
                            String agent_name = jo1.getString("O_Agent_Name");
                            String agent_contact_num = jo1.getString("O_Agent_Contact");

                            Details_Order dorder = new Details_Order(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
                            dorder.setOrder_Order_ID(order_id);
                            dorder.setOrder_Crop_ID(crop_id);
                            dorder.setOrder_CropName(crop_name);
                            dorder.setOrder_Order_Status(order_status);
                            dorder.setOrder_Display_Stage(order_disp_status);
                            dorder.setOrder_Ordered_Quantity(order_qty);
                            dorder.setOrder_Order_Date(order_date);
                            dorder.setOrder_Buyer_ID(buyer_id);
                            dorder.setOrder_Buyer_Name(buyer_name);
                            dorder.setOrder_Buyer_ContactNum(buyer_contact_num);
                            dorder.setOrder_Agent_ID(agent_id);
                            dorder.setOrder_Agent_Name(agent_name);
                            dorder.setOrder_Agent_Contact_Num(agent_contact_num);

                            Order_Array.add(dorder);

//                            JSONObject jupdate_status_object = new JSONObject();
//                            jupdate_status_object.put("O_Order_ID", order_id);
//                            jupdate_status_object.put("O_Order_Display_Status", Order_Status_Option_Farmer_Informed);
//                            jupdate_status_object.put("O_Order_Workflow_Status", Order_Workflow_Closed_Status);
//
//                            if (order_status.equals(Order_Status_for_Farmer_Info_Rejection1)) {
//                                jupdate_status_object.put("O_Order_Status", Order_Status_for_Farmer_Info_Rejection1_Closure);
//                                jupdate_status_object.put("O_Order_Display_Status", Order_Closure_Statement_Rejection1);
//
//                            }
//                            else if (order_status.equals(Order_Status_for_Farmer_Info_Rejection3)) {
//                                jupdate_status_object.put("O_Order_Status", Order_Status_for_Farmer_Info_Rejection3_Closure);
//
//                            } else if (order_status.equals(Order_Status_for_Farmer_Info_Rejection4)) {
//                                jupdate_status_object.put("O_Order_Status", Order_Status_for_Farmer_Info_Rejection4_Closure);
//
//                            }
//                            jupdate_status_array.put(jupdate_status_object);

                        }
                    }
                    if (Order_Array.size() > 0) {
//                        update_order_status_farmer_informed();
                        manage_order_adaptor();
                        process_order_status();
                    }

                } else if (Control_DB_Access.contains("U")) {
                    if (s.equals("success")) {
//                    Toast.makeText(this, "Farmer viewed and Status udpated in DB", Toast.LENGTH_SHORT).show();
//                    Intent i1 = new Intent();
//                    i1.putExtra("Data", "Success");
//                    setResult(2, i1);
//                    this.finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        lv = (ListView)findViewById(R.id.orders_farmer_cancelled_list);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_farmer_info_order_cancelled_customview, Order_Array);
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
                convertView = mInflater.inflate(R.layout.vieworderlist_farmer_info_order_cancelled_customview,null);
            }

            final Details_Order dto = order_data.get(position);

            TextView cropname = (TextView)convertView.findViewById(R.id.f_can_cropname);
            TextView order_status = (TextView)convertView.findViewById(R.id.f_can_status);
            TextView cropvol = (TextView) convertView.findViewById(R.id.f_can_cropvol);
            TextView croporderdate = (TextView)convertView.findViewById(R.id.f_can_date);
            TextView buyername = (TextView)convertView.findViewById(R.id.f_can_buyername);
            TextView buyercontact = (TextView)convertView.findViewById(R.id.f_can_buyer_contact);
            TextView agentname = (TextView)convertView.findViewById(R.id.f_can_agentname);
            TextView agentcontact = (TextView)convertView.findViewById(R.id.f_can_agent_contact);

            cropname.setText(dto.getOrder_CropName());
            order_status.setText(dto.getOrder_Display_Stage());
            cropvol.setText(dto.getOrder_Ordered_Quantity());
            croporderdate.setText(dto.getOrder_Order_Date());
            buyername.setText(dto.getOrder_Buyer_Name());
            buyercontact.setText(dto.getOrder_Buyer_ContactNum());
            agentname.setText(dto.getOrder_Agent_Name());
            agentcontact.setText(dto.getOrder_Agent_Contact_Num());

            return convertView;
        }
    }

    public void process_order_status()
    {

        for (Integer i1 = 0;i1<Order_Array.size();i1++)
        {
            Details_Order orders = Order_Array.get(i1);

            String temp_o_status = " ";
            String temp_close_stmt = " ";
            if (orders.getOrder_Order_Status().equals(Order_Status_for_Farmer_Info_Rejection1)) {
                temp_o_status =  Order_Status_for_Farmer_Info_Rejection1_Closure;
                temp_close_stmt = Order_Closure_Statement_Rejection1;
            }
            if (orders.getOrder_Order_Status().equals(Order_Status_for_Farmer_Info_Rejection11)) {
                temp_o_status =  Order_Status_for_Farmer_Info_Rejection11_Closure;
                temp_close_stmt = Order_Closure_Statement_Rejection11;
            }
            if (orders.getOrder_Order_Status().equals(Order_Status_for_Farmer_Info_Rejection12)) {
                temp_o_status =  Order_Status_for_Farmer_Info_Rejection12_Closure;
                temp_close_stmt = Order_Closure_Statement_Rejection12;
            }
            if (orders.getOrder_Order_Status().equals(Order_Status_for_Farmer_Info_Rejection3)) {
                temp_o_status =  Order_Status_for_Farmer_Info_Rejection3_Closure;
                temp_close_stmt = Order_Closure_Statement_Rejection3;
            }
            if (orders.getOrder_Order_Status().equals(Order_Status_for_Farmer_Info_Rejection4)) {
                temp_o_status =  Order_Status_for_Farmer_Info_Rejection4_Closure;
                temp_close_stmt = Order_Closure_Statement_Rejection4;
            }

            try {
                JSONObject jupdate_status_object = new JSONObject();
                jupdate_status_object.put("O_Order_ID", orders.getOrder_Order_ID());
                jupdate_status_object.put("O_Order_Workflow_Status", Order_Workflow_Closed_Status);
                jupdate_status_object.put("O_Order_Status", temp_o_status);
                jupdate_status_object.put("O_Order_Display_Status", temp_close_stmt);

                jupdate_status_array.put(jupdate_status_object);
                update_order_status_farmer_informed();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
