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
import java.time.DayOfWeek;
import java.time.Month;
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

    public static void createDB(String[] listOfFileNames) {

        Connection c;
        Statement stmt;

        ArrayList<Weather> whetherList = getWeatherInfo(listOfFileNames[0]);

        try {

            whetherList.get(0); // TODO: 03.06.2021 Можно ли так проверить на ошибку?

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
                sql = "CREATE TABLE IF NOT EXISTS KleshDB " +
                        "(ID INT PRIMARY     KEY     NOT NULL," +
                        " BITEDATE           DATE, " +
                        " INCITY             TEXT, " +
                        " AREA               TEXT, " +
                        " ADMINAREA          TEXT, " +
                        " AVGTEMPERATURE     DECIMAL(5,3), " +
                        " AVGHUMIDITY        DECIMAL(5,3))";


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

    public static String createDBFromFile(String fileName, ArrayList<Weather> whetherList, Statement stmt) throws SQLException {

        String[] splittedString = fileName.split("\\.");
        String extension = splittedString[splittedString.length - 1];

        String templateForSQL = "INSERT INTO KleshDB (ID, BITEDATE, INCITY,AREA, ADMINAREA, AVGTEMPERATURE, AVGHUMIDITY) VALUES ";
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
        } catch (IOException e) {
            System.out.println("Внимание! Имя файла с погодой введено неправильно, провьте параметры args!");
        }
        return weatherInfo;
    }


    // =============================== //
    // ВЫВОД ЭКСЕЛЬ ФАЙЛА ДЛЯ АНАЛИЗА //
    // ============================= //


    public static void analysis() {

        Connection c;
        Statement stmt;

        String URL = "jdbc:postgresql://localhost:5432/postgres";
        String USER = "postgres";
        String PASSWORD = "3112LarS";

        ArrayList<Analysis> analysesList = new ArrayList<>();

        TreeMap<Calendar, Integer> callsAmount = new TreeMap<>();
        TreeMap<Integer, Integer> daysOfWeekAmount = new TreeMap<>();
        TreeMap<Integer, Integer> monthsAmount = new TreeMap<>();


        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(URL,USER, PASSWORD);
            c.setAutoCommit(false);

            //--------------- SELECT DATA ------------------
            stmt = c.createStatement();
            String sql = "SELECT * FROM KleshDB;";

            ResultSet rs = stmt.executeQuery(sql);
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
            sql = "SELECT * FROM KleshDB;";

            rs = stmt.executeQuery(sql);
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
            sql = "SELECT * FROM KleshDB;";

            rs = stmt.executeQuery(sql);
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

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Внимание! Подключение к базе данных не произошло, проверьте данные");
        }

        // ====================================== //
        // Данные подготовлены, обработка данных //
        // ==================================== //

        // ======================================================================================================== //


        double[][] mX = new double[analysesList.size()][5];
        double[][] mXT = new double[5][analysesList.size()];

        double[][] mY = new double[analysesList.size()][1];

        double[][] mE = new double[5][5];

        for (int i = 0; i < analysesList.size(); i++) {
            mX[i][0] = mXT[0][i] = 1;
            mX[i][1] = mXT[1][i] = analysesList.get(i).getTemperature();
            mX[i][2] = mXT[2][i] = analysesList.get(i).getHumidity();
            mX[i][3] = mXT[3][i] = analysesList.get(i).getDayOfWeek();
            mX[i][4] = mXT[4][i] = analysesList.get(i).getMonth();

            mY[i][0] = analysesList.get(i).getAmountOfCalls();
        }

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


        /*for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 1; j++) {
                System.out.printf("%.3f",koefs[i][j]);
            }
            System.out.println();
        }*/


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

        System.out.printf("Точность модели составляет: %.1f %%",correctDeterKoef * 100);


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


                /*Cell dayOfWeek = newRow.createCell(3);
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
                highMonth.setCellValue(isHighMonth ? 1 : 0);*/

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
