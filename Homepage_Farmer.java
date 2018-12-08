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

import static my.project.agrim.R.id.add_view_crop_cardview;
import static my.project.agrim.R.id.add_view_farm_cardview;
import static my.project.agrim.R.id.farmer_pending_action_cardview;
import static my.project.agrim.R.id.view_orders_cancelled_cardview;
import static my.project.agrim.R.id.view_orders_closed_cardview;

//import android.support.annotation;


public class Homepage_Farmer extends AppCompatActivity {

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
    @BindView(add_view_crop_cardview) CardView addCropCardview;
    @BindView(add_view_farm_cardview) CardView add_view_farm;
    @BindView(farmer_pending_action_cardview) CardView farmer_pending_action;
    @BindView(view_orders_cancelled_cardview) CardView view_orders_cancelled;
    @BindView(view_orders_closed_cardview) CardView view_orders_closed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_farmer);
        ButterKnife.bind(this);

    }

@OnClick(R.id.add_view_crop_cardview)
    public void itemClick()
    {
        Intent i1 = new Intent(this,RegisterCrop.class);
        startActivity(i1);
    }

@OnClick(R.id.add_view_farm_cardview)
    void farmclick()
    {
        Intent i2 = new Intent(this,RegisterFarm.class);
        startActivity(i2);
    }

@OnClick(R.id.farmer_pending_action_cardview)
    void pendingorderclick()
    {
        Intent i3 = new Intent(this,ViewOrderlist_Farmer_Open_Order.class);
        startActivity(i3);
    }

@OnClick(R.id.view_orders_cancelled_cardview)
    void cancelledorderclick()
    {
        Intent i4 = new Intent(this,ViewOrderlist_Farmer_Info_Order_Cancelled.class);
        startActivity(i4);
    }

@OnClick(R.id.view_orders_closed_cardview)
    void closedorderclick()
    {
        Intent i5 = new Intent(this,ViewOrderlist_Farmer_Info_Order_Closed.class);
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
            case R.id.add_view_crop_cardview:
                Toast.makeText(this,"inside crop view",Toast.LENGTH_LONG).show();
                break;
            case R.id.add_view_farm_cardview:
                Toast.makeText(this,"inside farm view",Toast.LENGTH_LONG).show();
                break;
            case R.id.farmer_pending_action_cardview:
                Toast.makeText(this,"inside pending action ",Toast.LENGTH_LONG).show();
                break;
            case R.id.view_orders_cancelled_cardview:
                Toast.makeText(this,"inside cancelled order",Toast.LENGTH_LONG).show();
                break;
            case R.id.view_orders_closed_cardview:
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
