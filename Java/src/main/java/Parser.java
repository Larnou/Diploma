import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Parser {

    public static ArrayList<Bite> arrayList = new ArrayList<>();

    public static void parse2007(String fileName) throws IOException {

        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(fileName));
        HSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);


        for (int i = 1; i < myExcelSheet.getLastRowNum() + 1; i++) {

            HSSFRow row = myExcelSheet.getRow(i);


            int pp = (int) row.getCell(0).getNumericCellValue();

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

            Bite bite = new Bite(pp, callDate, biteDate, inCity, area, adminArea, material, kleshKB, kleshKE, antiGen, typeOfKlesh, genderOfKlesh);

            arrayList.add(bite);
        }
        myExcelBook.close();
    }



    /*public static void parse2013(String fileName) throws IOException {
        XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(fileName));
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);

        for (int i = 1; i < myExcelSheet.getLastRowNum() + 1; i++) {

            XSSFRow row = myExcelSheet.getRow(i);

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

            pp = (int) row.getCell(0).getNumericCellValue();

            if (row.getCell(1) == null) {
                callDate = new Date(0);
            } else {
                callDate = row.getCell(1).getDateCellValue();
            }

            if (row.getCell(2) == null) {
                biteDate = new Date(0);
            } else {
                biteDate = row.getCell(2).getDateCellValue();
            }

            if (row.getCell(3) == null) {
                inCity = "";
            } else {
                inCity = row.getCell(3).getStringCellValue();
            }

            if (row.getCell(4) == null) {
                area = "";
            } else {
                area = row.getCell(4).getStringCellValue();
            }

            if (row.getCell(5) == null) {
                adminArea = "";
            } else {
                adminArea = row.getCell(5).getStringCellValue();
            }

            if (row.getCell(6) == null) {
                material = "";
            } else {
                material = row.getCell(6).getStringCellValue();
            }

            if (row.getCell(7) == null) {
                kleshKB = "";
            } else {
                kleshKB = row.getCell(7).getStringCellValue();
            }

            if (row.getCell(8) == null) {
                kleshKE = "";
            } else {
                kleshKE = row.getCell(8).getStringCellValue();
            }

            if (row.getCell(9) == null) {
                antiGen = "";
            } else {
                antiGen = row.getCell(9).getStringCellValue();
            }

            if (row.getCell(10) == null) {
                typeOfKlesh = "";
            } else {
                typeOfKlesh = row.getCell(10).getStringCellValue();
            }

            if (row.getCell(11) == null) {
                genderOfKlesh = "";
            } else {
                genderOfKlesh = row.getCell(11).getStringCellValue();
            }


            Bite bite = new Bite(pp, callDate, biteDate, inCity, area, adminArea, material, kleshKB, kleshKE, antiGen, typeOfKlesh, genderOfKlesh);

            arrayList.add(bite);
        }

        myExcelBook.close();
    }*/



    public static void create() throws IOException {

        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Birthdays");

        // Нумерация начинается с нуля
        Row row = sheet.createRow(0);

        // Мы запишем имя и дату в два столбца
        // имя будет String, а дата рождения --- Date,
        // формата dd.mm.yyyy
        Cell name = row.createCell(0);
        name.setCellValue("John");

        Cell birthdate = row.createCell(1);

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
        birthdate.setCellStyle(dateStyle);


        // Нумерация лет начинается с 1900-го
        birthdate.setCellValue(new Date(110, 10, 10));

        // Меняем размер столбца
        sheet.autoSizeColumn(1);

        // Записываем всё в файл
        book.write(new FileOutputStream("test.xls"));
        book.close();


    }

}
