package pieronegri.RisposteStronze.ui.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import pieronegri.RisposteStronze.R;
import pieronegri.RisposteStronze.ui.model.BottomNavigation;
import pieronegri.RisposteStronze.utils.Utility;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

class BottomFragmentIMPL extends BottomFragmentABS {
    private static  String LogTAG = BottomFragmentABS.class.getName();
    private Integer loginFragmentId = R.id.navigation_login;
    private Integer bottomNavigationId = R.id.bottom_navigation;
    private Integer navigationMenuItemId;
    private static BottomNavigationView bottomNavigationView = null;
    private Integer layoutId = null;
    private static BottomNavigation model=null;

    public BottomFragmentIMPL() {
    }

    BottomFragmentIMPL(@NotNull Integer layoutId) {
        setLayoutId(layoutId);
    }

    public BottomFragmentIMPL(@NotNull Integer bottomNavigationId, @NotNull Integer loginFragmentId, @NotNull Integer layoutId) {
        this.loginFragmentId = loginFragmentId;
        this.bottomNavigationId = bottomNavigationId;
        setLayoutId(layoutId);
    }

    @NonNull
    BottomNavigationView getBottomNavigationView(){
        if (bottomNavigationView == null) {
            setBottomNavigationView();
        }
        return bottomNavigationView;
    }
    BottomNavigation getModel(){
        return model;
    }

    @Override
    public void onAttach(@NotNull Context ctx){
        super.onAttach(ctx);

        if(getModel()==null) {
            setModel();
        }
        setBottomNavigationView();
    }
    private void setBottomNavigationView(){
        bottomNavigationView = requireActivity().findViewById(bottomNavigationId);
    }

    private void setModel(){
        model = new ViewModelProvider(requireActivity()).get(BottomNavigation.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onViewCreated(@NotNull @NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
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

    protected void goToNavigationItem(Integer itemId){
        getBottomNavigationView().setSelectedItemId(itemId);
    }

    void _toast(String message){
        Utility._toast(getContext(),message);
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        bottomNavigationView=null;
        bottomNavigationId=null;
        loginFragmentId=null;
    }

 }
