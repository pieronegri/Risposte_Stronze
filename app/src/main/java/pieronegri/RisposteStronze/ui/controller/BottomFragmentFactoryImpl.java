package pieronegri.RisposteStronze.ui.controller;
import pieronegri.RisposteStronze.R;

public class BottomFragmentFactoryImpl implements BottomFragmentFactory {
    BottomFragmentFactoryImpl(){
        //Default constructor
    }
    @Override
    public BottomFragmentIMPL makeNavigation(Integer type) throws Exception {
        try {
            switch (type) {
                case R.id.navigation_risposta:
                    return Risposta.newInstance();
                case R.id.navigation_login:
                    return Login.newInstance();
                case R.id.navigation_credits:
                    return Credits.newInstance();
                default: throw new Exception("unmanaged type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}