import java.io.IOException;
import java.time.Month;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {

//        readExcelFileByName("Files/Клещи_2010.xls");
//        readExcelFileByName("Files/Клещи_2011.xls");
//        readExcelFileByName("Files/Клещи_2012.xlsx");

//        Parser.weather("Files/данные_по_погоде_2008_2013.xlsx");

//        Parser.create();

//        Parser.createForAnalysis();

//        Parser.getSetOfLocations();


        ArrayList<String> listOfFileNames = new ArrayList<>();
        listOfFileNames.add("Files/данные_по_погоде_2008_2013.xlsx");
        listOfFileNames.add("Files/Клещи_2010.xls");
        listOfFileNames.add("Files/Клещи_2011.xls");
        listOfFileNames.add("Files/Клещи_2012.xlsx");

        Parser.createDB(listOfFileNames);





        // TODO: 05.05.2021 Реализация будет добавлена позже
//        DrawMap drawMap = new DrawMap();
    }



    public static void readExcelFileByName(String fileName) throws IOException {

        String[] splittedString = fileName.split("\\.");
        String extension = splittedString[splittedString.length - 1];

        Parser.excelParser(fileName, extension);
    }







    }



