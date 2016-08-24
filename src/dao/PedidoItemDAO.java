package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.librosmario.Pedido;
import model.librosmario.PedidoADistribuidora;
import model.librosmario.PedidoItem;
import model.librosmario.Sistema;



public class PedidoItemDAO {
    //static Logger logger = Logger.getLogger(PedidoItemDAO.class);
    
    static private PedidoItemDAO instance = null;
    
    static public PedidoItemDAO instance() {
        if(null == instance) {
           instance = new PedidoItemDAO();
        }
        return instance;
     }
    
    public String insert(Pedido p,PedidoItem pi,boolean commit, Connection c) throws SQLException {
    	
        Connection connection = null;
        Statement ps;
        String result="";
        
        if (c==null){
        connection = Sistema.getConnection();
        connection.setAutoCommit(false);
        }else 
        {connection=c;};
        
        Sistema.logger.debug("insertando pedido item " + pi.getPi_nombre_libro() );
        String insert= "INSERT INTO librosmario.pi_pedido_item ( pi_pedido_pe,pi_catalogo_cg,pi_cantidad," +
                "pi_nombre_libro,pi_autor,pi_editorial,pi_isbn,pi_precio,pi_editorial_ed,pi_pendiente)"
          + " values (" + p.getPe_pedido_k() + ","  + pi.getCg_catalogo_k() + "," + 
          pi.getPi_cantidad() + ",'" + pi.getPi_nombre_libro().replaceAll("'", "''") + "','" 
          + pi.getPi_autor().replaceAll("'", "''") + "','" + pi.getPi_editorial().replaceAll("'", "''") + "','"
          + pi.getPi_isbn() + "',"+  pi.getPi_precio() + ","+ pi.getPi_editorial_ed().getEd_editorial_k() +"," + pi.getPi_pendiente() + ");";
        
        
        try {
            ps = connection.createStatement();
            ps.executeUpdate(insert);        
            Statement s = connection.createStatement();
            Sistema.logger.debug("antes de insertar:" + insert);
            String sql="select last_insert_id() pedido_item_k from librosmario.pi_pedido_item;";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                pi.setPi_pedido_item_k(rs.getInt("pedido_item_k"));
            }
            if (commit) {connection.commit();}    
            Sistema.logger.debug("inserté:" + insert);
            result="ok";
        }
        catch (SQLException e) {
        try {
                Sistema.logger.error("Error trying to insert pedido item to the database: " + e.getMessage() + 
                "insert: " + insert);                    
                if (commit) {connection.rollback();};    
                throw e;
            } catch (SQLException e1) {                    
            	Sistema.logger.error(e1.getMessage());
                throw e;
            }
                            
        }
        return result;
    }
    
    public String insert(PedidoItem pi,boolean commit) throws SQLException {
        Connection connection = null;
        Statement ps;
        String result="";
        
        connection = Sistema.getConnection();
        connection.setAutoCommit(false);
        
        Sistema.logger.debug("insertando pedido item " + pi.getPi_nombre_libro() + " en insert(PedidoItem pi,boolean commit)" );
        Sistema.logger.debug("pedido_k:" + pi.getPi_pedido_pe().getPe_pedido_k());
        String insert= "INSERT INTO librosmario.pi_pedido_item ( pi_pedido_pe,pi_catalogo_cg,pi_cantidad," +
                "pi_nombre_libro,pi_autor,pi_editorial,pi_isbn,pi_precio,pi_editorial_ed,pi_pendiente)"
          + " values (" + pi.getPi_pedido_pe().getPe_pedido_k()+ ","  
          + pi.getCg_catalogo_k() + "," 
          + pi.getPi_cantidad() + ",'" 
          + (pi.getPi_nombre_libro()==null?"":pi.getPi_nombre_libro().replaceAll("'", "''")) + "','" 
          + (pi.getPi_autor()==null?"":pi.getPi_autor().replaceAll("'", "''")) + "','" 
          + (pi.getPi_editorial()==null?"":pi.getPi_editorial().replaceAll("'", "''")) + "','"
          + pi.getPi_isbn() + "',"
          + pi.getPi_precio() + ","
          + pi.getPi_editorial_ed().getEd_editorial_k() +"," 
          + pi.getPi_pendiente() + ");";
        Sistema.logger.debug(insert);
        try {
            ps = connection.createStatement();
            ps.executeUpdate(insert);
            Sistema.logger.debug("antes de insertar:" + insert);
            Statement s = connection.createStatement();
            String sql="select last_insert_id() pedido_item_k from librosmario.pi_pedido_item;";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                pi.setPi_pedido_item_k(rs.getInt("pedido_item_k"));
            }
            if (commit) {connection.commit();}    
            Sistema.logger.debug("inserté:" + insert);
            result="ok";
        }
        catch (SQLException e) {
        try {
        	Sistema.logger.error("Error trying to insert pedido item to the database: " + e.getMessage() + 
                "insert: " + insert);                    
                if (commit) {connection.rollback();};    
                throw e;
            } catch (SQLException e1) {                    
            	Sistema.logger.error(e1.getMessage());
                throw e;
            }
                            
        }
        return result;
    }
    
    public String update(PedidoItem pi,boolean commit, Connection c) throws SQLException {
		Connection connection = null;
		String result="";
		try {
			
		 if (c==null){
		   connection = Sistema.getConnection();
		   connection.setAutoCommit(false);
        }else 
		   {connection=c;};
	    
		   Sistema.logger.debug("Actualizando pedido item " + pi.getPi_nombre_libro() );
		
		Sistema.logger.debug("antes de for (PedidoADistribuidora p:pi.getPedidosADistribuidoras()) {");
		
		for (PedidoADistribuidora p:pi.getPedidosADistribuidoras()) {
			String select="SELECT pdpi_pedido_a_distribuidora_pd,pdpi_pedido_item_pi " +
					" FROM librosmario.pdpi_pedido_distribuidora_item" +		
					" where  pdpi_pedido_a_distribuidora_pd= " + p.getpd_pedido_a_distribuidora_k() +
				  " and pdpi_pedido_item_pi=" + pi.getPi_pedido_item_k();
			Statement s = connection.createStatement();
			ResultSet rs = s.executeQuery(select);
			if (!rs.next()) 
			{  try {
				Sistema.logger.debug("insertando pdpi_pedido_distribuidora_item");
				String insert= "INSERT INTO librosmario.pdpi_pedido_distribuidora_item" +
		 		" ( pdpi_pedido_a_distribuidora_pd,pdpi_pedido_item_pi, pdpi_fecha)"
				+ " values (" + p.getpd_pedido_a_distribuidora_k()+ ","+ pi.getPi_pedido_item_k() + ",now())";
				Statement s1 = connection.createStatement();
	            s1.executeUpdate(insert);
	            pi.setPi_pendiente(false);
	            Sistema.logger.debug("inserté:"+ insert);
			} catch(SQLException e) {
				connection.rollback();
				result="Error trying to insert pedido a distribuidora to the database: " + e.getMessage();
				Sistema.logger.error("Error trying to insert pedido a distribuidora to the database: " + e.getMessage() );
				throw e;
			}
			
			}
		}
		Sistema.logger.debug("antes de hacer update:");		
		
		PreparedStatement ps = connection.prepareStatement("UPDATE librosmario.pi_pedido_item SET "
				+ "pi_pedido_pe = ?,pi_catalogo_cg = ?,pi_cantidad = ?,pi_nombre_libro = ?,pi_autor = ?,pi_editorial = ?,"
				+ "pi_isbn = ?,pi_precio = ?,pi_editorial_ed = ?,pi_pendiente = ?"
				+ " WHERE pi_pedido_item_k = ?;");
				
				Sistema.logger.debug("antes de hacer update:1");	
				ps.setInt(1, pi.getPi_pedido_pe().getPe_pedido_k());
				Sistema.logger.debug("antes de hacer update:2");
				ps.setInt(2,  pi.getCg_catalogo_k());
				Sistema.logger.debug("antes de hacer update:3");
				ps.setInt(3, pi.getPi_cantidad());
				Sistema.logger.debug("antes de hacer update:4");
				ps.setString(4, pi.getPi_nombre_libro()==null?  "":pi.getPi_nombre_libro().replaceAll("'", "''"));
				Sistema.logger.debug("antes de hacer update:5");
				ps.setString(5, pi.getPi_autor()==null?"":pi.getPi_autor().replaceAll("'", "''"));
				Sistema.logger.debug("antes de hacer update:6");
				ps.setString(6, pi.getPi_editorial()==null?"":pi.getPi_editorial().replaceAll("'", "''"));
				Sistema.logger.debug("antes de hacer update:7");
				ps.setString(7, pi.getPi_isbn());
				Sistema.logger.debug("antes de hacer update:8");
				ps.setDouble(8, pi.getPi_precio() );
				Sistema.logger.debug("antes de hacer update:9");
				ps.setInt(9,  pi.getPi_editorial_ed().getEd_editorial_k());
				Sistema.logger.debug("antes de hacer update:10");
				ps.setBoolean(10,  pi.getPi_pendiente());
				Sistema.logger.debug("antes de hacer update:11");
				ps.setInt(11, pi.getPi_pedido_item_k());
				Sistema.logger.debug("antes de hacer update:12");
				ps.executeUpdate();
				Sistema.logger.debug("hice update:" +ps.toString());
				
		
		if (commit) {connection.commit();}				
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
			if (commit) {connection.close();};
		}
		return result;
	}
    
    public List<PedidoItem> getPedidoItems(boolean pendientes) {
    	List<PedidoItem> list = new ArrayList<PedidoItem>();
    	Connection c = null;
		String sql="";
		String select="SELECT pi_pedido_item_k,pi_pedido_pe,pi_catalogo_cg,pi_cantidad,pi_nombre_libro,pi_autor," +
				"pi_editorial,pi_isbn,pi_precio,pi_editorial_ed,pi_pendiente" +
						" FROM librosmario.pi_pedido_item ";
		String where="";
		String orderby="ORDER BY pi_pedido_item_k;";
		//DOMConfigurator.configure("log4j.xml");
		if (pendientes) {where="where pi_pendiente=true";};
		try {
		Sistema.logger.debug("hola getPedidoItems");	
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + " " + where +" "+ orderby;
		ResultSet rs = s.executeQuery(sql);
		
		while (rs.next()) {
			PedidoItem pi=new PedidoItem();		
			
			pi.setPi_pedido_item_k(rs.getInt("pi_pedido_item_k"));
			PedidoDAO peDao=new PedidoDAO();
			pi.setPi_pedido_pe(peDao.getPedido(rs.getInt("pi_pedido_pe")));
			//cliente.setCl_email(rs.getString(rs.getString("cl_email")));
			CatalogoDAO cgDAO=new CatalogoDAO();
			pi.setPi_catalogo_cg(cgDAO.getCatalogo(rs.getInt("pi_catalogo_cg")));
			//logger.debug(rs.getString("cl_telefono_fijo"));
			pi.setPi_cantidad(rs.getInt("pi_cantidad"));
			//logger.debug(rs.getString("cl_telefono_laboral"));
			pi.setPi_nombre_libro(rs.getString("pi_nombre_libro"));
			//logger.debug(rs.getString("cl_telefono_movil"));
			pi.setPi_autor(rs.getString("pi_autor"));
			//logger.debug(rs.getString("cl_telefono_otro"));
			pi.setPi_editorial(rs.getString("pi_editorial"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			pi.setPi_isbn(rs.getString("pi_isbn"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			pi.setPi_precio(rs.getDouble("pi_precio"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			EditorialDAO edDAO=new EditorialDAO();
			pi.setPi_editorial_ed(edDAO.getEditorial(rs.getInt("pi_editorial_ed")));
			pi.setPi_pendiente(rs.getBoolean("pi_pendiente"));
			PedidoADistribuidoraDAO pdDAO=new PedidoADistribuidoraDAO();
			pi.setPedidosADistribuidoras(pdDAO.getPedidosADistribuidorasDePedidoItem(pi));
			list.add(pi);
		}
		
		}  
		
		catch (SQLException e)
		{
			Sistema.logger.error("Error trying to query to the database: " + e.getMessage() + 
					"select: " + sql);
			} catch (Exception e) {
				Sistema.logger.error("Error : " + e.getMessage() + 
					"in PedidoItemDao:" + select);
		}
		
		finally {
			try {
			c.close();
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}
		}
		Sistema.logger.debug("retorné " + list.size() + " items");
		return list;
    	
    }
    
    public ArrayList<PedidoItem> getPedidoItemsDePedidoADistribuidora(PedidoADistribuidora pd) {
    	ArrayList<PedidoItem> list = new ArrayList<PedidoItem>();
    	Connection c = null;
		String sql="";
		String select="SELECT pi_pedido_item_k,pi_pedido_pe,pi_catalogo_cg,pi_cantidad,pi_nombre_libro,pi_autor," +
				"pi_editorial,pi_isbn,pi_precio,pi_editorial_ed,pi_pendiente" +
						" FROM librosmario.pi_pedido_item,librosmario.pdpi_pedido_distribuidora_item";		
		String where="where pi_pedido_item_k=pdpi_pedido_item_pi" +
					  " and pdpi_pedido_a_distribuidora_pd=" + pd.getpd_pedido_a_distribuidora_k();
		String orderby="ORDER BY pi_pedido_item_k;";
		//DOMConfigurator.configure("log4j.xml");
		
		try {
			Sistema.logger.debug("getPedidoItemsDePedidoADistribuidora");	
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + " " + where + " " + orderby;
		Sistema.logger.debug("getPedidoItemsDePedidoADistribuidora sql:" + sql);
		ResultSet rs = s.executeQuery(sql);
		
		while (rs.next()) {
			PedidoItem pi=new PedidoItem();		
			
			pi.setPi_pedido_item_k(rs.getInt("pi_pedido_item_k"));
			Sistema.logger.debug("Pi_pedido_item_k:" + pi.getPi_pedido_item_k());
			//pi.setPi_pedido_pe(rs.getInt("pi_pedido_pe"));
			//cliente.setCl_email(rs.getString(rs.getString("cl_email")));
			CatalogoDAO cgDAO=new CatalogoDAO();
			pi.setPi_catalogo_cg(cgDAO.getCatalogo(rs.getInt("pi_catalogo_cg")));
			//logger.debug(rs.getString("cl_telefono_fijo"));
			pi.setPi_cantidad(rs.getInt("pi_cantidad"));
			//logger.debug(rs.getString("cl_teslefono_laboral"));
			pi.setPi_nombre_libro(rs.getString("pi_nombre_libro"));
			//logger.debug(rs.getString("cl_telefono_movil"));
			pi.setPi_autor(rs.getString("pi_autor"));
			//logger.debug(rs.getString("cl_telefono_otro"));
			pi.setPi_editorial(rs.getString("pi_editorial"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			pi.setPi_isbn(rs.getString("pi_isbn"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			pi.setPi_precio(rs.getDouble("pi_precio"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			PedidoDAO pDAO=new PedidoDAO();
			pi.setPi_pedido_pe(pDAO.getPedido(rs.getInt("pi_pedido_pe"), true));
			EditorialDAO edDAO=new EditorialDAO();
			pi.setPi_editorial_ed(edDAO.getEditorial(rs.getInt("pi_editorial_ed")));
			pi.setPi_pendiente(rs.getBoolean("pi_pendiente"));
			list.add(pi);
		}
		
		}  
		
		catch (SQLException e)
		{
			Sistema.logger.error("Error trying to query to the database: " + e.getMessage() + 
					"select: " + sql);
			} catch (Exception e) {
				Sistema.logger.error("Error : " + e.getMessage() + 
					"in ClienteDAO:" + select);
		}
		
		finally {
			try {
			c.close();
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}
		}
		return list;
    	
    }
    
    public ArrayList<PedidoItem> getPedidoItemsDePedido(Pedido pe) {
    	ArrayList<PedidoItem> list = new ArrayList<PedidoItem>();
    	Connection c = null;
		String sql="";
		String select="SELECT pi_pedido_item_k,pi_pedido_pe,pi_catalogo_cg,pi_cantidad,pi_nombre_libro,pi_autor," +
				"pi_editorial,pi_isbn,pi_precio,pi_editorial_ed,pi_pendiente" +
						" FROM librosmario.pi_pedido_item";		
		String where="where pi_pedido_pe=" + pe.getPe_pedido_k() + " ";
					  
		String orderby="ORDER BY pi_pedido_item_k";
		//DOMConfigurator.configure("log4j.xml");
		
		try {
		
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + " " + where + " " + orderby + ";";
		ResultSet rs = s.executeQuery(sql);
		Sistema.logger.debug("getPedidoItemsDePedido Query:" + sql );	
		while (rs.next()) {
			PedidoItem pi=new PedidoItem();		
			
			pi.setPi_pedido_item_k(rs.getInt("pi_pedido_item_k"));
			Sistema.logger.debug("pedido item nro:" +rs.getInt("pi_pedido_item_k") );
			//cliente.setCl_email(rs.getString(rs.getString("cl_email")));
			CatalogoDAO cgDAO=new CatalogoDAO();
			pi.setPi_catalogo_cg(cgDAO.getCatalogo(rs.getInt("pi_catalogo_cg")));
			//logger.debug(rs.getString("cl_telefono_fijo"));
			pi.setPi_cantidad(rs.getInt("pi_cantidad"));
			//logger.debug(rs.getString("cl_telefono_laboral"));
			pi.setPi_nombre_libro(rs.getString("pi_nombre_libro"));
			//logger.debug(rs.getString("cl_telefono_movil"));
			pi.setPi_autor(rs.getString("pi_autor"));
			//logger.debug(rs.getString("cl_telefono_otro"));
			pi.setPi_editorial(rs.getString("pi_editorial"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			pi.setPi_isbn(rs.getString("pi_isbn"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			pi.setPi_precio(rs.getDouble("pi_precio"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			EditorialDAO edDAO=new EditorialDAO();
			pi.setPi_editorial_ed(edDAO.getEditorial(rs.getInt("pi_editorial_ed")));
			pi.setPi_pendiente(rs.getBoolean("pi_pendiente"));
			list.add(pi);
		}
		
		}  
		
		catch (SQLException e)
		{
			Sistema.logger.error("Error trying to query to the database: " + e.getMessage() + 
					"select: " + sql);
			} catch (Exception e) {
				Sistema.logger.error("Error : " + e.getMessage() + 
					"in PedidoItemDAO:" + select);
		}
		
		finally {
			try {
			c.close();
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}
		}
		return list;
    	
    }
    
    public ArrayList<String> getLibrosPedidos() {
	 	ArrayList<String> list = new ArrayList<String>();
	 	Connection c = null;
			String sql="";
			String select="SELECT distinct pi_nombre_libro FROM librosmario.pi_pedido_item";			
			String orderby="ORDER BY pi_nombre_libro";
			//DOMConfigurator.configure("log4j.xml");
			
			try {
			
			c = Sistema.getConnection();
			Statement s = c.createStatement();
			sql=select + " " + orderby + ";";
			ResultSet rs = s.executeQuery(sql);
			Sistema.logger.debug("getLibrosPedidos Query:" + sql );	
			while (rs.next()) {
				list.add(rs.getString("pi_nombre_libro"));
				
				
			}
			
			}  
			
			catch (SQLException e)
			{
				Sistema.logger.error("Error trying to query to the database: " + e.getMessage() + 
						"select: " + sql);
				} catch (Exception e) {
					Sistema.logger.error("Error : " + e.getMessage() + 
						"in PedidoItemDAO:" + select);
			}
			
			finally {
				try {
				c.close();
				} catch (SQLException e1) {					
					Sistema.logger.error(e1.getMessage());
				}
			}
			return list;
	 }


}
