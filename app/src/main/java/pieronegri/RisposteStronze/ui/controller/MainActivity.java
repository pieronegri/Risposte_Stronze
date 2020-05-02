package pieronegri.RisposteStronze.ui.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;
import pieronegri.RisposteStronze.data_source.Firebase.FBRepository;
import pieronegri.RisposteStronze.utils.Utility;
import pieronegri.RisposteStronze.R;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    String Tag=String.valueOf(item.getItemId());
                    try {
                        if (item.getItemId() == R.id.navigation_risposta && !Utility.isUserSigned()) {
                            _toast(getString(R.string.logInBeforeRisposte));
                            return true;
                        }
                        open(new BottomFragmentFactoryImpl().makeNavigation(item.getItemId()),Tag);
                        return true;
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
        //crashButton();
        new FBRepository(FBNodeStructure.Risposta);
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        findViewById(R.id.Btn_Exit).setOnClickListener(myOnClickListener);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        navigateToFirstFragment(bottomNavigation);
    }

    private void navigateToFirstFragment(BottomNavigationView bottomNavigation) {
        try{
            ReplaceLastBackStackElement();
        }
        catch (Exception e){
            if(Utility.isUserSigned())
                bottomNavigation.setSelectedItemId(R.id.navigation_risposta);
            else
                bottomNavigation.setSelectedItemId(R.id.navigation_login);
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

    private void ReplaceLastBackStackElement() throws Exception {
        String Tag=getSupportFragmentManager().getBackStackEntryAt(
                            getSupportFragmentManager().getBackStackEntryCount() - 1)
                    .getName();
        deBugToastBackStackCount();
        open(getSupportFragmentManager().findFragmentByTag(Tag), Tag);
    }

    private void open(@NonNull Fragment f,@NonNull String Tag) throws Exception {
        try {
            if (Tag == null) throw new Exception("Tag is not Null");
            f=getSupportFragmentManager().findFragmentByTag(Tag) != null ?
                    getSupportFragmentManager().findFragmentByTag(Tag) : f;
            if (f == null) throw new Exception("f is not Null");
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, f, Tag);
            transaction.addToBackStack(Tag);
            transaction.commit();
            deBugToastBackStackCount();
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private void deBugToastBackStackCount(){
        if(BuildConfig.DEBUG) {
            _toast("backStack count " + getSupportFragmentManager().getBackStackEntryCount());
        }
    }

    private void _toast(String message){
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
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

    private void crashButton(){
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });
        addContentView(crashButton, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
    }

}
