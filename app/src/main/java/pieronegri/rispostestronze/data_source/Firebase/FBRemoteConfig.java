package pieronegri.rispostestronze.data_source.Firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import pieronegri.rispostestronze.R;

public class FBRemoteConfig {
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private OnCompleteListener<Void> listener;

    public FBRemoteConfig(OnCompleteListener<Void> listener) {
        firebaseRemoteConfig=FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());
        firebaseRemoteConfig.getInstance().setDefaults(R.xml.remote_config_defaults);
        setListener(listener);
        setCurrentConfig();
    }
    public FirebaseRemoteConfig getCurrentConfig() {
        return firebaseRemoteConfig;
    }
    public void setListener(OnCompleteListener<Void> listener){
        this.listener =listener;
    }

    public void setCurrentConfig() {
        firebaseRemoteConfig.fetch(0).addOnCompleteListener(listener);
    }

}