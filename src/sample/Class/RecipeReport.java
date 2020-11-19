package sample.Class;

public class RecipeReport {

    private String recNameReport;
    private Double recommend_fqReport;
    private Double total_fqReport;
    private String note;

    public RecipeReport(String recNameReport, Double recommend_fqReport, Double total_fqReport, String note) {
        this.recNameReport = recNameReport;
        this.recommend_fqReport = recommend_fqReport;
        this.total_fqReport = total_fqReport;
        this.note = note;
    }

    public  RecipeReport(String recNameReport, Double recommend_fqReport, Double total_fqReport) {
        this.recNameReport = recNameReport;
        this.recommend_fqReport = recommend_fqReport;
        this.total_fqReport = total_fqReport;
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

    public String getNote() {
        return note;
    }
}
