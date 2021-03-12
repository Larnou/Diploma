import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class Solution {

    public static void main(String[] args) throws IOException, InvalidFormatException {

//        Parser.parse2007("test.xls");
        Parser.parse2013("test.xlsx");

        for (int i = 0; i < Parser.arrayList.size(); i++) {
            System.out.println(Parser.arrayList.get(i).toString());
        }
    }
}
