package pieronegri.RisposteStronze.ui.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;
import pieronegri.RisposteStronze.data_source.Firebase.FBRemoteConfigCallBack;
import pieronegri.RisposteStronze.data_source.Firebase.FBRemoteConfigListener;
import pieronegri.RisposteStronze.data_source.Firebase.FBRepository;
import pieronegri.RisposteStronze.data_source.Firebase.FBRepositoryCallBack;

class BottomNavigationUtil extends ViewModel {
    MutableLiveData<DataSnapshot> DataSnapshotMutableLiveData = null;
    BottomNavigationUtil(){
        fetchConfig();
        setDataSnapshot();
    }
    void fetchConfig(){
        FirebaseRemoteConfig rc=FirebaseRemoteConfig.getInstance();
        new FBRemoteConfigListener(rc,
            new FBRemoteConfigCallBack() {
                @Override
                public void onSuccess() {
                    rc.activate();
                }
                @Override public void onError(){/*TO-DO*/}
            });
    }

    static MutableLiveData<String> getMutable(MutableLiveData<String> mutable){
        if(mutable==null)
            mutable=new MutableLiveData<String>();
        return mutable;
    }
    void setDataSnapshot() {
        if (DataSnapshotMutableLiveData == null) {
            new FBRepository(FBNodeStructure.Risposta, true, true)
                    .addListener(new FBRepositoryCallBack() {
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            DataSnapshotMutableLiveData = new MutableLiveData<>();
                            DataSnapshotMutableLiveData.setValue(dataSnapshot);
                        }
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
