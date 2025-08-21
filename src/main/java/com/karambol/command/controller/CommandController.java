package com.karambol.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/run")
@RequiredArgsConstructor
@Slf4j
public class CommandController {

    @PostMapping
    public void runBatFile() {
        try {
            // 1. Define the working directory
            File workingDirectory = new File("C:\\pool\\pc\\ip-cam-video");

            ProcessBuilder javaKillBuilder = new ProcessBuilder("cmd.exe", "/c", "taskkill /IM java.exe /FI \"WINDOWTITLE eq video\"");

            Process javaKillProcess = javaKillBuilder.start();
            javaKillProcess.waitFor();
            log.info("Java processes were killed. Waiting 5 sec.");

            Thread.sleep(5000);

            ProcessBuilder gitPullBuilder = new ProcessBuilder("cmd.exe", "/c", "git pull");
            gitPullBuilder.directory(workingDirectory);

            Process gitPullProcess = gitPullBuilder.start();
            gitPullProcess.waitFor();
            log.info("Git pull command was executed. Waiting 10 sec.");

            Thread.sleep(10000);

            ProcessBuilder mvnInstallBuilder = new ProcessBuilder("cmd.exe", "/c", "mvn clean install");
            mvnInstallBuilder.directory(workingDirectory);

            Process mvnInstallProcess = gitPullBuilder.start();
            mvnInstallProcess.waitFor();
            log.info("Mvn clean install command was executed. Waiting 20 sec.");

            Thread.sleep(20000);

            ProcessBuilder javaStartBuilder = new ProcessBuilder("cmd.exe", "/c", "start \"video\" java -jar target\\video_test-0.0.1-SNAPSHOT.jar --main-camera-ip=192.168.100.150 --readrate=false --localhost=localhost");
            javaStartBuilder.directory(workingDirectory);

            Process javaStartProcess = gitPullBuilder.start();
            javaStartProcess.waitFor();
            log.info("Start java application was done. Waiting 20 sec.");

            Thread.sleep(20000);


//            // Specify the path to your batch file
//            String batFilePath = "C:\\Users\\user\\Desktop\\pool_java_start_up.bat";
//
//            // Execute the batch file using cmd /c to ensure the command prompt closes after execution
//            Process process = Runtime.getRuntime().exec("cmd /c " + batFilePath);
//
//            // Optionally, wait for the process to complete and get its exit value
//            int exitCode = process.waitFor();
//            System.out.println("Batch file exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}