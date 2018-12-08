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
public class ViewOrderlist_Agent_Info_Buyer_Pending_Payment_Groupview extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ListView lv;
    String Agent_ID, Occupation, Agent_name, Agent_contact = null;
    String Order_Status_for_Agent_Info_Buyer_Payment  = "3BC";
    String Order_Status_for_Agent_Info_Buyer_Pending_Acceptance  = "3B";
    ArrayList<Details_Agent_Pending_Delivery_Groupview> Order_Array = null;
    ArrayList<String> order_status_edit_intent = null;
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    Integer Reprocess_Status = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_pending_delivery_groupview);

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

        Order_Array = new ArrayList<Details_Agent_Pending_Delivery_Groupview>();
        getorderlist_pending_agent_delivery();

//        this.finish();

    }

    public void getorderlist_pending_agent_delivery(){

        JSONArray jorder_array = new JSONArray();
        JSONObject jorder_object = new JSONObject();
        try {
            jorder_object.put("ID", Agent_ID);
            jorder_object.put("O_Order_Status1",Order_Status_for_Agent_Info_Buyer_Payment);
            jorder_object.put("O_Order_Status2",Order_Status_for_Agent_Info_Buyer_Pending_Acceptance);
            jorder_array.put(jorder_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Query_Table = "Order";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetOrderdetailsInGroupbybuyerforagentpendingbuyerpayment.php";
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
            Toast.makeText(this,"No Orders Pending Buyer Payment",Toast.LENGTH_LONG).show();
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
                        String cnt = jo1.getString("O_Count");
                        String buyerid = jo1.getString("O_Buyer_ID");
                        String buyername = jo1.getString("O_Buyer_Name");
                        String buyer_contact_num = jo1.getString("O_Buyer_ContactNum");
                        String farmer_name = jo1.getString("O_Farmer_Name");

                        Details_Agent_Pending_Delivery_Groupview dorder = new Details_Agent_Pending_Delivery_Groupview(" ", " ", " ", " ", " ");
                        dorder.setCount(cnt);
                        dorder.setBuyer_ID(buyerid);
                        dorder.setBuyer_Name(buyername);
                        dorder.setBuyer_ContactNum(buyer_contact_num);
                        dorder.setFarmer_Name(farmer_name);

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
        lv = (ListView)findViewById(R.id.orders_agent_pending_delivery_groupview);
        Orders_Adaptor oa = new Orders_Adaptor(this,R.layout.vieworderlist_agent_pending_delivery_groupview_customview, Order_Array);
        lv.setAdapter(oa);

    }



    @Override
    public void onClick(View v) {

    }

    private class Orders_Adaptor extends ArrayAdapter<Details_Agent_Pending_Delivery_Groupview>
    {
        ArrayList<Details_Agent_Pending_Delivery_Groupview> order_data = new ArrayList<Details_Agent_Pending_Delivery_Groupview>();
        private LayoutInflater mInflater;

        public Orders_Adaptor(Context context, int resource, ArrayList<Details_Agent_Pending_Delivery_Groupview> object)
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
                convertView = mInflater.inflate(R.layout.vieworderlist_agent_pending_delivery_groupview_customview,null);
            }

            final Details_Agent_Pending_Delivery_Groupview dto = order_data.get(position);

            TextView buyername = (TextView)convertView.findViewById(R.id.agent_pd_buyername);
            TextView farmrname = (TextView)convertView.findViewById(R.id.agent_pd_farmername);
            TextView count = (TextView)convertView.findViewById(R.id.agent_pd_order_count);

            buyername.setText(dto.getBuyer_Name());
            farmrname.setText(dto.getFarmer_Name());
            count.setText(dto.getCount());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    order_status_edit_intent = new ArrayList<String>();

                    order_status_edit_intent.add("Update");
                    order_status_edit_intent.add(dto.getBuyer_ID());

                    Intent i = new Intent(getApplicationContext(),ViewOrderlist_Agent_Info_Buyer_Pending_Payment.class);
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
                getorderlist_pending_agent_delivery();
//                Intent i1 = new Intent(this,ViewOrderlist_Farmer_Open_Order.class);
//                setResult(1, i1);
//                this.finish();
                break;
            case 2:
                this.finish();
                break;
        }

    }

}
