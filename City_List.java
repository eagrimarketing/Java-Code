package my.project.agrim;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vivek on 01-04-2017.
 */
public class City_List implements HttpResponse {

    ArrayList<String> City = null;
    String url_sendnumtodb = null;
    String json_array_data = null;
    private Context mContext;


    public City_List(Context C) {
        mContext = C;
    }

    public ArrayList<String> getcitylist() {

        City = new ArrayList<String>();

        getcitydetails();

        return City;
    }

    private void getcitydetails() {
        JSONArray jcity_array = new JSONArray();
        JSONObject jcity_obj = new JSONObject();
        try {
            jcity_obj.put("City", "City");
            jcity_array.put(jcity_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/GetCityList.php";
        json_array_data = jcity_array.toString();
        push_data_to_cloud(url_sendnumtodb, json_array_data);

    }


    public void push_data_to_cloud(String url, String jsondata) {
        AsyncHttpRequest citydata = new AsyncHttpRequest(mContext, url, "Test", jsondata, this);
//            AsyncHttpRequest citydata = new AsyncHttpRequest(mContext, url, "GetFarmdetails", jsondata, mContext);
        citydata.execute();
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

                int i = 0;
                jo = ja.getJSONObject(1);
                JSONArray ja1 = new JSONArray();
                JSONObject jo1 = new JSONObject();
                ja1 = jo.getJSONArray("Data");
                jo1 = new JSONObject();

                for (i = 0; i < ja1.length(); i++) {
                    jo1 = ja1.getJSONObject(i);
                    String city_name = jo1.getString("City");

                    City.add(city_name);
                }
            } else {getcitydetails();}
        } catch(Exception e) {e.printStackTrace();}

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
