package com.eappcat.arthas.starter;

import com.eappcat.arthas.starter.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.lang.management.ManagementFactory;

@Component
@Slf4j
public class ArthasInstallRunner implements ApplicationRunner {
    @Autowired
    private ArthasConfigProperties configProperties;
    @Value("${java.io.tmpdir:/tmp}")
    private String tempDir;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (configProperties.getEnabled()){
            ClassPathResource classPathResource=new ClassPathResource(configProperties.getZipName());
            File dir=new File(tempDir,configProperties.getInstallName());
            if (!dir.exists()){
                dir.mkdirs();
                try(InputStream inputStream=classPathResource.getInputStream()) {
                    ZipUtils.unzip(inputStream,dir.getAbsolutePath());
                }
                log.info("install arthas into {}",dir.getAbsolutePath());
            }else {
                log.info("arthas bin exists, skip to install");
            }

            String pid=getPid();
            log.info("Get current PID: {}",pid);
            ProcessBuilder processBuilder=new ProcessBuilder("java","-jar","arthas-boot.jar",pid,"--target-ip",configProperties.getTargetIp(),"--telnet-port",configProperties.getTelnetPort().toString(),"--http-port",configProperties.getHttpPort().toString());
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            processBuilder.directory(dir);
            processBuilder.start();

        }else {
            log.info("the arthas is not enabled, skip to install");
        }

    }

    public String getPid(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }
}
