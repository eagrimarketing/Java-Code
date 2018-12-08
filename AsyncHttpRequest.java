package my.project.agrim;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vivek on 26-02-2017.
 */
public class AsyncHttpRequest extends AsyncTask<String, Void, String>
{
    ProgressDialog pd;
    Context mContext;
    String url, responseType, jsondata;
    HttpResponse httpres;



    public AsyncHttpRequest (Context mContext, String url12, String responseType, String jsondata, HttpResponse httpres)
    {
        this.mContext=mContext;
        this.url= "http://" + url12;
//        this.url= "https://" + url12;
        this.responseType=responseType;
        this.jsondata=jsondata;
        this.httpres=httpres;


    }

    @Override
    protected void onPreExecute()
    {
        // TODO Auto-generated method stub
        super.onPreExecute();
        pd = ProgressDialog.show(mContext,"Progress Dialog", "Please wait...");

    }

    @Override
    protected String doInBackground(String... params)
    {
        // TODO Auto-generated method stub

        return setURLandData(url, jsondata);
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
        }catch (Exception e){e.printStackTrace();}

//        pd.dismiss();
        httpres.getResponse(result, responseType);
    }

    public String setURLandData(String urlString,String JsonString)
    {

        String response = "";
        try {
            URL url = new URL(urlString);
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(JsonString);

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

//            if (responseCode == HttpsURLConnection.HTTP_OK)
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null)
                {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;


    }



}
