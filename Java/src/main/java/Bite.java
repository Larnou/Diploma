import java.util.Date;

public class Bite {

    Date callDate;
    Date biteDate;
    String inCity;
    String area;
    String adminArea;
    String material;
    String kleshKB;
    String kleshKE;
    String antiGen;
    String typeOfKlesh;
    String genderOfKlesh;


    public Date getCallDate() {
        return callDate;
    }

    public Date getBiteDate() {
        return biteDate;
    }

    public String getInCity() {
        return inCity;
    }

    public String getArea() {
        return area;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public String getMaterial() {
        return material;
    }

    public String getKleshKB() {
        return kleshKB;
    }

    public String getKleshKE() {
        return kleshKE;
    }

    public String getAntiGen() {
        return antiGen;
    }

    public String getTypeOfKlesh() {
        return typeOfKlesh;
    }

    public String getGenderOfKlesh() {
        return genderOfKlesh;
    }

    public Bite(Date callDate, Date biteDate, String inCity, String area, String adminArea,
                String material, String kleshKB, String kleshKE, String antiGen, String typeOfKlesh, String genderOfKlesh) {
        this.callDate = callDate;
        this.biteDate = biteDate;
        this.inCity = inCity;
        this.area = area;
        this.adminArea = adminArea;
        this.material = material;
        this.kleshKB = kleshKB;
        this.kleshKE = kleshKE;
        this.antiGen = antiGen;
        this.typeOfKlesh = typeOfKlesh;
        this.genderOfKlesh = genderOfKlesh;
    }

    @Override
    public String toString() {
        return "Bite{" +
                "callDate=" + callDate +
                ", biteDate=" + biteDate +
                ", inCity='" + inCity + '\'' +
                ", area='" + area + '\'' +
                ", adminArea='" + adminArea + '\'' +
                ", material='" + material + '\'' +
                ", kleshKB='" + kleshKB + '\'' +
                ", kleshKE='" + kleshKE + '\'' +
                ", antiGen='" + antiGen + '\'' +
                ", typeOfKlesh='" + typeOfKlesh + '\'' +
                ", genderOfKlesh='" + genderOfKlesh + '\'' +
                '}';
    }
}
