package pieronegri.RisposteStronze.ui.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;


import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import pieronegri.RisposteStronze.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Credits#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Credits extends BottomFragmentIMPL {
    // TODO: Rename and change types of parameters
    private String TAG= Credits.class.getName();
    public Credits() {
        super(R.layout.fragment_credits);
    }

    private static Credits newInstance(String param1, String param2) {
        return BottomFragmentABS.newInstance(new Credits(), param1, param2);
    }

    static Credits newInstance() {
        return newInstance("", "");
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.Btn_FBPageLibro).setOnClickListener(v -> onClick(v));
        view.findViewById(R.id.Btn_OrderForm).setOnClickListener(v -> onClick(v));
        view.findViewById(R.id.TxtDisegniDi).setOnClickListener(v -> onClick(v));

    }

    private void onClick(View view) {
        String url;
        try {
            _toast(getString(R.string.Txt_PleaseWait));
            switch (view.getId()) {
                case R.id.Btn_OrderForm:
                    openUrl(getString(R.string.UrlOrder));
                    break;
                case R.id.Btn_FBPageLibro:
                    openUrl(getOpenFacebookUrl(getString(R.string.UrlFBPageId)));
                    break;
                case R.id.TxtDisegniDi:
                    openUrl(getOpenFacebookUrl(getString(R.string.UrlFBPageLiseFischerId)));
                    break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            _toast(getString(R.string.Txt_PleaseTryAgain));
        }
    }

    private String getOpenFacebookUrl(String fbId) {
        Intent i;
        String result;
        try {
            result="fb://page/"+fbId;
            getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            i = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
            } catch (Exception e) {
                result="https://www.facebook.com/"+fbId;
                i= new Intent(Intent.ACTION_VIEW,Uri.parse(result));
            }
        return result;
        }

    private void openUrl(String Url) {
        // e.g. if your URL is https://www.facebook.com/EXAMPLE_PAGE, you should put EXAMPLE_PAGE at the end of this URL, after the ?
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(browserIntent);
    }
}
