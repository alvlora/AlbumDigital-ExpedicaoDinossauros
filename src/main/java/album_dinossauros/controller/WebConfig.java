package album_dinossauros.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String rotaCapasSrc = Paths.get("src/main/resources/static/img/capas/").toAbsolutePath().toUri().toString();
        String rotaCapasTarget = Paths.get("target/classes/static/img/capas/").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/img/capas/**")
                .addResourceLocations(rotaCapasSrc, rotaCapasTarget);
                
        String rotaFigurinhasSrc = Paths.get("src/main/resources/static/img/figurinhas/").toAbsolutePath().toUri().toString();
        String rotaFigurinhasTarget = Paths.get("target/classes/static/img/figurinhas/").toAbsolutePath().toUri().toString();
        
        registry.addResourceHandler("/img/figurinhas/**")
                .addResourceLocations(rotaFigurinhasSrc, rotaFigurinhasTarget);
        }
}