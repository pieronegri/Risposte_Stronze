package pieronegri.rispostestronze.data_source.Node;

import pieronegri.rispostestronze.data_source.Firebase.FBNodeStructure;
import pieronegri.rispostestronze.data_source.Node.Abstract.NodeAbs;
import pieronegri.rispostestronze.utils.Utility;

public class User extends NodeAbs<String, String> {
    protected String DisplayName;
    protected String Uid;
    protected String Email;

    public User() {
        super(Utility.getCurrentUser().getUid(), Utility.getCurrentUser().getDisplayName());
        setRootNode();
        set_User();
        setPath();
    }

    @Override
    public void setPath() {
        Path = String.format(temp3s, getRootNode(), getDisplayName(), getTimeStamp());
    }

    private void setRootNode() {
        rootNode = FBNodeStructure.User;
    }

    public void set_User() {
        this.DisplayName = Utility.getCurrentUser().getDisplayName();
        this.Uid = Utility.getCurrentUser().getUid();
        this.Email = Utility.getCurrentUser().getEmail();
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getEmail() {
        return Email;
    }

    public String getUid() {
        return Uid;
    }

}
