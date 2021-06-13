//import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.*;
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

    public static int PRIMARY_KEY = 0;

    // ============== //
    // ПОДГОТОВКА БД //
    // ============ //

    public static void createDB(String[] listOfFileNames) {

        Connection c;
        Statement stmt;

        ArrayList<Weather> whetherList = getWeatherInfo(listOfFileNames[0]);

        try {

            whetherList.get(0);

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
                        " AVGTEMPERATURE     DECIMAL(5,3), " +
                        " AVGHUMIDITY        DECIMAL(5,3)," +
                        " MATERIAL           TEXT, " +
                        " KLESH_KB           TEXT, " +
                        " KLESH_KE           TEXT, " +
                        " BLOOD_KE           TEXT, " +
                        " PRECIPITATIONS     TEXT);";


                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                System.out.println("-- Table created successfully");

                stmt = c.createStatement();

                for (int i = 1; i < listOfFileNames.length; i++) {
                    String fileName = listOfFileNames[i];
                    System.out.println(createDBFromFile(fileName, whetherList, stmt));
                }

                stmt.close();
                c.commit();
                System.out.println("-- Records created successfully");
                c.close();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.out.println("Внимание! Подключение с базе данных PostgreSQL не произошло. Проверьте данные!");
                System.exit(0);
            }
            System.out.println("-- All Operations done successfully");

        } catch (Exception e) {
            System.out.println("Внимание! Ошибки в именах файлов, таблица не будет создана!");
        }
    }

    public static String createDBFromFile(String fileName, ArrayList<Weather> whetherList, Statement stmt) throws SQLException {

        String[] splittedString = fileName.split("\\.");
        String extension = splittedString[splittedString.length - 1];

        String templateForSQL = "INSERT INTO KleshDB (ID, BITEDATE, INCITY,AREA, ADMINAREA, AVGTEMPERATURE, AVGHUMIDITY, MATERIAL, KLESH_KB, KLESH_KE, BLOOD_KE, PRECIPITATIONS ) VALUES ";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

        switch (extension) {
            case "xls" -> {

                try {
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

                            String sql = templateForSQL + String.format("(%s, '%s', '%s', '%s', '%s', %s, %s, '%s', '%s', '%s', '%s', '%s');",
                                    ++PRIMARY_KEY,
                                    formater.format(biteDate),
                                    inCity,
                                    area,
                                    adminArea,
                                    getWeatherInformationByDate(biteDate, whetherList).getTemperature(),
                                    getWeatherInformationByDate(biteDate, whetherList).getHumidity(),
                                    material,
                                    kleshKB,
                                    kleshKE,
                                    antiGen,
                                    getWeatherInformationByDate(biteDate, whetherList).getPrecipitations());

                            stmt.executeUpdate(sql);
                        }
                    }
                    excel.getWorkbook().close();
                } catch (IOException e) {
                    return "Файл: " + fileName + " не был прочитан! Проверьте, верно ли указано название файла";
                }
            }

            case "xlsx" -> {

                try {
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

                            String sql = templateForSQL + String.format("(%s, '%s', '%s', '%s', '%s', %s, %s, '%s', '%s', '%s', '%s', '%s');",
                                    ++PRIMARY_KEY,
                                    formater.format(biteDate),
                                    inCity,
                                    area,
                                    adminArea,
                                    getWeatherInformationByDate(biteDate, whetherList).getTemperature(),
                                    getWeatherInformationByDate(biteDate, whetherList).getHumidity(),
                                    material,
                                    kleshKB,
                                    kleshKE,
                                    antiGen,
                                    getWeatherInformationByDate(biteDate, whetherList).getPrecipitations());

                            stmt.executeUpdate(sql);
                        }
                    }
                    excel.getWorkbook().close();

                } catch (IOException e) {
                    return "Файл: " + fileName + " не был прочитан! Проверьте, верно ли указано название файла";
                }

            }
        }
        return "Файл: " + fileName + " успешно прочитан!";
    }

    public static ArrayList<Weather> getWeatherInfo(String fileName) {
        ArrayList<Weather> weatherInfo = new ArrayList<>();

        try {
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

                    ArrayList<String> precipitationsList = new ArrayList<>();

                    for (int k = 1; k < weatherSheet.getLastRowNum(); k++) {



                        XSSFRow row = weatherSheet.getRow(k);

                        if (row != null) {
                            if (row.getCell(1) != null) {

                                Calendar dayToWrite = new GregorianCalendar();
                                dayToWrite.setTime(row.getCell(1).getDateCellValue());

                                if (    dayToWrite.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) &&
                                        dayToWrite.get(Calendar.MONTH) == currentDay.get(Calendar.MONTH) &&
                                        dayToWrite.get(Calendar.DAY_OF_MONTH) == currentDay.get(Calendar.DAY_OF_MONTH)) {


                                    if (row.getCell(5) != null) {
                                        precipitationsList.add(row.getCell(5).getStringCellValue());
                                    } else {
                                        precipitationsList.add("ясно");
                                    }


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

                        // ---- тут вызвать метод с погодой
                        String rawPres = getTypeOfPrecipitations(precipitationsList);

                        ArrayList<String> rain = new ArrayList<>();
                        rain.add("слаб. морось");
                        rain.add("в посл. час ливневой дождь{гроза}");
                        rain.add("ливневой дождь");
                        rain.add("{гроза, ливн. осадки}");
                        rain.add("{ливн. осадки}");
                        rain.add("слаб. ливневой дождь");
                        rain.add("дымка{ливн. осадки}");

                        ArrayList<String> snow = new ArrayList<>();
                        snow.add("слаб. снег");
                        snow.add("ледяные иглы{снег}");
                        snow.add("ливневой снег");
                        snow.add("слаб. ливневой снег");

                        ArrayList<String> cleanSky = new ArrayList<>();
                        cleanSky.add("дымка");
                        cleanSky.add("мгла");
                        cleanSky.add("ясно");
                        cleanSky.add("ледяные иглы");

                        ArrayList<String> fog = new ArrayList<>();
                        fog.add("туман");
                        fog.add("переохл. туман");
                        fog.add("туман на расстоянии");
                        fog.add("дымка{туман}");
                        fog.add("дым");
                        fog.add("слаб. ливневой снег{туман}");


                        if (rain.contains(rawPres)) {
                            String precipitation = "дождь";
                            Weather weather = new Weather(day, avgTemperature, avgHumidity, precipitation);
                            weatherInfo.add(weather);
//                            System.out.println("ya " + precipitation + " in this " + rawPres);
                        }

                        if (snow.contains(rawPres)) {
                            String precipitation = "снег";
                            Weather weather = new Weather(day, avgTemperature, avgHumidity, precipitation);
                            weatherInfo.add(weather);
//                            System.out.println("ya " + precipitation + " in this " + rawPres);
                        }

                        if (cleanSky.contains(rawPres)) {
                            String precipitation = "ясно";
                            Weather weather = new Weather(day, avgTemperature, avgHumidity, precipitation);
                            weatherInfo.add(weather);
//                            System.out.println("ya " + precipitation + " in this " + rawPres);
                        }

                        if (fog.contains(rawPres)) {
                            String precipitation = "туман";
                            Weather weather = new Weather(day, avgTemperature, avgHumidity, precipitation);
                            weatherInfo.add(weather);
//                            System.out.println("ya " + precipitation + " in this " + rawPres);
                        }




                    }

                    currentDay.add(Calendar.DAY_OF_YEAR, 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Внимание! Имя файла с погодой введено неправильно, провьте параметры args!");
        }
        return weatherInfo;
    }

    public static String getTypeOfPrecipitations(ArrayList<String> list) {
        String precipitations = "";

        Map<String, Integer> presep = new HashMap<>();

        for (String string : list) {
            presep.put(string, presep.getOrDefault(string, 0) + 1);
        }

        int maxValue = 0;
        for (Map.Entry<String, Integer> entry : presep.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                precipitations = entry.getKey();
            }
        }
        return precipitations;
    }


    // ============== //
    // ВЫВОД АНАЛИЗА //
    // ============ //

    public static void analysis() {

        ArrayList<String> typeOfWeather = new ArrayList<>();
        typeOfWeather.add("любая");
        typeOfWeather.add("дождь");
        typeOfWeather.add("ясно");


        for (String weatherType : typeOfWeather) {

            Connection c;
            Statement stmt;

            String URL = "jdbc:postgresql://localhost:5432/postgres";
            String USER = "postgres";
            String PASSWORD = "3112LarS";

            ArrayList<Analysis> analysesList = new ArrayList<>();

            TreeMap<Calendar, Integer> callsAmount = new TreeMap<>();
            TreeMap<Integer, Integer> daysOfWeekAmount = new TreeMap<>();
            TreeMap<Integer, Integer> monthsAmount = new TreeMap<>();

            int booraleozAmount = 0;
            int enciphalitAmount = 0;
            int BD_SIZE = 0;

            double borKoef;
            double enciKoef;


            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(URL,USER, PASSWORD);
                c.setAutoCommit(false);

                //--------------- SELECT DATA ------------------
                stmt = c.createStatement();

                String SQLDayMonth =     "SELECT * FROM KleshDB WHERE PRECIPITATIONS = '" + weatherType + "';";
                String SQLcalls =        "SELECT * FROM KleshDB WHERE PRECIPITATIONS = '" + weatherType + "';";
                String SQLtempHumid =    "SELECT * FROM KleshDB WHERE PRECIPITATIONS = '" + weatherType + "';";
                String SQLillnessKoefs = "SELECT KLESH_KB, KLESH_KE, BLOOD_KE FROM KleshDB WHERE PRECIPITATIONS = '" + weatherType + "';";

                if (weatherType.equals("любая")) {
                    SQLDayMonth = "SELECT * FROM KleshDB;";
                    SQLcalls =        "SELECT * FROM KleshDB;";
                    SQLtempHumid =    "SELECT * FROM KleshDB;";
                    SQLillnessKoefs = "SELECT KLESH_KB, KLESH_KE, BLOOD_KE FROM KleshDB;";
                }



                ResultSet rs = stmt.executeQuery(SQLDayMonth);
                while ( rs.next() ) {
                    Date date = rs.getDate("bitedate");
                    Calendar day = new GregorianCalendar();
                    day.setTime(date);

                    daysOfWeekAmount.put(day.get(Calendar.DAY_OF_WEEK), daysOfWeekAmount.getOrDefault(day.get(Calendar.DAY_OF_WEEK), 0) + 1);
                    monthsAmount.put(day.get(Calendar.MONTH), monthsAmount.getOrDefault(day.get(Calendar.MONTH), 0) + 1);
                }

                rs.close();
                stmt.close();
                c.commit();

                //--------------- SELECT DATA ------------------
                stmt = c.createStatement();
                rs = stmt.executeQuery(SQLcalls);
                while ( rs.next() ) {
                    Date date = rs.getDate("bitedate");
                    Calendar day = new GregorianCalendar();
                    day.setTime(date);

                    callsAmount.put(day, callsAmount.getOrDefault(day, 0) + 1);
                }
                rs.close();
                stmt.close();
                c.commit();


                for (Map.Entry<Calendar, Integer> entry : callsAmount.entrySet()) {

                    ArrayList<Integer> pickDaysOfWeek = getHighMonthsOrDays(daysOfWeekAmount);
                    ArrayList<Integer> pickMonths = getHighMonthsOrDays(monthsAmount);

                    int dayOfWeek = pickDaysOfWeek.contains(entry.getKey().get(Calendar.DAY_OF_WEEK)) ? 1 : 0;
                    int month = pickMonths.contains(entry.getKey().get(Calendar.MONTH)) ? 1 : 0;

                    Analysis analysis = new Analysis(entry.getKey(), 0, 0 , dayOfWeek, month, entry.getValue());
                    analysesList.add(analysis);
                }


                //--------------- SELECT DATA ------------------
                stmt = c.createStatement();
                rs = stmt.executeQuery(SQLtempHumid);
                while ( rs.next() ) {
                    Date date = rs.getDate("bitedate");
                    Calendar day = new GregorianCalendar();
                    day.setTime(date);

                    for (Analysis analysis : analysesList) {

                        if (day.get(Calendar.DAY_OF_MONTH) == analysis.getDay().get(Calendar.DAY_OF_MONTH) &&
                                day.get(Calendar.MONTH) == analysis.getDay().get(Calendar.MONTH) &&
                                day.get(Calendar.YEAR) == analysis.getDay().get(Calendar.YEAR)) {

                            analysis.setTemperature(rs.getDouble("avgtemperature"));
                            analysis.setHumidity(rs.getDouble("avghumidity"));
                        }
                    }
                }

                rs.close();
                stmt.close();
                c.commit();

                // ----------------------------------------

                stmt = c.createStatement();
                rs = stmt.executeQuery(SQLillnessKoefs);

                int size = 0;
                while ( rs.next() ) {

                    String isBorreleox = rs.getString("KLESH_KB");
                    String isEncipalit = rs.getString("KLESH_KE");
                    String isEncipalitB = rs.getString("BLOOD_KE");
                    size++;

                    if (isBorreleox.equals("") && isEncipalit.equals("") && isEncipalitB.equals("")) {
                        BD_SIZE++;
                    }

                    if (isBorreleox.contains("ПОЛОЖ")) {
                        booraleozAmount++;
                    }

                    if (isEncipalit.contains("ПОЛОЖ") || isEncipalitB.contains("isEncipalitB")) {
                        enciphalitAmount++;
                    }
                }
                BD_SIZE = size - BD_SIZE;



            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                System.out.println("Внимание! Подключение к базе данных не произошло, проверьте данные");
            }

            borKoef = booraleozAmount / (double) BD_SIZE;
            enciKoef = enciphalitAmount / (double) BD_SIZE;


            // ====================================== //
            // Данные подготовлены, обработка данных //
            // ==================================== //

            // ======================================================================================================== //


            double[][] mX = new double[analysesList.size()][5];
            double[][] mXT = new double[5][analysesList.size()];
            double[][] mY = new double[analysesList.size()][1];

            for (int i = 0; i < analysesList.size(); i++) {
                mX[i][0] = mXT[0][i] = 1;
                mX[i][1] = mXT[1][i] = analysesList.get(i).getTemperature();
                mX[i][2] = mXT[2][i] = analysesList.get(i).getHumidity();
                mX[i][3] = mXT[3][i] = analysesList.get(i).getDayOfWeek();
                mX[i][4] = mXT[4][i] = analysesList.get(i).getMonth();

                mY[i][0] = analysesList.get(i).getAmountOfCalls();
            }

            double[][] mE = new double[5][5];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    mE[i][j] = i == j ? 1 : 0;
                }
            }

            double[][] mXTX = matrixMultiplication(mXT, mX);
            double[][] mXTY = matrixMultiplication(mXT, mY);


            RealMatrix A = new Array2DRowRealMatrix(mXTX);

            DecompositionSolver solver = new LUDecomposition(A).getSolver();
            RealMatrix I = new Array2DRowRealMatrix(mE);
            RealMatrix B = solver.solve(I);

            double[][] mXTXRev = B.getData();

            double[][] koefs = matrixMultiplication(mXTXRev, mXTY);

            // ==============================================================================================
            // В работе присутствуют 4 фактора

            double[][] factorInfo = new double[analysesList.size()][20];
            // 0 кол - игрик
            // 1 кол - х1 температура
            // 2 кол - х2 влажность
            // 3 кол - х3 день недели
            // 4 кол - х4 месяц
            // 5 кол - игрик в квадрате
            // 6 кол - х1 температура в квадрате
            // 7 кол - х2 влажность в квадрате
            // 8 кол - х3 день недели в квадрате
            // 9 кол - х4 месяц в квадрате
            //10 кол - игрик на х1
            //11 кол - игрик на х2
            //12 кол - игрик на х3
            //13 кол - игрик на х4
            //14 кол - х1 на х2
            //15 кол - х1 на х3
            //16 кол - х1 на х4
            //17 кол - х2 на х3
            //18 кол - х2 на х4
            //19 кол - х3 на х4

            for (int i = 0; i < analysesList.size() ; i++) {

                factorInfo[i][0] = mY[i][0];
                factorInfo[i][1] = mX[i][1];
                factorInfo[i][2] = mX[i][2];
                factorInfo[i][3] = mX[i][3];
                factorInfo[i][4] = mX[i][4];

                factorInfo[i][5] = mY[i][0] * mY[i][0];
                factorInfo[i][6] = mX[i][1] * mX[i][1];
                factorInfo[i][7] = mX[i][2] * mX[i][2];
                factorInfo[i][8] = mX[i][3] * mX[i][3];
                factorInfo[i][9] = mX[i][4] * mX[i][4];

                factorInfo[i][10] = mY[i][0] * mX[i][1];
                factorInfo[i][11] = mY[i][0] * mX[i][2];
                factorInfo[i][12] = mY[i][0] * mX[i][3];
                factorInfo[i][13] = mY[i][0] * mX[i][4];

                factorInfo[i][14] = mX[i][1] * mX[i][2];
                factorInfo[i][15] = mX[i][1] * mX[i][3];
                factorInfo[i][16] = mX[i][1] * mX[i][4];

                factorInfo[i][17] = mX[i][2] * mX[i][3];
                factorInfo[i][18] = mX[i][2] * mX[i][4];

                factorInfo[i][19] = mX[i][3] * mX[i][4];
            }

            ArrayList<Double> middleValues = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                double sum = 0;
                for (int j = 0; j < analysesList.size(); j++) {
                    sum += factorInfo[j][i];
                }
                double middle = sum / analysesList.size();
                middleValues.add(middle);
            }


            double[][] mKor = new double[5][5];

            mKor[0][0] = mKor[1][1] = mKor[2][2] = mKor[3][3] = mKor[4][4] = 1;

            mKor[0][1] = mKor[1][0] = (middleValues.get(10) - middleValues.get(0) * middleValues.get(1)) /
                    (Math.pow(middleValues.get(5) - middleValues.get(0) * middleValues.get(0), 0.5) *
                            Math.pow(middleValues.get(6) - middleValues.get(1) * middleValues.get(1), 0.5));

            mKor[0][2] = mKor[2][0] = (middleValues.get(11) - middleValues.get(0) * middleValues.get(2)) /
                    (Math.pow(middleValues.get(5) - middleValues.get(0) * middleValues.get(0), 0.5) *
                            Math.pow(middleValues.get(7) - middleValues.get(2) * middleValues.get(2), 0.5));

            mKor[0][3] = mKor[3][0] = (middleValues.get(12) - middleValues.get(0) * middleValues.get(3)) /
                    (Math.pow(middleValues.get(5) - middleValues.get(0) * middleValues.get(0), 0.5) *
                            Math.pow(middleValues.get(8) - middleValues.get(3) * middleValues.get(3), 0.5));

            mKor[0][4] = mKor[4][0] = (middleValues.get(13) - middleValues.get(0) * middleValues.get(4)) /
                    (Math.pow(middleValues.get(5) - middleValues.get(0) * middleValues.get(0), 0.5) *
                            Math.pow(middleValues.get(9) - middleValues.get(4) * middleValues.get(4), 0.5));

            // ===============

            mKor[1][2] = mKor[2][1] = (middleValues.get(14) - middleValues.get(1) * middleValues.get(2)) /
                    (Math.pow(middleValues.get(6) - middleValues.get(1) * middleValues.get(1), 0.5) *
                            Math.pow(middleValues.get(7) - middleValues.get(2) * middleValues.get(2), 0.5));

            mKor[1][3] = mKor[3][1] = (middleValues.get(15) - middleValues.get(1) * middleValues.get(3)) /
                    (Math.pow(middleValues.get(6) - middleValues.get(1) * middleValues.get(1), 0.5) *
                            Math.pow(middleValues.get(8) - middleValues.get(3) * middleValues.get(3), 0.5));

            mKor[1][4] = mKor[4][1] = (middleValues.get(16) - middleValues.get(1) * middleValues.get(4)) /
                    (Math.pow(middleValues.get(6) - middleValues.get(1) * middleValues.get(1), 0.5) *
                            Math.pow(middleValues.get(9) - middleValues.get(4) * middleValues.get(4), 0.5));

            // ===========

            mKor[2][3] = mKor[3][2] = (middleValues.get(17) - middleValues.get(2) * middleValues.get(3)) /
                    (Math.pow(middleValues.get(7) - middleValues.get(2) * middleValues.get(2), 0.5) *
                            Math.pow(middleValues.get(8) - middleValues.get(3) * middleValues.get(3), 0.5));

            mKor[2][4] = mKor[4][2] = (middleValues.get(18) - middleValues.get(2) * middleValues.get(4)) /
                    (Math.pow(middleValues.get(7) - middleValues.get(2) * middleValues.get(2), 0.5) *
                            Math.pow(middleValues.get(9) - middleValues.get(4) * middleValues.get(4), 0.5));

            // =========

            mKor[3][4] = mKor[4][3] = (middleValues.get(19) - middleValues.get(3) * middleValues.get(4)) /
                    (Math.pow(middleValues.get(8) - middleValues.get(3) * middleValues.get(3), 0.5) *
                            Math.pow(middleValues.get(9) - middleValues.get(4) * middleValues.get(4), 0.5));


            double[][] minorKor = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    minorKor[i][j] = mKor[i + 1][j + 1];
                }
            }

            RealMatrix C = new Array2DRowRealMatrix(mKor);
            double determinant = new LUDecomposition(C).getDeterminant();

            RealMatrix D = new Array2DRowRealMatrix(minorKor);
            double minorDeterminant = new LUDecomposition(D).getDeterminant();

            double determinationKoef = Math.pow(1 - determinant / minorDeterminant, 0.5);
            double correctDeterKoef = 1 - (1 - Math.pow(determinationKoef,2)) * ((analysesList.size() - 1) /
                    ((double) (analysesList.size() - 4 - 1)));


            System.out.println();
            System.out.println("Данные представлены для типы погоды: " + weatherType);
            System.out.printf("Точность модели составляет: %.1f %%",correctDeterKoef * 100);
            System.out.println();

            // ----- Вывод итогового уравнения

            String equation = String.format("y = (%.2f + %.2f * x1 - %.2f * x2 + %.2f * x3 + %.2f * x4)",
                    koefs[0][0], koefs[1][0], koefs[2][0]*-1, koefs[3][0], koefs[4][0]);

            String borIlness = String.format("y = (%.2f + %.2f * x1 - %.2f * x2 + %.2f * x3 + %.2f * x4) * %.4f",
                    koefs[0][0], koefs[1][0], koefs[2][0]*-1, koefs[3][0], koefs[4][0], borKoef);

            String enciIlness = String.format("y = (%.2f + %.2f * x1 - %.2f * x2 + %.2f * x3 + %.2f * x4) * %.4f",
                    koefs[0][0], koefs[1][0], koefs[2][0]*-1, koefs[3][0], koefs[4][0], enciKoef);

            System.out.println(equation);
            System.out.println(borIlness);
            System.out.println(enciIlness);
        }
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
        return new Weather(calendar, 0, 0, "");
    }

    public static ArrayList<Integer> getHighMonthsOrDays(Map<Integer, Integer> dict) {

        Map<Integer, Integer> map = new HashMap<>(dict);
        ArrayList<Integer> pickPeriod = new ArrayList<>();


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

    public static double[][] matrixMultiplication(double[][] mOne, double[][] mTwo) {

        int a = mOne.length;
        int b = mTwo[0].length;
        int c = mTwo.length;

        double[][] result = new double[a][b];

        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                for (int k = 0; k < c; k++) {
                    result[i][j] += mOne[i][k] * mTwo[k][j];
                }
            }
        }
        return result;
    }
}
