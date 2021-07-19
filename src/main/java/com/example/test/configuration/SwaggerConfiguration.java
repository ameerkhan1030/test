package com.example.test.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.example.test.interceptor.UserInterceptor;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***
 * Swagger COnfiguration
 * 
 * @author npc
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

	@Autowired
	private UserInterceptor interceptor;

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api").apiInfo(apiInfo()).select().build();
	}


	// change the below details according to project
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("PMS API").description(
				"API reference for developers</br> Dont Use basic error, Operation-handler and web-mvc-links handler, those API used for Internal Purpose")
				.build();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
}
