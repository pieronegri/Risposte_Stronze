package pieronegri.RisposteStronze.ui.controller;

import android.graphics.Color;
import android.os.Bundle;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.jetbrains.annotations.NotNull;

import pieronegri.RisposteStronze.R;


public class Risposta extends BottomFragmentIMPL {
    private static final String TAG = Login.class.getName();

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
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getModel().fetchWelcomeMessage();
        view.findViewById(R.id.Btn_getNewMessage).setOnClickListener(v -> getRisposta(v));
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
                textView.setText(newMessage);
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

    private void getRisposta(View v) {
        try {
            getView().findViewById(R.id.ProgressBar_cyclic).setVisibility(View.VISIBLE);
            if (v.getId() == R.id.Btn_getNewMessage) {
                getModel().fetchMessage();
                getModel().fetchColor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            getView().findViewById(R.id.ProgressBar_cyclic).setVisibility(View.GONE);
        }
    }
}