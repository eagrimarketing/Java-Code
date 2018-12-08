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

import static my.project.agrim.R.id.agentcv_closed_orders_cardview;
import static my.project.agrim.R.id.agentcv_openorder_cardview;
import static my.project.agrim.R.id.agentcv_update_details_cardview;

//import android.support.annotation;


public class Homepage_Agent extends AppCompatActivity {

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
    @BindView(agentcv_openorder_cardview) CardView agent_openorder;
    @BindView(agentcv_closed_orders_cardview) CardView agent_closed_orders;
    @BindView(agentcv_update_details_cardview) CardView agent_update_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage__agent);
        ButterKnife.bind(this);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }

    @OnClick(R.id.agentcv_openorder_cardview)
    public void itemClick()
    {
        Intent i1 = new Intent(this,ViewOrderlist_Agent_OpenOrder.class);
        startActivity(i1);
    }

    @OnClick(R.id.agentcv_closed_orders_cardview)
    void closedorder()
    {
        Intent i6 = new Intent(this,ViewOrderlist_Agent_Info_Order_Closed.class);
        startActivity(i6);
    }

    @OnClick(R.id.agentcv_update_details_cardview)
    void agentdetails()
    {
        Intent i7 = new Intent(this,RegisterAgent.class);
        startActivity(i7);
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
            case R.id.agentcv_openorder_cardview:
                Toast.makeText(this,"inside open order view",Toast.LENGTH_LONG).show();
                break;
            case R.id.agentcv_closed_orders_cardview:
                Toast.makeText(this,"inside cancelled order",Toast.LENGTH_LONG).show();
                break;
            case R.id.agentcv_update_details_cardview:
                Toast.makeText(this,"inside closed order",Toast.LENGTH_LONG).show();
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
