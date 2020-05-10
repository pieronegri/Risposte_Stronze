package pieronegri.RisposteStronze.data_source.Node;

import pieronegri.RisposteStronze.data_source.Firebase.FBNodeStructure;
import pieronegri.RisposteStronze.data_source.Node.Abstract.NodeAbs;
import pieronegri.RisposteStronze.utils.Utility;

public class User extends NodeAbs<String, String> {
    private String DisplayName;
    private String Uid;
    private String Email;

    public User() {
        super(Utility.getCurrentUser().getUid(), Utility.getCurrentUser().getDisplayName());
        setRootNode();
        set_User();
        setPath();
    }

    @Override
    public void setPath() {
        Path = String.format(temp3s, getRootNode(), getUid(), getTimeStamp());
    }

    private void setRootNode() {
        rootNode = FBNodeStructure.User;
    }

    private void set_User() {
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
