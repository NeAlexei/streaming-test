package com.kinomo.streaming.test;

import com.kinomo.streaming.test.metrics.Metrics;
import com.kinomo.streaming.test.task.DownloadTask;
import com.kinomo.streaming.test.task.UploadDownloadTask;
import com.kinomo.streaming.test.utils.FileFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.kinomo.streaming.test.config.Settings.FILE_PATH;

public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        runStressTest();
    }

    private static void runSingleTest() throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(FILE_PATH));
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new UploadDownloadTask(fileContent));
        executorService.shutdown();
    }

    private static void runSingleDownloadTest() throws IOException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new DownloadTask());
        executorService.shutdown();
    }

    private static void runStressTest() throws IOException, InterruptedException {
        byte[] fileContent = Files.readAllBytes(Paths.get(FILE_PATH));

        final ExecutorService executorService = Executors.newFixedThreadPool(20);


        for (int i = 0; i < 40; i++) {
            executorService.submit(new UploadDownloadTask(fileContent));
            Thread.sleep(2 * 1000);
        }

        executorService.shutdown();
        Metrics.print();
        Metrics.reset();
        logger.info("\n\n");
    }





    private static void runImagesStressTest() throws InterruptedException {

        final ExecutorService executorService = Executors.newFixedThreadPool(40);

        for (int i = 0; i < 100; i++) {
            executorService.submit(new DownloadTask());
            Thread.sleep( 1000);
        }
        executorService.shutdown();

    }


}
