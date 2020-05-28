package pieronegri.RisposteStronze.data_source.Firebase;

import android.util.Log;

import pieronegri.RisposteStronze.data_source.Node.Date;
import pieronegri.RisposteStronze.data_source.Node.Risposta;
import pieronegri.RisposteStronze.data_source.Node.User;

import java.util.HashMap;
import java.util.Map;

public class FBTransaction extends FBRepository {

    public static final String TAG = FBTransaction.class.getName();
    private Exception e;
    private Date Date;
    private Risposta Risposta;
    private User User;
    private HashMap<String,String> updateObject = new HashMap<>();

    public FBTransaction(Exception e) {
        super(FBNodeStructure.Error);
        this.e=e;
        push(e);
    }
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
    }

    public void push() {
        try {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(Date.getPath(), this.updateObject);
            childUpdates.put(Risposta.getPath(), this.updateObject);
            getFirebaseDatabaseReference().updateChildren(childUpdates);
            String message = String.format("%1$s %2$s", Risposta.getName(), this.Risposta.getValue());
            Log.w(TAG, message);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    private void push(Exception e) {
        try {
            Map<String, Object> childUpdates = new HashMap<>();
            String path="/"+ System.currentTimeMillis();
            childUpdates.put(path, e.getMessage());
            getFirebaseDatabaseReference().updateChildren(childUpdates);
        } catch (Exception e1) {
            e1.printStackTrace();
            throw e1;
        }
    }

}

