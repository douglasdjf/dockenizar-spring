package br.com.douglasdjf21.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.douglasdjf21.converter.YamlJackson2HttpMessageConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	
	private static final MediaType MEDIA_TYPE_APPLICATION_YAML = MediaType.valueOf("application/x-yaml");
	
	@Value("${cors.originPatterns:default}")
	private String corsOriginPatterns = "";
	

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new YamlJackson2HttpMessageConverter());
	}
	
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		var allowedOrigins = corsOriginPatterns.split(",");
		registry.addMapping("/**")
			//.allowedMethods("GET","POST","PUT","DELETE")
			.allowedMethods("*")
			.allowedOrigins(allowedOrigins)
			.allowCredentials(true);
	}


	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		
		/**
		 * Query Param
		 *   http://localhost:8080/usuarios?mediaType=xml
		 * http://localhost:8080/usuarios?mediaType=json
		 * 
		 
		
		configurer.favorParameter(true)
			.parameterName("mediaType").ignoreAcceptHeader(true)
			.useRegisteredExtensionsOnly(false)
			.defaultContentType(MediaType.APPLICATION_JSON)
			   .mediaType("json", MediaType.APPLICATION_JSON )
			   .mediaType("xml", MediaType.APPLICATION_XML);
			   
	    */
		
		
		/**
		 * Header Param
		 *  Accept   application/xml    ou application/json  ou application/x-yaml
		 */
		
		configurer.favorParameter(false)
		.ignoreAcceptHeader(false)
		.useRegisteredExtensionsOnly(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("json", MediaType.APPLICATION_JSON)
		.mediaType("xml", MediaType.APPLICATION_XML)
		.mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YAML);
		
		
		
		
	}
	
	

}
