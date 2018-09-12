package sam.gurio.confortbus.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sam.gurio.confortbus.ListBusActivity;
import sam.gurio.confortbus.R;
import sam.gurio.confortbus.adapter.RecyclerAdapter;
import sam.gurio.confortbus.booking.adapter.AirplaneAdapter;
import sam.gurio.confortbus.objects.Bus;
import sam.gurio.confortbus.objects.UserDAO;

import static android.content.ContentValues.TAG;

public class FirebaseUtils {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser firebaseUser;
    private Activity activity;
    private AppPreferences appPreferences;
    private ProgressDialog progress;

    public FirebaseUtils(Activity activity) {
        this.activity = activity;
        this.mAuth = FirebaseAuth.getInstance();
        this.appPreferences = AppPreferences.getInstance(activity);
        if (!appPreferences.isPersistent()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            appPreferences.setPersistent(true);
            firebaseAuth();
        }
    }

    private void firebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    System.err.println(task.getException());
                                    Log.d(TAG, "Error login anonymous");
                                    Toast.makeText(activity, "Autenticação falhou, favor entrar em contato com o suporte.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "loginAnonymous:success");
                                }
                            }
                        });
            }
        };
    }

    public FirebaseUser getCurrentUser() {

        if (firebaseUser != null) {
            // Name, email address, and profile photo Url
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            Uri photoUrl = firebaseUser.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = firebaseUser.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = firebaseUser.getUid();
        }

        return firebaseUser;
    }

    public void signUp(final String username, final String password, final String nome) {


        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();

                            firebaseAuth();

                            String email = firebaseUser.getEmail();
                            email = email.substring(0, email.indexOf("@"));

                            UserDAO user = new UserDAO();
                            user.setEmail(email);
                            user.setName(nome);
                            user.setAdmin(false);

                            insertData("users/" + email + "/", user);

                            activity.finish();

                            //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println("Erro Firebase: " + task.getException());
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });


    }

    public void insertData(String path, Object data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(path);
        ref.setValue(data);
    }

    public void signIn(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();

                            String email = firebaseUser.getEmail();

                            if (email == null || email.equals("")) {
                                System.err.println("Email invalido");
                                Toast.makeText(activity, "Erro no login.", Toast.LENGTH_SHORT).show();
                            } else {
                                email = email.substring(0, email.indexOf("@"));
                                appPreferences.setTempEmail(email);
                                getUserInfo(email);

                            }


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void getUserInfo(final String email) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/" + email);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserDAO user = dataSnapshot.getValue(UserDAO.class);

                        if (user == null) {
                            getUserInfo(email);
                            return;
                        }
                        //Toast.makeText(activity, "" + user.isAdmin(), Toast.LENGTH_SHORT).show();

                        appPreferences.setUsername(user.getName());
                        appPreferences.setAdmin(user.isAdmin());

                        appPreferences.setUserLogged(true);
                        activity.startActivity(new Intent(activity, ListBusActivity.class));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(activity, "Erro de conexão. Verifique sua conexão com a internet ou contate o suporte.", Toast.LENGTH_SHORT).show();
                        // ...
                    }
                });

    }

    private void insertString(String path, String data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(path + data);
        ref.setValue(data);
    }

    public void setBusInfo(int position, final ActionBar supportActionBar, final TextView count, final TextView busName, final ImageView way, final TextView up, final AirplaneAdapter adapter) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("bus/" + position);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Bus bus = dataSnapshot.getValue(Bus.class);

                        if (bus == null) {
                            Toast.makeText(activity, "Ocorreu algum problema, favor tentar novamente.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (supportActionBar != null) {
                            supportActionBar.setTitle(bus.getLineName());
                        }
                        count.setText(bus.getTakenSeats() + "/" + bus.getTotalSeats());
                        busName.setText(bus.getLineName());
                        if (bus.getWay() == 0) {
                            way.setImageDrawable(activity.getDrawable(R.drawable.arrow_down));
                        } else {
                            way.setImageDrawable(activity.getDrawable(R.drawable.arrow_up));
                        }

                        up.setText(bus.getTakenUp() + "/" + bus.getTotalUp());

                        String seats = bus.getSeats();

                        String[] div = seats.split(",");
                        List<String> s = new ArrayList<>();
                        for (int i = 0; i < div.length; i++) {
                            s.add(div[i]);
                        }

                        adapter.setSeats(s);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(activity, "Erro de conexão. Verifique sua conexão com a internet e tente novamente", Toast.LENGTH_SHORT).show();
                        // ...
                    }
                });

    }

    public void getBusList(final List<Bus> buses, final RecyclerAdapter busAdapter) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Toast.makeText(act, AppPreferences.getInstance(act).getUsername(), Toast.LENGTH_SHORT).show();
        DatabaseReference ref = database.getReference("bus/");

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                            System.err.println(noteSnapshot.getKey());
                            Bus onibus = noteSnapshot.getValue(Bus.class);
                            //Toast.makeText(activity, "" + onibus.getLineName(), Toast.LENGTH_SHORT).show();
                            if (busAdapter.getItem(i) == null) {
                                busAdapter.getmBuses().add(onibus);
                            } else {
                                busAdapter.setItem(i, onibus);
                            }

                            busAdapter.notifyDataSetChanged();
                            i++;

                        }

/*                        if (count == 0) {
                            answerList.add("");
                            if (platform.equals("messenger")) {
                                answerList.add("");
                                answerList.add("");
                            }
                            answerAdapter.notifyDataSetChanged();
                        } else {

                            for (int i = 0; i < count; i++) {
                                DatabaseReference ref2 = database.getReference("users/" + AppPreferences.getInstance(act).getUsername() + "/" + platform + "/temas/" + tema + "/" + i + "/");
                                ref2.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String answer = dataSnapshot.getValue(String.class);
                                                answerList.add(answer);
                                                //Toast.makeText(act, "" + answer, Toast.LENGTH_SHORT).show();
                                                answerAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                internetError();
                                            }
                                        });


                            }
                        }
*/

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //internetError();
                    }
                });


    }

    public void setBusSeats(int position, List<String> seats) {

        int count = 0;
        String seat = "";
        for (String s : seats) {
            if (s.equals("1")) {
                count++;
            }
            seat += s + ",";
        }

        seat = seat.substring(0, seat.length() - 1);

        insertData("bus/" + position + "/seats", seat);


        insertData("bus/" + position + "/takenSeats", count);
    }

    public void getBusPeopleAmount(int position, final TextView busName, final TextView peopleAmount, final TextView fuelConsuption) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("bus/" + position);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Bus bus = dataSnapshot.getValue(Bus.class);

                        if (bus == null) {
                            Toast.makeText(activity, "Ocorreu algum problema, favor tentar novamente.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        peopleAmount.setText(bus.getTakenUp() + bus.getTakenSeats());
                        busName.setText(bus.getLineName());
                        //double cons = 8.5714 * bus.getTakenSeats() + bus.getTakenUp();
                        //fuelConsuption.setText(cons + "");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(activity, "Erro de conexão. Verifique sua conexão com a internet e tente novamente", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

