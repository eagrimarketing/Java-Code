package my.project.agrim;

/* Entry level Program for Farmers, Buyers and Agents personal registration.
It will add entries to 3 tables  - Farmer, Middleman and Buyer basis occupation selected by the user */

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    EditText fname, lname, phone1, phone2, housenum, streetname, city, district, state, adharid, password;
    String fnm, lnm, ph1, ph2, hsn, stn, cty, dist, stt, adr, pwd;
    Button submit;
    Spinner occupation;
    String address = null;
    String url_sendnumtodb = null;
    String DB_Query_Table = null;
    String Unique_ID = null;


    String json_array_data = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    String Occupation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);


        Intent geti = getIntent();

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        String s = sp.getString("ID", null);
        Occupation = sp.getString("Occupation",null);

        if(s!=null)
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

        fname = (EditText)findViewById(R.id.f_n);
        lname = (EditText)findViewById(R.id.test1);
        phone1 = (EditText)findViewById(R.id.phone1);
        phone2 = (EditText)findViewById(R.id.phone2);
        housenum = (EditText)findViewById(R.id.address1);
        streetname = (EditText)findViewById(R.id.address2);
        city = (EditText)findViewById(R.id.cityvillage);
        district = (EditText)findViewById(R.id.district);
        state = (EditText)findViewById(R.id.state);
        occupation = (Spinner)findViewById(R.id.occupation);
        adharid = (EditText)findViewById(R.id.adhar);
        password=(EditText)findViewById(R.id.password);

        ArrayAdapter<CharSequence> Occp = ArrayAdapter.createFromResource(this, R.array.Occupation_list, R.layout.support_simple_spinner_dropdown_item);
        occupation.setAdapter(Occp);

        submit = (Button) findViewById(R.id.Register);
        submit.setOnClickListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Registration Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.vivek.prj.agri.agrim/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Registration Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.vivek.prj.agri.agrim/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }

    @Override
    public void onClick(View v) {

        /* Validate data entered by user on the Common Registration screen*/
        fnm = fname.getText().toString();
        lnm = lname.getText().toString();
        ph1 = phone1.getText().toString();
        ph2 =  phone2.getText().toString();
        hsn = housenum.getText().toString();
        stn = streetname.getText().toString();
        cty = city.getText().toString();
        dist = district.getText().toString();
        stt = state.getText().toString();
        adr = adharid.getText().toString();
        pwd=password.getText().toString();

        int i=0;
        int fnamelen = 0;
        int lnamelen = 0;
        int ph1len = 0;

        if (fnm.length() <5)
        {fname.setError("Please enter valid First Name");}
        else {i=i+1;
        fnamelen = fnm.length();}


        if (lnm.length() <5)
        {lname.setError("Please enter valid Last Name");}
        else {i=i+1;
            lnamelen = lnm.length();}

        if (ph1.length() != 10)
        {phone1.setError("Please Enter 10 digit number");}
        else {i=i+1;
            ph1len = ph1.length();}

        if (ph2.length() != 10)
        {phone2.setError("Please Enter 10 digit number");}
        else {i=i+1;}

        if (hsn.isEmpty())
        {housenum.setError("Please Enter House/Plot/Shop num");}
        else {i=i+1;}

        if (stt.isEmpty())
        {streetname.setError("Please Enter Street name");}
        else {i=i+1;}

        if (cty.isEmpty())
        {city.setError("Please Enter City");}
        else {i=i+1;}

        if (dist.isEmpty())
        {district.setError("Please Enter District");}
        else {i=i+1;}

        if (stt.isEmpty())
        {state.setError("Please Enter State");}
        else {i=i+1;}

        if (adr.isEmpty())
        {adharid.setError("Please Enter State");}
        else {i=i+1;}

        if (pwd.length()<8)
        {adharid.setError("Please Enter 8 Characters for Password");}
        else {i=i+1;}

        if (i==11) {

/* Create Unique ID for each user*/


            if(occupation.getSelectedItem().toString().contains("Farmer"))
            {Unique_ID =  "F_"+fnm.toString().substring(0,fnamelen)+ lnm.toString().substring(0,lnamelen)+ph1.toString().substring(0,ph1len)+"01";}
            else if (occupation.getSelectedItem().toString().contains("Buyer/Store Owner"))
            {Unique_ID =  "B_"+fnm.toString().substring(0,fnamelen)+lnm.toString().substring(0,lnamelen)+ph1.toString().substring(0,ph1len)+"01";}
            else if(occupation.getSelectedItem().toString().contains("Agent"))
            {Unique_ID =  "A_"+fnm.toString().substring(0,fnamelen)+lnm.toString().substring(0,lnamelen)+ph1.toString().substring(0,ph1len)+"01";}

            address = hsn + " " + stn + " " + cty + " " + dist + " " + stt;
//            Toast.makeText(this, "JSON address:" + address, Toast.LENGTH_LONG).show();

            JSONArray jarray = new JSONArray();
            JSONObject jobject = new JSONObject();
            try {
                jobject.put("ID", Unique_ID);
                jobject.put("MPhone", ph1);
                jobject.put("APhone", ph2);
                jobject.put("FName", fnm);
                jobject.put("LName", lnm);
                jobject.put("Address", address);
                jobject.put("Occupation", occupation.getSelectedItem().toString());
                jobject.put("Adharid", adr);
                jobject.put("Password",pwd);

                jarray.put(jobject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/registerUser.php";
            json_array_data = jarray.toString();
            push_data_to_cloud(url_sendnumtodb, json_array_data);
        }
    }

    public void push_data_to_cloud (String url, String jsondata)
    {

            AsyncHttpRequest ahr = new AsyncHttpRequest(this,url,"collectType",jsondata,this);
            ahr.execute();
    }


    @Override
    public void getResponse(String serverResponse, String responseType)
    {
        Toast.makeText(this, "server response: "+serverResponse, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "response type: "+responseType, Toast.LENGTH_LONG).show();
        try
        {
            JSONArray ja = new JSONArray(serverResponse);
            JSONObject jo = new JSONObject();
            jo=ja.getJSONObject(0);
            String s = jo.getString("Status");
            if (s.equals("Success")) {
                /* Below record will be inserted in Farmer, Buyer or Agent table basis 1st Character in the Unique field value*/
                SharedPreferences sp = getSharedPreferences("agrimuser", MODE_PRIVATE);
                SharedPreferences.Editor spe = sp.edit();

                spe.putString("ID", Unique_ID);
                spe.putString("MPhone", ph1);
                spe.putString("APhone", ph2);
                spe.putString("FName", fnm);
                spe.putString("LName", lnm);
                spe.putString("Address", address);
                spe.putString("Occupation", occupation.getSelectedItem().toString());
                spe.putString("Adharid", adr);
                spe.commit();

                String Occ = occupation.getSelectedItem().toString();
                if (Occ.contentEquals("Farmer")) {
                    Intent farmer_homepage = new Intent(this,Homepage_Farmer.class);
                    startActivity(farmer_homepage);
                    this.finish();
                }
                else if (Occ.contentEquals("Agent")){
                    Intent agent_homepage = new Intent(this,Homepage_Agent.class);
                    startActivity(agent_homepage);
                    this.finish();
                }
                else if (Occ.contentEquals("Buyer/Store Owner")){
                    Intent buyer_homepage = new Intent(this,Homepage_Buyer.class);
                    startActivity(buyer_homepage);
                    this.finish();
                }
            } else {
                Toast.makeText(this,"Please Try Again",Toast.LENGTH_LONG).show();
            }
        }        catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
