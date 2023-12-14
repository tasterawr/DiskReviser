package org.example.task3;

import dorkbox.peParser.PE;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class NetFunctionsAnalyser {
    private List<String> extensions = List.of("exe", "dll", "acm", "ax",
            "cpl", "drv", "efi", "mui", "ocx", "scr", "sys", "tsp", "mun");
    private List<String> functions = new ArrayList<>();
    private final String FUNCTIONS_LIST_FILENAME = "functions.txt";

    public void startAnalyser(String directory) {
        getFunctionsList();

        Path path = Paths.get(directory);
        searchForFiles(path);
    }

    private  void getFunctionsList() {
        Path p = Paths.get(".", FUNCTIONS_LIST_FILENAME);

        try {
            functions = Arrays.stream(Files.readString(p).split("\n")).toList();
        }catch (IOException e) {
            displayMessage("Ошибка чтения файла PE-функций!");
        }
    }

    private List<String> searchForFiles(Path directory) {
        List<String> filePaths = new ArrayList<>();
        try {
            Files.walk(directory).forEach(f -> {
                if (f == directory){

                } else if (Files.isDirectory(f)){
                    searchForFiles(f);
                } else {
                    checkFunctions(f);
                }
            });
        } catch (IOException e){
            displayMessage("Ошибка чтения: " + directory);
        }

        return filePaths;
    }

    private void checkFunctions(Path filepath) {
        PE pefile = new PE(filepath.toString());
        String info = "";
        if (pefile.isPE()){
            System.out.println(pefile.getInfo());
        }
//        String fileName = filepath.getFileName().toString();
//        int ind = fileName.lastIndexOf('.');
//        if (!extensions.contains(fileName.substring(ind+1))) {
//            return;
//        }
//        List<String> foundFunctions = new ArrayList<>();
//        try{
//            String fileData = Files.readString(filepath, Charset.forName("windows-1251"));
//            functions.forEach(f -> {
//                if (fileData.contains(f)) {
//                    foundFunctions.add(f);
//                }
//            });
//        } catch (IOException e){
//            displayMessage("Ошибка чтения: " + filepath);
//        }
//        displayMessage(filepath.toString());
//        displayMessage(foundFunctions + "\n\n");
    }

    private void displayMessage(String mes) {
        System.out.println(mes);
    }
}
