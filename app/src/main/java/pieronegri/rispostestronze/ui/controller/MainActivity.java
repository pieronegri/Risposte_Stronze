package pieronegri.rispostestronze.ui.controller;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;

import pieronegri.rispostestronze.data_source.Firebase.FBNodeStructure;
import pieronegri.rispostestronze.data_source.Firebase.FBRepository;
import pieronegri.rispostestronze.utils.Utility;
import pieronegri.rispostestronze.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    try {
                        switch (item.getItemId()) {
                            case R.id.navigation_risposta:
                                open(Risposta.newInstance());
                                return true;
                            case R.id.navigation_sign:
                                open(Login.newInstance());
                                return true;
                            case R.id.navigation_dashboard:
                                open(Credits.newInstance());
                                return true;
                        }
                    } catch (Exception e) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
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

         */
        super.onCreate(savedInstanceState);
        new FBRepository(FBNodeStructure.Risposta);
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (Utility.isUserSigned()) {
            try {
                Utility.SetOnLinePresence();
            } catch (Exception e) {
                e.printStackTrace();
            }
            bottomNavigation.setSelectedItemId(R.id.navigation_risposta);
        } else bottomNavigation.setSelectedItemId(R.id.navigation_sign);
    }

    private void open(Fragment f) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        }
        else{
            finishAffinity();
        }
    }

}
