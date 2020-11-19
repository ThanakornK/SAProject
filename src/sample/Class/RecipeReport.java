package sample.Class;

public class RecipeReport {

    private String recNameReport;
    private Double recommend_fqReport;
    private Double total_fqReport;
    private Double leftOver_fqReport;
    private String note;

    public RecipeReport(String recNameReport, Double recommend_fqReport, Double total_fqReport, Double leftOver_fqReport) {
        this.recNameReport = recNameReport;
        this.recommend_fqReport = recommend_fqReport;
        this.total_fqReport = total_fqReport;
        this.leftOver_fqReport = leftOver_fqReport;
    }

    public RecipeReport(String recNameReport, Double recommend_fqReport, Double total_fqReport) {
        this.recNameReport = recNameReport;
        this.recommend_fqReport = recommend_fqReport;
        this.total_fqReport = total_fqReport;
    }

    public RecipeReport(String recNameReport, Double recommend_fqReport, Double total_fqReport, String note) {
        this.recNameReport = recNameReport;
        this.recommend_fqReport = recommend_fqReport;
        this.total_fqReport = total_fqReport;
        this.note = note;
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

    public Double getLeftOver_fqReport() {
        return leftOver_fqReport;
    }
}
