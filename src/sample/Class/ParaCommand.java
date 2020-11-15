package sample.Class;

public class ParaCommand {
    String paraType;
    String paraData;

    public ParaCommand(String paraType, String paraData) {
        this.paraType = paraType;
        this.paraData = paraData;
    }

    public String getParaType() {
        return paraType;
    }

    public void setParaType(String paraType) {
        this.paraType = paraType;
    }

    public String getParaData() {
        return paraData;
    }

    public void setParaData(String paraData) {
        this.paraData = paraData;
    }
}
