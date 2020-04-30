package pieronegri.RisposteStronze.data_source.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class FBRepository/*<Model>*/ {

    private DatabaseReference databaseReference;
    private FBRepositoryCallBack/*<Model>*/ firebaseCallback;


    private String TAG = FBRepository.class.getName();
    private String rootNode;
    private FBEventListener/*<Model>*/ listener;

    public FBRepository() {
        Build(FBNodeStructure.Risposta, true, true);
    }

    public FBRepository(@NotNull String rootNode, Boolean enabled, Boolean synced) {
       Build(rootNode,enabled,enabled);
    }

    public FBRepository(@NotNull String rootNode) {
        Build(rootNode,true,true);
    }
    public void Build(@NotNull String rootNode, Boolean enabled, Boolean synced) {
        setRootNode(rootNode);
        try {
            setPersistenceEnabled(true);
            setKeepSynced(rootNode, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setFirebaseDatabaseReference();
    }

    public void clear(){
        databaseReference.removeEventListener(listener);
        this.firebaseCallback = null;
        this.listener=null;
    }
    public void addListener(FBRepositoryCallBack/*<Model>*/ firebaseCallback) {
        this.firebaseCallback = firebaseCallback;
        this.listener = new FBEventListener/*<Model>*/(firebaseCallback);
        databaseReference.addValueEventListener(listener);
    }

    public void removeListener() {
        databaseReference.removeEventListener(listener);
    }

    public void setFirebaseDatabaseReference() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference(getRootNode());
    }

    /*public void setMapper(FbMapper mapper){
        this.mapper=mapper;
    }*/

    public void setPersistenceEnabled(boolean enabled) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(enabled);
    }

    public void setKeepSynced(String node, boolean synced) {
        FirebaseDatabase.getInstance().getReference().child(node).keepSynced(synced);
    }

    @NotNull
    public String getRootNode() {
        return rootNode;
    }

    public void setRootNode(String node) {
        this.rootNode = node;
    }

    @NotNull
    public DatabaseReference getFirebaseDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference(getRootNode());
    }

}