package sam.gurio.confortbus;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sam.gurio.confortbus.booking.AbstractItem;
import sam.gurio.confortbus.booking.CenterItem;
import sam.gurio.confortbus.booking.EdgeItem;
import sam.gurio.confortbus.booking.EmptyItem;
import sam.gurio.confortbus.booking.OnSeatSelected;
import sam.gurio.confortbus.booking.adapter.AirplaneAdapter;
import sam.gurio.confortbus.utils.AppPreferences;
import sam.gurio.confortbus.utils.FirebaseUtils;

public class ChooseSeatActivity extends AppCompatActivity implements OnSeatSelected, View.OnClickListener {

    private static final int COLUMNS = 5;
    private TextView txtSeatSelected;

    private TextView count;
    private TextView busName;
    private TextView temperature;
    private ImageView way;
    private TextView up;
    private ImageView more;
    private ImageView less;

    private FirebaseUtils firebaseUtils;
    private AppPreferences appPreferences;
    private int position;
    private int totalUp;
    private int countUp;

    private AirplaneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_seat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseUtils = new FirebaseUtils(this);
        appPreferences = AppPreferences.getInstance(this);
        position = getIntent().getExtras().getInt("position");

        getSupportActionBar().setTitle("");

        txtSeatSelected = (TextView) findViewById(R.id.txt_seat_selected);
        txtSeatSelected.setOnClickListener(this);
        txtSeatSelected.setText("Confirmar");

        count = findViewById(R.id.seats);
        busName = findViewById(R.id.bus_line_name);
        temperature = findViewById(R.id.temperature);
        way = findViewById(R.id.sentido);
        more = findViewById(R.id.more);
        up = findViewById(R.id.up_count);
        less = findViewById(R.id.less);

        if (!appPreferences.isAdmin()) {
            txtSeatSelected.setVisibility(View.GONE);
            findViewById(R.id.up_mod).setVisibility(View.GONE);
        }


        more.setOnClickListener(this);
        less.setOnClickListener(this);

        List<AbstractItem> items = new ArrayList<>();
        for (int i = 0; i < 38; i++) {

            if (i % COLUMNS == 0 || i % COLUMNS == 4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i % COLUMNS == 1 || i % COLUMNS == 3) {
                items.add(new CenterItem(String.valueOf(i)));
            } else {
                items.add(new EmptyItem(String.valueOf(i)));
            }
        }

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);

        adapter = new AirplaneAdapter(this, items);
        recyclerView.setAdapter(adapter);

        firebaseUtils.setBusInfo(position, getSupportActionBar(), count, busName, way, up, adapter);
    }

    @Override
    public void onSeatSelected(int count) {
        //txtSeatSelected.setText(count + " lugares selecionados");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_seat_selected:

                List<String> seats = adapter.getSeats();
                List<Integer> selected = adapter.getSelectedItems();
                for (Integer integer : selected) {
                    seats.set(integer, "1");
                }

                firebaseUtils.setBusSeats(position, seats);
                firebaseUtils.insertData("bus/" + position + "/takenUp", countUp);
                finish();

                //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more:

                countUp = Integer.valueOf(up.getText().toString().split("/")[0]);
                totalUp = Integer.valueOf(up.getText().toString().split("/")[1]);

                if (countUp < totalUp) {
                    countUp++;
                }

                up.setText(countUp + "/" + totalUp);

                break;
            case R.id.less:

                countUp = Integer.valueOf(up.getText().toString().split("/")[0]);
                totalUp = Integer.valueOf(up.getText().toString().split("/")[1]);

                if (countUp > 0) {
                    countUp--;
                }

                up.setText(countUp + "/" + totalUp);

                break;
        }


    }
}
