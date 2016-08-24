package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.librosmario.Remito;
import model.librosmario.RemitoItem;
import model.librosmario.Sistema;

public class RemitoItemDAO {

	
    static private RemitoItemDAO instance = null;
    
    static public RemitoItemDAO instance() {
        if(null == instance) {
           instance = new RemitoItemDAO();
        }
        return instance;
     }
    
 public String insert(Remito r,RemitoItem ri,boolean commit, Connection c) throws SQLException {
    	
        Connection connection = null;
        Statement ps;
        String result="";
        
        if (c==null){
        connection = Sistema.getConnection();
        connection.setAutoCommit(false);
        }else 
        {connection=c;};
        
        Sistema.logger.debug("insertando remito item " + ri.getRi_nombre_libro() );
        String insert= "INSERT INTO librosmario.ri_remito_item ( ri_remito_re,ri_cantidad," +
                "ri_nombre_libro,ri_autor,ri_editorial,ri_isbn,ri_motivo,ri_factura)"
          + " values (" + r.getRe_remito_k() + ","  +  ri.getRi_cantidad() 
          + ",'" + ri.getRi_nombre_libro().replaceAll("'", "''") + "','" 
          + ri.getRi_autor().replaceAll("'", "''") + "','" + ri.getRi_editorial().replaceAll("'", "''") + "','"
          + ri.getRi_isbn() + "','"+  ri.getRi_motivo() + "','"+ ri.getRi_factura() +"');";
        
        
        try {
            ps = connection.createStatement();
            ps.executeUpdate(insert);        
            Statement s = connection.createStatement();
            Sistema.logger.debug("antes de insertar:" + insert);
            String sql="select last_insert_id() remito_item_k from librosmario.ri_remito_item;";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                ri.setRi_remito_item_k(rs.getInt("remito_item_k"));
            }
            if (commit) {connection.commit();}    
            Sistema.logger.debug("inserté:" + insert);
            result="ok";
        }
        catch (SQLException e) {
        try {
                Sistema.logger.error("Error trying to insert remito item to the database: " + e.getMessage() + 
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
 
 public ArrayList<RemitoItem> getRemitoItemsDeRemito(Remito r) {
 	ArrayList<RemitoItem> list = new ArrayList<RemitoItem>();
 	Connection c = null;
		String sql="";
		String select=" select ri_remito_item_k,ri_remito_re,ri_catalogo_cg,ri_cantidad,ri_nombre_libro," +
				"ri_autor,ri_editorial,ri_isbn,ri_motivo,ri_factura" +
				" FROM librosmario.ri_remito_item";		
		String where="where ri_remito_re=" + r.getRe_remito_k() + " ";
					  
		String orderby="ORDER BY ri_remito_item_k";
		//DOMConfigurator.configure("log4j.xml");
		
		try {
		
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + " " + where + " " + orderby + ";";
		ResultSet rs = s.executeQuery(sql);
		Sistema.logger.debug("getRemitoItemsDeRemito Query:" + sql );	
		while (rs.next()) {
			RemitoItem ri=new RemitoItem();		
			
			ri.setRi_remito_item_k(rs.getInt("ri_remito_item_k"));			
			//logger.debug(rs.getString("cl_telefono_fijo"));
			ri.setRi_remito_re(r);
			//logger.debug(rs.getString("cl_telefono_laboral"));
			ri.setRi_nombre_libro(rs.getString("ri_nombre_libro"));
			//logger.debug(rs.getString("cl_telefono_movil"));
			ri.setRi_autor(rs.getString("ri_autor"));
			//logger.debug(rs.getString("cl_telefono_otro"));
			ri.setRi_editorial(rs.getString("ri_editorial"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			ri.setRi_isbn(rs.getString("ri_isbn"));			
			ri.setRi_cantidad(rs.getInt("ri_cantidad"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			ri.setRi_motivo(rs.getString("ri_motivo"));
			//logger.debug(rs.getString("cl_telefono_otro_descr"));
			ri.setRi_factura(rs.getString("ri_factura"));
			list.add(ri);
		}
		
		}  
		
		catch (SQLException e)
		{
			Sistema.logger.error("Error trying to query to the database: " + e.getMessage() + 
					"select: " + sql);
			} catch (Exception e) {
				Sistema.logger.error("Error : " + e.getMessage() + 
					"in RemitoItemDAO:" + select);
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
