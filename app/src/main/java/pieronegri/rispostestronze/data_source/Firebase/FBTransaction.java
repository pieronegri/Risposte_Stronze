package pieronegri.rispostestronze.data_source.Firebase;

import android.util.Log;

import pieronegri.rispostestronze.data_source.Node.Date;
import pieronegri.rispostestronze.data_source.Node.Risposta;
import pieronegri.rispostestronze.data_source.Node.User;

import java.util.HashMap;
import java.util.Map;

public class FBTransaction extends FBRepository {

    public static final String TAG = FBTransaction.class.getName();
    private pieronegri.rispostestronze.data_source.Node.Date Date;
    private pieronegri.rispostestronze.data_source.Node.Risposta Risposta;
    private pieronegri.rispostestronze.data_source.Node.User User;
    private HashMap<String,String> updateObject = new HashMap<>();

    public FBTransaction(String rName, String rValue) {
        super(FBNodeStructure.Transaction);//rootnode
        this.User = new User();
        this.Risposta = new Risposta(rName, rValue);
        this.Date = new Date();
        setUpdateObject();
    }

    private void setUpdateObject() {

        updateObject.put("DateName", String.valueOf(this.Date.getName()));
        updateObject.put("DateValue", this.Date.getValue());
        updateObject.put("RispostaValue", this.Risposta.getValue());
        updateObject.put("RispostaName", this.Risposta.getName());
        updateObject.put("UserValue", this.User.getValue());
        updateObject.put("UserName", this.User.getName());
        updateObject.put("UserEmail", this.User.getEmail());
    }

    public void push() {
        try {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(Date.getPath(), this.updateObject);
            childUpdates.put(Risposta.getPath(), this.updateObject);
            childUpdates.put(User.getPath(), this.updateObject);
            getFirebaseDatabaseReference().updateChildren(childUpdates);
            String message = String.format("%1$s %2$s %3$s", User.getDisplayName(), Risposta.getName(), this.Risposta.getValue());
            Log.w(TAG, message);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}

