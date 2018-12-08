package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vivek on 01-07-2017.
 */
public class RegisterAgent extends AppCompatActivity implements View.OnClickListener, HttpResponse, AdapterView.OnItemSelectedListener {

    TextView Screen_Agent_ID, Screen_Agent_Name, Screen_Agent_Contact_Num;
    EditText Screen_Agent_Address = null;

    String Agent_ID, Occupation, Agent_name, Agent_contact, Agent_Address, Transaction_Type  = null;
    ArrayList<Details_Agent> Agent_Details = null;
    ArrayList<String> village_array=null;
    ListView lv;
    Button submit;
    ArrayList<String> cities = null;

    Integer DB_which_process=0;
    Integer DB_vil_process, DB_city_process=0;
    String DB_Query_Table, City_var = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    Spinner Agent_Pickup_Village1,Agent_Pickup_Village2, Agent_Pickup_Village3, Agent_Pickup_Village4, Agent_Pickup_Village5, Agent_Pickup_Village6, Agent_Pickup_Village7, Agent_Pickup_Village8, Agent_Pickup_Village9, Agent_Pickup_Village10, Agent_Deliv_City1, Agent_Deliv_City2;
    String selected_village1,selected_village2,selected_village3,selected_village4,selected_village5,selected_village6,selected_village7,selected_village8,selected_village9,selected_village10,selected_city1,selected_city2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_agent);
        Intent geti = getIntent();

        Screen_Agent_ID=(TextView)findViewById(R.id.Agent_ID);
        Screen_Agent_Name = (TextView)findViewById(R.id.Agent_name);
        Screen_Agent_Contact_Num= (TextView)findViewById(R.id.Agent_Contactnum);
        Screen_Agent_Address = (EditText)findViewById(R.id.Agent_Address);
        Agent_Pickup_Village1= (Spinner) findViewById(R.id.Pickup_Village1);
        Agent_Pickup_Village1.setOnItemSelectedListener(this);
        Agent_Pickup_Village2= (Spinner) findViewById(R.id.Pickup_Village2);
        Agent_Pickup_Village2.setOnItemSelectedListener(this);
        Agent_Pickup_Village3= (Spinner) findViewById(R.id.Pickup_Village3);
        Agent_Pickup_Village3.setOnItemSelectedListener(this);
        Agent_Pickup_Village4= (Spinner) findViewById(R.id.Pickup_Village4);
        Agent_Pickup_Village4.setOnItemSelectedListener(this);
        Agent_Pickup_Village5= (Spinner) findViewById(R.id.Pickup_Village5);
        Agent_Pickup_Village5.setOnItemSelectedListener(this);
        Agent_Pickup_Village6= (Spinner) findViewById(R.id.Pickup_Village6);
        Agent_Pickup_Village6.setOnItemSelectedListener(this);
        Agent_Pickup_Village7= (Spinner) findViewById(R.id.Pickup_Village7);
        Agent_Pickup_Village7.setOnItemSelectedListener(this);
        Agent_Pickup_Village8= (Spinner) findViewById(R.id.Pickup_Village8);
        Agent_Pickup_Village8.setOnItemSelectedListener(this);
        Agent_Pickup_Village9= (Spinner) findViewById(R.id.Pickup_Village9);
        Agent_Pickup_Village9.setOnItemSelectedListener(this);
        Agent_Pickup_Village10= (Spinner) findViewById(R.id.Pickup_Village10);
        Agent_Pickup_Village10.setOnItemSelectedListener(this);
        Agent_Deliv_City1 = (Spinner) findViewById(R.id.Delivery_City1);
        Agent_Deliv_City1.setOnItemSelectedListener(this);
        Agent_Deliv_City2= (Spinner) findViewById(R.id.Delivery_City2);
        Agent_Deliv_City2.setOnItemSelectedListener(this);

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        Agent_ID = sp.getString("ID", null);
        Occupation = sp.getString("Occupation",null);
        Agent_name=sp.getString("FName",null)+" "+sp.getString("LName",null);
        Agent_contact=sp.getString("MPhone",null);
        Agent_Address = sp.getString("Address",null);

        Transaction_Type = "Add";

        if(Occupation.contentEquals("Agent"))
        {
        Screen_Agent_ID.setText(Agent_ID);
        Screen_Agent_Name.setText(Agent_name);
        Screen_Agent_Contact_Num.setText(Agent_contact);
        Screen_Agent_Address.setText(Agent_Address);
        }
        else
        {
            Toast.makeText(this, "Only Agent can edit the details"+Occupation, Toast.LENGTH_SHORT).show();
            this.finish();
        }

        Agent_Details = new ArrayList<Details_Agent>();
        City_var = "NA";

        //* Send Agent ID to fetch details of the current details*//
        getvillagedetails();

        getcitydetails();

        getexisting_agent_details();


        submit = (Button)findViewById(R.id.Submit);
        submit.setOnClickListener(this);

    }

    private void getvillagedetails() {
        JSONArray jvil_array = new JSONArray();
        JSONObject jvil_obj = new JSONObject();
        try {
            jvil_obj.put("Village", "NA");
            jvil_array.put(jvil_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_vil_process=1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetVillageList.php";
        json_array_data = jvil_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    private void getcitydetails() {
        JSONArray jcity_array = new JSONArray();
        JSONObject jcity_obj = new JSONObject();
        try {
            jcity_obj.put("City", City_var);
            jcity_array.put(jcity_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_city_process=1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetCityList.php";
        json_array_data = jcity_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    public void getexisting_agent_details() {

        JSONArray jagent_array = new JSONArray();
        JSONObject jagent_object = new JSONObject();
        try {
            jagent_object.put("ID", Agent_ID);
            jagent_array.put(jagent_object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DB_which_process=1;
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetAgentdetails.php";
        json_array_data = jagent_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);
    }

    public void push_data_to_cloud (String url, String jsondata) {

        if (DB_which_process == 1)
        {
            AsyncHttpRequest agentdata = new AsyncHttpRequest(this, url, "Getagentdetails", jsondata, this);
            agentdata.execute();
        }
        else if (DB_which_process == 2)
        {
            AsyncHttpRequest agentdataupd = new AsyncHttpRequest(this, url, "Updateagentdetails", jsondata, this);
            agentdataupd.execute();
            }
        else if (DB_vil_process==1)
        {
            AsyncHttpRequest village_all = new AsyncHttpRequest(this,url,"GetVillagedetails",jsondata,this);
            village_all.execute();
        }
        else if (DB_city_process==1)
        {
            AsyncHttpRequest city_all = new AsyncHttpRequest(this,url,"GetCitydetails",jsondata,this);
            city_all.execute();
        }


    }


    @Override
     public void getResponse(String serverResponse, String responseType) {

        if(DB_which_process == 1) {
            if (serverResponse.contentEquals("256[]")) {
                Toast.makeText(this, "Data Displaying", Toast.LENGTH_LONG).show();
                DB_which_process = 0;
                Transaction_Type = "Add";
            } else {
                try {

                    JSONArray ja = new JSONArray(serverResponse);
                    JSONObject jo = new JSONObject();
                    jo = ja.getJSONObject(0);
                    String s = jo.getString("Status");

                    if (s.equals("Success")) {
                        Transaction_Type = "Update";
                        int i = 0;
                        jo = ja.getJSONObject(1);
                        JSONArray ja1 = new JSONArray();
                        JSONObject jo1 = new JSONObject();
                        ja1 = jo.getJSONArray("Data");

                            for (i = 0; i < ja1.length(); i++) {
                                jo1 = ja1.getJSONObject(i);
                                String agent_id = jo1.getString("Agent_ID");
                                String agent_name = jo1.getString("Agent_Name");
                                String agent_num = jo1.getString("Agent_Contact_Num");
                                String agent_address = jo1.getString("Agent_Address");
                                String village1 = jo1.getString("Agent_Pickup_Village1");
                                String village2 = jo1.getString("Agent_Pickup_Village2");
                                String village3 = jo1.getString("Agent_Pickup_Village3");
                                String village4 = jo1.getString("Agent_Pickup_Village4");
                                String village5 = jo1.getString("Agent_Pickup_Village5");
                                String village6 = jo1.getString("Agent_Pickup_Village6");
                                String village7 = jo1.getString("Agent_Pickup_Village7");
                                String village8 = jo1.getString("Agent_Pickup_Village8");
                                String village9 = jo1.getString("Agent_Pickup_Village9");
                                String village10 = jo1.getString("Agent_Pickup_Village10");
                                String delv_city1 = jo1.getString("Agent_Delivery_City1");
                                String delv_city2 = jo1.getString("Agent_Delivery_City2");

                                Details_Agent da = new Details_Agent(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
                                da.setAgent_ID(agent_id);
                                da.setAgent_Name(agent_name);
                                da.setAgent_Contact_Num(agent_num);
                                da.setAgent_Address(agent_address);
                                da.setAgent_Pickup_Village1(village1);
                                da.setAgent_Pickup_Village2(village2);
                                da.setAgent_Pickup_Village3(village3);
                                da.setAgent_Pickup_Village4(village4);
                                da.setAgent_Pickup_Village5(village5);
                                da.setAgent_Pickup_Village6(village6);
                                da.setAgent_Pickup_Village7(village7);
                                da.setAgent_Pickup_Village8(village8);
                                da.setAgent_Pickup_Village9(village9);
                                da.setAgent_Pickup_Village10(village10);
                                da.setAgent_Deliv_City1(delv_city1);
                                da.setAgent_Deliv_City2(delv_city2);

                                Agent_Details.add(da);

                                if (agent_id.length() > 0) {
                                    Screen_Agent_ID.setText(agent_id);
                                }
                                if (agent_name.length() > 0) {
                                    Screen_Agent_Name.setText(agent_name);
                                }
                                if (agent_num.length() > 0) {
                                    Screen_Agent_Contact_Num.setText(agent_num);
                                }

                                Screen_Agent_Address.setText(agent_address);
                                int i1 = 0;
                                for (i1 = 0; i1 < village_array.size(); i1++) {
                                    if (village_array.get(i1).contentEquals(village1))
                                    {Agent_Pickup_Village1.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village2))
                                    {Agent_Pickup_Village2.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village3))
                                    {Agent_Pickup_Village3.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village4))
                                    {Agent_Pickup_Village4.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village5))
                                    {Agent_Pickup_Village5.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village6))
                                    {Agent_Pickup_Village6.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village7))
                                    {Agent_Pickup_Village7.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village8))
                                    {Agent_Pickup_Village8.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village9))
                                    {Agent_Pickup_Village9.setSelection(i1);}
                                    if (village_array.get(i1).contentEquals(village10))
                                    {Agent_Pickup_Village10.setSelection(i1);}
                                }
                                int i2=0;
                                for (i2 = 0; i2 < cities.size(); i2++) {
                                    if (cities.get(i2).contentEquals(delv_city1))
                                    {Agent_Deliv_City1.setSelection(i2);}
                                    if (cities.get(i2).contentEquals(delv_city2))
                                    {Agent_Deliv_City2.setSelection(i2);}

                                }

                            }

                        DB_which_process = 0;
                    } else {
                        Transaction_Type = "Add";
                        getexisting_agent_details();
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (DB_which_process ==2)
        {
            try {
                JSONArray ja1 = new JSONArray(serverResponse);
                JSONObject jo1 = new JSONObject();
                jo1 = ja1.getJSONObject(0);
                String s = jo1.getString("Status");
                if (s.equals("Success"))
                {
                    this.finish();
                    DB_which_process=0;
                } else {
                    push_data_to_cloud(url_sendnumtodb, json_array_data);
//  Loop back to send the data to Cloud for Product details in case of failure status in Json response
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (DB_vil_process==1)
        {
            village_array = new ArrayList<String>();

            try
            {
                JSONArray ja_vil = new JSONArray(serverResponse);
                JSONObject jo_vil = new JSONObject();
                jo_vil = ja_vil.getJSONObject(0);
                String s = jo_vil.getString("Status");

                if (s.equals("Success"))
                {

                    int i = 0;
                    jo_vil = ja_vil.getJSONObject(1);
                    JSONArray ja1_vil = new JSONArray();
                    JSONObject jo1_vil = new JSONObject();
                    ja1_vil = jo_vil.getJSONArray("Data");

                    village_array.add("Select-->");

                    for (i = 0; i < ja1_vil.length(); i++) {
                        jo1_vil = ja1_vil.getJSONObject(i);
                        String village_name = jo1_vil.getString("Village");
                        village_array.add(village_name);
                    }
                    DB_vil_process=0;
                } else {getvillagedetails();}
            } catch(Exception e) {e.printStackTrace();}

            if (village_array.size()> 0) {
                manage_vil_adaptor();
            }
        }
        if (DB_city_process==1)
        {
            cities = new ArrayList<String>();
            cities.add("Select-->");
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
                    jo1 = new JSONObject();

                    for (i = 0; i < ja1.length(); i++) {
                        jo1 = ja1.getJSONObject(i);
                        String city_name = jo1.getString("City");
                        Toast.makeText(this,"City DB: "+city_name,Toast.LENGTH_LONG).show();
                        cities.add(city_name);
                    }
                    DB_city_process=0;
                } else {getcitydetails();}
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cities.size() > 0) {
                manage_city_adaptor();
            }

        }


    }

    public void manage_vil_adaptor()
    {
        ArrayAdapter<String> vil = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,village_array);
        vil.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Agent_Pickup_Village1.setAdapter(vil);
        Agent_Pickup_Village2.setAdapter(vil);
        Agent_Pickup_Village3.setAdapter(vil);
        Agent_Pickup_Village4.setAdapter(vil);
        Agent_Pickup_Village5.setAdapter(vil);
        Agent_Pickup_Village6.setAdapter(vil);
        Agent_Pickup_Village7.setAdapter(vil);
        Agent_Pickup_Village8.setAdapter(vil);
        Agent_Pickup_Village9.setAdapter(vil);
        Agent_Pickup_Village10.setAdapter(vil);

    }

    public void manage_city_adaptor()
    {
        ArrayAdapter<String> city = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,cities);
        city.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Agent_Deliv_City1.setAdapter(city);
        Agent_Deliv_City2.setAdapter(city);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

        String agent_id = Screen_Agent_ID.getText().toString();
        String agent_name = Screen_Agent_Name.getText().toString();
        String agent_num = Screen_Agent_Contact_Num.getText().toString();
        String agent_address = Screen_Agent_Address.getText().toString();
        String village1 = selected_village1;
        String village2 = selected_village2;
        String village3 = selected_village3;
        String village4 = selected_village4;
        String village5 = selected_village5;
        String village6 = selected_village6;
        String village7 = selected_village7;
        String village8 = selected_village8;
        String village9 = selected_village9;
        String village10 = selected_village10;
        String delv_city1 = selected_city1;
        String delv_city2 = selected_city2;

        int ii = 0;

//        if (agent_num.isEmpty())
//        {Screen_Agent_Contact_Num.setError("Please enter 10 Digit Mobile number");}
//        else {ii=ii+1;}

        if (agent_address.isEmpty())
        {Screen_Agent_Address.setError("Please Enter your Address");}
        else {ii=ii+1;}

        if (delv_city1.isEmpty())
        {Screen_Agent_Address.setError("Please Enter Delivery City");}
        else {ii=ii+1;}

        if (delv_city2.isEmpty())
        {Screen_Agent_Address.setError("Please Enter Delivery City");}
        else {ii=ii+1;}

        if (village1.isEmpty())
        {Screen_Agent_Address.setError("Please Enter Pickup Village");}
        else {ii=ii+1;}

        if (ii==4) {
            JSONArray j_array = new JSONArray();
            JSONObject j_object = new JSONObject();
            try {

                String Unique_ID = null;
                if (Agent_ID.isEmpty())
                {
                    Unique_ID = "Agent_" + agent_name.toString() + "_"+ agent_num;
                }else
                {
                    Unique_ID = Agent_ID;
                }

                if (Transaction_Type.contentEquals("Add"))
                {j_object.put("OPERATION","Add");}
                else if (Transaction_Type.contentEquals("Update"))
                {j_object.put("OPERATION","Update");}

                j_object.put("Agent_ID", Unique_ID);
                j_object.put("Agent_Name", agent_name);
                j_object.put("Agent_Contact_Num", agent_num);
                j_object.put("Agent_Address", agent_address);
                j_object.put("Agent_Pickup_Village1", village1);
                j_object.put("Agent_Pickup_Village2", village2);
                j_object.put("Agent_Pickup_Village3", village3);
                j_object.put("Agent_Pickup_Village4", village4);
                j_object.put("Agent_Pickup_Village5", village5);
                j_object.put("Agent_Pickup_Village6", village6);
                j_object.put("Agent_Pickup_Village7", village7);
                j_object.put("Agent_Pickup_Village8", village8);
                j_object.put("Agent_Pickup_Village9", village8);
                j_object.put("Agent_Pickup_Village10", village10);
                j_object.put("Agent_Delivery_City1", delv_city1);
                j_object.put("Agent_Delivery_City2", delv_city2);

                j_array.put(j_object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DB_which_process=2;
            url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/AddUpdateAgentDetails.php";
            json_array_data = j_array.toString();
            push_data_to_cloud(url_sendnumtodb, json_array_data);
        }



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId())
        {
            case R.id.Pickup_Village1:
                selected_village1 = Agent_Pickup_Village1.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village2:
                selected_village2 = Agent_Pickup_Village2.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village3:
                selected_village3 = Agent_Pickup_Village3.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village4:
                selected_village4 = Agent_Pickup_Village4.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village5:
                selected_village5 = Agent_Pickup_Village5.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village6:
                selected_village6 = Agent_Pickup_Village6.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village7:
                selected_village7 = Agent_Pickup_Village7.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village8:
                selected_village8 = Agent_Pickup_Village8.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village9:
                selected_village9 = Agent_Pickup_Village9.getSelectedItem().toString();
                break;
            case R.id.Pickup_Village10:
                selected_village10 = Agent_Pickup_Village10.getSelectedItem().toString();
                break;
            case R.id.Delivery_City1:
                selected_city1 = Agent_Deliv_City1.getSelectedItem().toString();
                break;
            case R.id.Delivery_City2:
                selected_city2 = Agent_Deliv_City2.getSelectedItem().toString();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
