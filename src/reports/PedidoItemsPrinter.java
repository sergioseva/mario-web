package reports;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import model.librosmario.PedidoItem;
import model.librosmario.Sistema;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class PedidoItemsPrinter {
	
//static Logger logger = Logger.getLogger(PedidoPrinter.class);
	
	public static void ImprimirPedido(List<PedidoItem> items,String distribuidora) {
		
		//DOMConfigurator.configure("log4j.xml");
		try {
			 	
				Map parameters = new HashMap();
		      	parameters.put("distribuidora", distribuidora);
		      	Sistema.logger.debug("distribuidora:"+distribuidora);
		      	Sistema.logger.debug("items:"+items.size());
		      	Collections.sort(items);
		      	PedidoItemsDataSource pids=new PedidoItemsDataSource(items);
			 	JasperReport report = JasperCompileManager.compileReport(
				    	  Sistema.getInstance().getPathReports() + "pedido_distribuidora.jrxml");  
		        JasperPrint print = JasperFillManager.fillReport(report, parameters, pids);  
		        JasperExportManager.exportReportToPdfFile(print,
			    		  Sistema.getInstance().getPathReports() + "pedido_distribuidora.pdf");
		        JasperViewer.viewReport(print, false);
		        JasperPrintManager.printReport(print, false);
		        
		      
		    }
		    catch (Exception e) {
		      Sistema.logger.error("error imprimiendo pedido a distribuidoras:" + e.toString());		    	
		    }

		
	}

}
