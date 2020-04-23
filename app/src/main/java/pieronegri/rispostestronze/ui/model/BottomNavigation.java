package pieronegri.rispostestronze.ui.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import pieronegri.rispostestronze.data_source.Firebase.FBNodeStructure;
import pieronegri.rispostestronze.data_source.Firebase.FBRepository;
import pieronegri.rispostestronze.data_source.Firebase.FBTransaction;
import pieronegri.rispostestronze.data_source.Firebase._ViewModel;
import pieronegri.rispostestronze.data_source.Node.Risposta;
import pieronegri.rispostestronze.data_source.Firebase.FBRepositoryCallback;
import pieronegri.rispostestronze.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.List;


public class BottomNavigation extends _ViewModel {
    private FirebaseDatabase database;
    private String RispostaTemplate = "%1$s.\n\n%2$s";
    private String child = FBNodeStructure.Risposta;
    private String TAG = BottomNavigation.class.getName();
    private MutableLiveData<DataSnapshot> DataSnapshotMutableLiveData = null;
    private MutableLiveData<List<Risposta>> _RispostaMutableLiveData;
    private FBRepository repository;
    private DataSnapshot myDataSnapshot;
    private MutableLiveData<String> MessageMutableLiveData = null;
    private MutableLiveData<String> ColorMutableLiveData = null;
    private FirebaseRemoteConfig remoteConfig;
    private String  welcomeMessage;
    private FBRepositoryCallback rispostaFBRepositoryCallback;

    public BottomNavigation() {
        this.repository = new FBRepository<Risposta>(FBNodeStructure.Risposta, true, true);
        getDBRisposte();
        updateRemoteConfig();
    }

    private LiveData<List<Risposta>> getDBRisposte() {
        if (DataSnapshotMutableLiveData == null) {
            DataSnapshotMutableLiveData = new MutableLiveData<>();
            rispostaFBRepositoryCallback = new FBRepositoryCallback() {
                public void onSuccess(DataSnapshot dataSnapshot) {
                    DataSnapshotMutableLiveData.setValue(dataSnapshot);
                }
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };
            repository.addListener(rispostaFBRepositoryCallback);
        }
        return _RispostaMutableLiveData;
    }

    public MutableLiveData<String> getMessageMutableLiveData() {
        if (MessageMutableLiveData == null)
            MessageMutableLiveData = new MutableLiveData<String>();
        return MessageMutableLiveData;
    }

    @Override
    public FirebaseRemoteConfig updateRemoteConfig(){
        getRemoteConfig();
        setRemoteconfig();
        return remoteConfig;
    }

    private FirebaseRemoteConfig getRemoteConfig(){
        if(remoteConfig ==null){
            remoteConfig =FirebaseRemoteConfig.getInstance();
            remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(true)
                    .build());
            remoteConfig.getInstance().setDefaults(R.xml.remote_config_defaults);
        }
        return remoteConfig;
    }

    private void setRemoteconfig(){
        remoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task){
                if(task.isSuccessful()){
                    remoteConfig.activateFetched();
                    setColorMutableLiveData();
                }
            }});
    }

    public MutableLiveData<String> getColorMutableLiveData(){
        if (ColorMutableLiveData == null)
            ColorMutableLiveData = new MutableLiveData<String>();
        return ColorMutableLiveData;
    }

    public String getWelcomeMessage() {
        return FirebaseRemoteConfig.getInstance().getString("welcome_messageRisposta");
    }

    public void updateColorMutableLiveData() {
        updateRemoteConfig();
    }

    private void setColorMutableLiveData() {
        ColorMutableLiveData = getColorMutableLiveData();
        ColorMutableLiveData.setValue(FirebaseRemoteConfig.getInstance().getString("colorText"));
    }
    public String getTextColor() {
        return FirebaseRemoteConfig.getInstance().getString("colorText");
    }

    @Override
    public void setMessageMutableLiveData(String value ) {
        MessageMutableLiveData = getMessageMutableLiveData();
        MessageMutableLiveData.setValue(value);
    }

    public String updateMessageMutableLiveData() throws Exception {
        if (DataSnapshotMutableLiveData == null) {
            getDBRisposte();
        }
        MessageMutableLiveData = getMessageMutableLiveData();
        try {
            Risposta r = new Risposta(DataSnapshotMutableLiveData.getValue());
            (new FBTransaction(r.getName(), r.getValue())
            ).push();
            MessageMutableLiveData.setValue(String.format(RispostaTemplate, r.getName(), r.getValue()));
            return MessageMutableLiveData.getValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
