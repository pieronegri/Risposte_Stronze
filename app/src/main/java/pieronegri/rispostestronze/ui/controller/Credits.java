package pieronegri.rispostestronze.ui.controller;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;


import androidx.fragment.app.Fragment;

import pieronegri.rispostestronze.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Credits#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Credits extends BottomFragmentIMPL {
    // TODO: Rename and change types of parameters
private String TAG= Credits.class.getName();
    public Credits() throws Exception {
        super(R.layout.fragment_credits);
    }

    private static Credits newInstance(String param1, String param2) throws Exception {
        return BottomFragmentABS.newInstance(new Credits(), param1, param2);
    }

    static Credits newInstance() throws Exception {
        return newInstance("", "");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isUserSigned()) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_sign);
        }
        view.findViewById(R.id.Btn_FBPageLibro).setOnClickListener(v -> onClick(v));
        view.findViewById(R.id.Btn_OrderForm).setOnClickListener(v -> onClick(v));
        view.findViewById(R.id.TxtDisegniDi).setOnClickListener(v -> onClick(v));
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.Btn_OrderForm:
                openUrl(getString(R.string.UrlOrder));
                break;
            case R.id.Btn_FBPageLibro:
                openUrl(getString(R.string.UrlFBPage));
                break;
            case R.id.TxtDisegniDi:
                openUrl(getString(R.string.UrlFBPageLiseFischer));
                break;
        }
    }

    private void openUrl(String Url) {
        // e.g. if your URL is https://www.facebook.com/EXAMPLE_PAGE, you should put EXAMPLE_PAGE at the end of this URL, after the ?
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(browserIntent);
    }

    public void sendMail() {


        String subject = getString(R.string.mailSubject);
        String bodyText = getString(R.string.mailBody);
        String mailto = getString(R.string.mailTo) +
                "&subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(bodyText);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

}
