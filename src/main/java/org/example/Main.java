package org.example;

import org.example.task1.DiskReviser;
import org.example.task2.ExeFileReviser;
import org.example.task3.NetFunctionsAnalyser;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int task = 1;
        if (task == 1) {
            startTask1();
        } else if (task == 2){
            startTask2();
        } else {
            startTask3();
        }
    }

    // Запуск задания 1
    public static void startTask1(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите путь до каталога (для выхода введите exit):");
            String directory = scanner.nextLine();

            if (directory.equals("exit")){
                return;
            }

            String mode = "";
            while (!mode.equals("hash") && !mode.equals("revise")) {
                System.out.println("Укажите режим работы hash/revise (для выхода введите exit):");
                mode = scanner.nextLine();
                if (mode.equals("exit")){
                    return;
                }
            }
            // ew

            // wewe

            DiskReviser diskReviser = new DiskReviser();
            diskReviser.startDiskReviser(new File(directory), mode);
        }
    }

    // Запускает задание 2
    public static void startTask2 () {
        //ew w
        ExeFileReviser exeFileReviser = new ExeFileReviser();
        exeFileReviser.startExeFileReviser();
        Scanner scanner = new Scanner(System.in);
        // wewaE
        scanner.nextLine();
    }

    public static void startTask3(){
        //ew w
        NetFunctionsAnalyser netFunctionsAnalyser = new NetFunctionsAnalyser();
        System.out.println("Введите путь до каталога: ");
        //ew w
        Scanner scanner = new Scanner(System.in);
        String dir = scanner.nextLine();
        //ew w
        netFunctionsAnalyser.startAnalyser(dir);
    }
}