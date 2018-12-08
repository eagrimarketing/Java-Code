package my.project.agrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vivek on 02-07-2017.
 */
public class ViewOrderlist_Farmer_Pending_Action_Update extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, HttpResponse {

    ArrayList<String> order_status_edit_intent, agent_id_array, agent_name_array, agent_num_array = null;
//    ArrayList<String> order_status = null;
    Spinner order_status_spinner, agent_name = null;
    EditText order_comments = null;
    Button submit = null;
    String Order_ID = null;
    String Farm_Village, Buyer_City = null;
    String DB_Call=null;
    String Crop_ID_Intent, Order_Qty_Intent = null;
    Integer Order_Qty_Calc = 0;

    String Order_Status_Option_Farmer_Accept = "Farmer Accepted";
    String Order_Status_Option_Farmer_Reject = "Farmer Rejected";
    String Order_Status_Code_Farmer_Accepted = "2";
    String Order_Status_Code_Farmer_Rejected = "4";

    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    String status_entered, agent_entered, agent_id_entered, agent_num_entered = null;
    private boolean isSpinnerInitial = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_farmer_pending_action_update);

        order_status_edit_intent = new ArrayList<String>();
        agent_id_array = new ArrayList<String>();
        agent_name_array = new ArrayList<String>();
        agent_num_array = new ArrayList<String>();

        Intent i = getIntent();
        order_status_edit_intent = i.getStringArrayListExtra("Data");
        Order_ID = order_status_edit_intent.get(1);
        Farm_Village = order_status_edit_intent.get(2);
        Buyer_City = order_status_edit_intent.get(3);
        Crop_ID_Intent = order_status_edit_intent.get(4);
        Order_Qty_Intent = order_status_edit_intent.get(5);

        order_comments = (EditText)findViewById(R.id.Farmer_add_comment);

        order_status_spinner = (Spinner)findViewById(R.id.Farmer_select_order_delivery_status);

        ArrayAdapter<CharSequence> os = ArrayAdapter.createFromResource(this, R.array.Farmer_accept_reject_order, R.layout.support_simple_spinner_dropdown_item);
        order_status_spinner.setAdapter(os);
        order_status_spinner.setOnItemSelectedListener(this);

        agent_name = (Spinner) findViewById(R.id.agent_name_selectiom);
        agent_name.setOnItemSelectedListener(this);

//        ArrayAdapter<String> os = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, order_status);
//        os.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        order_status_spinner.setAdapter(os);

        submit=(Button)findViewById(R.id.Register);
        submit.setOnClickListener(this);
    }


    @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

        Spinner spinner = (Spinner) parent;

        if (isSpinnerInitial)
        {
            isSpinnerInitial=false;
        }
        else {
            if (spinner.getId() == R.id.Farmer_select_order_delivery_status) {
                status_entered = order_status_spinner.getSelectedItem().toString();
                getagent_info();
            }
        }

        if (agent_name_array.size()>0)
        {
            if (spinner.getId()==R.id.agent_name_selectiom)
            {
                agent_entered = agent_name.getSelectedItem().toString();
                agent_id_entered = agent_id_array.get(position);
                agent_num_entered = agent_num_array.get(position);
            }
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v)
    {

        int i = 0;

        if (agent_name_array.isEmpty())
        {
            getagent_info();
        }
        else {
            String order_comment = order_comments.getText().toString();

//            <item></item>
//            <item></item>

            if (status_entered != null) {
                if (status_entered.isEmpty()) {
                    order_comments.setError("Please Select Status from dropdown");
                } else {
                    if (status_entered.contentEquals("Farmer Accepted")) {
                        i = i + 1; //for status entered
                        i = i + 1; //for comments, as non mandatory for accepted order
                        if (agent_entered != null && agent_entered.isEmpty()) {
                            order_comments.setError("Please Select the Agent Name");
                        } else {
                            i = i + 1;
                        }
                    } else {
                        if (status_entered.contentEquals("Farmer Rejected")) {
                            i = i + 1; //for status drop down
                            i = i + 1; // for agent drop down as selection is not important for rejected order
                            if (order_comment != null && order_comment.isEmpty()) {
                                order_comments.setError("Please Enter Comments");
                            } else {
                                i = i + 1;
                            }
                        }
                    }
                }

                if ((status_entered.contentEquals("Farmer Accepted") && i == 3) || (status_entered.contentEquals("Farmer Rejected") && i == 3)) {
//                    Toast.makeText(this, "Readying for table Update", Toast.LENGTH_LONG).show();

                    String temp_order_status = " ";

                    if (status_entered.contains(Order_Status_Option_Farmer_Accept))
                    {
                        temp_order_status = Order_Status_Code_Farmer_Accepted;
                    } else
                    {
                        temp_order_status = Order_Status_Code_Farmer_Rejected;
                    }

                    JSONArray jarray = new JSONArray();
                    JSONObject jobject = new JSONObject();
                    try {


                        jobject.put("OPERATION", "Update");
                        jobject.put("O_Order_ID", Order_ID);
                        jobject.put("O_Order_Display_Status", status_entered);
                        jobject.put("O_Order_Status", temp_order_status);
                        jobject.put("O_Order_Workflow_Status", "O");
                        jobject.put("O_Comments", order_comments.getText().toString());
                        jobject.put("O_Agent_ID", agent_id_entered);
                        jobject.put("O_Agent_Name", agent_entered);
                        jobject.put("O_Agent_Contact", agent_num_entered);


                        jarray.put(jobject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DB_Call = "Order";
                    url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateOrderDetailsforFarmer.php";
                    json_array_data = jarray.toString();
                    push_data_to_cloud(url_sendnumtodb, json_array_data);
                }
            }
        }
    }

    public void getagent_info()
    {
        JSONArray jagent_array = new JSONArray();
        JSONObject jagent_object = new JSONObject();
        try {
            jagent_object.put("Village", Farm_Village);
            jagent_object.put("City",Buyer_City);
            jagent_array.put(jagent_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Call = "Agent";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetAgentforOrder.php";
        json_array_data = jagent_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    public void update_Crop_Quantity()
    {
        JSONArray jcrop_array = new JSONArray();
        JSONObject jcrop_object = new JSONObject();
        try {
            jcrop_object.put("CropID",Crop_ID_Intent );
            jcrop_object.put("QTY",Order_Qty_Intent);
            jcrop_array.put(jcrop_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_Call = "Crop";
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateCropQuantitypostFarmerRejection.php";
        json_array_data = jcrop_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "UpdateOrderdetailsforfarmer", jsondata, this);
        orderdata.execute();
    }


    @Override
    public void getResponse(String serverResponse, String responseType)
    {
        if (DB_Call.contains("Agent"))
        {
//            Toast.makeText(this,"Agent",Toast.LENGTH_LONG).show();
            if (serverResponse.contains("256[]"))
            {
                agent_id_array.add("No Agent Available");
                agent_name_array.add("No Agent Available");
                agent_num_array.add("No Agent Available");
            }
            else {
                try {
                    JSONArray ja_agent = new JSONArray(serverResponse);
                    JSONObject jo_agent = new JSONObject();
                    jo_agent = ja_agent.getJSONObject(0);
                    String s = jo_agent.getString("Status");

                    if (s.equals("Success")) {
                        int i = 0;
                        jo_agent = ja_agent.getJSONObject(1);
                        JSONArray ja1_agent = new JSONArray();
                        JSONObject jo1_agent = new JSONObject();
                        ja1_agent = jo_agent.getJSONArray("Data");
                        agent_name_array.clear();
                        String select = "Select-->";
                        agent_id_array.add(select);
                        agent_name_array.add(select);
                        agent_num_array.add(select);
                        for (i = 0; i < ja1_agent.length(); i++) {
                            jo1_agent = ja1_agent.getJSONObject(i);
                            String agent_id = jo1_agent.getString("Agent_ID");
                            String agent_name = jo1_agent.getString("Agent_Name");
                            String agent_contact_num = jo1_agent.getString("Agent_Contact_Num");

                            agent_id_array.add(agent_id);
                            agent_name_array.add(agent_name);
                            agent_num_array.add(agent_contact_num);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(agent_id_array.size()>0)
            {
                manage_agent_adaptor();
            }
        }
        else if (DB_Call.contains("Order")) {
//            Toast.makeText(this,"Table Update",Toast.LENGTH_LONG).show();

                if (serverResponse.contains("256[]")) {
                } else {
                    try {
                        JSONArray ja = new JSONArray(serverResponse);
                        JSONObject jo = new JSONObject();
                        jo = ja.getJSONObject(0);
                        String s = jo.getString("Status");

                        if (s.equals("Success")) {
//                            Intent i1 = new Intent(this, ViewOrderlist_Farmer_Pending_Action.class);
//                            i1.putExtra("Data", "Success");
//                            setResult(1, i1);
//                            this.finish();
                            update_Crop_Quantity();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (DB_Call.contains("Crop"))
            {
                try {
                    JSONArray ja = new JSONArray(serverResponse);
                    JSONObject jo = new JSONObject();
                    jo = ja.getJSONObject(0);
                    String s = jo.getString("Status");

                    if (s.equals("Success")) {
                        DB_Call = "NA";
                        Toast.makeText(this,"Crop Quantiy updated",Toast.LENGTH_LONG).show();
                        Intent i1 = new Intent(this, ViewOrderlist_Farmer_Pending_Action.class);
                        i1.putExtra("Data", "Success");
                        setResult(1, i1);
                        this.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }


    public void manage_agent_adaptor()
    {
        ArrayAdapter<String> ss = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, agent_name_array);
        ss.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        agent_name.setAdapter(ss);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        Toast.makeText(this,"Nothing selected",Toast.LENGTH_LONG).show();
        Intent i2 = new Intent(this, ViewOrderlist_Farmer_Pending_Action.class);
        i2.putExtra("Data", "ok");
        setResult(2, i2);
        this.finish();
    }
}
