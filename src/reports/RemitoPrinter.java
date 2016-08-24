package reports;

import java.util.HashMap;
import java.util.Map;

import model.librosmario.Remito;
import model.librosmario.Sistema;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;

public class RemitoPrinter {

	public static void ImprimirRemito(Remito r) throws Exception {
		java.sql.Connection c=null;
		//DOMConfigurator.configure("log4j.xml");
		try {
		      Map parameters = new HashMap();
		      c=Sistema.getConnection();
		      parameters.put("nroRemito", r.getRe_remito_k());		      
		      JasperReport report = JasperCompileManager.compileReport(
		    	  Sistema.getInstance().getPathReports() + "remito.jrxml");
		      JasperPrint print = JasperFillManager.fillReport(report, parameters, c);
		      // Exporta el informe a PDF
		      //JasperExportManager.exportReportToPdfFile(print,Sistema.getPathReports() + "pedido_cliente.pdf");
		      //Para visualizar el pdf directamente desde java
		      //JasperViewer.viewReport(print, false);
		      JasperPrintManager.printReport(print,false);
		      JasperPrintManager.printReport(print,false);
		      
		    }
		    catch (Exception e) {
		      Sistema.logger.error("error imprimiendo remito:" + e.getStackTrace());
		      throw e;
		    }
		    finally {
		      /*
		       * Cleanup antes de salir
		       */
		      try {
		        if (c != null) {		          
		          c.close();
		        }
		      }
		      catch (Exception e) {
		        e.printStackTrace();
		      }
		    }
	
		
	}
}
