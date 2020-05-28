package pieronegri.RisposteStronze.ui.model;
import androidx.lifecycle.MutableLiveData;

import pieronegri.RisposteStronze.data_source.Firebase.FBTransaction;
import pieronegri.RisposteStronze.data_source.Node.Risposta;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class Model extends BottomNavigationUtil {
    private static Model uniqueInstance;
    private String RispostaTemplate = "%1$s.\n\n%2$s";
    private Integer currentNavigationId = null;
    private MutableLiveData<String> MessageMutableLiveData = null;
    private MutableLiveData<String> ColorMutableLiveData = null;
    private MutableLiveData<String> WelcomeMessageMutableLiveData=null;
    private String OrderBookMessage=null;
    private Risposta r;


    public Model() {
        super();
        fetchConfig();
    }

    public static Model GetInstance(){
        if(uniqueInstance==null){
            uniqueInstance=new Model();
        }
        return uniqueInstance;
    }

    @Override
    public void onCleared(){
        super.onCleared();
        currentNavigationId=null;
        DataSnapshotMutableLiveData = null;
        MessageMutableLiveData = null;
        ColorMutableLiveData= null;
        WelcomeMessageMutableLiveData =null;
    }

    public void fetchWelcomeMessage(){
        fetchConfig();
        getWelcomeMessageMutableLiveData().setValue(FirebaseRemoteConfig.getInstance().getString("welcome_messageRisposta"));
    }
    public void fetchOrderBookMessage() {
        fetchConfig();
        OrderBookMessage=FirebaseRemoteConfig.getInstance().getString("orderBook");
    }

    public void fetchColor(){
        fetchConfig();
        getColorMutableLiveData().setValue(FirebaseRemoteConfig.getInstance().getString("colorText"));
    }

    public String fetchWebAppLink(){
        fetchConfig();
        return FirebaseRemoteConfig.getInstance().getString("webappLink");
    }

    public void fetchMessage() throws Exception {
        setDataSnapshot();
        r = new Risposta(DataSnapshotMutableLiveData.getValue());
        setMessage(r);
    }

    public void fetchMessage(int rName) throws Exception {
        setDataSnapshot();
        r = new Risposta(rName,DataSnapshotMutableLiveData.getValue());
        setMessage(r);
    }

    private void setMessage(Risposta r) throws Exception
    {
        try{
            (new FBTransaction(r.getName(), r.getValue())).push();
            getMessageMutableLiveData().setValue(String.format(RispostaTemplate, r.getName(), r.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    public Risposta getRisposta(){
        return r;
    }

    public MutableLiveData<String> getColorMutableLiveData(){
        ColorMutableLiveData=getMutable(ColorMutableLiveData);
        return ColorMutableLiveData;
    }
    public MutableLiveData<String> getWelcomeMessageMutableLiveData(){
        WelcomeMessageMutableLiveData= getMutable(WelcomeMessageMutableLiveData);
        return WelcomeMessageMutableLiveData;
    }
    public MutableLiveData<String> getMessageMutableLiveData(){
        MessageMutableLiveData=getMutable(MessageMutableLiveData);
        return MessageMutableLiveData;
    }

    public String getOrderBookMessage(){
        fetchOrderBookMessage();
        return OrderBookMessage;
    }

}
