package my.project.agrim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static my.project.agrim.R.id.agentcv_pending_acceptance_cardview;
import static my.project.agrim.R.id.agentcv_pending_buyer_payment_cardview;
import static my.project.agrim.R.id.agentcv_pending_delivery_cardview;
import static my.project.agrim.R.id.agentcv_pending_payment_accept_status_cardview;
import static my.project.agrim.R.id.agentcv_post_delivery_status_cardview;

//import android.support.annotation;


public class ViewOrderlist_Agent_OpenOrder extends AppCompatActivity {

    //    GridView gv;
    String data = "logout";
    String Occupation = null;
    String Text_For_Intent_Action = "Action";
    String Text_For_Intent_Cancel = "Cancel";
    String Text_For_Intent_History = "History";
    //    CardView add_view_crop;
//    CardView add_view_farm;
//    CardView farmer_pending_action;
//    CardView view_orders_cancelled;
//    CardView view_orders_closed;
    @BindView(agentcv_pending_acceptance_cardview) CardView agent_pending_acceptance;
    @BindView(agentcv_pending_delivery_cardview) CardView agent_pending_delivery;
    @BindView(agentcv_pending_payment_accept_status_cardview) CardView agent_pending_pay_accept;
    @BindView(agentcv_pending_buyer_payment_cardview) CardView agent_pending_buyer_payment;
    @BindView(agentcv_post_delivery_status_cardview) CardView agent_post_delivery_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_openorder);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.agentcv_pending_acceptance_cardview)
    public void itemClick()
    {
        Intent i1 = new Intent(this,ViewOrderlist_Agent_Pending_Action.class);
        startActivity(i1);
    }

    @OnClick(R.id.agentcv_pending_delivery_cardview)
    void pendingdelivery()
    {
        Intent i2 = new Intent(this,ViewOrderlist_Agent_Pending_Delivery_Groupview.class);
        startActivity(i2);
    }

    @OnClick (R.id.agentcv_pending_payment_accept_status_cardview)
    void acceptpayment()
    {
        Intent i3 = new Intent(this,ViewOrderlist_Agent_Pending_Payment_Acceptance.class);
        startActivity(i3);
    }

    @OnClick (R.id.agentcv_pending_buyer_payment_cardview)
    void buyerpayment()
    {
        Intent i4 = new Intent(this,ViewOrderlist_Agent_Info_Buyer_Pending_Payment_Groupview.class);
        startActivity(i4);
    }

    @OnClick(R.id.agentcv_post_delivery_status_cardview)
    void postdelivery()
    {
        Intent i5 = new Intent(this,ViewOrderlist_Agent_Info_Order_Accepted_Rejected.class);
        startActivity(i5);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_homepage_listview,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.agentcv_pending_acceptance_cardview:
                Toast.makeText(this,"inside crop view",Toast.LENGTH_LONG).show();
                break;
            case R.id.agentcv_pending_delivery_cardview:
                Toast.makeText(this,"inside farm view",Toast.LENGTH_LONG).show();
                break;
            case R.id.agentcv_post_delivery_status_cardview:
                Toast.makeText(this,"inside pending action ",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_logout:
                SharedPreferences sp = getSharedPreferences("agrimuser", MODE_PRIVATE);
                SharedPreferences.Editor spe = sp.edit();
                spe.clear();
                spe.commit();

                Intent lo = new Intent(getApplicationContext(), Entry.class);
                startActivity(lo);
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
