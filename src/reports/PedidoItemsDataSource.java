package reports;

import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.xml.DOMConfigurator;

import model.librosmario.PedidoItem;
import model.librosmario.Sistema;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class PedidoItemsDataSource implements JRDataSource {
    private List<PedidoItem> pedidoItemsList=new ArrayList<PedidoItem>();
    private int indice=-1;
    //static Logger logger = Logger.getLogger(PedidoPrinter.class);
    
    
    public PedidoItemsDataSource(List<PedidoItem> l) {
    	this.setPedidoItemsList(l);
    }
	public List<PedidoItem> getPedidoItemsList() {
		return pedidoItemsList;
	}

	public void setPedidoItemsList(List<PedidoItem> pedidoItemsList) {
		this.pedidoItemsList = pedidoItemsList;
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		Object valor = null; 
		//DOMConfigurator.configure("log4j.xml");
		if("cantidad".equals(jrField.getName())) 
	    { 
	        valor = pedidoItemsList.get(indice).getPi_cantidad();
	        Sistema.logger.debug("valor cantidad:"+valor);
	    } 
		 else if("nombre".equals(jrField.getName())) 
		 { 
			valor = pedidoItemsList.get(indice).getPi_nombre_libro();
			Sistema.logger.debug("valor nombre:"+valor);
		 }
		 else if("autor".equals(jrField.getName())) 
		 { 
			valor = pedidoItemsList.get(indice).getPi_autor();
			Sistema.logger.debug("valor autor:"+valor);
		 }
		 else if("editorial".equals(jrField.getName())) 
		 { 
			valor = pedidoItemsList.get(indice).getPi_editorial();
			Sistema.logger.debug("valor editorial:"+valor);
		 }
		return valor;
	}

	public boolean next() throws JRException {
		DOMConfigurator.configure("log4j.xml");
		Sistema.logger.debug("indice:"+indice);
		return ++indice < pedidoItemsList.size();
	}

}
