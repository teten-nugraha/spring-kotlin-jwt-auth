package id.ten.auth.kotlinjwt.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        val REACTJS = "http://localhost:3000"
        val VUEJS = "http://localhost:8080"
        val ANGULAR = "http://localhost:4200"

        registry.addMapping("/**")
            .allowedOrigins(REACTJS,VUEJS, ANGULAR)
            .allowCredentials(true)
    }

}