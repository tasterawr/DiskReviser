package org.example.task1;

import java.io.*;
import java.util.*;

public class DiskReviser {
    private Map<String, String> hashes = new HashMap<>();
    private String HASH_FILE_NAME = "hashes.txt";

    public void startDiskReviser(File directory, String mode) {
        try{
            if (mode.equals("hash")) {
                hash(directory);
            } else if (mode.equals("revise")) {
                revise(directory);
            }
        }catch (FileNotFoundException e) {
            System.out.println("Отсутствует файл хэш-сумм! Запустите программу в режиме 'hash' для формирования файла.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void hash(File directory) throws IOException {
        // Читаем файлы и подсчитываем хеши
        List<String> result = searchForFiles(directory);
        if (result == null) {
            return;
        }

        // Записываем хеш в файл хеш-сумм
        FileOutputStream fos = new FileOutputStream(directory.getPath() + "\\" + HASH_FILE_NAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(hashes);
        displayMessage("Файл хэш-сумм успешно сгенерирован. Обработано " + hashes.size() + " файлов.");
        oos.close();
    }

    private void revise(File directory) throws IOException, ClassNotFoundException {
        Map<String, String> hashesFromFile;
        FileInputStream fis = new FileInputStream(directory.getPath() + "\\" + HASH_FILE_NAME);
        ObjectInputStream ois = new ObjectInputStream(fis);
        // Читаем файлы и подсчитываем хеши
        List<String> result = searchForFiles(directory);
        if (result == null) {
            return;
        }

        // Читаем подсчитанные ранее хеши из файла хеш-сумм и сравниваем с новыми
        hashesFromFile = (Map<String, String>) ois.readObject();
        ois.close();

        List<String> changedFiles = compareHashes(hashesFromFile);
        displayChangedFiles(changedFiles);
    }

    private List<String> compareHashes(Map<String, String> hashesFromFile) {
        List<String> result = new ArrayList<>();
        for (String key : hashesFromFile.keySet()) {
            // Если новый хеш не совпадает с предыдущим, сохраняем название измененного файла
            if (!hashesFromFile.get(key).equals(hashes.get(key))) {
                result.add(key);
            }
        }

        return result;
    }

    private void displayChangedFiles(List<String> changedFiles) {
        if (changedFiles.size() == 0) {
            displayMessage("Файлы в указанном каталоге не были изменены.");
        } else {
            displayMessage("Следующие файлы были изменены:");
            for (String fileName : changedFiles){
                displayMessage(fileName);
            }
        }
    }

    private List<String> searchForFiles(File directory){
        List<String> filePaths = new ArrayList<>();
        File[] files = directory.listFiles();

        for (File file : files){
            if (file.isDirectory()){
                //comment
                List<String> subDirFilePaths = searchForFiles(file); // если файл является каталогом, рекурсивно просматриваем его
                //comment

                for (String filePath : subDirFilePaths){
                    filePaths.add(filePath);
                }
                //comment

            }
            else{
                if (file.getName().equals(HASH_FILE_NAME)) { // если файл является файлом хеш-сумм, пропускаем его
                    continue;
                }
                //comment
                //comment

                String hash = makeHashForFile(file);
                if (hash == null) {
                    return null;
                }
                String filepath = directory.getPath() + "\\" + file.getName();
                hashes.put(filepath, hash);
                filePaths.add(filepath);
            }
        }
        //comment
        return filePaths;
    }

    private String makeHashForFile(File file) {
        try{
            //comment
            FileInputStream stream = new FileInputStream(file);

            //comment
            int b1;
            int b2;
            String[] resultHashSum = null;
            String[] binaryStringToAdd;
            // Подсчитываем XOR
            while ((b1 = stream.read()) != -1){
                b2 = stream.read();
                if (resultHashSum == null) {
                    //comment
                    resultHashSum = bytesToBinary(b1, b2).split("");
                } else {
                    binaryStringToAdd = bytesToBinary(b1, b2).split("");

                    // Применяем XOR для каждой пары двоичных строк
                    for (int i = 0; i < resultHashSum.length; i++) {
                        resultHashSum[i] = xor(resultHashSum[i], binaryStringToAdd[i]);
                    }
                }
            }

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

        return null;
    }

    private void displayMessage(String mes) {
        System.out.println(mes);
    }

    private String bytesToBinary(int b1, int b2) {
        // Переводим в двоичную строку
        String binaryString1 = Integer.toBinaryString(b1);
        String result;
        //comment
        int diff = 8 - binaryString1.length();
        result = "0".repeat(diff) + binaryString1;

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
