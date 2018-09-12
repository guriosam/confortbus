package sam.gurio.confortbus;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sam.gurio.confortbus.utils.AppPreferences;
import sam.gurio.confortbus.utils.FirebaseUtils;
import sam.gurio.confortbus.utils.Util;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity act;
    private AppPreferences appPreferences;
    private FirebaseUtils firebaseUtils;
    private EditText email;
    private EditText name;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        act = this;

        try {
            appPreferences = AppPreferences.getInstance(act);
            firebaseUtils = new FirebaseUtils(act);

            email = findViewById(R.id.edit_email);
            name = findViewById(R.id.edit_name);
            password = findViewById(R.id.edit_password);
            findViewById(R.id.sign_up).setOnClickListener(this);
            findViewById(R.id.sign_up_box).setOnClickListener(this);

//            View decorView = getWindow().getDecorView();
//            // Hide the status bar.
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//            // Remember that you should never show the action bar if the
//            // status bar is hidden, so hide that too if necessary.
//            ActionBar actionBar = getSupportActionBar();
//            actionBar.hide();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sign_up_box:
            case R.id.sign_up:

                String username = "";
                if (this.email != null) {
                    username = this.email.getText().toString();
                }

                String nome = "";

                if(this.name != null){
                    nome = this.name.getText().toString();
                }

                String password = "";
                if (this.password != null) {
                    password = this.password.getText().toString();
                }

                if (username != null && !username.equals("")) {
                    String user = username;
                    if (Util.checkInput(act, user)) {
                        firebaseUtils.signUp(user, password, nome);
                    }

                } else {
                    Toast.makeText(act, "Digite um nome de usuário.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
