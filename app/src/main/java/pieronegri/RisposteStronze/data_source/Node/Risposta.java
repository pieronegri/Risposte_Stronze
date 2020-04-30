package pieronegri.RisposteStronze.data_source.Node;


import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;
import pieronegri.RisposteStronze.data_source.Node.Abstract.NodeAbs;

import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class Risposta extends NodeAbs<String, String> {
    private long count = 5;
    private DataSnapshot dataSnapshot;


    public Risposta() {
        super();
    }

    public Risposta(@NotNull String Name, @NotNull String Value) {
        super(Name, Value);
        setRootNode();
        setPath();
    }

    public Risposta(@NotNull DataSnapshot ds) throws Exception {
        super();
        if (ds.getChildrenCount() < 1) {
            throw new Exception("DataSnapshot can not be null or with no nodes");
        }
        setRootNode();
        setDatasnapshot(ds);
        setCount();
        setName();
        setValue();
        setPath();
    }

    public void setRootNode() {
        rootNode = FBNodeStructure.Risposta;
    }

    private void setDatasnapshot(DataSnapshot ds) {
        dataSnapshot = ds;
    }

    @Override
    public void setPath() {
        Path = String.format(temp3s, getRootNode(), getName(), getTimeStamp());
    }

    public void setName() {
        Random rand = new Random();
        Integer randomInt = rand.nextInt(getIntCount())+1;// random integer in a range between
        // 0 (inclusive) and getIntCount() (inclusive). Therfore add 1
        setName(Integer.toString(randomInt));//risposteId starts from 0
    }

    public void setValue() {
        setValue(dataSnapshot.child(getName()).getValue(String.class));
    }

    public void setCount() throws Exception {
        if(dataSnapshot.getChildrenCount()>0)
            count = dataSnapshot.getChildrenCount();
        else
            throw new Exception("DB risposte is empty");
    }

    public Integer getIntCount() {
        return (int) count;
    }

    public String getValue() {
        return Value;
    }

    public String getName() {
        return Name;
    }

}

