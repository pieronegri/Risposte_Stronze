package pieronegri.rispostestronze.ui.controller;

import android.graphics.Color;
import android.os.Bundle;


import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import pieronegri.rispostestronze.R;


public class Risposta extends BottomFragmentIMPL {
    private static final String TAG = Sign.class.getName();
    private android.animation.Animator Animator;

    public Risposta() throws Exception {
        super(R.layout.fragment_risposta);
    }

    private static Risposta newInstance(String param1, String param2) throws Exception {
        return newInstance(new Risposta(), param1, param2);
    }

    static Risposta newInstance() throws Exception {
        return newInstance("", "");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isUserSigned()) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_sign);
        }
        view.findViewById(R.id.Btn_getNewMessage).setOnClickListener(v -> getRisposta(v));
        TextView textView = view.findViewById(R.id.Txt_displayMessage);
        textView.setTextColor(Color.parseColor(model.getTextColor()));
        textView.setText(model.getWelcomeMessage());
        final Observer<String> RisposteObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newMessage) {
                textView.setText(newMessage);
            }
        };
        model.getMessageMutableLiveData().observe(requireActivity(), RisposteObserver);

        final Observer<String> ColObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newColor) {
                textView.setTextColor(Color.parseColor(newColor));
            }
        };
        model.getColorMutableLiveData().observe(requireActivity(), ColObserver);
    }

    private void getRisposta(View v) {
        try {
            getView().findViewById(R.id.ProgressBar_cyclic).setVisibility(View.VISIBLE);
            switch (v.getId()) {
                case R.id.Btn_getNewMessage:
                    model.updateMessageMutableLiveData();
                    model.updateColorMutableLiveData() ;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            getView().findViewById(R.id.ProgressBar_cyclic).setVisibility(View.GONE);
        }
    }

}
