package pieronegri.RisposteStronze.ui.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import pieronegri.RisposteStronze.BuildConfig;
import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;
import pieronegri.RisposteStronze.data_source.Firebase.FBRepository;
import pieronegri.RisposteStronze.utils.Utility;
import pieronegri.RisposteStronze.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    String Tag=String.valueOf(item.getItemId());
                    try {
                        switch (item.getItemId()) {
                            case R.id.navigation_risposta:
                                open(Risposta.newInstance(), Tag);
                                return true;
                            case R.id.navigation_login:
                                open(Login.newInstance(), Tag);
                                return true;
                            case R.id.navigation_credits:
                                open(Credits.newInstance(), Tag);
                                return true;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    return false;
                }
            };


    private View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickCallBack(v);
        }

        private void onClickCallBack(View v) {
            if (v.getId() == R.id.Btn_Exit) {
                finishAffinity();
            }
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        if(BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectAll() //for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        */
        super.onCreate(savedInstanceState);
        new FBRepository(FBNodeStructure.Risposta);
        setContentView(R.layout.activity_bottom_navigation);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);findViewById(R.id.Btn_Exit).setOnClickListener(myOnClickListener);
            try{
                ReplaceWithLastBackStackElement();
            }
            catch (Exception e1){
                try {
                    Utility.SetOnLinePresence();
                    bottomNavigation.setSelectedItemId(R.id.navigation_risposta);
                } catch (Exception e2) {
                    e1.printStackTrace();
                    e2.printStackTrace();
                    bottomNavigation.setSelectedItemId(R.id.navigation_login);
                }
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbarActionEsci) {
            finishAffinity();
        }
        return true;
    }


    private void ReplaceWithLastBackStackElement() throws Exception {
        String Tag=getSupportFragmentManager().getBackStackEntryAt(
                            getSupportFragmentManager().getBackStackEntryCount() - 1)
                    .getName();
        open(Tag);
    }

    private void open(@NonNull Fragment f, @NonNull String Tag) throws Exception {
        try{
            open(Tag);
        }
        catch(Exception e){
            open(f,Tag,true);
        }
    }
    private void open(String Tag) throws Exception {
        Fragment f=getSupportFragmentManager().findFragmentByTag(Tag);
        open(f, Tag, false);
    }

    private void open(@NonNull Fragment f,@NonNull String Tag, Boolean isToAddToBackStack) throws Exception {
        try {
            if (Tag == null) throw new Exception("Tag is not Null");
            if (f == null) throw new Exception("f is not Null");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, f, Tag);
            if (isToAddToBackStack) {
                transaction.addToBackStack(Tag);
            }
            transaction.commit();
            /*
            if(BuildConfig.DEBUG) {
                Toast.makeText(getApplicationContext(), "backStack count " + getSupportFragmentManager().getBackStackEntryCount(),
                        Toast.LENGTH_SHORT).show();
            }
             */
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        }
        else{
            findViewById(R.id.Btn_Exit).setClickable(true);
            finishAffinity();
        }
    }

}
