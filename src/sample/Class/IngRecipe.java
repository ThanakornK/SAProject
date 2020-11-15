package sample.Class;


public class IngRecipe {
    private String ingName;
    private Double ingQuan;

    public IngRecipe(String ingName, Double recQuan){
        this.ingName = ingName;
        this.ingQuan = recQuan;
    }

    public String getIngName() {
        return ingName;
    }

    public Double getIngQuan() {
        return ingQuan;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public void setRecQuan(Double recQuan) {
        this.ingQuan = recQuan;
    }
}
