package sample.Class;


public class IngRecipe {
    private String ingName;
    private String recName;
    private double totalCost;
    private double ingQuan;

    public IngRecipe(String ingName, String recName, double recQuan){
        this.ingName = ingName;
        this.recName = recName;
        this.ingQuan = recQuan;
//        this.totalCost = ingPrice*ingQuan;
    }

    public String getIngName() {
        return ingName;
    }

    public Double getIngQuan() {
        return ingQuan;
    }

    public double getIngPrice() {
        return totalCost;
    }

    public void setIngName(String ingName) {
        this.ingName = ingName;
    }

    public void setRecQuan(Double recQuan) {
        this.ingQuan = recQuan;
    }

    public void setTotalCost(double ingPrice) {
        this.totalCost = this.ingQuan*ingPrice;
    }
}
