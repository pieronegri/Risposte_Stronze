package pieronegri.rispostestronze.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;


import pieronegri.rispostestronze.R;
import pieronegri.rispostestronze.ui.model.BottomNavigation;
import pieronegri.rispostestronze.utils.Utility;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class BottomFragmentIMPL extends BottomFragmentABS {
    private static final String TAG = BottomFragmentABS.class.getName();
    protected static Integer signFragmentId = R.id.navigation_sign;
    protected static Integer bottomNavigationId = R.id.bottom_navigation;

    BottomNavigationView bottomNavigationView = null;
    private Integer layoutId = null;
    BottomNavigation model=null;

    public BottomFragmentIMPL() {
    }

    BottomFragmentIMPL(@NotNull Integer layoutId) {
        setLayoutId(layoutId);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public BottomFragmentIMPL(@NotNull Integer bottomNavigationId, @NotNull Integer signFragmentId, @NotNull Integer layoutId) {
        signFragmentId = bottomNavigationId;
        bottomNavigationId = signFragmentId;
        setLayoutId(layoutId);
    }

    @Override
    public void onViewCreated(@NotNull @NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        if(model==null) {
            model = new ViewModelProvider(this).get(BottomNavigation.class);
        }
        if (bottomNavigationView == null) {
            try {
                bottomNavigationView = getActivity().findViewById(bottomNavigationId);
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
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        bottomNavigationView=null;
    }

 }
