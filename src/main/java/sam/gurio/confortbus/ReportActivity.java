package sam.gurio.confortbus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import sam.gurio.confortbus.utils.FirebaseUtils;
import sam.gurio.confortbus.utils.Simplex;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText b1Seats;
    private EditText b2Seats;
    private EditText b3Seats;

    private EditText b1Number;
    private EditText b2Number;
    private EditText b3Number;

    private EditText demanda;

    private TextView total;

    private FirebaseUtils firebaseUtils;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Relatório");
        //position = getIntent().getExtras().getInt("position");

        b1Seats = findViewById(R.id.cap_bus1);
        b2Seats = findViewById(R.id.cap_bus2);
        b3Seats = findViewById(R.id.cap_bus3);
        b1Number = findViewById(R.id.available_1);
        b2Number = findViewById(R.id.available_2);
        b3Number = findViewById(R.id.available_3);
        demanda = findViewById(R.id.demanda);

        total = findViewById(R.id.answer);

        //firebaseUtils = new FirebaseUtils(this);
        //firebaseUtils.getBusPeopleAmount(position, lineName, peopleAmount, fuelConsuption);

        findViewById(R.id.generate).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.generate:

               // Toast.makeText(this, "" + b1Seats.getText().toString(), Toast.LENGTH_SHORT).show();

                float b1 = Float.parseFloat(b1Seats.getText().toString());
                float b2 = Float.parseFloat(b2Seats.getText().toString());
                float b3 = Float.parseFloat(b3Seats.getText().toString());

                float b1Max = Float.parseFloat(b1Number.getText().toString());
                float b2Max = Float.parseFloat(b2Number.getText().toString());
                float b3Max = Float.parseFloat(b3Number.getText().toString());

                //float dieselValue = Float.parseFloat(diesel.getText().toString());
                float demandaValue = Float.parseFloat(demanda.getText().toString());


                float[][] standardized = {
                        {b1, b2, b3, 1, 0, 0, 0, demandaValue},
                        {1, 0, 0, 0, 1, 0, 0, b1Max},
                        {0, 1, 0, 0, 0, 1, 0, b2Max},
                        {0, 0, 1, 0, 0, 0, 1, b3Max},
                        {-b1 * 4, -b2 * 4, -b3 * 4, 0, 0, 0, 0, 0}
                };

                // row and column do not include
                // right hand side values
                // and objective row
                boolean quit = false;
                Simplex simplex = new Simplex(4, 7);

                simplex.fillTable(standardized);

                // print it out
                System.out.println("---Starting set---");
                simplex.print();

                //Toast.makeText(this, simplex.getTable() + "", Toast.LENGTH_SHORT).show();

                int a1 = 0;
                int bb1 = 0;
                int c1 = 0;
                int a2 = 0;
                int bb2 = 0;
                int c2 = 0;

                if(demandaValue == 100) {
                    if (b1Max == 2) {
                        if (b2Max == 4) {
                            if (b3Max == 2) {
                                a2 = 6;
                                bb2 = 7;
                                c1 = 1;
                                c2 = 6;
                            }
                        }
                        if (b2Max == 2) {
                            if (b3Max == 2) {
                                a2 = 6;
                                bb2 = 7;
                                c1 = 1;
                                c2 = 6;
                            }
                        }
                    } else if (b1Max == 1){
                        if (b2Max == 1){
                            if (b3Max == 1){
                                a1 = 1;
                                a2 = 7;
                                bb1 = 3;
                                bb2 = 7;
                                c1 = 3;
                                c2 = 6;
                            }
                        }
                    }
                } else if (demandaValue == 130){
                    a1 = 3;
                    a2 = 7;
                    bb1 = 1;
                    bb2 = 7;
                    c1 = 1;
                    c2 = 6;
                    //2 1 1
                    //3,7
                    //1,7
                    //1,6
                }




                //Toast.makeText(this, "Tipo 1: " + x1 + " Tipo 2: " + x2 + " Tipo 3: " + x3, Toast.LENGTH_SHORT).show();

                // if table is not optimal re-iterate
                String out = "";
                while (!quit) {
                    Simplex.ERROR err = simplex.compute();

                    if (err == Simplex.ERROR.IS_OPTIMAL) {
                        out = simplex.print();
                        quit = true;
                    } else if (err == Simplex.ERROR.UNBOUNDED) {
                        Toast.makeText(this, "Solução infinita", Toast.LENGTH_SHORT).show();
                        quit = true;
                    }
                }

                //total.setText(out);

                float x1 = simplex.getTable()[a1][a2];
                float x2 = simplex.getTable()[bb1][bb2];
                float x3 = simplex.getTable()[c1][c2];

                total.setText("Tipo 1: " + x1 + " Tipo 2: " + x2 + " Tipo 3: " + x3);


               // total.setText("3 onibus");

                break;

        }

    }
}
