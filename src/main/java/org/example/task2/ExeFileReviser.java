package org.example.task2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//comment
public class ExeFileReviser {
    private String hash = "";
    private String HASH_FILE_NAME = "hash.txt";
    private String SECRET_WORD = "secret";
    private String EXE_NAME = "DiskReviser.exe";

    public void startExeFileReviser() {
        //comment
        System.out.println("Начало исполнения...");
        Path path = Paths.get(".", HASH_FILE_NAME);

        try {
            String fileData = Files.readString(path);
            // Ищем волшебное слово, если обнаружили, создаем хеш для исполняемого файла
            if (fileData.equals(SECRET_WORD)) {
                System.out.println("Обнаружено волшебное слово, начинается вычисление хеш-суммы.");
                String hashFromFile = makeHashForFile(new File(".\\" + EXE_NAME));
                if (hashFromFile == null) {
                    return;
                }
                //comment
                Files.write(path, hashFromFile.getBytes());
                System.out.println("Вычисление хеш-суммы завершено. Для проверки целостности исполняемого файла перезапустите программу.");
            } else {
                // Если обнаружили хеш сумму, пересчитываем ее заново для сравнения
                System.out.println("Обнаружена хеш-сумма, начинается вычисление текущей хеш-суммы.");
                hash = fileData;
                String hashFromFile = makeHashForFile(new File(".\\" + EXE_NAME));
                if (hashFromFile == null) {
                    return;
                }
                //comment
                System.out.println("Вычисление хеш-суммы завершено.");

                displayResult(compareHashes(hashFromFile));
            }
        } catch (IOException e) {
            displayMessage("Ошибка: Файл hash.txt не найден!");
        }
    }

    private boolean compareHashes(String hashFromFile) {
        return hash.equals(hashFromFile);
    }

    // Вывод сообщения о результате
    private void displayResult(boolean result) {
        //comment
        if (result) {
            displayMessage("Исполняемый файл не был изменен.");
        } else {
            displayMessage("Исполняемый файл был изменен.");
        }
    }

    private String makeHashForFile(File file) {
        try{
            //comment
            FileInputStream stream = new FileInputStream(file);

            int b1;
            int b2;
            String[] resultHashSum = null;
            String[] binaryStringToAdd;
            // Подсчитываем XOR
            while ((b1 = stream.read()) != -1){
                b2 = stream.read();
                if (resultHashSum == null) {
                    resultHashSum = bytesToBinary(b1, b2).split("");
                } else {
                    //comment
                    binaryStringToAdd = bytesToBinary(b1, b2).split("");

                    // Применяем XOR для каждой пары двоичных строк
                    for (int i = 0; i < resultHashSum.length; i++) {
                        resultHashSum[i] = xor(resultHashSum[i], binaryStringToAdd[i]);
                    }
                }
            }
            //comment
            if (resultHashSum != null) {
                return String.join("", resultHashSum);
            }
        }
        //comment
        catch (FileNotFoundException e) {
            displayMessage("Ошибка: FileInputStream exception."); // Если не был обнаружен файл
        } catch (IOException e) {
            displayMessage("Ошибка: Read exception."); // Ошибка чтения
        }
        //comment
        return null;
    }

    private void displayMessage(String mes) {
        //comment
        System.out.println(mes);
    }

    private String bytesToBinary(int b1, int b2) {
        // Переводим в двоичную строку
        String binaryString1 = Integer.toBinaryString(b1);
        String result;

        int diff = 8 - binaryString1.length();
        result = "0".repeat(diff) + binaryString1;
        //comment
        if (b2 == -1) {
            return result + "0".repeat(8);
        }
        //comment
        String binaryString2 = Integer.toBinaryString(b2);
        diff = 8 - binaryString2.length();
        return result + "0".repeat(diff) + binaryString2;
    }

    // Функция для вычисления XOR двух строковых значений
    private String xor(String s1, String s2){
        boolean op1 = s1.equals("1");
        boolean op2 = s2.equals("1");
        return op1 ^ op2 ? "1" : "0";
    }
}
