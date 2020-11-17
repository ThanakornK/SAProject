package sample.Class;

public class RecipeReport {

//    private String menuName;
    private String recNameReport;
    private Double recommend_fqReport;
    private Double total_fqReport;

    public  RecipeReport(String recNameReport, Double recommend_fqReport, Double total_fqReport) {
//        this.menuName = menuName;
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

//    public String getMenuName() {
//        return menuName;
//    }
}
