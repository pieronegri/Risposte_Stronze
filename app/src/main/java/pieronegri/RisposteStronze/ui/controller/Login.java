package pieronegri.RisposteStronze.ui.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import pieronegri.RisposteStronze.utils.Utility;
import pieronegri.RisposteStronze.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends BottomFragmentIMPL {
    private static String TAG = Login.class.getName();
    private static final int RC_SIGN_IN = 123;
    private static String currentSingIn = "Sei registrato come %1$s";
    private View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickCallBack(v);
        }
    };

    public Login()  {
        super(R.layout.fragment_login);
    }

    private static Login newInstance(String param1, String param2) {
        return newInstance(new Login(), param1, param2);
    }

    static Login newInstance()  {//package private
        return newInstance("", "");
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);

        ImageButton signIn = view.findViewById(R.id.signIn);
        signIn.setOnClickListener(myOnClickListener);
        ImageButton signOut = view.findViewById(R.id.signOut);
        signOut.setOnClickListener(myOnClickListener);
        TextView t = view.findViewById(R.id.Txt_displayMessage);

        try{
            t.setText(String.format(currentSingIn, Utility.getCurrentUser().getDisplayName()));
        }
        catch(Exception e){
            e.printStackTrace();
            t.setText(getString(R.string.logInError));
        }
    }

    private void onClickCallBack(View view) {
        switch (view.getId()){
            case R.id.signIn:
                SignIn();
                break;
            case R.id.signOut:
                SignOut();
                break;
        }
    }

    private void SignIn()
    {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void SignOut() {
        // [START auth_fui_signout]
        if(getActivity()==null){return;}
        TextView t = getView().findViewById(R.id.Txt_displayMessage);
        if (!isUserSigned()) {
            t.setText(getString(R.string.logOutError));
            return;
        }

        try{
            AuthUI.getInstance()
                    .signOut(getActivity())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            t.setText(getString(R.string.logOutSuccess));
            Utility.setCurrentUser();
            Log.w(TAG,getString(R.string.logOutOKMessage));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        // [END auth_fui_signout]
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode != Activity.RESULT_OK) {
                try {
                    response.getError().printStackTrace();
                } catch (Exception e) {
                }
                return;
            }
            try{
                    Utility.setCurrentUser();
                    TextView t = getView().findViewById(R.id.Txt_displayMessage);
                    t.setText(String.format(currentSingIn, Utility.getCurrentUser().getDisplayName()));
                    Menu menu = getBottomNavigationView().getMenu();
                    getBottomNavigationView().setSelectedItemId(R.id.navigation_risposta);
                }
                catch (Exception err){
                    err.printStackTrace();
                }
                // ...
            } else {
                Log.w(TAG,"unreachable state?");
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        myOnClickListener=null;
    }
}