package pieronegri.rispostestronze.data_source.Firebase;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public abstract class _ViewModel extends ViewModel{

    public abstract FirebaseRemoteConfig updateRemoteConfig();
    public abstract void setMessageMutableLiveData(String value );
    public abstract MutableLiveData<String> getMessageMutableLiveData();
    public static void reLoadModel(RemoteMessage remoteMessage){

    }
}
