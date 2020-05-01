package pieronegri.RisposteStronze.ui.controller;

import androidx.fragment.app.Fragment;

public interface BottomFragmentFactory {
    BottomFragmentIMPL makeNavigation(Integer type) throws Exception;
}
