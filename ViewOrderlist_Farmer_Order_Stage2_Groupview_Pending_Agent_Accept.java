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
public class ViewOrderlist_Farmer_Order_Stage2_Groupview_Pending_Agent_Accept extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Farmer_ID, Occupation, Farmer_name, Farmer_contact = null;
    String Order_Status_for_Farmer_Info_Pending_Agent_Accept = "2";
    ArrayList<Details_Farmer_Stage2_Groupview> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    Integer Reprocess_Status = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_farmer_info_order_stage2_groupview);

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

        Order_Array = new ArrayList<Details_Farmer_Stage2_Groupview>();
        getpending_orderlist_farmer_view();

//        lv = (ListView)findViewById(R.id.orders_farmer_closed_list);
//        ArrayAdapter<Details_Order> aa = new Orders_Adaptor();
//        lv.setAdapter(aa);

    }

    public void getpending_orderlist_farmer_view(){

        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Farmer_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Farmer_Info_Pending_Agent_Accept);
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderdetailsInGroupbyagent.php";
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

        if (serverResponse.contentEquals("256[]"))
        {
            Toast.makeText(this,"No Orders Pending Agent Acceptance",Toast.LENGTH_LONG).show();
            if (Reprocess_Status == 1)
            {
                Reprocess_Status = 0;
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
                        String cnt = jo1.getString("O_Count");
                        String agent_id = jo1.getString("O_Agent_ID");
                        String agent_name = jo1.getString("O_Agent_Name");
                        String agent_contact_num = jo1.getString("O_Agent_Contact");
                        String buyer_name = jo1.getString("O_Buyer_Name");

                        Details_Farmer_Stage2_Groupview dorder = new Details_Farmer_Stage2_Groupview(" ", " ", " ", " ", " ");
                        dorder.setCount(cnt);
                        dorder.setAgent_ID(agent_id);
                        dorder.setAgent_Name(agent_name);
                        dorder.setAgent_ContactNum(agent_contact_num);
                        dorder.setBuyer_Name(buyer_name);

                        Order_Array.add(dorder);
                    }
                } else {
                    getpending_orderlist_farmer_view();
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
        lv = (ListView)findViewById(R.id.orders_farmer_stage2_groupview);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_farmer_info_order_stage2_groupview_customview, Order_Array);
        lv.setAdapter(oa);

    }


    private class Orders_Adaptor extends ArrayAdapter<Details_Farmer_Stage2_Groupview>
    {
        ArrayList<Details_Farmer_Stage2_Groupview> order_data = new ArrayList<Details_Farmer_Stage2_Groupview>();
        private LayoutInflater mInflater;

        public Orders_Adaptor(Context context, int resource, ArrayList<Details_Farmer_Stage2_Groupview> object)
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
                convertView = mInflater.inflate(R.layout.vieworderlist_farmer_info_order_stage2_groupview_customview,null);
            }

            final Details_Farmer_Stage2_Groupview dto = order_data.get(position);

            TextView buyername = (TextView)convertView.findViewById(R.id.s2_buyername);
            TextView agentname = (TextView)convertView.findViewById(R.id.s2_agentname);
            TextView count = (TextView)convertView.findViewById(R.id.s2_order_count);

            buyername.setText(dto.getBuyer_Name());
            agentname.setText(dto.getAgent_Name());
            count.setText(dto.getCount());

            convertView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {


                    order_status_edit_intent = new ArrayList<String>();

                    order_status_edit_intent.add("Update");
                    order_status_edit_intent.add(dto.getAgent_ID());

                    Intent i = new Intent(getApplicationContext(), ViewOrderlist_Farmer_Order_Stage2_Pending_Agent_Accept.class);
                    i.putStringArrayListExtra("Data", order_status_edit_intent);
                    startActivityForResult(i, 1);
                }

            });


            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
//                String ss = data.getStringExtra("Data");
                Order_Array.clear();
                Reprocess_Status = 1;
                getpending_orderlist_farmer_view();
//                this.finish();
                break;
            case 2:
                this.finish();
                break;
        }
    }

}
