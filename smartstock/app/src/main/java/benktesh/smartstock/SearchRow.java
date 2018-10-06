package benktesh.smartstock;

public class SearchRow {
    private int Id;
    private String Symbol;

    public Double getChange() {
        return Change;
    }

    public void setChange(Double change) {
        Change = change;
    }

    private Double Change;
    private String Detail;

    public SearchRow(int id, String symbol, Double change ,String detail) {
        Id = id;
        Symbol = symbol;
        Change = change;
        Detail = detail;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }
}
