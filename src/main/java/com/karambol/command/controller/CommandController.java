package com.karambol.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/run")
@RequiredArgsConstructor
@Slf4j
public class CommandController {

    @PostMapping
    public void runBatFile() {
        try {
            // Specify the path to your batch file
            String batFilePath = "C:\\Users\\user\\Desktop\\pool_java_start_up.bat";

            // Execute the batch file using cmd /c to ensure the command prompt closes after execution
            Process process = Runtime.getRuntime().exec("cmd /c " + batFilePath);

            // Optionally, wait for the process to complete and get its exit value
            int exitCode = process.waitFor();
            System.out.println("Batch file exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}