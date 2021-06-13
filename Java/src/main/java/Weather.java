import java.util.Calendar;

public class Weather {

    Calendar currentDay;
    double temperature;
    double humidity;
    String precipitations;

    public Weather(Calendar currentDay, double temperature, double humidity, String precipitations) {
        this.currentDay = currentDay;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitations = precipitations;
    }


    public String getPrecipitations() {
        return precipitations;
    }

    public Calendar getCurrentDay() {
        return currentDay;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "currentDay=" + currentDay +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipitations='" + precipitations + '\'' +
                '}';
    }
}
