package sample.Class;

public class RecipeReport {

    private String recNameReport;
    private Double recommend_fqReport;
    private Double total_fqReport;
    private Integer ID;

    public  RecipeReport(String recNameReport, Double recommend_fqReport, Double total_fqReport) {
        this.recNameReport = recNameReport;
        this.recommend_fqReport = recommend_fqReport;
        this.total_fqReport = total_fqReport;
        this.ID = -1;
    }

    public String getRecNameReport() {
        return recNameReport;
    }

    public Double getRecommend_fqReport() {
        return recommend_fqReport;
    }

    public Double getTotal_fqReport() {
        return total_fqReport;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
