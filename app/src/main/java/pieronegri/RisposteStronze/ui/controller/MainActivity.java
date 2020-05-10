package pieronegri.RisposteStronze.ui.controller;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;
import pieronegri.RisposteStronze.data_source.Firebase.FBRepository;
import pieronegri.RisposteStronze.utils.Utility;
import pieronegri.RisposteStronze.R;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private ImageButton exitButton;
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(exitButton!=null){
                        exitButton.setVisibility(View.GONE);
                    }
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
        Utility.SetOnLinePresence();
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        navigateToFirstFragment(bottomNavigation);
    }

    private void navigateToFirstFragment(BottomNavigationView bottomNavigation) {
        try{
            ReplaceLastBackStackElement();
        }
        catch (Exception e){
            if(Utility.isUserSigned()) {
                bottomNavigation.setSelectedItemId(R.id.navigation_risposta);
            }
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
            Goodbye();
        }
        return true;
    }
    private void Goodbye(){
        Utility.goOffLine();
        finishAffinity();
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
        Utility._toast(this,message);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if(exitButton==null){
                exitButton = new ImageButton(this);
                exitButton.setImageResource(R.drawable.esci);
                exitButton.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                exitButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Goodbye();
                    }
                });
            }
            try{
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                exitButton.setLayoutParams(params);
                addContentView(exitButton, params);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void crashButton(){
        Button crashButton = new Button(this);
        crashButton.setText("---");
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
