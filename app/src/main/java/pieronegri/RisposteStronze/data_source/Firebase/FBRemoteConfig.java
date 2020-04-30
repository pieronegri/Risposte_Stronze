package pieronegri.RisposteStronze.data_source.Firebase;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import pieronegri.RisposteStronze.R;

public class FBRemoteConfig  {
    public FBRemoteConfig(FBRemoteConfigListener listener){
        /*setDefaultConfig();
        setListener(callBack);
        setActiveConfig();*/
        listener.getRemoteConfig().setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build());
        listener.getRemoteConfig().setDefaultsAsync(R.xml.remote_config_defaults);
        listener.getRemoteConfig().fetch().addOnCompleteListener(listener);
    }
/*
    private void setListener(FBRemoteConfigCallBack callBack){
        this.callBack = callBack;
        listener=new FBRemoteListener(mFirebaseRemoteConfig,callBack);
    }

    private void setDefaultConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build());
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
    }

    private void setActiveConfig(){
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(listener);
    }
*/
}