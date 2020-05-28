package pieronegri.RisposteStronze.ui.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;


import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
        model.fetchWelcomeMessage();
        view.findViewById(R.id.Btn_getRandomMessage).setOnClickListener(v -> onCLick(v));
        view.findViewById(R.id.Btn_share).setOnClickListener(v -> onCLick(v));
        TextView textView = getActivity().findViewById(R.id.Txt_displayMessage);
        final Observer<String> WelcomeObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newMessage) {
                DisplayWelcomeMessage(newMessage);
            }
        };
        model.getWelcomeMessageMutableLiveData().observe(getViewLifecycleOwner(), WelcomeObserver);

        final Observer<String> RisposteObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newMessage) {
                DisplayNewRisposta(textView,newMessage);
            }
        };
        model.getMessageMutableLiveData().observe(getViewLifecycleOwner(), RisposteObserver);

        final Observer<String> ColObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newColor) {
                textView.setTextColor(Color.parseColor(newColor));
            }
        };
        model.getColorMutableLiveData().observe(getViewLifecycleOwner(), ColObserver);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProgressBar p= getActivity().findViewById(R.id.ProgressBar_cyclic);
        p.setVisibility(View.INVISIBLE);
    }

    private void DisplayNewRisposta(TextView textView, String message){
        textView.setVisibility(View.VISIBLE);
        textView.setText(message);
        if(message.length()>120){
            textView.setTextSize(20);
        }
        else{
            textView.setTextSize(24);
        }
        getActivity().findViewById(R.id.ProgressBar_cyclic).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.Txt_welcomeMessage).setVisibility(View.GONE);
        //uploadView(model.getRisposta().getName());
    }

    private void DisplayWelcomeMessage(String message){
        TextView t = getActivity().findViewById(R.id.Txt_welcomeMessage);
        t.setText(message);
    }

    private void onCLick(View v) {
        try {
            getActivity().findViewById(R.id.ProgressBar_cyclic).setVisibility(View.VISIBLE);
            if (v.getId() == R.id.Btn_getRandomMessage) {
                GetRandomMessage();
            }
            if(v.getId()==R.id.Btn_share){
                ShareOnFacebook();
            }

        } catch (Exception e) {
            OnClickExceptionHandling(e);
        }
    }

    private void GetRandomMessage() throws Exception {
        //getActivity().findViewById(R.id.Btn_share).setVisibility(View.GONE);
        model.fetchMessage();
        //getModel().fetchMessage();
        //getModel().fetchColor();
    }

    private void ShareOnFacebook() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(model.fetchWebAppLink() + PadRispostaName()))
                    .build();
            shareDialog.show(content);
                    /*
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
                     */
        }
        else{_toast("per qualche motivo non puoi condividere risposte st**nze");}
    }
    private void OnClickExceptionHandling(Exception e){
        e.printStackTrace();
        _toast(getString(R.string.Txt_PleaseTryAgain));
        new FBTransaction(e);
    }

    private String PadRispostaName(){

        try{
            String unpaddedName= model.getRisposta().getName();
            return "000".substring(unpaddedName.length()) + unpaddedName;
        }
        catch(Exception e){
            return "0";
        }
    }

    public void goToNextView(){
        int id;
        try{
            id= Integer.parseInt(model.getRisposta().getName());
        }catch(Exception e){
            id=0;
        }
        try{
            model.fetchMessage(id+1);
        }catch(Exception e){
            id=0;
        }
    }
    public void uploadView(String unpaddedName) {
        try {

            String paddedName = "000".substring(unpaddedName.length()) + unpaddedName;

            Bitmap bitmap  = Utility.loadBitmapFromView(getView());
            File cacheDir = new File(getContext().getCacheDir(), "images");
            cacheDir.mkdirs();
            File shareFile = File.createTempFile("risposta"+paddedName, ".jpg", cacheDir);
            // val shareFile =  File(cacheDir, "image.jpg")
            OutputStream output = new FileOutputStream(shareFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference ref = storageReference.child("images/" + paddedName);
            ref.putFile(Uri.fromFile(shareFile))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        try {
                            goToNextView();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}