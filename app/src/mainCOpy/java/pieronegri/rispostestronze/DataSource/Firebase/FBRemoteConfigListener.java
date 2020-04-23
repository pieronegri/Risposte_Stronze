package pieronegri.rispostestronze.data_source.Firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FBRemoteConfigListener implements OnCompleteListener<Void> {
    private FirebaseRemoteConfig firebaseRemoteConfig;
    public FBRemoteConfigListener()
    {
        // Create the event notifier and pass ourself to it.

        FBRemoteConfig fb= new FBRemoteConfig(this);
    }
    public FirebaseRemoteConfig getConfig(){
        return firebaseRemoteConfig;
    }
    @Override
    public void onComplete(@NonNull Task<Void> task){}
}
