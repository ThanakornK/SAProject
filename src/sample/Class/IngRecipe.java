package sample.Class;


public class IngRecipe {
    private String ingName;
    private Double recQuan;

    public IngRecipe(String ingName, Double recQuan){
        this.ingName = ingName;
        this.recQuan = recQuan;
    }

    public String getIngName() {
        return ingName;
    }

    public Double getRecQuan() {
        return recQuan;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public void setRecQuan(Double recQuan) {
        this.recQuan = recQuan;
    }
}
