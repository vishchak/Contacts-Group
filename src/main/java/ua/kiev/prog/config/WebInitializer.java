package ua.kiev.prog.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // #1
        //creating context
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        //connecting to config class
        ctx.setConfigLocation(getClass().getPackage().getName());
        // #2
        //creating servlet
        ServletRegistration.Dynamic servlet = servletContext
                //pairing servlet with context
                .addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }
}
