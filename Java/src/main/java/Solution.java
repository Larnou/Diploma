import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException, InvalidFormatException {


        Parser.parse2007("dataS.xls");
//        Parser.parse2013("data.xlsx");

        /*for (int i = 0; i < Parser.arrayList.size(); i++) {
            System.out.println(Parser.arrayList.get(i).toString());
        }*/

        getStatByMonth(Parser.arrayList);
        System.out.println("____________________");
        getStatByDay(Parser.arrayList);








        // TODO: 04.05.2021 Реализация будет добавлена позже 
//        DrawMap drawMap = new DrawMap();
    }



    public static void getStatByMonth(ArrayList<Bite> arrayList) {
        Map<Integer, Integer> monthStat = new HashMap<>();


        for (int i = 0; i < arrayList.size(); i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(Parser.arrayList.get(i).callDate);


            if (!monthStat.containsKey(calendar.get(Calendar.MONTH))) {
                monthStat.put(calendar.get(Calendar.MONTH), 1);
            } else {
                monthStat.put(   calendar.get(Calendar.MONTH), monthStat.get(calendar.get(Calendar.MONTH)) + 1  );
            }
        }

        for (Map.Entry month: monthStat.entrySet()) {
            System.out.println(month);
        }
    }

    public static void getStatByDay(ArrayList<Bite> arrayList) {
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
    }

    public static void getStatByDayInMonth(ArrayList<Bite> arrayList) {

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
        }




    }


}
