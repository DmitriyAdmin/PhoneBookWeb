package main.config;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    URL res = getClass().getClassLoader().getResource("img/");
    File file = null;
    try {
      file = Paths.get(res.toURI()).toFile();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    String absolutePath = file.getAbsolutePath();

    registry.addResourceHandler("/img/**")
        .addResourceLocations("file:/" + absolutePath + "/");
  }
}