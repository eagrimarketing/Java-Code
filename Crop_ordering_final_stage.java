package my.project.agrim;

import android.content.Intent;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Crop_ordering_final_stage extends AppCompatActivity implements View.OnClickListener, HttpResponse {

    ArrayList<String> data_from_intent = null;
    TextView crop_id, crop_name, crop_grade, crop_selling_rate, crop_rating, farmer_name, farmer_contact, avl_qty;
    Button submit;
    EditText order_quantity;
    Double Order_Value=0.0;
    Double Calc_Agrim_Service_Charges = 0.0;
    String Order_Status_Placed = "1";
    String Order_Display_Status_placed = "Buyer Submitted";
    String Order_Workflow_Status = "O";
    Integer qty_remaning = 0;
    Integer tmp_qty = 0;
    String status = null;
    ArrayList<String> data_to_intent = null;


    /*Order Status: Open, Rejected, Cancelled, Complete
     * Order Stage: Buyer_Submitted, Buyer_ReSubmitted, Farmer_Accepted, Farmer_Rejected, Farmer_Cancelled, Buyer_Rejected, Buyer_Approved
     */

    Integer DB_Crop_Table = 0;
    Integer DB_Order_Table = 0;
    String url_sendnumtodb = null;
    String json_array_data = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_ordering_final_stage);

        Intent ii = getIntent();
        data_from_intent = new ArrayList<String>();
        data_from_intent=ii.getStringArrayListExtra("Data");

        order_quantity=(EditText)findViewById(R.id.Crop_ordering_final_quantity);
        crop_id=(TextView)findViewById(R.id.Crop_ordering_final_cropid);
        crop_name=(TextView)findViewById(R.id.Crop_ordering_final_cropname);
        crop_grade=(TextView)findViewById(R.id.Crop_ordering_final_grade);
        crop_selling_rate=(TextView)findViewById(R.id.Crop_ordering_final_rate);
        crop_rating=(TextView)findViewById(R.id.Crop_ordering_final_rating);
        farmer_name=(TextView)findViewById(R.id.Crop_ordering_final_farmername);
        farmer_contact=(TextView)findViewById(R.id.Crop_ordering_final_farmercontact);
        avl_qty = (TextView)findViewById(R.id.Crop_ordering_avl_qty);

        order_quantity.setText("0");
        crop_id.setText(data_from_intent.get(0));
        crop_name.setText(data_from_intent.get(3));
        crop_grade.setText(data_from_intent.get(4));
        crop_selling_rate.setText(data_from_intent.get(5));
        crop_rating.setText(data_from_intent.get(6));
        farmer_name.setText(data_from_intent.get(7));
        farmer_contact.setText(data_from_intent.get(8));
        avl_qty.setText(data_from_intent.get(22));

        data_to_intent = new ArrayList<String>();

        submit = (Button)findViewById(R.id.Register);
        submit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        String order_qty = order_quantity.getText().toString();

        Integer avl_qty_calc = Integer.parseInt(data_from_intent.get(22));
        tmp_qty = Integer.parseInt(order_qty);

        qty_remaning = avl_qty_calc - tmp_qty;

        if(order_qty.isEmpty() || tmp_qty ==0)
            {
                order_quantity.setError("Please Enter Quantity in numbers: ");
            }
        else if (avl_qty_calc<tmp_qty)
        {
            order_quantity.setError("Please Enter Quantity less than Available Quantity");
            Toast.makeText(this,"Please Enter Quantity less than Available Quantity",Toast.LENGTH_LONG).show();
        }
        else
        {
            JSONArray jarray = new JSONArray();
            JSONObject jobject = new JSONObject();
            try {

                java.util.Calendar cal = java.util.Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyhhmmss");
                String datetime = sdf.format(cal.getTime());

                String date = datetime.substring(0,6);

                String Unique_Order_ID = null;
                Unique_Order_ID = "Order_" + datetime + "_"+ data_from_intent.get(3);

                Integer tmp_rate = Integer.parseInt(data_from_intent.get(5).toString());
                Double tmp_agrim_Svc_percent = Double.parseDouble(data_from_intent.get(21).toString());

                Order_Value = tmp_qty*(tmp_rate*(1+tmp_agrim_Svc_percent));
                DecimalFormat df = new DecimalFormat("#.##");
                String O_value = df.format(Order_Value);

                Calc_Agrim_Service_Charges = tmp_qty*tmp_agrim_Svc_percent;
                String Calc_svc_chg  = df.format(Calc_Agrim_Service_Charges);

                jobject.put("OPERATION","Add");
                jobject.put("O_Order_ID", Unique_Order_ID);
                jobject.put("O_Order_Status",Order_Status_Placed);
                jobject.put("O_Order_Display_Status", Order_Display_Status_placed);
                jobject.put("O_Order_Workflow_Status",Order_Workflow_Status);
                jobject.put("O_Order_Date",date);
                jobject.put("O_Order_Value", O_value);
                jobject.put("O_Crop_ID", data_from_intent.get(0));
                jobject.put("O_CropName", data_from_intent.get(3));
                jobject.put("O_Grade", data_from_intent.get(4));
                jobject.put("O_Rate", data_from_intent.get(5));
                jobject.put("O_Ordered_Quantity", order_qty);
                jobject.put("O_Rating", data_from_intent.get(6));
                jobject.put("O_Farmer_ID",data_from_intent.get(1));
                jobject.put("O_Farmer_Name",data_from_intent.get(7));
                jobject.put("O_Farmer_ContactNum",data_from_intent.get(8));
                jobject.put("O_Farm_ID", data_from_intent.get(9));
                jobject.put("O_Farm_Address", data_from_intent.get(10));
                jobject.put("O_Farm_Latitide", data_from_intent.get(11));
                jobject.put("O_Farm_Longitude", data_from_intent.get(12));
                jobject.put("O_Buyer_ID", data_from_intent.get(2));
                jobject.put("O_Buyer_Name",data_from_intent.get(13));
                jobject.put("O_Buyer_ContactNum",data_from_intent.get(14));
                jobject.put("O_Buyer_Shop_ID", data_from_intent.get(15));
                jobject.put("O_Buyer_Shop_Address", data_from_intent.get(16));
                jobject.put("O_Shop_Latitide", data_from_intent.get(17));
                jobject.put("O_Shop_Longitude", data_from_intent.get(18));
                jobject.put("O_Buyer_City",data_from_intent.get(19));
                jobject.put("O_Farm_Village",data_from_intent.get(20));
                jobject.put("O_Activity_Timestamp", datetime);
                jobject.put("O_Agent_ID"," ");
                jobject.put("O_Agent_Name"," ");
                jobject.put("O_Agent_Contact"," ");
                jobject.put("O_Comments"," ");
                jobject.put("O_Agrim_Service_Percent",data_from_intent.get(21).toString());
                jobject.put("O_Agrim_Service_Charges",Calc_svc_chg);
                jobject.put("O_Agrim_Service_Charge_Received","N");

                jarray.put(jobject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DB_Order_Table = 1;
            url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/AddOrderDetails.php";
            json_array_data = jarray.toString();
            push_data_to_cloud(url_sendnumtodb, json_array_data);
        }
    }

//    public void update_crop_quantity_remaining()
//    {
//        JSONArray jqty_array = new JSONArray();
//        JSONObject jqty_object = new JSONObject();
//        try {
//            jqty_object.put("CropID",data_from_intent.get(0));
//            jqty_object.put("QTY", qty_remaning);
//            jqty_array.put(jqty_object);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        DB_Crop_Table = 1;
//        url_sendnumtodb = "wwwagrimcom.000webhostapp.com/agrim/UpdateCropQuantitypostOrderplacement.php";
//        json_array_data = jqty_array.toString();
//
//        push_data_to_cloud(url_sendnumtodb, json_array_data);
//    }

    public void push_data_to_cloud (String url, String jsondata)
    {
        if (DB_Order_Table == 1) {
            AsyncHttpRequest orderdata = new AsyncHttpRequest(this, url, "Add order", jsondata, this);
            orderdata.execute();
        }
//        if (DB_Crop_Table == 1) {
//            AsyncHttpRequest cropdata = new AsyncHttpRequest(this, url, "Update crop", jsondata, this);
//            cropdata.execute();
//        }

    }


    @Override
    public void getResponse(String serverResponse, String responseType) {

        if (serverResponse.contentEquals("256[]")) {
            Toast.makeText(this, "Pls Try Again with Correct Inputs", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (DB_Order_Table == 1) {
                try {
                    JSONArray ja = new JSONArray(serverResponse);
                    JSONObject jo = new JSONObject();
                    jo = ja.getJSONObject(0);
                    String s = jo.getString("Status");

                    if (s.equals("Success")) {
                        DB_Order_Table = 0;
                        data_to_intent.add(data_from_intent.get(0));
                        data_to_intent.add(qty_remaning.toString());
                        Intent i1 = new Intent(getApplicationContext(), Crop_ordering.class);
                        i1.putExtra("Data", data_to_intent);
                        setResult(22, i1);
                        this.finish();

//                        update_crop_quantity_remaining();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if (DB_Crop_Table == 1)
//            {
//                try {
//                    JSONArray ja = new JSONArray(serverResponse);
//                    JSONObject jo = new JSONObject();
//                    jo = ja.getJSONObject(0);
//                    String s = jo.getString("Status");
//
//                    if (s.equals("Success")) {
//                        DB_Crop_Table = 0;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



}
