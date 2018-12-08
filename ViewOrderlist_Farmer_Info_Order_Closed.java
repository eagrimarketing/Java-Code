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
public class ViewOrderlist_Farmer_Info_Order_Closed extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Farmer_ID, Occupation, Farmer_name, Farmer_contact = null;
    String Order_Status_for_Farmer_Info_Closed1 = "4ZF";
    String Order_Status_for_Farmer_Info_Closed2 = "3AZF";
    String Order_Status_for_Farmer_Info_Closed3 = "3BBZ2";
    String Order_Status_for_Farmer_Info_Closed4 = "3BAZ";
    ArrayList<Details_Order> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_farmer_info_order_closed);

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
//            setResult(3, i1);
            this.finish();
        }

        Order_Array = new ArrayList<Details_Order>();
        getclosed_orderlist_farmer_view();

//        lv = (ListView)findViewById(R.id.orders_farmer_closed_list);
//        ArrayAdapter<Details_Order> aa = new Orders_Adaptor();
//        lv.setAdapter(aa);

    }

    public void getclosed_orderlist_farmer_view(){

        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Farmer_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Farmer_Info_Closed1);
            jorder_object.put("O_Order_Status2",Order_Status_for_Farmer_Info_Closed2);
            jorder_object.put("O_Order_Status3",Order_Status_for_Farmer_Info_Closed3);
            jorder_object.put("O_Order_Status4",Order_Status_for_Farmer_Info_Closed4);
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderclosedforfarmer.php";
        json_array_data = jorder_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "GetOrderdetailsforfarmer", jsondata, this);
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

            if (s.equals("Success"))
            {
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
                        String order_date = jo1.getString("O_Order_Date");
                        String buyer_id = jo1.getString("O_Buyer_ID");
                        String buyer_name = jo1.getString("O_Buyer_Name");
                        String buyer_contact_num = jo1.getString("O_Buyer_ContactNum");
                        String agent_id = jo1.getString("O_Agent_ID");
                        String agent_name = jo1.getString("O_Agent_Name");
                        String agent_contact_num = jo1.getString("O_Agent_Contact");

                        Details_Order dorder = new Details_Order(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "," "," ");
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
                    }
                }else {getclosed_orderlist_farmer_view();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                }
        } catch(Exception e){e.printStackTrace();}

        if (Order_Array.size()>0)
        {
            manage_order_adaptor();
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
        lv = (ListView)findViewById(R.id.orders_farmer_closed_list);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_farmer_info_order_closed_customview, Order_Array);
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
                convertView = mInflater.inflate(R.layout.vieworderlist_farmer_info_order_closed_customview,null);
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


}
