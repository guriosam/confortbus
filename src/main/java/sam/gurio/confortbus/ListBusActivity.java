package sam.gurio.confortbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sam.gurio.confortbus.adapter.RecyclerAdapter;
import sam.gurio.confortbus.booking.AbstractItem;
import sam.gurio.confortbus.booking.CenterItem;
import sam.gurio.confortbus.booking.EdgeItem;
import sam.gurio.confortbus.booking.EmptyItem;
import sam.gurio.confortbus.booking.OnSeatSelected;
import sam.gurio.confortbus.booking.adapter.AirplaneAdapter;
import sam.gurio.confortbus.objects.Bus;
import sam.gurio.confortbus.utils.AppPreferences;
import sam.gurio.confortbus.utils.FirebaseUtils;

public class ListBusActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mAdapter;

    private FirebaseUtils firebaseUtils;
    private AppPreferences appPreferences;

    private TextView logout;
    private TextView report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseUtils = new FirebaseUtils(this);
        appPreferences = AppPreferences.getInstance(this);

        report = findViewById(R.id.report);

        if (!appPreferences.isAdmin()) {
            getSupportActionBar().setTitle("Ônibus próximos a você");
            report.setVisibility(View.GONE);
        } else {
            getSupportActionBar().setTitle("Escolha um ônibus para modificar");
        }

        List<Bus> buses = new ArrayList<>();
        buses.add(new Bus());

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.lst_items);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerAdapter(buses, 0);
        mRecyclerView.setAdapter(mAdapter);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        report.setOnClickListener(this);

        firebaseUtils.getBusList(buses, mAdapter);

        DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("bus/");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        notificationReference.addValueEventListener(postListener);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.logout:
                appPreferences.clearPreferences();
                finish();
                break;
            case R.id.report:
                startActivity(new Intent(this, ReportActivity.class));
                break;
        }

    }

    @Override
    public void onBackPressed() {

    }
}
