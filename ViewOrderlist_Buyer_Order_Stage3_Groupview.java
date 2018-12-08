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
 * Created by Vivek on 22-07-2018.
 */
public class ViewOrderlist_Buyer_Order_Stage3_Groupview extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Buyer_ID, Occupation, Buyer_name, Buyer_contact = null;
    String Order_Status_for_Buyer_Accept_Reject = "3BC";
    ArrayList<Details_Buyer_Stage2_Groupview> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;

    Integer Reprocess_Status = 0;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_buyer_order_stage3_groupview);

        Intent i = getIntent();
        String Text_From_Intent_Option_Selection = i.getStringExtra("Data");

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

        Order_Array = new ArrayList<Details_Buyer_Stage2_Groupview>();
        getorderlist_pending_buyer_action();


    }

    public void getorderlist_pending_buyer_action(){

        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Buyer_ID);
            jorder_object.put("Occupation",Occupation);
            jorder_object.put("O_Order_Status",Order_Status_for_Buyer_Accept_Reject);
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderdetailsInGroup.php";
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
            Toast.makeText(this,"No Orders Pending for Your Payment",Toast.LENGTH_LONG).show();
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
                        String order_count = jo1.getString("O_Count");
                        String farmer_id = jo1.getString("O_Farmer_ID");
                        String farmer_name = jo1.getString("O_Farmer_Name");
                        String agent_id = jo1.getString("O_Agent_ID");
                        String agent_name = jo1.getString("O_Agent_Name");
                        String farmer_contact = jo1.getString("O_Agent_Contact");
                        String agent_contact = jo1.getString("O_Farmer_ContactNum");

                        Details_Buyer_Stage2_Groupview dorder = new Details_Buyer_Stage2_Groupview(" ", " ", " ", " ", " ", " ", " ");
                        dorder.setOrder_Count(order_count);
                        dorder.setOrder_Farmer_ID(farmer_id);
                        dorder.setOrder_Farmer_Name(farmer_name);
                        dorder.setOrder_Agent_ID(agent_id);
                        dorder.setOrder_Agent_Name(agent_name);
                        dorder.setAgent_Contact(agent_contact);
                        dorder.setFarmer_ContactNum(farmer_contact);

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
        lv = (ListView)findViewById(R.id.orders_buyer_stage3_groupview);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_buyer_order_stage3_groupview_customview, Order_Array);
        lv.setAdapter(oa);
    }


    private class Orders_Adaptor extends ArrayAdapter<Details_Buyer_Stage2_Groupview>
    {
        ArrayList<Details_Buyer_Stage2_Groupview> order_data = new ArrayList<Details_Buyer_Stage2_Groupview>();
        private LayoutInflater mInflater;

        public Orders_Adaptor(Context context, int resource, ArrayList<Details_Buyer_Stage2_Groupview> object)
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
                convertView = mInflater.inflate(R.layout.vieworderlist_buyer_order_stage3_groupview_customview,null);
            }

            final Details_Buyer_Stage2_Groupview dto = order_data.get(position);

            TextView farmername = (TextView)convertView.findViewById(R.id.s3_farmername);
            TextView agentname = (TextView)convertView.findViewById(R.id.s3_agentname);
            TextView ordercount = (TextView)convertView.findViewById(R.id.s3_order_count);
            TextView farmercontact = (TextView)convertView.findViewById(R.id.s3_farmernum);
            TextView agentcontact = (TextView)convertView.findViewById(R.id.s3_agentnum);

            farmername.setText(dto.getOrder_Farmer_Name());
            agentname.setText(dto.getOrder_Agent_Name());
            ordercount.setText(dto.getOrder_Count());
            farmercontact.setText(dto.getFarmer_ContactNum());
            agentcontact.setText(dto.getAgent_Contact());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    order_status_edit_intent = new ArrayList<String>();

                    order_status_edit_intent.add("Update");
                    order_status_edit_intent.add(dto.getOrder_Agent_ID());
                    order_status_edit_intent.add(dto.getOrder_Farmer_ID());

                    Intent i = new Intent(getApplicationContext(),ViewOrderlist_Buyer_Order_Stage3_Pending_Payment.class);
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
//                this.finish();
                break;
            case 2:
                this.finish();
                break;
        }

    }


}

