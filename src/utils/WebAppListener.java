package utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;


import model.librosmario.Sistema;

public class WebAppListener implements javax.servlet.ServletContextListener {

	public void contextInitialized(ServletContextEvent scEvent) {
         ServletContext sc = scEvent.getServletContext();
         String webappPath = sc.getRealPath("/");
         System.out.println(webappPath);
         Sistema.getInstance().setPath(webappPath);
         Sistema.getInstance().setLogger(webappPath);
         // ... initialize your the path here.
    } 


	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
