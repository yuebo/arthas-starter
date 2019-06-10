package com.eappcat.arthas.starter;

import com.eappcat.arthas.starter.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.io.File;
import java.io.InputStream;
import java.lang.management.ManagementFactory;

@Slf4j
public class ArthasInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment=applicationContext.getEnvironment();
        Binder binder=new Binder(ConfigurationPropertySources.get(environment));
        BindResult<ArthasConfigProperties> bindResult= binder.bind("spring.arthas",ArthasConfigProperties.class);

        String tempDir= environment.getProperty("java.io.tmpdir","/tmp");
        ArthasConfigProperties configProperties=bindResult.get();

        try {
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
                log.info("arthas is starting up...");

            }else {
                log.info("the arthas is not enabled, skip to install");
            }
        }catch (Exception e){
            throw new IllegalArgumentException("error to start arthas");

        }


    }

    public String getPid(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }
}
