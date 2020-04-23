package pieronegri.rispostestronze.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import pieronegri.rispostestronze.R;
import pieronegri.rispostestronze.ui.model.BottomNavigation;
import pieronegri.rispostestronze.utils.Utility;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class BottomFragmentIMPL extends BottomFragmentABS {
    private static final String TAG = BottomFragmentABS.class.getName();
    protected static Integer signFragmentId = R.id.navigation_sign;
    protected static Integer bottomNavigationId = R.id.bottom_navigation;

    BottomNavigationView bottomNavigationView;
    private Integer layoutId = null;
    BottomNavigation model;

    public BottomFragmentIMPL() {
    }

    BottomFragmentIMPL(@NotNull Integer layoutId) {
        setLayoutId(layoutId);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(BottomNavigation.class);
    }
    public BottomFragmentIMPL(@NotNull Integer bottomNavigationId, @NotNull Integer signFragmentId, @NotNull Integer layoutId) {
        signFragmentId = bottomNavigationId;
        bottomNavigationId = signFragmentId;
        setLayoutId(layoutId);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        bottomNavigationView = getActivity().findViewById(bottomNavigationId);
        if (bottomNavigationView == null) {
            try {
                throw new Exception("I did not find such bottomNavigationId " + bottomNavigationId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(layoutId, container, false);
    }

    @NotNull
    public Integer getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(@NotNull Integer layoutId) {
        this.layoutId = layoutId;
    }

    protected Boolean isUserSigned() {
        return Utility.isUserSigned();
    }


    public void open(Fragment f) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
