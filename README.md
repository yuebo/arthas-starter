# Arthas For Spring boot Starter

A starter for Spring boot application to install the arthas inside applicaiton.

[wiki](https://alibaba.github.io/arthas)                                   
[tutorials](https://alibaba.github.io/arthas/arthas-tutorials)

Usage:

```xml
<dependencies>
    <dependency>
        <groupId>com.eappcat.arthas</groupId>
        <artifactId>arthas-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

Configuration:

```yaml
spring:
  arthas:
    enabled: true
    http-port: 8563
    telnet-port: 3658
    target-ip: 0.0.0.0
```