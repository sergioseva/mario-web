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
import model.librosmario.Remito;
import model.librosmario.RemitoItem;
import model.librosmario.Sistema;

public class RemitoDAO {
	
	
	public Remito insert(Remito r) throws SQLException {
    	Sistema.logger.debug("insertando remito");
        Connection connection = null;      
        
        
        connection = Sistema.getConnection();
        connection.setAutoCommit(false);

        try {
        	PreparedStatement ps = connection.prepareStatement("INSERT INTO librosmario.re_remito ( re_fecha," +
        	        "re_distribuidora_ed,re_observaciones)"
        	          + " values (now(),?,? );");
        	
    		ps.setInt(1, r.getRe_distribuidora_ed().getEd_editorial_k() );  
    		ps.setString(2, r.getRe_observaciones() );
    		ps.executeUpdate();
            Statement s = connection.createStatement();
            String sql="select last_insert_id() remito_k from librosmario.re_remito;";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                r.setRe_remito_k(rs.getInt("remito_k"));
            }
            
            for (RemitoItem ri:r.getItems()) {
                Sistema.logger.debug("insertando remito " + ri.getRi_nombre_libro() );
                RemitoItemDAO riDAO=new RemitoItemDAO(); 
                riDAO.insert(r,ri,false,connection);
            }
            
            connection.commit();    
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
                throw e;
            }
                            
        }
        catch (Exception e) {
            Sistema.logger.error("Error insertando remito: " + e.getMessage() );
        }
        finally {
    		connection.close();
    	}
        return r;
    }
	
	public List<Remito> getRemitos(Editorial distribuidora,Date fechaDesde, Date fechaHasta, String libro) {
		Remito re;
		Sistema.logger.debug("consultando remitos");
		Connection c = null;
		String select="select distinct re_remito_k,re_fecha,re_distribuidora_ed,re_observaciones" +				
				" from librosmario.re_remito,librosmario.ri_remito_item " +
				" where ri_remito_re=re_remito_k";
		
		List<Remito> remitos = new ArrayList<Remito>();
		String where="";
		String orderby="ORDER BY re_remito_k";
		String sql = null;
		
		if (distribuidora !=null) 
		{where+= " re_remito.re_distribuidora_ed=" + distribuidora.getEd_editorial_k() + " AND"; }
		
		if (fechaDesde!=null) 
		{where+= " DATE_FORMAT(re_fecha,'%Y-%m-%d')>='" + fechaDesde.toString() + "' AND" ; }
		
		if (fechaHasta!=null) 
		{where+= " DATE_FORMAT(re_fecha,'%Y-%m-%d')<='" + fechaHasta.toString() + "' AND" ; }
		
		if (libro!="") 
		{where+= " ri_nombre_libro like '%" + libro + "%' AND" ; }
		
		//le saco el ultimo and
		if 	(where!="") 
		{where=where.substring(0, where.length()-3-1);
		 where = " AND " + where;
		}
		
		try {
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + " " + where +" "+ orderby;
		Sistema.logger.debug("sql:" + sql);
		ResultSet rs = s.executeQuery(sql);
		while (rs.next()) {
				re=new Remito();
				re.setRe_remito_k(rs.getInt("re_remito_k"));
				EditorialDAO edDAO=new EditorialDAO();
				re.setRe_distribuidora_ed(edDAO.getEditorial(rs.getInt("re_distribuidora_ed")));
				re.setRe_fecha(rs.getDate("re_fecha"));
				re.setRe_observaciones(rs.getString("re_observaciones"));
				RemitoItemDAO piDAO=new RemitoItemDAO();
				re.setItems(piDAO.getRemitoItemsDeRemito(re));
				remitos.add(re);
		}
		}catch (SQLException e)
		{
			Sistema.logger.error("Error trying to query to the database: " + e.getMessage() + 
					"select: " + sql );
			} catch (Exception e) {
			Sistema.logger.error("Error : " + e.getMessage() + 
					"in PedidoDAO:" + select);
		}
		finally {
			try {
			c.close();
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}
		}
		Sistema.logger.debug("retornando remitos:" + remitos.size());
		return remitos;
	}

}
