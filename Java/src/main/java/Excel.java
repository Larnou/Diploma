import java.util.Date;

public class Excel<T, S> {

    private T workbook;
    private S sheet;


    public Excel(T workbook, S sheet) {
        this.workbook = workbook;
        this.sheet = sheet;

    }

    public T getWorkbook() {
        return workbook;
    }

    public void setWorkbook(T workbook) {
        this.workbook = workbook;
    }

    public S getSheet() {
        return sheet;
    }

    public void setSheet(S sheet) {
        this.sheet = sheet;
    }



}
