package Jay.BoardP;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.dir}/")
    private String path;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imagePath/**")
            .addResourceLocations("file:///" + path + "/images/");

        registry.addResourceHandler("/filePath/**")
            .addResourceLocations("file:///" + path + "/generals/");

    }

}
