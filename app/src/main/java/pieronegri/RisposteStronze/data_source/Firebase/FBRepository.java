package pieronegri.RisposteStronze.data_source.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class FBRepository/*<Model>*/ {

    //private DatabaseReference databaseReference;
    //private FBRepositoryCallBack/*<Model>*/ firebaseCallback;


    private String TAG = FBRepository.class.getName();
    private String rootNode;
    private FBEventListener/*<Model>*/ listener;

    public FBRepository() {
        Build(FBNodeStructure.Risposta, true, true);
    }

    public FBRepository(@NotNull String rootNode, Boolean enabled, Boolean synced) {
       Build(rootNode,enabled,synced);
    }

    public FBRepository(@NotNull String rootNode) {
        Build(rootNode,true,true);
    }
    private void Build(@NotNull String rootNode, Boolean enabled, Boolean synced) {
        this.rootNode = rootNode;
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(enabled);
            FirebaseDatabase.getInstance().getReference().child(rootNode).keepSynced(synced);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getFirebaseDatabaseReference();
    }

    public void clear(){
        getFirebaseDatabaseReference().removeEventListener(listener);
        //this.firebaseCallback = null;
        this.listener=null;
    }
    public void addListener(FBRepositoryCallBack/*<Model>*/ firebaseCallback) {
        //this.firebaseCallback = firebaseCallback;
        this.listener = new FBEventListener/*<Model>*/(firebaseCallback);
        getFirebaseDatabaseReference().addValueEventListener(listener);
    }

    @NotNull
    public DatabaseReference getFirebaseDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference(rootNode);
    }
}