package pieronegri.RisposteStronze.data_source.Firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class FBRemoteConfigListener implements OnCompleteListener<Void> {
    private FBRemoteConfigCallBack callBack;
    private FirebaseRemoteConfig remoteConfig;

    public FBRemoteConfigListener(FirebaseRemoteConfig rc, FBRemoteConfigCallBack cb) {
        this.callBack = cb;
        this.remoteConfig = rc;
        new FBRemoteConfig(this);
    }

    public FirebaseRemoteConfig getRemoteConfig() {
        return remoteConfig;
    }

    public FBRemoteConfigCallBack getCallBack() {
        return callBack;
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            callBack.onSuccess();
        } else {
            callBack.onError();
        }
    }
}