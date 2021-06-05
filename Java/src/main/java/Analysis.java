import java.time.Month;
import java.util.Calendar;

public class Analysis {

    Calendar day;
    double temperature;
    double humidity;
    int dayOfWeek;
    int month;
    int amountOfCalls;

    public Analysis(Calendar day, double temperature, double humidity, int dayOfWeek, int month, int amountOfCalls) {
        this.day = day;
        this.temperature = temperature;
        this.humidity = humidity;
        this.dayOfWeek = dayOfWeek;
        this.month = month;
        this.amountOfCalls = amountOfCalls;
    }

    public Calendar getDay() {
        return day;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getMonth() {
        return month;
    }

    public int getAmountOfCalls() {
        return amountOfCalls;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "Analysis{" + day.get(Calendar.YEAR) + " " + Month.of(day.get(Calendar.MONTH) + 1)  + " " + day.get(Calendar.DAY_OF_MONTH) +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", dayOfWeek=" + dayOfWeek +
                ", month=" + month +
                ", amountOfCalls=" + amountOfCalls +
                '}';
    }
}
