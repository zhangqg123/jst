package org.jeecg;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
public class JeecgApplication {

  public static void main(String[] args) throws UnknownHostException {
    //System.setProperty("spring.devtools.restart.enabled", "true");

    ConfigurableApplicationContext application = SpringApplication.run(JeecgApplication.class, args);
    Environment env = application.getEnvironment();
    String ip = InetAddress.getLocalHost().getHostAddress();
    String port = env.getProperty("server.port");
    String path = env.getProperty("server.servlet.context-path");
    log.info("\n----------------------------------------------------------\n\t" +
        "Application Jeecg-Boot is running! Access URLs:\n\t" +
        "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
        "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
        "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
        "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
        "----------------------------------------------------------");

  }
/*  
  @Value("${http.port}")
  private Integer port;

  @Value("${server.port}")
  private Integer httpsPort;

  @Bean
  public ServletWebServerFactory servletContainer() {
      TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
          @Override
          protected void postProcessContext(Context context) {
              // 如果要强制使用https，请松开以下注释
              // SecurityConstraint constraint = new SecurityConstraint();
              // constraint.setUserConstraint("CONFIDENTIAL");
              // SecurityCollection collection = new SecurityCollection();
              // collection.addPattern("/*");
              // constraint.addCollection(collection);
              // context.addConstraint(constraint);
          }
      };
      tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
      return tomcat;
  }

  // 配置http
  private Connector createStandardConnector() {
      // 默认协议为org.apache.coyote.http11.Http11NioProtocol
      Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
      connector.setSecure(false);
      connector.setScheme("http");
      connector.setPort(port);
      connector.setRedirectPort(httpsPort); // 当http重定向到https时的https端口号
      return connector;
  }
  */
}