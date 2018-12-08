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
public class ViewOrderlist_Buyer_Order_Stage4_Pending_Agent_Accept extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Buyer_ID, Occupation, Buyer_name, Buyer_contact = null;
    String Order_Status_for_Buyer_Payment_Pending_Agent_Acceptance  = "3BA";
//    String Order_Status_for_Buyer_Info_Rejection1_Closure = "4ZF";
//    String Order_Status_for_Buyer_Info_Rejection2 = "3A";
//    String Order_Status_for_Buyer_Info_Rejection2_Closure = "3AZ1";
//    String Order_Status_for_Buyer_Info_Rejection3 = "3AZ2";
//    String Order_Status_for_Buyer_Info_Rejection3_Closure = "3AZF";
//    String Order_Status_Option_Buyer_Informed = "Buyer Informed";
//    String Order_Workflow_Closed_Status = "C";
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
        setContentView(R.layout.vieworderlist_buyer_order_stage4_pending_agent_accept);

        Intent ii = getIntent();
//        String Text_From_Intent_Option_Selection = ii.getStringExtra("Data");

//        Toast.makeText(this,"Inside Order Cancelled Code",Toast.LENGTH_LONG).show();
        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        Buyer_ID = sp.getString("ID", null);
        Occupation = sp.getString("Occupation",null);
        Buyer_name=sp.getString("FNAME",null)+" "+sp.getString("LNAME",null);
        Buyer_contact=sp.getString("MPhone",null);

        if (Occupation.contentEquals("Buyer/Store Owner"))
        {}
        else
        {
            Toast.makeText(this, "Only Buyer can view these details", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        Order_Array = new ArrayList<Details_Order>();

        Control_DB_Access="R";
        getcancelled_orderlist_buyer_view();


    }

    public void getcancelled_orderlist_buyer_view(){

        Control_DB_Access = "R";
        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("O_Buyer_ID", Buyer_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Buyer_Payment_Pending_Agent_Acceptance);
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderpendingagentpayacceptforBuyer.php";
        json_array_data = jorder_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

//    public  void update_order_status_buyer_informed()
//    {
//        Control_DB_Access="U";
//        DB_Query_Table = "Order";
//        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateCancelledOrder.php";
//        json_array_data = jupdate_status_array.toString();
//        push_data_to_cloud(url_sendnumtodb, json_array_data);
//
//    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "GetOrderdetailsforBuyer", jsondata, this);
        orderdata.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType) {


        if (serverResponse.contentEquals("256[]"))
        {Toast.makeText(this,"No Payment Pending Agent Acceptance",Toast.LENGTH_LONG).show();}
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
                            String farmer_id = jo1.getString("O_Farmer_ID");
                            String farmer_name = jo1.getString("O_Farmer_Name");
                            String farmer_contact_num = jo1.getString("O_Farmer_ContactNum");
                            String agent_id = jo1.getString("O_Agent_ID");
                            String agent_name = jo1.getString("O_Agent_Name");
                            String agent_contact_num = jo1.getString("O_Agent_Contact");

                            Details_Order dorder = new Details_Order(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
                            dorder.setOrder_Crop_ID(crop_id);
                            dorder.setOrder_CropName(crop_name);
                            dorder.setOrder_Order_Status(order_status);
                            dorder.setOrder_Display_Stage(order_disp_status);
                            dorder.setOrder_Ordered_Quantity(order_qty);
                            dorder.setOrder_Order_Date(order_date);
                            dorder.setOrder_Farmer_ID(farmer_id);
                            dorder.setOrder_Farmer_Name(farmer_name);
                            dorder.setOrder_Farmer_ContactNum(farmer_contact_num);
                            dorder.setOrder_Agent_ID(agent_id);
                            dorder.setOrder_Agent_Name(agent_name);
                            dorder.setOrder_Agent_Contact_Num(agent_contact_num);

                            Order_Array.add(dorder);

//                            JSONObject jupdate_status_object = new JSONObject();
//                            jupdate_status_object.put("O_Order_ID", order_id);
//                            jupdate_status_object.put("O_Order_Display_Status", Order_Status_Option_Buyer_Informed);
//                            jupdate_status_object.put("O_Order_Workflow_Status", Order_Workflow_Closed_Status);
//
//                            if (order_status.equals(Order_Status_for_Buyer_Info_Rejection1)) {
//                                jupdate_status_object.put("O_Order_Status", Order_Status_for_Buyer_Info_Rejection1_Closure);
//                            }
//                            if (order_status.equals(Order_Status_for_Buyer_Info_Rejection2)) {
//                                jupdate_status_object.put("O_Order_Status", Order_Status_for_Buyer_Info_Rejection2_Closure);
//                            }
//                            if (order_status.equals(Order_Status_for_Buyer_Info_Rejection3)) {
//                                jupdate_status_object.put("O_Order_Status", Order_Status_for_Buyer_Info_Rejection3_Closure);
//                            }
//                            jupdate_status_array.put(jupdate_status_object);

                        }
                    }
                    if (Order_Array.size() > 0) {
//                        update_order_status_buyer_informed();
                        manage_order_adaptor();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    public void manage_order_adaptor()
    {
        lv = (ListView)findViewById(R.id.orders_buyer_pending_agent_payment_acceptance);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_buyer_order_stage4_pending_agent_accept_customview, Order_Array);
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
                convertView = mInflater.inflate(R.layout.vieworderlist_buyer_order_stage4_pending_agent_accept_customview,null);
            }

            final Details_Order dto = order_data.get(position);

            TextView agentname = (TextView)convertView.findViewById(R.id.s4_agentname);
            TextView agentcontact = (TextView)convertView.findViewById(R.id.s4_agent_contact);
            TextView cropname = (TextView)convertView.findViewById(R.id.s4_cropname);
            TextView order_status = (TextView)convertView.findViewById(R.id.s4_status);
            TextView cropcost = (TextView) convertView.findViewById(R.id.s4_cost);

            agentname.setText(dto.getOrder_Agent_Name());
            agentcontact.setText(dto.getOrder_Agent_Contact_Num());
            cropname.setText(dto.getOrder_CropName());
            order_status.setText(dto.getOrder_Display_Stage());
            cropcost.setText(dto.getOrder_Order_Value());

            return convertView;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }


}
