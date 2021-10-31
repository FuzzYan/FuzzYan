package ru.vkbot;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Data {
    public int test = 0;
    public String gotovo = " ";

    public String find (String group) throws IOException {
        File myFolder = new File("C:\\расписание");
        File [] files = myFolder.listFiles();
        for (int i=0; i< files.length; i++){
            if (SearchGroup(group, files[i].toString())==1){
                gotovo = "группа найдена";
                break;
            }
        }
        if (gotovo.equals("")){
            gotovo="не найдена";
        }
        return gotovo;
    }
    public int SearchGroup(String group, String puti) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(puti);
        Workbook wb = new XSSFWorkbook(fileInputStream);
        for (Row row : wb.getSheetAt(5)){
            for (Cell cell:row) {
                if (getCellText(cell).equals(group)){
                    test = 1;
                    break;
                }
            }
        }
        return test;
    }
    public String getCellText(Cell cell){
        String res="";
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_STRING:
                res = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    res = cell.getDateCellValue().toString() ;

                }
                else {
                    res = Double.toString(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                res = Boolean.toString(cell.getBooleanCellValue()) ;
                break;
            case Cell.CELL_TYPE_FORMULA:
                res = cell.getCellFormula().toString() ;
                break;
            default:
                break;
        }
        return res;
    }
}
