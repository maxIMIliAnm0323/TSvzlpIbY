// 代码生成时间: 2025-10-09 02:16:23
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;

/**
 * DiskSpaceManager is a utility class to manage disk space by analyzing directories and files.
 * It provides functionality to calculate disk usage, find large files, and clean up unnecessary files.
 */
public class DiskSpaceManager {

    private static final Logger logger = LoggerFactory.getLogger(DiskSpaceManager.class);

    /**
     * Calculates the total disk usage of a given directory.
     *
     * @param directoryPath the path of the directory to analyze
     * @return the total size in bytes
     * @throws IOException if an I/O error occurs
     */
    public static long calculateDiskUsage(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        return FileUtils.sizeOfDirectory(directory);
    }

    /**
     * Finds files larger than a specified size in a directory.
     *
     * @param directoryPath the path of the directory to search
     * @param fileSizeLimit the file size limit (in bytes)
     * @return a list of files larger than the specified size
     * @throws IOException if an I/O error occurs
     */
    public static File[] findLargeFiles(String directoryPath, long fileSizeLimit) throws IOException {
        File directory = new File(directoryPath);
        return directory.listFiles((dir, name) -> {
            File file = new File(dir, name);
            return file.isFile() && file.length() > fileSizeLimit;
        });
    }

    /**
     * Deletes files in a directory that are older than a specified number of days.
     *
     * @param directoryPath the path of the directory to clean
     * @param days the number of days to consider a file as old
     * @return the number of deleted files
     * @throws IOException if an I/O error occurs
     */
    public static int deleteOldFiles(String directoryPath, int days) throws IOException {
        File directory = new File(directoryPath);
        int deletedFilesCount = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory.toPath())) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    long fileAge = System.currentTimeMillis() - Files.getLastModifiedTime(entry).toMillis();
                    if (fileAge > days * 86400000) { // 86400000 ms in a day
                        Files.delete(entry);
                        deletedFilesCount++;
                    }
                }
            }
        }

        return deletedFilesCount;
    }

    public static void main(String[] args) {
        try {
            String directoryPath = 