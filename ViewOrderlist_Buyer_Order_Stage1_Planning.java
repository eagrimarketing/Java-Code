package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class ViewOrderlist_Buyer_Order_Stage1_Planning extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Buyer_ID, Occupation, Buyer_name, Buyer_contact, Intent_Farmer_ID = null;
    String Order_Status_for_Buyer_Pending_Farmer_Accept = "1";
    String Order_Status_for_Buyer_Pending_Agent_Accept = "2";
    String Order_Status_for_Buyer_Pending_Agent_Action = "3";
    ArrayList<Details_Order> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    JSONArray jupdate_status_array = new JSONArray();

    String Control_DB_Access = null;   /* R=Read DB for Listview, U=update Order status  */
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_buyer_order_stage1_planning);

        order_status_edit_intent = new ArrayList<String>();
        Intent ii = getIntent();
        order_status_edit_intent = ii.getStringArrayListExtra("Data");
        Intent_Farmer_ID = order_status_edit_intent.get(1);

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
        get_orderstatus_buyer_view();




    }


    public void get_orderstatus_buyer_view(){

        Control_DB_Access = "R";
        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("O_Buyer_ID", Buyer_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Buyer_Pending_Farmer_Accept);
            jorder_object.put("O_Order_Status2",Order_Status_for_Buyer_Pending_Agent_Accept);
            jorder_object.put("O_Order_Status3",Order_Status_for_Buyer_Pending_Agent_Action);
            jorder_object.put("O_Farmer_ID",Intent_Farmer_ID);
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderStatusforBuyer.php";
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
        {Toast.makeText(this,"No Open Orders",Toast.LENGTH_LONG).show();}
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

                        }
                    }
                    if (Order_Array.size() > 0) {
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
        lv = (ListView)findViewById(R.id.orders_buyer_stage1_planning);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_buyer_order_stage1_planning_customview, Order_Array);
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
                convertView = mInflater.inflate(R.layout.vieworderlist_buyer_order_stage1_planning_customview,null);
            }

            final Details_Order dto = order_data.get(position);

            TextView cropname = (TextView)convertView.findViewById(R.id.s1_cropname);
            TextView order_status = (TextView)convertView.findViewById(R.id.s1_status);
            TextView cropvol = (TextView) convertView.findViewById(R.id.s1_cropvol);
            TextView croporderdate = (TextView)convertView.findViewById(R.id.s1_order_date);
            TextView farmername = (TextView)convertView.findViewById(R.id.s1_farmername);
            TextView farmercontact = (TextView)convertView.findViewById(R.id.s1_farmer_contact);

            cropname.setText(dto.getOrder_CropName());
            order_status.setText(dto.getOrder_Display_Stage());
            cropvol.setText(dto.getOrder_Ordered_Quantity());
            croporderdate.setText(dto.getOrder_Order_Date());
            farmername.setText(dto.getOrder_Farmer_Name());
            farmercontact.setText(dto.getOrder_Farmer_ContactNum());

            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
