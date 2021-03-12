import java.util.Date;

public class Bite {

    int pp;
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

    public Bite(int pp, Date callDate, Date biteDate, String inCity, String area, String adminArea,
                String material, String kleshKB, String kleshKE, String antiGen, String typeOfKlesh, String genderOfKlesh) {
        this.pp = pp;
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
                "pp=" + pp +
                ", callDate=" + callDate +
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
