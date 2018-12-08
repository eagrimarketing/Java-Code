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

public class ViewOrderlist_Buyer_Order_Stage1_Groupview extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Buyer_ID, Occupation, Buyer_name, Buyer_contact = null;
    String Order_Status_for_Buyer_Pending_Farmer_Accept = "1";
    String Order_Status_for_Buyer_Pending_Agent_Accept = "2";
    String Order_Status_for_Buyer_Pending_Agent_Action = "3";
    ArrayList<Details_Buyer_Stage1_Groupview> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    JSONArray jupdate_status_array = new JSONArray();

    String Control_DB_Access = null;   /* R=Read DB for Listview, U=update Order status  */
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    Integer Reprocess_Status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_buyer_order_stage1_groupview);

        Intent ii = getIntent();
        String Text_From_Intent_Option_Selection = ii.getStringExtra("Data");

        Toast.makeText(this,"Inside Order Stage1 Planning ",Toast.LENGTH_LONG).show();
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

        Order_Array = new ArrayList<Details_Buyer_Stage1_Groupview>();

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
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
//        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderStatusforBuyer.php";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderdetailsInGroupbyfarmer.php";
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
            Toast.makeText(this,"No Open Orders",Toast.LENGTH_LONG).show();
            if (Reprocess_Status == 1)
            {
                Reprocess_Status = 0;

            }
        }
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
                            String cnt = jo1.getString("O_Count");
                            String farmer_id = jo1.getString("O_Farmer_ID");
                            String farmer_name = jo1.getString("O_Farmer_Name");
                            String farmer_contact_num = jo1.getString("O_Farmer_ContactNum");

                            Details_Buyer_Stage1_Groupview dsgroup1 = new Details_Buyer_Stage1_Groupview(" "," "," "," ");
                            dsgroup1.setOrder_Count(cnt);
                            dsgroup1.setOrder_Farmer_ID(farmer_id);
                            dsgroup1.setOrder_Farmer_Name(farmer_name);
                            dsgroup1.setFarmer_ContactNum(farmer_contact_num);

                            Order_Array.add(dsgroup1);

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
        lv = (ListView)findViewById(R.id.orders_buyer_stage1_groupview);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_buyer_order_stage1_groupview_customview,Order_Array);
        lv.setAdapter(oa);

    }

    private class Orders_Adaptor extends ArrayAdapter<Details_Buyer_Stage1_Groupview>
    {

        ArrayList<Details_Buyer_Stage1_Groupview> order_data = new ArrayList<Details_Buyer_Stage1_Groupview>();
        private LayoutInflater mInflater;

        public Orders_Adaptor(Context context, int resource, ArrayList<Details_Buyer_Stage1_Groupview> object)
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
                convertView = mInflater.inflate(R.layout.vieworderlist_buyer_order_stage1_groupview_customview,null);
            }

            final Details_Buyer_Stage1_Groupview dto = order_data.get(position);

            TextView farmername = (TextView)convertView.findViewById(R.id.s1_farmername);
            TextView farmercontact = (TextView)convertView.findViewById(R.id.s1_farmernum);
            TextView count = (TextView) convertView.findViewById(R.id.s1_order_count);

            farmername.setText(dto.getOrder_Farmer_Name());
            farmercontact.setText(dto.getFarmer_ContactNum());
            count.setText(dto.getOrder_Count());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    order_status_edit_intent = new ArrayList<String>();

                    order_status_edit_intent.add("Update");
                    order_status_edit_intent.add(dto.getOrder_Farmer_ID());

                    Intent i = new Intent(getApplicationContext(), ViewOrderlist_Buyer_Order_Stage1_Planning.class);
                    i.putStringArrayListExtra("Data", order_status_edit_intent);
                    startActivityForResult(i, 1);
                }

                });


            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
//                String ss = data.getStringExtra("Data");
                Order_Array.clear();
                Reprocess_Status = 1;
                get_orderstatus_buyer_view();
//                this.finish();
                break;
            case 2:
                this.finish();
                break;
        }
    }


}
