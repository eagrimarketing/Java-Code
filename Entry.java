package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vivek on 26-11-2018.
 */
public class Entry extends AppCompatActivity implements View.OnClickListener, HttpResponse{

    Button Login;
    TextView New_User;
        EditText Phone, Password;
//    TextInputEditText Phone, Password;
    String url_sendnumtodb = null;
    String DB_Query_Table, json_array_data  = null;
    String Occupation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        String checkuser = sp.getString("MPhone", null);
        Occupation = sp.getString("Occupation",null);

        if(checkuser!=null)
        {
            if (Occupation.contentEquals("Farmer")) {
                Intent farmer_homepage = new Intent(this,Homepage_Farmer.class);
                startActivity(farmer_homepage);
                this.finish();
            }
            else if (Occupation.contentEquals("Agent")){
                Intent agent_homepage = new Intent(this,Homepage_Agent.class);
                startActivity(agent_homepage);
                this.finish();
            }
            else if (Occupation.contentEquals("Buyer/Store Owner")){
                Intent buyer_homepage = new Intent(this,Homepage_Buyer.class);
                startActivity(buyer_homepage);
                this.finish();
            }
        }
        New_User = (TextView) findViewById(R.id.New_user_signup);
        Login = (Button)findViewById(R.id.Existing_user_login);
        Phone = (EditText) findViewById(R.id.Phone_num);
        Password = (EditText)findViewById(R.id.Password);

        New_User.setOnClickListener(this);
        Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.New_user_signup:
                Intent i2 = new Intent(this, Registration.class);
                startActivity(i2);
                this.finish();
                break;
            case R.id.Existing_user_login:
                manage_user_login();
                break;
        }
    }

    private void manage_user_login()
    {
        String phone = Phone.getText().toString();
        String pwd = Password.getText().toString();

        int i=0;

        if (phone.length() < 10)
        {Phone.setError("Please enter valid Phone Number");
            Phone.requestFocus();
        }
        else {i=i+1;}

        if (pwd.length() < 4)
        {Password.setError("Please enter atleast 4 character Password");}
        else {i=i+1;}

        if (i==2)
        {
            i=0;
            JSONArray jarray = new JSONArray();
            JSONObject jobject = new JSONObject();
            try {
                jobject.put("Phone", phone);
                jobject.put("Password", pwd);
                jarray.put(jobject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetUserDetails.php";
            json_array_data = jarray.toString();
            push_data_to_cloud(url_sendnumtodb, json_array_data);
        }
    }

    public void push_data_to_cloud (String url, String jsondata) {

        AsyncHttpRequest login_process = new AsyncHttpRequest(this, url, "check login", jsondata, this);
        login_process.execute();
    }

    @Override
    public void getResponse(String serverResponse, String responseType) {
        if (serverResponse.contentEquals("256[]")) {
            Toast.makeText(this, "Connectivity Problem or ID/Password is incorrect. Pls Check and Try Again", Toast.LENGTH_LONG).show();
        } else {
            try {
                JSONArray ja = new JSONArray(serverResponse);
                JSONObject jo = new JSONObject();
                jo = ja.getJSONObject(0);
                String s = jo.getString("Status");

                if (s.equals("Success")) {
                    jo = ja.getJSONObject(1);
                    ja = jo.getJSONArray("Data");
                    jo = ja.getJSONObject(0);

                    String uid = jo.getString("Unique_ID");
                    String mphone = jo.getString("Mphone");
                    String aphone = jo.getString("Aphone");
                    String fname = jo.getString("FName");
                    String lname = jo.getString("LName");
                    String address = jo.getString("Address");
                    String occ = jo.getString("Occupation");
                    String adharid = jo.getString("Adhar_ID");

                    SharedPreferences sp = getSharedPreferences("agrimuser", MODE_PRIVATE);
                    SharedPreferences.Editor spe = sp.edit();

                    spe.putString("ID", uid);
                    spe.putString("MPhone", mphone);
                    spe.putString("APhone", aphone);
                    spe.putString("FName", fname);
                    spe.putString("LName", lname);
                    spe.putString("Address", address);
                    spe.putString("Occupation", occ);
                    spe.putString("Adharid", adharid);
                    spe.commit();

                    if (occ.contentEquals("Farmer")) {
                        Intent farmer_homepage = new Intent(this, Homepage_Farmer.class);
                        startActivity(farmer_homepage);
                        this.finish();
                    } else if (occ.contentEquals("Agent")) {
                        Intent agent_homepage = new Intent(this, Homepage_Agent.class);
                        startActivity(agent_homepage);
                        this.finish();
                    } else if (occ.contentEquals("Buyer/Store Owner")) {
                        Intent buyer_homepage = new Intent(this, Homepage_Buyer.class);
                        startActivity(buyer_homepage);
                        this.finish();
                    }
                } else {

                    Phone.setError("Please enter valid Phone Number");
                    Password.setError("Please enter valid Password");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Either Phone or Password are Incorrect", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

}
