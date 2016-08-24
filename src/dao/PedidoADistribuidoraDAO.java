package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.librosmario.Editorial;

import model.librosmario.PedidoADistribuidora;
import model.librosmario.PedidoItem;
import model.librosmario.Sistema;


public class PedidoADistribuidoraDAO {
	//static Logger logger = Sistema.logger.getLogger(PedidoDAO.class);
	private static PedidoADistribuidoraDAO instance=new PedidoADistribuidoraDAO();
	
	
	public static PedidoADistribuidoraDAO getInstance() {
		return instance;
	}

	public List<PedidoADistribuidora> getPedidosADistribuidoras(Editorial distribuidora,
			Date fechaDesde, 
			Date fechaHasta, 
			String libro,
			boolean pedidosPendientes) throws SQLException {
		List<PedidoADistribuidora> list = new ArrayList<PedidoADistribuidora>();
		Connection c = null;
		String where="";
		try {
		String select="SELECT pd_pedido_a_distribuidora_k,pd_fecha,pd_distribuidora_ed,pd_pedido_realizado" +
				" FROM pd_pedido_a_distribuidora";
		
		if (distribuidora !=null) 
		{where+= " pd_pedido_a_distribuidora.pd_distribuidora_ed=" + distribuidora.getEd_editorial_k() + " AND"; }
		
		if (fechaDesde!=null) 
		{where+= " DATE_FORMAT(pd_pedido_a_distribuidora.pd_fecha,'%Y-%m-%d')>='" + fechaDesde.toString() + "' AND" ; }
		
		if (fechaHasta!=null) 
		{where+= " DATE_FORMAT(pd_pedido_a_distribuidora.pd_fecha,'%Y-%m-%d')<='" + fechaHasta.toString() + "' AND" ; }
		
		if (libro!="") 
		{where+= " exists (select  * from pi_pedido_item,pdpi_pedido_distribuidora_item " + 
						   " where pdpi_pedido_item_pi=pi_pedido_item_k " + 
						   	  " and pdpi_pedido_a_distribuidora_pd=pd_pedido_a_distribuidora_k " +
						   	  " and pi_nombre_libro like '%" + libro + "%') AND" ; }
		if (pedidosPendientes) {
			where+=	" exists (select  * from pi_pedido_item,pdpi_pedido_distribuidora_item " + 
							" where pdpi_pedido_item_pi=pi_pedido_item_k " + 
							" and pdpi_pedido_a_distribuidora_pd=pd_pedido_a_distribuidora_k " +
							" and pi_pendiente=true) AND" ;
			
		}
		//DOMConfigurator.configure("log4j.xml");
		
		String sql="";
		
		
		if 	(where!="") 
		{where=where.substring(0, where.length()-3-1);
		where= " WHERE " + where;
		}
			
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + where;
		Sistema.logger.debug("getPedidosADistribuidoras sql" + sql);
		ResultSet rs = s.executeQuery(sql);
		
		
		while (rs.next()) {
		PedidoADistribuidora pd= new PedidoADistribuidora();
		//Sistema.logger.debug(rs.getInt("ed_editorial_k"));
		pd.setpd_pedido_a_distribuidora_k(rs.getInt("pd_pedido_a_distribuidora_k"));
		//Sistema.logger.debug(rs.getString("ed_descripcion"));
		pd.setPd_fecha(rs.getDate("pd_fecha"));
		EditorialDAO edDAO=new EditorialDAO();
		pd.setpd_distribuidora_ed(edDAO.getEditorial(rs.getInt("pd_distribuidora_ed")));
		pd.setPd_pedido_realizado(rs.getBoolean("pd_pedido_realizado"));		
		PedidoItemDAO piDAO=new PedidoItemDAO();
		pd.setItems(piDAO.getPedidoItemsDePedidoADistribuidora(pd));
		list.add(pd);
		
		}
		} finally {
		c.close();
		}
		return list;
	}
	
	public List<PedidoADistribuidora> getPedidosADistribuidorasDePedidoItem(PedidoItem pi) throws SQLException {
		List<PedidoADistribuidora> list = new ArrayList<PedidoADistribuidora>();
		Connection c = null;
		try {
		String select="SELECT pd_pedido_a_distribuidora_k,pd_fecha,pd_distribuidora_ed,pd_pedido_realizado" +
				" FROM pd_pedido_a_distribuidora,pdpi_pedido_distribuidora_item";
		String where=" where pdpi_pedido_item_pi=" + pi.getPi_pedido_item_k() +
		  			 " and pdpi_pedido_a_distribuidora_pd=pd_pedido_a_distribuidora_k" ;
		
		//DOMConfigurator.configure("log4j.xml");
		
		
		Sistema.logger.debug("getPedidosADistribuidorasDePedidoItem");
		String sql="";
			
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + where;
		ResultSet rs = s.executeQuery(sql);
		
		
		while (rs.next()) {
		Sistema.logger.debug("encontre pedido a distribuidora " + rs.getInt("pd_pedido_a_distribuidora_k") + 
				" de pedido item " + pi.getPi_pedido_item_k() + "-" + pi.getPi_nombre_libro() );	
		PedidoADistribuidora pd= new PedidoADistribuidora();
		//Sistema.logger.debug(rs.getInt("ed_editorial_k"));
		pd.setpd_pedido_a_distribuidora_k(rs.getInt("pd_pedido_a_distribuidora_k"));
		//Sistema.logger.debug(rs.getString("ed_descripcion"));
		pd.setPd_fecha(rs.getDate("pd_fecha"));
		EditorialDAO edDAO=new EditorialDAO();
		pd.setpd_distribuidora_ed(edDAO.getEditorial(rs.getInt("pd_distribuidora_ed")));
		pd.setPd_pedido_realizado(rs.getBoolean("pd_pedido_realizado"));		
		
		list.add(pd);
		
		}
		} finally {
		c.close();
		}
		return list;
	}
	
	public PedidoADistribuidora insert(PedidoADistribuidora pedido)  {
	    	
	        Connection connection = null;       
	        String result="";
	        
	        try {
	        	connection = Sistema.getConnection();
		        connection.setAutoCommit(false);
		        String insert="INSERT INTO librosmario.pd_pedido_a_distribuidora " +
    			"(pd_fecha,pd_distribuidora_ed,pd_pedido_realizado)" +
    			" values (now(),?, false);";
		        
	        	PreparedStatement ps = connection.prepareStatement(insert);
	        	
	    		ps.setInt(1, pedido.getpd_distribuidora_ed().getEd_editorial_k());
	    		Sistema.logger.debug("insertando pedido a distribuidora:" + ps.toString());
	    		ps.executeUpdate();
	            Statement s = connection.createStatement();
	            String sql="select last_insert_id() pedido_a_editorial_k from librosmario.pd_pedido_a_distribuidora;";
	            ResultSet rs = s.executeQuery(sql);
	            while (rs.next()) {
	                pedido.setpd_pedido_a_distribuidora_k(rs.getInt("pedido_a_editorial_k"));
	            }
	            //se supone que los items del pedido ya vienen con el pedido a editorial actualizado
	            for (PedidoItem pi:pedido.getItems()) {
	                Sistema.logger.debug("actualizando pedido item:" + pi.getPi_nombre_libro() );
	                pi.addPedidoAdistribuidora(pedido);
	                PedidoItemDAO piDAO=new PedidoItemDAO(); 
	                piDAO.update(pi,false,connection);
	            }
	            
	            connection.commit(); 
	            result="ok";

	            //Sistema.logger.debug("inserté:" + insert);
	            
	        }
	        catch (SQLException e) {
	        try {
	              //  Sistema.logger.error("Error trying to insert to the database: " + e.getMessage() + 
	              //  "insert: " + insert);                    
	                connection.rollback();
	                //result= "Error trying to insert to the database: " + e.getMessage() + 
	               // "insert: " + insert;
	                throw e;
	            } catch (SQLException e1) {                    
	                Sistema.logger.error(e1.getMessage());
	                }
	                            
	        }
	        catch (Exception e) {
	            Sistema.logger.error("Error insertando pedido: " + e.getMessage() );
	        }
	        finally {
	    		try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	        return pedido;
	    }

	public String update(PedidoADistribuidora pi) throws SQLException {
		Connection connection = null;
		String result="";
		try {
			
		 
	   connection = Sistema.getConnection();
	   connection.setAutoCommit(false);
        
		PreparedStatement ps = connection.prepareStatement("UPDATE librosmario.pd_pedido_a_distribuidora " +
				"SET pd_distribuidora_ed = ?,pd_pedido_realizado = ? WHERE pd_pedido_a_distribuidora_k = ?;");

		
		ps.setInt(2,pi.getpd_distribuidora_ed().getEd_editorial_k());
		ps.setBoolean(3, pi.getPd_pedido_realizado());
		ps.setInt(4,pi.getpd_pedido_a_distribuidora_k() );
		
		
		ps.executeUpdate();			
		connection.commit();				
		Sistema.logger.debug(ps.toString());
		result="ok";
		}
		catch (SQLException e) {
			try {
				connection.rollback();
				result="Error trying to update pedido item to the database: " + e.getMessage();
				Sistema.logger.error("Error trying to update pedido item to the database: " + e.getMessage() );
				throw e;
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
				throw e1;
			}					
		}finally {
		connection.close();
		}
		return result;
	}

}
