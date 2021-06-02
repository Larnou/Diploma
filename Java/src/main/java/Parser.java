import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Parser {

    // TODO: 03.06.2021 После выборки с бд эти массивы скорее всего не пригодятся
    public static ArrayList<Bite> gigaArray = new ArrayList<>();
    public static ArrayList<Weather> weatherArray = new ArrayList<>();

    public static int PRIMARY_KEY = 0;


    // ============== //
    // ПОДГОТОВКА БД //
    // ============ //

    public static void createDB(ArrayList<String> listOfFileNames) {

        Connection c;
        Statement stmt;

        String URL = "jdbc:postgresql://localhost:5432/postgres";
        String USER = "postgres";
        String PASSWORD = "3112LarS";

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(URL,USER, PASSWORD);
            c.setAutoCommit(false);
            System.out.println("-- Opened database successfully");
            String sql;

            //-------------- CREATE TABLE ---------------
            stmt = c.createStatement();
            sql = "CREATE TABLE KleshDB " +
                    "(ID INT PRIMARY     KEY     NOT NULL," +
                    " BITEDATE           DATE, " +
                    " INCITY             TEXT, " +
                    " AREA               TEXT, " +
                    " ADMINAREA          TEXT, " +
                    " AVGTEMPERATURE     DECIMAL, " +
                    " AVGHUMIDITY        DECIMAL)";


            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            System.out.println("-- Table created successfully");

            stmt = c.createStatement();

            ArrayList<Weather> whetherList = getWeatherInfo(listOfFileNames.get(0));


            for (int i = 1; i < listOfFileNames.size(); i++) {
                String fileName = listOfFileNames.get(i);
                createDBFromFile(fileName, whetherList, stmt);
            }

            stmt.close();
            c.commit();
            System.out.println("-- Records created successfully");


            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("-- All Operations done successfully");


        // ОЧЕНЬ НУЖНЫЕ КОММЕТАРИИ НЕ УДАЛЯТЬ!!!!!!!
        /*//--------------- INSERT ROWS ---------------

         *//*sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (1, 'Paul', 32, 'California', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) VALUES (41, 'Dima', 22, 'Irkutsk ', 10.0 );";
            stmt.executeUpdate(sql);*//*



            //-------------- UPDATE DATA ------------------
            stmt = c.createStatement();
            sql = "UPDATE COMPANY set SALARY = 25000.00 where ID=1;";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();

            System.out.println("-- Operation UPDATE done successfully");*/


            /*//--------------- SELECT DATA ------------------
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                int age  = rs.getInt("age");
                String  address = rs.getString("address");
                float salary = rs.getFloat("salary");
                System.out.println(String.format("ID=%s NAME=%s AGE=%s ADDRESS=%s SALARY=%s",id,name,age,address,salary));
            }
            rs.close();
            stmt.close();
            c.commit();
            System.out.println("-- Operation SELECT done successfully");*/


            /*//-------------- DELETE DATA ----------------------
            stmt = c.createStatement();
            sql = "DELETE from COMPANY where ID=2;";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            System.out.println("-- Operation DELETE done successfully");*/
    }

    public static void createDBFromFile(String fileName, ArrayList<Weather> whetherList, Statement stmt) throws IOException, SQLException {

        String[] splittedString = fileName.split("\\.");
        String extension = splittedString[splittedString.length - 1];

        String templateForSQL = "INSERT INTO KleshDB (ID, BITEDATE, INCITY,AREA, ADMINAREA, AVGTEMPERATURE, AVGHUMIDITY) VALUES ";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

        switch (extension) {
            case "xls" -> {
                Excel<HSSFWorkbook, HSSFSheet> excel = new Excel<>(
                        new HSSFWorkbook(new FileInputStream(fileName)),
                        new HSSFWorkbook(new FileInputStream(fileName)).getSheetAt(0));

                for (int i = 1; i < excel.getSheet().getLastRowNum() + 1; i++) {

                    HSSFRow row = excel.getSheet().getRow(i);

                    Date callDate = (row.getCell(1) == null) ? new Date(0) : row.getCell(1).getDateCellValue();
                    Date biteDate = (row.getCell(2) == null) ? new Date(0) : row.getCell(2).getDateCellValue();
                    String inCity = (row.getCell(3) == null) ? "" : row.getCell(3).getStringCellValue();
                    String area = (row.getCell(4) == null) ? "" : row.getCell(4).getStringCellValue();
                    String adminArea = (row.getCell(5) == null) ? "" : row.getCell(5).getStringCellValue();
                    String material = (row.getCell(6) == null) ? "" : row.getCell(6).getStringCellValue();
                    String kleshKB = (row.getCell(7) == null) ? "" : row.getCell(7).getStringCellValue();
                    String kleshKE = (row.getCell(8) == null) ? "" : row.getCell(8).getStringCellValue();
                    String antiGen = (row.getCell(9) == null) ? "" : row.getCell(9).getStringCellValue();
                    String typeOfKlesh = (row.getCell(10) == null) ? "" : row.getCell(10).getStringCellValue();
                    String genderOfKlesh = (row.getCell(11) == null) ? "" : row.getCell(11).getStringCellValue();


                    if (biteDate != null && getWeatherInformationByDate(biteDate, whetherList).getHumidity() != 0) {

                        String sql = templateForSQL + String.format("(%s, '%s', '%s', '%s', '%s', %s, %s);",
                                ++PRIMARY_KEY,
                                formater.format(biteDate),
                                inCity,
                                area,
                                adminArea,
                                getWeatherInformationByDate(biteDate, whetherList).getTemperature(),
                                getWeatherInformationByDate(biteDate, whetherList).getHumidity());

                        stmt.executeUpdate(sql);
                    }
                }
                excel.getWorkbook().close();
            }

            case "xlsx" -> {

                Excel<XSSFWorkbook, XSSFSheet> excel = new Excel<>(
                        new XSSFWorkbook(new FileInputStream(fileName)),
                        new XSSFWorkbook(new FileInputStream(fileName)).getSheetAt(0));

                for (int i = 1; i < excel.getSheet().getLastRowNum() + 1; i++) {

                    XSSFRow row = excel.getSheet().getRow(i);

                    Date callDate = (row.getCell(1) == null) ? new Date(0) : row.getCell(1).getDateCellValue();
                    Date biteDate = (row.getCell(2) == null) ? new Date(0) : row.getCell(2).getDateCellValue();
                    String inCity = (row.getCell(3) == null) ? "" : row.getCell(3).getStringCellValue();
                    String area = (row.getCell(4) == null) ? "" : row.getCell(4).getStringCellValue();
                    String adminArea = (row.getCell(5) == null) ? "" : row.getCell(5).getStringCellValue();
                    String material = (row.getCell(6) == null) ? "" : row.getCell(6).getStringCellValue();
                    String kleshKB = (row.getCell(7) == null) ? "" : row.getCell(7).getStringCellValue();
                    String kleshKE = (row.getCell(8) == null) ? "" : row.getCell(8).getStringCellValue();
                    String antiGen = (row.getCell(9) == null) ? "" : row.getCell(9).getStringCellValue();
                    String typeOfKlesh = (row.getCell(10) == null) ? "" : row.getCell(10).getStringCellValue();
                    String genderOfKlesh = (row.getCell(11) == null) ? "" : row.getCell(11).getStringCellValue();

                    if (biteDate != null && getWeatherInformationByDate(biteDate, whetherList).getHumidity() != 0) {

                        String sql = templateForSQL + String.format("(%s, '%s', '%s', '%s', '%s', %s, %s);",
                                ++PRIMARY_KEY,
                                formater.format(biteDate),
                                inCity,
                                area,
                                adminArea,
                                getWeatherInformationByDate(biteDate, whetherList).getTemperature(),
                                getWeatherInformationByDate(biteDate, whetherList).getHumidity());

                        stmt.executeUpdate(sql);
                    }
                }
                excel.getWorkbook().close();
            }
        }
    }

    public static ArrayList<Weather> getWeatherInfo(String fileName) throws IOException {
        ArrayList<Weather> weatherInfo = new ArrayList<>();

        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(fileName));

        for (int i = 0; i < 3; i++) {

            XSSFSheet weatherSheet = myExcelBook.getSheetAt(i);
            Calendar currentDay = new GregorianCalendar(2010 + i, Calendar.JANUARY, 1);

            for (int j = 0; j < 360; j++) {

                Calendar day = null;
                double avgTemperature;
                double avgHumidity;
                ArrayList<Double> temparaturesPerDay = new ArrayList<>();
                ArrayList<Double> humidityPerDay = new ArrayList<>();

                for (int k = 1; k < weatherSheet.getLastRowNum(); k++) {

                    XSSFRow row = weatherSheet.getRow(k);

                    if (row != null) {
                        if (row.getCell(1) != null) {

                            Calendar dayToWrite = new GregorianCalendar();
                            dayToWrite.setTime(row.getCell(1).getDateCellValue());

                            if (    dayToWrite.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) &&
                                    dayToWrite.get(Calendar.MONTH) == currentDay.get(Calendar.MONTH) &&
                                    dayToWrite.get(Calendar.DAY_OF_MONTH) == currentDay.get(Calendar.DAY_OF_MONTH)) {

                                if (row.getCell(8) != null && row.getCell(10) != null) {
                                    if (row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING) {
                                        double temperature = Double.parseDouble(row.getCell(8).getStringCellValue());
                                        temparaturesPerDay.add(temperature);
                                    }
                                    if (row.getCell(8).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        double temperature = row.getCell(8).getNumericCellValue();
                                        temparaturesPerDay.add(temperature);
                                    }
                                    humidityPerDay.add(row.getCell(10).getNumericCellValue());
                                    day = dayToWrite;
                                }
                            }
                        }
                    }
                }

                if (day != null) {

                    double tempSum = 0;
                    for (double el : temparaturesPerDay) {
                        tempSum += el;
                    }

                    double humSum = 0;
                    for (double el : humidityPerDay) {
                        humSum += el;
                    }

                    avgTemperature = tempSum / temparaturesPerDay.size();
                    avgHumidity = humSum / humidityPerDay.size();

                    Weather weather = new Weather(day, avgTemperature, avgHumidity);
                    weatherInfo.add(weather);
                }
                currentDay.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return weatherInfo;
    }


    // =============================== //
    // ВЫВОД ЭКСЕЛЬ ФАЙЛА ДЛЯ АНАЛИЗА //
    // ============================= //

    // TODO: 03.06.2021 Переписать под выборку из базы данных
    public static void createForAnalysis() throws IOException {

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Статистика для анализа");
        Row row = sheet.createRow(0);

        // ======================================//
        // === Записываем названия столбцов === //
        // ====================================//

        Cell callDateC = row.createCell(0);
        callDateC.setCellValue("Дата обращения");

        Cell avgTemperatureC = row.createCell(1);
        avgTemperatureC.setCellValue("Средняя температура");

        Cell avgHumidityC = row.createCell(2);
        avgHumidityC.setCellValue("Средняя влажность воздуха");

        Cell dayOfWeekC = row.createCell(3);
        dayOfWeekC.setCellValue("Если выходной - 1, иначе 0");

        Cell highMonthC = row.createCell(4);
        highMonthC.setCellValue("Пиковый месяц - 1, иначе 0");

        Cell amountC = row.createCell(5);
        amountC.setCellValue("Количество обращений");

        //=======================================================//
        // Заполняем значения таблицы необходимыми нами данными //
        //=====================================================//

        TreeMap<Calendar, Integer> callsStats = getMapWithDataAndAmount();

        int cnt = 0;

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

        for (Map.Entry<Calendar, Integer> entry : callsStats.entrySet()) {

            Weather weatherInfo = getWeatherInfoByDate(entry.getKey().getTime());

            if (weatherInfo.getHumidity() != 0) {

                Row newRow = sheet.createRow(++cnt);

                Cell callDate = newRow.createCell(0);
                callDate.setCellValue(entry.getKey());
                callDate.setCellStyle(dateStyle);

                Cell avgTemperature = newRow.createCell(1);
                avgTemperature.setCellValue(weatherInfo.getTemperature());


                Cell avgHumidity = newRow.createCell(2);
                avgHumidity.setCellValue(weatherInfo.getHumidity());


                Cell dayOfWeek = newRow.createCell(3);
                ArrayList<Integer> pickDaysOfWeek = getHighMonthsOrDays(Calendar.DAY_OF_WEEK);
                boolean isHighDayOfWeek = false;

                for (Integer pickDayOfWeek : pickDaysOfWeek) {
                    if (weatherInfo.getCurrentDay().get(Calendar.DAY_OF_WEEK) == pickDayOfWeek) {
                        isHighDayOfWeek = true;
                    }
                }
                dayOfWeek.setCellValue(isHighDayOfWeek ? 1 : 0);


                Cell highMonth = newRow.createCell(4);
                ArrayList<Integer> pickMonths = getHighMonthsOrDays(Calendar.MONTH);
                boolean isHighMonth = false;

                for (Integer pickMonth : pickMonths) {
                    if (weatherInfo.getCurrentDay().get(Calendar.MONTH) == pickMonth) {
                        isHighMonth = true;
                    }
                }
                highMonth.setCellValue(isHighMonth ? 1 : 0);

                // TODO: 01.06.2021 Если подумать на клещей не влияет день недели, это влияет на активность людей, а те в свою очередь чаще встречаются с клещами


                Cell amount = newRow.createCell(5);
                amount.setCellValue(entry.getValue());
            }
        }

        // Авторазмер ширины столбца для корректного отображения данных ячейки
        for (int i = 0; i < 10 ; i++) {
            sheet.autoSizeColumn(i);
        }

        // Записываем всё в файл
        book.write(new FileOutputStream("Analysis.xlsx"));
        book.close();
    }


    // ======================= //
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ //
    // ===================== //

    public static Weather getWeatherInformationByDate(Date date, ArrayList<Weather> weatherArray) {

        Calendar calendar = new GregorianCalendar();

        if (date != null) {

            calendar.setTime(date);

            for (Weather weather : weatherArray) {
                Calendar currentDay = weather.getCurrentDay();

                if (calendar.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) &&
                        calendar.get(Calendar.MONTH) == currentDay.get(Calendar.MONTH) &&
                        calendar.get(Calendar.DAY_OF_MONTH) == currentDay.get(Calendar.DAY_OF_MONTH)) {
                    return weather;
                }
            }
        }
        return new Weather(calendar, 0, 0);
    }

    // TODO: 03.06.2021 Переписать под выборку из базы данных
    public static TreeMap<Calendar, Integer> getMapWithDataAndAmount() {

        TreeMap<Calendar, Integer> map = new TreeMap<>();

        for (Bite bite : gigaArray) {

            if (bite.getBiteDate() != null) {

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(bite.getBiteDate());

                Calendar day = new GregorianCalendar(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                if (day.get(Calendar.YEAR) >= 2010 && day.get(Calendar.YEAR) <= 2012) {
                    map.put(day, map.getOrDefault(day, 0) + 1);
                }
            }
        }


        return map;
    }

    // TODO: 03.06.2021 Переписать под выборку из базы данных
    public static ArrayList<Integer> getHighMonthsOrDays(int param) {

        Map<Integer, Integer> map = new HashMap<>();
        ArrayList<Integer> pickPeriod = new ArrayList<>();

        for (Bite bite : gigaArray) {
            Calendar calendar = new GregorianCalendar();
            if (bite.getBiteDate() != null) {
                calendar.setTime(bite.getBiteDate());
                map.put(calendar.get(param), map.getOrDefault(calendar.get(param), 0) + 1);
            }
        }

        for (int i = 0; i < 2; i++) {
            int maxValue = 0;
            pickPeriod.add(0);

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                if (entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
                    pickPeriod.set(i, entry.getKey());
                }
            }
            map.remove(pickPeriod.get(i));
        }
        return pickPeriod;
    }


    // =============================== //
    // СТАРЫЕ МЕТОДЫ НА БАЗЕ МАССИВОВ //
    // ============================= //

    public static void create() throws IOException {

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Общая статистика за 2010-2012 года");


        // ======================================//
        // === Записываем названия столбцов === //
        // ====================================//

        Row row = sheet.createRow(0);

        Cell ppC = row.createCell(0);
        ppC.setCellValue("№ П/П");

        Cell callDateC = row.createCell(1);
        callDateC.setCellValue("Дата обращения");

        Cell biteDateC = row.createCell(2);
        biteDateC.setCellValue("Дата укуса");

        Cell inCityC = row.createCell(3);
        inCityC.setCellValue("В черте города");

        Cell areaC = row.createCell(4);
        areaC.setCellValue("Местность");

        Cell adminAreaC = row.createCell(5);
        adminAreaC.setCellValue("Административная территория");

        Cell materialC = row.createCell(6);
        materialC.setCellValue("Материал");

        Cell kleshKBC = row.createCell(7);
        kleshKBC.setCellValue("Клещ КБ");

        Cell kleshKEC = row.createCell(8);
        kleshKEC.setCellValue("Клещ КЭ");

        Cell antiGenC = row.createCell(9);
        antiGenC.setCellValue("Антиген КЭ (сыв)");

        Cell typeOfKleshC = row.createCell(10);
        typeOfKleshC.setCellValue("Вид клеща");

        Cell genderOfKleshC = row.createCell(11);
        genderOfKleshC.setCellValue("Пол клеща");

        Cell avgTemperatureC = row.createCell(12);
        avgTemperatureC.setCellValue("Средняя температура");

        Cell avgHumidityC = row.createCell(13);
        avgHumidityC.setCellValue("Средняя влажность воздуха");

        //=======================================================//
        // Заполняем значения таблицы необходимыми нами данными //
        //=====================================================//

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

        for (int i = 0; i < gigaArray.size(); i++) {

            Row newRow = sheet.createRow(i + 1);


            Cell pp = newRow.createCell(0);
            pp.setCellValue(i + 1);

            Cell callDate = newRow.createCell(1);
            if (gigaArray.get(i).getCallDate() == null) {
                callDate.setCellType(Cell.CELL_TYPE_BLANK);
            } else {
                callDate.setCellValue(gigaArray.get(i).getCallDate());
                callDate.setCellStyle(dateStyle);
            }

            Cell biteDate = newRow.createCell(2);
            Calendar calendarBiteDate = new GregorianCalendar();

            if (gigaArray.get(i).biteDate == null) {
                biteDate.setCellType(Cell.CELL_TYPE_BLANK);
            } else {
                calendarBiteDate.setTime(gigaArray.get(i).biteDate);

                if (calendarBiteDate.get(Calendar.YEAR) == 1970) {
                    biteDate.setCellType(Cell.CELL_TYPE_BLANK);
                } else {
                    biteDate.setCellValue(gigaArray.get(i).getBiteDate());
                    biteDate.setCellStyle(dateStyle);
                }
            }

            Cell inCity = newRow.createCell(3);
            inCity.setCellValue(gigaArray.get(i).getInCity());

            Cell area = newRow.createCell(4);
            area.setCellValue(gigaArray.get(i).getArea());

            Cell adminArea = newRow.createCell(5);
            adminArea.setCellValue(gigaArray.get(i).getAdminArea());

            Cell material = newRow.createCell(6);
            material.setCellValue(gigaArray.get(i).getMaterial());

            Cell kleshKB = newRow.createCell(7);
            kleshKB.setCellValue(gigaArray.get(i).getKleshKB());

            Cell kleshKE = newRow.createCell(8);
            kleshKE.setCellValue(gigaArray.get(i).getKleshKE());

            Cell antiGen = newRow.createCell(9);
            antiGen.setCellValue(gigaArray.get(i).getAntiGen());

            Cell typeOfKlesh = newRow.createCell(10);
            typeOfKlesh.setCellValue(gigaArray.get(i).getTypeOfKlesh());

            Cell genderOfKlesh = newRow.createCell(11);
            genderOfKlesh.setCellValue(gigaArray.get(i).getGenderOfKlesh());

            // =====================
            // добавление погоды
            // ========================

            Weather weatherInfo = getWeatherInfoByDate(gigaArray.get(i).getCallDate());

            Cell avgTemperature = newRow.createCell(12);
            avgTemperature.setCellValue(weatherInfo.getTemperature());

            Cell avgHumidity = newRow.createCell(13);
            avgHumidity.setCellValue(weatherInfo.getHumidity());
        }

        // Авторазмер ширины столбца для корректного отображения данных ячейки
        for (int i = 0; i < 14 ; i++) {
            sheet.autoSizeColumn(i);
        }

        // Записываем всё в файл
        book.write(new FileOutputStream("Stats.xlsx"));
        book.close();
    }

    public static void excelParser(String fileName, String extension) throws IOException {

        ArrayList<Bite> localArray = new ArrayList<>();

        switch (extension) {
            case "xls" -> {
                Excel<HSSFWorkbook, HSSFSheet> excel = new Excel<>(
                        new HSSFWorkbook(new FileInputStream(fileName)),
                        new HSSFWorkbook(new FileInputStream(fileName)).getSheetAt(0));

                for (int i = 1; i < excel.getSheet().getLastRowNum() + 1; i++) {

                    HSSFRow row = excel.getSheet().getRow(i);

                    Date callDate = (row.getCell(1) == null) ? new Date(0) : row.getCell(1).getDateCellValue();
                    Date biteDate = (row.getCell(2) == null) ? new Date(0) : row.getCell(2).getDateCellValue();
                    String inCity = (row.getCell(3) == null) ? "" : row.getCell(3).getStringCellValue();
                    String area = (row.getCell(4) == null) ? "" : row.getCell(4).getStringCellValue();
                    String adminArea = (row.getCell(5) == null) ? "" : row.getCell(5).getStringCellValue();
                    String material = (row.getCell(6) == null) ? "" : row.getCell(6).getStringCellValue();
                    String kleshKB = (row.getCell(7) == null) ? "" : row.getCell(7).getStringCellValue();
                    String kleshKE = (row.getCell(8) == null) ? "" : row.getCell(8).getStringCellValue();
                    String antiGen = (row.getCell(9) == null) ? "" : row.getCell(9).getStringCellValue();
                    String typeOfKlesh = (row.getCell(10) == null) ? "" : row.getCell(10).getStringCellValue();
                    String genderOfKlesh = (row.getCell(11) == null) ? "" : row.getCell(11).getStringCellValue();

                    Bite bite = new Bite(callDate, biteDate, inCity, area, adminArea, material, kleshKB, kleshKE,
                            antiGen, typeOfKlesh, genderOfKlesh);

                    localArray.add(bite);
                }
                excel.getWorkbook().close();
                gigaArray.addAll(localArray);
            }
            case "xlsx" -> {

                Excel<XSSFWorkbook, XSSFSheet> excel = new Excel<>(
                        new XSSFWorkbook(new FileInputStream(fileName)),
                        new XSSFWorkbook(new FileInputStream(fileName)).getSheetAt(0));

                for (int i = 1; i < excel.getSheet().getLastRowNum() + 1; i++) {

                    XSSFRow row = excel.getSheet().getRow(i);

                    Date callDate = (row.getCell(1) == null) ? new Date(0) : row.getCell(1).getDateCellValue();
                    Date biteDate = (row.getCell(2) == null) ? new Date(0) : row.getCell(2).getDateCellValue();
                    String inCity = (row.getCell(3) == null) ? "" : row.getCell(3).getStringCellValue();
                    String area = (row.getCell(4) == null) ? "" : row.getCell(4).getStringCellValue();
                    String adminArea = (row.getCell(5) == null) ? "" : row.getCell(5).getStringCellValue();
                    String material = (row.getCell(6) == null) ? "" : row.getCell(6).getStringCellValue();
                    String kleshKB = (row.getCell(7) == null) ? "" : row.getCell(7).getStringCellValue();
                    String kleshKE = (row.getCell(8) == null) ? "" : row.getCell(8).getStringCellValue();
                    String antiGen = (row.getCell(9) == null) ? "" : row.getCell(9).getStringCellValue();
                    String typeOfKlesh = (row.getCell(10) == null) ? "" : row.getCell(10).getStringCellValue();
                    String genderOfKlesh = (row.getCell(11) == null) ? "" : row.getCell(11).getStringCellValue();

                    Bite bite = new Bite(callDate, biteDate, inCity, area, adminArea, material, kleshKB, kleshKE,
                            antiGen, typeOfKlesh, genderOfKlesh);

                    localArray.add(bite);
                }
                excel.getWorkbook().close();
                gigaArray.addAll(localArray);
            }
        }
    }

    public static void weather(String fileName) throws IOException {

        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(fileName));

        for (int i = 0; i < 3; i++) {

            XSSFSheet weatherSheet = myExcelBook.getSheetAt(i);
            Calendar currentDay = new GregorianCalendar(2010 + i, Calendar.JANUARY, 1);

            for (int j = 0; j < 360; j++) {

                Calendar day = null;
                double avgTemperature;
                double avgHumidity;
                ArrayList<Double> temparaturesPerDay = new ArrayList<>();
                ArrayList<Double> humidityPerDay = new ArrayList<>();

                for (int k = 1; k < weatherSheet.getLastRowNum(); k++) {

                    XSSFRow row = weatherSheet.getRow(k);

                    if (row != null) {
                        if (row.getCell(1) != null) {

                            Calendar dayToWrite = new GregorianCalendar();
                            dayToWrite.setTime(row.getCell(1).getDateCellValue());

                            if (    dayToWrite.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) &&
                                    dayToWrite.get(Calendar.MONTH) == currentDay.get(Calendar.MONTH) &&
                                    dayToWrite.get(Calendar.DAY_OF_MONTH) == currentDay.get(Calendar.DAY_OF_MONTH)) {

                                if (row.getCell(8) != null && row.getCell(10) != null) {
                                    if (row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING) {
                                        double temperature = Double.parseDouble(row.getCell(8).getStringCellValue());
                                        temparaturesPerDay.add(temperature);
                                    }
                                    if (row.getCell(8).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        double temperature = row.getCell(8).getNumericCellValue();
                                        temparaturesPerDay.add(temperature);
                                    }
                                    humidityPerDay.add(row.getCell(10).getNumericCellValue());
                                    day = dayToWrite;
                                }
                            }
                        }
                    }
                }

                if (day != null) {

                    double tempSum = 0;
                    for (double el : temparaturesPerDay) {
                        tempSum += el;
                    }

                    double humSum = 0;
                    for (double el : humidityPerDay) {
                        humSum += el;
                    }

                    avgTemperature = tempSum / temparaturesPerDay.size();
                    avgHumidity = humSum / humidityPerDay.size();

                    Weather weather = new Weather(day, avgTemperature, avgHumidity);
                    weatherArray.add(weather);
                }
                currentDay.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    public static Weather getWeatherInfoByDate(Date date) {

        Calendar calendar = new GregorianCalendar();

        if (date != null) {

            calendar.setTime(date);

            for (Weather weather : weatherArray) {
                Calendar currentDay = weather.getCurrentDay();

                if (calendar.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) &&
                        calendar.get(Calendar.MONTH) == currentDay.get(Calendar.MONTH) &&
                        calendar.get(Calendar.DAY_OF_MONTH) == currentDay.get(Calendar.DAY_OF_MONTH)) {
                    return weather;
                }
            }
        }
        return new Weather(calendar, 0, 0);
    }

    public static void getSetOfLocations() {

        /*TreeMap<String, Integer> map = new TreeMap<>();

        for (Bite bite : gigaArray) {
            map.put(bite.getArea(), map.getOrDefault(bite.getArea(), 0) + 1);
        }


        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            cnt += entry.getValue();
            System.out.println(entry.getKey() + " === " + entry.getValue());
        }


        System.out.println(cnt);*/ // TODO: 01.06.2021 Что тут делать с геоданными их много и надо как-то обработать




    }

}
