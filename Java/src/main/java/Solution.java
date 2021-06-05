import java.io.IOException;


public class Solution {

    public static void main(String[] args) {

        // НАЗВАНИЯ ДЛЯ СЕБЯ ЧТОБЫ НЕ ПОТЕРЯТЬ
        // Files/данные_по_погоде_2008_2013.xlsx
        // Files/Клещи_2010.xls
        // Files/Клещи_2011.xls
        // Files/Клещи_2012.xlsx

//        Parser.createDB(args);

        Parser.analysis();

        // TODO: 03.06.2021 Избавится и считать сразу в джаве!
//        Parser.createForAnalysis();

        // TODO: 05.05.2021 Реализация будет добавлена позже
//        DrawMap drawMap = new DrawMap();
    }
}



