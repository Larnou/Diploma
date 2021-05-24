import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Parser {

    public static ArrayList<Bite> gigaArray = new ArrayList<>();
    public static ArrayList<Weather> weatherArray = new ArrayList<>();

    public static void excelParser(String fileName, String extension) throws IOException {

        ArrayList<Bite> localArray = new ArrayList<>();

        switch (extension) {
            case "xls" -> {
                Excel<HSSFWorkbook, HSSFSheet> excel = new Excel<>(
                        new HSSFWorkbook(new FileInputStream(fileName)),
                        new HSSFWorkbook(new FileInputStream(fileName)).getSheetAt(0)
                );

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

            for (int j = 0; j < 330; j++) {

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

//                System.out.println("Day: " + day.get(Calendar.DAY_OF_MONTH) + " " + (day.get(Calendar.MONTH)+1) + " " + day.get(Calendar.YEAR)
//                        + " Tempa: " + avgTemperature + " Humki: " + avgHumidity);

                    Weather weather = new Weather(day, avgTemperature, avgHumidity);
                    weatherArray.add(weather);
                }
                currentDay.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }


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

            /*Cell weather = newRow.createCell(12);
            weather.setCellValue(findTemperatureByDate(gigaArray.get(i).getCallDate()));*/
        }



        // Авторазмер ширины столбца для корректного отображения данных ячейки
        for (int i = 0; i < 12 ; i++) {
            sheet.autoSizeColumn(i);
        }









        /*Cell callDate = row.createCell(1);
        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
        callDate.setCellStyle(dateStyle);
        // Нумерация лет начинается с 1900-го
        callDate.setCellValue(new Date(98, 11, 01));*/



        // Записываем всё в файл
        book.write(new FileOutputStream("Stats.xlsx"));
        book.close();


    }


}
