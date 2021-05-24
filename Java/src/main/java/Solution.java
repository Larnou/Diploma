import java.io.IOException;
import java.time.Month;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {


//

//    Parser.findTemperatureByDate();


//        readExcelFileByName("Files/Клещи_2010.xls");
//    readExcelFileByName("Files/Клещи_2011.xls");
//    readExcelFileByName("Files/Клещи_2012.xlsx");

        Parser.weather("Files/данные_по_погоде_2008_2013.xlsx");


//    Parser.create();
//        getPredictIn2013(Parser.gigaArray);





        // TODO: 05.05.2021 Реализация будет добавлена позже
//        DrawMap drawMap = new DrawMap();
    }

    public static void readExcelFileByName(String fileName) throws IOException {

        String[] splittedString = fileName.split("\\.");
        String extension = splittedString[splittedString.length - 1];

        Parser.excelParser(fileName, extension);
    }




    // === Может пригодится, но вряд ли === //

    public static void getPredictIn2013(ArrayList<Bite> arrayList) {
        Map<Integer, Integer> monthStat = new HashMap<>();

        for (int i = 0; i < arrayList.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            if (Parser.gigaArray.get(i).callDate != null) {
                calendar.setTime(Parser.gigaArray.get(i).callDate);
                if (!monthStat.containsKey(calendar.get(Calendar.MONTH))) {
                    monthStat.put(calendar.get(Calendar.MONTH), 1);
                } else {
                    monthStat.put(   calendar.get(Calendar.MONTH), monthStat.get(calendar.get(Calendar.MONTH)) + 1  );
                }
            }
        }


        int maxValueInMap=(Collections.max(monthStat.values()));
        for (Map.Entry<Integer, Integer> entry : monthStat.entrySet()) {
            if (entry.getValue()==maxValueInMap) {

                int year = 2013;
                int month = entry.getKey();
                Calendar cal = new GregorianCalendar(year, month, 1);
                do {
                    int day = cal.get(Calendar.DAY_OF_WEEK);
                    if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                        System.out.println(cal.get(Calendar.DAY_OF_MONTH) + " " + Month.of(month + 1) + " " + year + " В этот день лучше посидеть дома");
                    }
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                }  while (cal.get(Calendar.MONTH) == month);
            }
        }
    }

    /*public static void getStatByDay(ArrayList<Bite> arrayList) {
        Map<Integer, Integer> dayStat = new HashMap<>();

        for (int i = 0; i < arrayList.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(Parser.arrayList.get(i).callDate);

            if (!dayStat.containsKey(calendar.get(Calendar.DAY_OF_MONTH))) {
                dayStat.put(calendar.get(Calendar.DAY_OF_MONTH), 1);
            } else {
                dayStat.put(   calendar.get(Calendar.DAY_OF_MONTH), dayStat.get(calendar.get(Calendar.DAY_OF_MONTH)) + 1  );
            }
        }

        for (Map.Entry day : dayStat.entrySet()) {
            System.out.println("   " + day);
        }
    }*/

    /*public static void getStatByDayInMonth(ArrayList<Bite> arrayList) {

        // TODO: 04.05.2021 Написать нормальную реализацию вывода по месяцам и дням 
        
        Map<Integer, Integer> monthStat = new HashMap<>();
        Map<Integer, Integer> dayStat = new HashMap<>();


        for (int i = 0; i < arrayList.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(Parser.arrayList.get(i).callDate);


            if (!monthStat.containsKey(calendar.get(Calendar.MONTH))) {
                monthStat.put(calendar.get(Calendar.MONTH), 1);
            } else {
                monthStat.put(   calendar.get(Calendar.MONTH), monthStat.get(calendar.get(Calendar.MONTH)) + 1  );
            }
        }*/




    }
    // =================================== //


