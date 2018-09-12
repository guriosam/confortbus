package sam.gurio.confortbus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sam.gurio.confortbus.adapter.RecyclerAdapter;
import sam.gurio.confortbus.objects.Bus;
import sam.gurio.confortbus.utils.AppPreferences;
import sam.gurio.confortbus.utils.FirebaseUtils;

public class ChooseReportActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mAdapter;

    private FirebaseUtils firebaseUtils;
    private AppPreferences appPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseUtils = new FirebaseUtils(this);
        appPreferences = AppPreferences.getInstance(this);


        getSupportActionBar().setTitle("Escolha um ônibus para obter análise");

        List<Bus> buses = new ArrayList<>();
        buses.add(new Bus());

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.lst_items);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerAdapter(buses, 1);
        mRecyclerView.setAdapter(mAdapter);
        firebaseUtils.getBusList(buses, mAdapter);
    }

}
