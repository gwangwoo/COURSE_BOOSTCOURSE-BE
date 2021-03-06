/** 이 클래스는 DispatcherServlet이 실행될 때 읽어들일 설정파일이다!! */
package kr.or.connect.mvcexam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration // 설정파일임을 알려줌
@EnableWebMvc // 웹에 필요한 Bean들을 자동으로 설정
@ComponentScan(basePackages = {"kr.or.connect.mvcexam.controller"}) // 찾을 대상 패키지 설정
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter // 기본 이외 기능
{
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) 
	{
		// 과거에는 '/'대신에  .do나 .x를 사용했었음
		// '/' 때문에 모든 요청을 다 받아버리게 되는 문제, 때문에 asset, css, img, js와 같은 리소스에 대한 요청도 다 받게 됨
		// 아래는 이를 처리하기 위해 이러한 리소스에 대한 요청이 들어오면 해당 어플리케이션 디렉터리에서 찾게 하는 기능을 하는 코드이다
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
    
	
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) 
    {
        configurer.enable(); // DefaultServletHandler를 사용하게 함
        // 매핑 정보가 없는 URL요청 : DefaultServletHttpRequestHandler -> WAS의 DefaultServlet이 static한 리소스를 읽어 보여주게 해줌
    }
   
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) 
    {
    	// 특정 URL에 대한 처리를 컨트롤러 클래스를 작성하지 않고 매핑할 수 있도록 해줌
    	
    	System.out.println("addViewControllers가 호출됩니다. ");
        registry.addViewController("/").setViewName("main"); // 기본설정에서 주소가 없을 떄 index.jsp를 리턴하므로 index.jsp를 삭제한다
        // 요청이 '/'로 들어오면 main이라는 뷰로 보여주도록 설정 -> 뷰 네임은 ViewResolver를 이용해 찾아냄
    }
    
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() 
    {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        
        resolver.setPrefix("/WEB-INF/views/"); // prefix 지정
        resolver.setSuffix(".jsp"); // suffix 지정
        // 변환 결과 : /WEB-INF/views/main.jsp로 결과를 반환하여 해당 파일을 보여줌
        
        return resolver;
    }
}