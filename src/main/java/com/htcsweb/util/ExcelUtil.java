package com.htcsweb.util;


import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import java.util.List;

public class ExcelUtil {

    //readFromFile
    public static List<List<Object>> readFromFile(String fileFullname){

        //fileFullname="/Users/kurt/Documents/apache-tomcat-8.5.27/webapps/ROOT/upload/pipes/pipes.xls";
        File file = new File(fileFullname);
        StringBuffer sb = new StringBuffer();
        try {

            Workbook book = Workbook.getWorkbook(file);
            try{

                Sheet sheet = book.getSheet(0);
                for(int i = 0 ; i < 10 ; i++){
                    for(int j = 0 ; j < 10 ; j++){
                        //第一个参数代表列，第二个参数代表行。(默认起始值都为0)
                        sb.append(sheet.getCell(j, i).getContents()+"\t");
                    }
                    sb.append("\n");
                }
                System.out.println(sb);
            }finally{
                if(book != null){
                    book.close();
                    return null;
                }
            }

        } catch (BiffException e) {
            System.err.println(e+"");

        } catch (IOException e) {
            System.err.println(e+"文件读取错误");
        }

        return null;
    }//end readFromFile

}
