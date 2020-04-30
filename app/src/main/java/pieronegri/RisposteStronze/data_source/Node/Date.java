package pieronegri.RisposteStronze.data_source.Node;

import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;
import pieronegri.RisposteStronze.data_source.Node.Abstract.NodeAbs;

public class Date extends NodeAbs<Long, String> {

    public Long Name;
    public String Value;

    public Date() {
        setRootNode();
        setName(getTimeStamp());
        setValue(getReadableTimeStamp());
        setPath();
    }

    private void setRootNode() {
        rootNode = FBNodeStructure.Date;
    }

    @Override
    public void setPath() {
        Path = String.format(temp2s, getRootNode(), getTimeStamp());
    }
}
