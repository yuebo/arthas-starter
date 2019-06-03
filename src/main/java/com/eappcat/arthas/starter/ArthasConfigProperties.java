package com.eappcat.arthas.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.arthas")
@Getter
@Setter
public class ArthasConfigProperties {
    /**
     * http 端口地址，用于web console访问
     */
    private Integer httpPort=8563;
    /**
     * telnet 端口，用于telnet访问
     */
    private Integer telnetPort=3658;
    /**
     * 监听地址，默认本地，远程需要修改为0.0.0.0
     */
    private String targetIp="127.0.0.1";
    /**
     * 是否启动Arthas
     */
    private Boolean enabled=true;
    /**
     * zip的名称
     */
    private String zipName="arthas-3.1.1-bin.zip";
    /**
     * 临时目录名称
     */
    private String installName="arthas-3.1.1";
}
