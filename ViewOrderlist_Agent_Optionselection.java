package my.project.agrim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Vivek on 01-07-2017.
 */
public class ViewOrderlist_Agent_Optionselection extends AppCompatActivity implements View.OnClickListener{

    Button Order_accept_reject, Order_Pending_Delivery, Order_cancelled, Order_closed;
    String Text_For_Intent_Action = "Action";
    String Text_For_Intent_Cancel = "Cancel";
    String Text_For_Intent_History = "History";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_optionselection);

        Intent geti = getIntent();

        Order_accept_reject = (Button)findViewById(R.id.Pending_accept_reject);
        Order_Pending_Delivery = (Button)findViewById(R.id.Pending_Delivery);
        Order_cancelled = (Button)findViewById(R.id.Orders_cancelled);
        Order_closed = (Button)findViewById(R.id.Orders_closed);

        SharedPreferences sp = getSharedPreferences("agrimuser", Context.MODE_PRIVATE);
        String Agent_ID = sp.getString("ID", null);
        String Occupation = sp.getString("Occupation",null);
        String Agent_name=sp.getString("FNAME",null)+" "+sp.getString("LNAME",null);
        String Agent_contact=sp.getString("MPhone",null);

        if (Occupation.contentEquals("Agent"))
        {}
        else
        {
            Toast.makeText(this, "Only Agent can view these details "+Occupation, Toast.LENGTH_SHORT).show();
//            Intent i1 = new Intent();
//            i1.putExtra("Data", "Failure");
//            setResult(1, i1);
            this.finish();
        }


        Order_accept_reject.setOnClickListener(this);
        Order_Pending_Delivery.setOnClickListener(this);
        Order_cancelled.setOnClickListener(this);
        Order_closed.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Pending_accept_reject:
                Toast.makeText(this,"For Pending Agent action-1",Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(getApplicationContext(),ViewOrderlist_Agent_Pending_Action.class);
                i1.putExtra("Data", Text_For_Intent_Action);
                startActivityForResult(i1,1);
                break;

            case R.id.Pending_Delivery:
                Toast.makeText(this,"For Pending Agent action-2",Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(getApplicationContext(),ViewOrderlist_Agent_Pending_Delivery.class);
                i2.putExtra("Data", Text_For_Intent_Action);
                startActivityForResult(i2,2);
                break;

            case R.id.Orders_cancelled:
                Toast.makeText(this,"View Order Rejected- 3BB",Toast.LENGTH_SHORT).show();
                Intent i3 = new Intent(getApplicationContext(),ViewOrderlist_Agent_Info_Order_Accepted_Rejected.class);
                i3.putExtra("Data", Text_For_Intent_Cancel);
                startActivityForResult(i3,3);
                break;

            case R.id.Orders_closed:
                Toast.makeText(this,"View Order History - 4Z, 3AZ2, 3BBZ2, 3BAZ2",Toast.LENGTH_SHORT).show();
                Intent i4 = new Intent(getApplicationContext(),ViewOrderlist_Agent_Info_Order_Closed.class);
                i4.putExtra("Data", Text_For_Intent_History);
                startActivityForResult(i4,4);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1)
        {
                this.finish();
        }

        if (requestCode==2)
        {
                this.finish();
        }

        if (requestCode==3)
        {
            this.finish();
        }

        if (requestCode==4)
        {
            this.finish();
        }


    }


}
