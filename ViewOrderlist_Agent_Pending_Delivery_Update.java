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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vivek on 02-07-2017.
 */
public class ViewOrderlist_Agent_Pending_Delivery_Update extends AppCompatActivity implements View.OnClickListener, HttpResponse, AdapterView.OnItemSelectedListener{

    ArrayList<String> order_status_edit_intent = null;
    ArrayList<String> order_status = null;
    Spinner order_status_spinner = null;
    EditText order_comments = null;
    Button button = null;
    String Order_ID = null;
    private boolean isSpinnerInitial = true;
    String status_entered=null;

    String Order_Status_Option_Agent_Delivered = "Agent Delivered";
    String Order_Status_Option_Agent_Cancelled = "Agent Cancelled";
    String Order_Status_Code_Agent_Delivered = "3B";
    String Order_Status_Code_Agent_Cancelled = "3A";
    String DB_Query_Table = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    String status_open = "O";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_pending_delivery_update);

        order_status_edit_intent = new ArrayList<String>();
        order_status = new ArrayList<String>();

        Intent i = getIntent();
        order_status_edit_intent = i.getStringArrayListExtra("Data");
        Order_ID = order_status_edit_intent.get(1);

        order_comments = (EditText)findViewById(R.id.Agent_delivery_comment);

        order_status_spinner = (Spinner)findViewById(R.id.Agent_select_order_delivery_status);

        ArrayAdapter<CharSequence> os = ArrayAdapter.createFromResource(this,R.array.Agent_deliver_cancel_order,R.layout.support_simple_spinner_dropdown_item);
        order_status_spinner.setAdapter(os);
        order_status_spinner.setOnItemSelectedListener(this);

        button=(Button)findViewById(R.id.Register);
        button.setOnClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

        String order_comment = order_comments.getText().toString();

        int i = 0;

        if (status_entered.isEmpty())
        {order_comments.setError("Please Select Status from dropdown");}
        else {i=i+1;}

        if (order_comment.isEmpty())
        {order_comments.setError("Please Enter Comments");}
        else {i=i+1;}


        if (i==2) {
            String status = " ";
            if (status_entered.contains(Order_Status_Option_Agent_Delivered))
            {
                status = Order_Status_Code_Agent_Delivered;
            }
            else
            if (status_entered.contains(Order_Status_Option_Agent_Cancelled))
            {
                status = Order_Status_Code_Agent_Cancelled;
            }


            JSONArray jarray = new JSONArray();
            JSONObject jobject = new JSONObject();
            try {

                jobject.put("OPERATION", "Update");
                jobject.put("O_Order_ID", Order_ID);
                jobject.put("O_Order_Display_Status", status_entered);
                jobject.put("O_Order_Workflow_Status", status_open);
                jobject.put("O_Order_Status", status);
                jobject.put("O_Comments", order_comment);
                jobject.put("O_Order_Rating"," ");

                jarray.put(jobject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateOrderDetailsforAgent.php";
            json_array_data = jarray.toString();
            push_data_to_cloud(url_sendnumtodb, json_array_data);
        }


    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "UpdateOrderdetailsforagent", jsondata, this);
        orderdata.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType) {

        try {
            JSONArray ja_agent = new JSONArray(serverResponse);
            JSONObject jo_agent = new JSONObject();
            jo_agent = ja_agent.getJSONObject(0);
            String s = jo_agent.getString("Status");

            if (s.equals("Success"))
            {
                Intent i1 = new Intent(this, ViewOrderlist_Agent_Pending_Delivery.class);
                i1.putExtra("Data", "Success");
                setResult(1, i1);
                this.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if (isSpinnerInitial)
        {
            isSpinnerInitial=false;
        }
        else {
            if (spinner.getId() == R.id.Agent_select_order_delivery_status) {
                status_entered = order_status_spinner.getSelectedItem().toString();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
