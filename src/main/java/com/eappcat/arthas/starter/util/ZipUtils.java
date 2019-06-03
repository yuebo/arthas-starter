package com.eappcat.arthas.starter.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ZipUtils {
    private static final int BUFFER_SIZE = 1024;

    /**
     * 解压缩包
     * @param inputStream
     * @param destDir
     * @return
     * @throws Exception
     */
    public static List<String> unzip(InputStream inputStream, String destDir) throws Exception {
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        ZipArchiveInputStream is = null;
        List<String> fileNames = new ArrayList<>();

        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(inputStream, BUFFER_SIZE));
            ZipArchiveEntry entry;
            while ((entry = is.getNextZipEntry()) != null) {
                fileNames.add(entry.getName());

                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                } else {
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())), BUFFER_SIZE);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch(Exception e) {
            log.error("error to unzip {}",e);
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }

        return fileNames;
    }

}
