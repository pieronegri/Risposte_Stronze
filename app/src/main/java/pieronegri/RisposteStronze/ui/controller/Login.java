package pieronegri.RisposteStronze.ui.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        super.onViewCreated(view, savedInstanceState);
        View.OnClickListener myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCallBack(v);
            }
        };
        ImageButton signIn = view.findViewById(R.id.signIn);
        signIn.setOnClickListener(myOnClickListener);
        ImageButton signOut = view.findViewById(R.id.signOut);
        signOut.setClickable(true);
        signOut.setOnClickListener(myOnClickListener);
        TextView t = view.findViewById(R.id.Txt_displayMessage);
        try{
            t.setText(String.format(currentSingIn, Utility.getCurrentUser().getDisplayName()));
        }
        catch(Exception e){
            e.printStackTrace();
            t.setText(getString(R.string.Txt_signIn_invitation));
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
           _toast(getString(R.string.logOutMoreThanOnce));
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
            Log.w(TAG,getString(R.string.logOutOKMessage));
            _toast(getString(R.string.logOutOKMessage));
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
                    _toast(getString(R.string.Txt_PleaseTryAgain));
                    response.getError().printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            try{
                 Utility.setCurrentUser();
                 TextView t = getView().findViewById(R.id.Txt_displayMessage);
                 t.setText(String.format(currentSingIn, Utility.getCurrentUser().getDisplayName()));
                _toast(String.format(currentSingIn, Utility.getCurrentUser().getDisplayName()));
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
}