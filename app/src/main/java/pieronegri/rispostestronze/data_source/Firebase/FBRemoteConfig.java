package pieronegri.rispostestronze.data_source.Firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import pieronegri.rispostestronze.R;

public class FBRemoteConfig {
    private FirebaseRemoteConfig remoteConfig;
    private OnCompleteListener<Void> listener;

    public FBRemoteConfig(OnCompleteListener<Void> listener) {
        remoteConfig =FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfig.getInstance().setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds (10)
                .build());
        FirebaseRemoteConfig.getInstance().setDefaultsAsync(R.xml.remote_config_defaults);
        setListener(listener);
        setCurrentConfig();
    }
    public FirebaseRemoteConfig getCurrentConfig() {
        return remoteConfig;
    }
    private void setListener(OnCompleteListener<Void> listener){
        this.listener =listener;
    }

    protected void setCurrentConfig() {
        remoteConfig.fetch(0).addOnCompleteListener(listener);
    }

}