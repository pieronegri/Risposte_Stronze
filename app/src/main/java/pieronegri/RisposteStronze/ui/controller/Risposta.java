package pieronegri.RisposteStronze.ui.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;


import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.jetbrains.annotations.NotNull;

import pieronegri.RisposteStronze.R;
import pieronegri.RisposteStronze.data_source.Firebase.FBTransaction;
import pieronegri.RisposteStronze.utils.Utility;


public class Risposta extends BottomFragmentIMPL {
    private static final String TAG = Login.class.getName();
    private View myView;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    public Risposta()  {
        super(R.layout.fragment_risposta);
    }

    private static Risposta newInstance(String param1, String param2)  {
        return newInstance(new Risposta(), param1, param2);
    }

    static Risposta newInstance()  {
        return newInstance("", "");
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        // shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() { ... });
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getModel().fetchWelcomeMessage();
        view.findViewById(R.id.Btn_getNewMessage).setOnClickListener(v -> onCLick(v));
        view.findViewById(R.id.Btn_share).setOnClickListener(v -> onCLick(v));
        TextView textView = view.findViewById(R.id.Txt_displayMessage);
        final Observer<String> WelcomeObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newMessage) {
                TextView t = view.findViewById(R.id.Txt_welcomeMessage);
                t.setText(newMessage);
            }
        };
        getModel().getWelcomeMessageMutableLiveData().observe(getViewLifecycleOwner(), WelcomeObserver);

        final Observer<String> RisposteObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newMessage) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(newMessage);
                if(newMessage.length()>120){
                    textView.setTextSize(20);
                }
                else{
                    textView.setTextSize(24);
                }
                view.findViewById(R.id.Txt_welcomeMessage).setVisibility(View.GONE);
            }
        };
        getModel().getMessageMutableLiveData().observe(getViewLifecycleOwner(), RisposteObserver);

        final Observer<String> ColObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newColor) {
                textView.setTextColor(Color.parseColor(newColor));
            }
        };
        getModel().getColorMutableLiveData().observe(getViewLifecycleOwner(), ColObserver);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProgressBar p= getActivity().findViewById(R.id.ProgressBar_cyclic);
        p.setVisibility(View.INVISIBLE);
    }

    private void onCLick(View v) {

        try {
            if (v.getId() == R.id.Btn_getNewMessage) {
                getModel().fetchMessage();
                getModel().fetchColor();
            }
            if(v.getId()==R.id.Btn_share){
                if ( ShareDialog.canShow(SharePhotoContent.class)) {
                    ProgressBar p= getActivity().findViewById(R.id.ProgressBar_cyclic);
                    p.setVisibility(View.VISIBLE);
                    Bitmap image = Utility.loadBitmapFromView(getView());
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(image)
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(content);
                }
                else{_toast("per qualche motivo non puoi condividere risposte st**nze");}
            }
        } catch (Exception e) {
            e.printStackTrace();
            _toast(getString(R.string.Txt_PleaseTryAgain));
            new FBTransaction(e);
        }
    }
}