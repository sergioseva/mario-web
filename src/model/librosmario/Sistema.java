package model.librosmario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import dao.EditorialDAO;
import dao.PedidoADistribuidoraDAO;
import dao.PedidoItemDAO;



public class Sistema {
	public static Logger logger = Logger.getLogger(Sistema.class);
	DataSource pool;
	private static Sistema instance;
	private String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Sistema() {
		 pool=new BasicDataSource();
		 //TODO poner esto en un archivo, http://www.chuidiang.com/java/mysql/BasicDataSource-Pool-Conexiones.php
		((BasicDataSource) pool).setDriverClassName("com.mysql.jdbc.Driver");
		((BasicDataSource) pool).setUrl("jdbc:mysql://localhost:3306/librosmario");
		((BasicDataSource) pool).setUsername("root");
		((BasicDataSource) pool).setPassword("dracula");
		
		//DOMConfigurator.configure(System.getProperty("user.dir") + "log4j.xml");
		//DOMConfigurator.configure(  "log4j.xml");
		
		
	}
	
	public void setLogger(String path) {
		System.setProperty("path",this.getPathLogs());
		DOMConfigurator.configure( path + "WEB-INF\\log4j.xml");
	}
	
	public static Sistema getInstance(){
		if (instance == null) {
			instance = new Sistema();
		}
		return instance;
		
	}
	
	public  String getPathLogs() {
		return "C:\\sistema_pedidos\\logs\\";	
		
	}
	
	
	public String getPathReports() {
		//return "C:\\blazeds\\tomcat\\webapps\\libros-mario-server\\WEB-INF\\reports\\";
		return path + "WEB-INF\\reports\\";
	}
	
	public void importarCatalogo() {
		CatalogoConverter catalogoConverter;
	    catalogoConverter=new CatalogoConverter();
	    /*catalogoConverter.importarLuongo();*/	
	}
	
	public static Connection getConnection() throws SQLException {
		if (instance == null) {
			instance = new Sistema();
		}
		try {
			return instance.pool.getConnection();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public Pedido nuevoPedido() {
		return (new Pedido());
	}
	
	public List<PedidoItem> agregarItemAPedido(Pedido p, PedidoItem pi) {
		logger.debug("p.addPedido(pi);");
		p.addPedido(pi);
		logger.debug("p.addPedido(pi)2;");
		return p.getItems();
	}
	
	public void grabarPedido(ArrayList<PedidoItem> pedidoItems, Editorial distribuidora ) {
		logger.debug("grabar pedido:" + distribuidora.getEd_editorial_k() +"-"+distribuidora.getEd_descripcion());
		if (distribuidora.getEd_editorial_k()==0) {//todos
			ArrayList<Editorial> editoriales=(ArrayList<Editorial>) EditorialDAO.getInstance().getEditoriales();
			for (Editorial ed:editoriales) {
				logger.debug("generando pedidos para editorial:" + ed.getEd_editorial_k() + "-" + ed.getEd_descripcion());
				ArrayList<PedidoItem> pedidoItemsFiltrados;
				PredicateDistribuidora predicate=new PredicateDistribuidora(ed);
				pedidoItemsFiltrados= (ArrayList<PedidoItem>) ((ArrayList<PedidoItem>)pedidoItems).clone();
				CollectionUtils.filter(pedidoItemsFiltrados, predicate);
				if (pedidoItemsFiltrados.size()>0) {
				this.grabarPedido(pedidoItemsFiltrados, ed);
				}
		}
		} else {
			PedidoADistribuidora pedidoADistribuidora=new PedidoADistribuidora();
			pedidoADistribuidora.setpd_distribuidora_ed(distribuidora);
			pedidoADistribuidora.setItems(pedidoItems);
			PedidoADistribuidoraDAO.getInstance().insert(pedidoADistribuidora);
		}
	}
	
	public boolean dividirPedidoItem(PedidoItem pi,int cantidad) throws SQLException {
		try {
		PedidoItem pedidoItemNuevo= (PedidoItem) pi.clone();
        pedidoItemNuevo.setPi_pedido_item_k(0);
        pedidoItemNuevo.setPi_cantidad(pi.getPi_cantidad()-cantidad);
 		pi.setPi_cantidad(cantidad);
 		PedidoItemDAO.instance().insert(pedidoItemNuevo, true);
 		PedidoItemDAO.instance().update(pi,true,null);
		} catch (SQLException e) {
			logger.debug("Error tratando de dividir pedido item:" + e.getMessage());
			throw e;
			
		}
 		return true;
	}
	
}