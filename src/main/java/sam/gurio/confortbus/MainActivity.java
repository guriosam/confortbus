package sam.gurio.confortbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sam.gurio.confortbus.utils.AppPreferences;
import sam.gurio.confortbus.utils.FirebaseUtils;
import sam.gurio.confortbus.utils.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUtils firebaseUtils;
    private AppPreferences appPreferences;
    private Activity act;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {

            act = this;

            appPreferences = AppPreferences.getInstance(act);

            if (appPreferences.isUserLogged()) {
               // Toast.makeText(act, "UserLogged: " + appPreferences.isAdmin(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(act, ListBusActivity.class));
            }

            firebaseUtils = new FirebaseUtils(act);

            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();


            firebaseUtils = new FirebaseUtils(act);

            findViewById(R.id.sign_in).setOnClickListener(this);
            findViewById(R.id.sign_in_box).setOnClickListener(this);
            findViewById(R.id.sign_up).setOnClickListener(this);
            findViewById(R.id.sign_up_box).setOnClickListener(this);
            username = findViewById(R.id.edit_username);
            password = findViewById(R.id.edit_password);

            username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;

                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        password.requestFocus();
                        return true;
                    }
                    return handled;
                }
            });

            password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;

                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        //Handle go key click
                        if (v != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                        return true;
                    }
                    return handled;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_up_box:
            case R.id.sign_up:
                startActivity(new Intent(act, CadastroActivity.class));
                break;
            case R.id.sign_in_box:
            case R.id.sign_in:


                String username = "";
                if (this.username != null) {
                    username = this.username.getText().toString();
                }

                String password = "";
                if (this.password != null) {
                    password = this.password.getText().toString();
                }

                if (username != null && !username.equals("")) {
                    String user = username;
                    if (Util.checkInput(act, user)) {
                        firebaseUtils.signIn(user, password);
                    }

                } else {
                    Toast.makeText(act, "Digite um nome de usuário.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


}
