package pieronegri.rispostestronze.data_source.Node.Abstract;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public abstract class NodeAbs<Tn, Tv> {
    protected static final String temp3s = "/%1$s/%2$s/%3$s";
    protected static final String temp2s = "/%1$s/%2$s";
    private static final String dateFormat = "yyyy-MM-dd 'at' HH:mm:ss z";
    protected long timeStamp;
    protected String readableTimeStamp;
    protected String rootNode;
    protected String Path;
    protected Tn Name;
    protected Tv Value;


    public NodeAbs() {
        setTimeStamp();
    }

    public NodeAbs(String node) {
        setTimeStamp();

    }

    public NodeAbs(Tn name, Tv value) {
        setTimeStamp();
        setName(name);
        setValue(value);
    }

    public NodeAbs(String node, Tn name, Tv value) {
        setTimeStamp();
        setRootNode(node);
        setName(name);
        setValue(value);
        setPath();
    }

    protected void setTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        this.timeStamp = System.currentTimeMillis();
        this.readableTimeStamp = formatter.format(timeStamp);
    }

    public abstract void setPath();

    @NotNull
    public Tn getName() {
        return Name;
    }

    public void setName(Tn name) {
        Name = name;
    }

    public Tv getValue() {
        return Value;
    }

    public void setValue(Tv value) {
        Value = value;
    }

    @NotNull
    public String getPath() {
        return Path;
    }

    @NotNull
    public long getTimeStamp() {
        return timeStamp;
    }

    @NotNull
    public String getReadableTimeStamp() {
        return readableTimeStamp;
    }

    @NotNull
    public String getRootNode() {
        return rootNode;
    }

    public void setRootNode(String node) {
        rootNode = node;
    }

}

