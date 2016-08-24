package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.librosmario.Cliente;
import model.librosmario.Pedido;
import model.librosmario.PedidoItem;
import model.librosmario.Sistema;

public class PedidoDAO {
    //static Logger logger = Sistema.logger.getLogger(PedidoDAO.class);

    public Pedido getPedido(int pe_pedido_k) {
    	
    	return this.getPedido(pe_pedido_k,true);
		
	}
    
    public Pedido getPedido(int pe_pedido_k, boolean getItems) {
		Pedido pe=new Pedido();
		Connection c = null;
		String select="SELECT pe_pedido.pe_pedido_k,pe_pedido.pe_cliente_cl,pe_pedido.pe_fecha," +
				"pe_pedido.pe_senia,pe_pedido.pe_total,pe_pedido.pe_adomicilio,pe_pedido.pe_domicilio," +
				"pe_pedido.pe_fecha_envio,pe_pedido.pe_observaciones FROM pe_pedido where pe_pedido_k=" + pe_pedido_k;
		try {
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		Sistema.logger.debug("getPedido:" + pe_pedido_k);
		ResultSet rs = s.executeQuery(select);
		while (rs.next()) {
		pe.setPe_pedido_k(rs.getInt("pe_pedido_k"));
		ClienteDAO clDAO=new ClienteDAO();
		pe.setCliente(clDAO.getCliente(rs.getInt("pe_cliente_cl")));
		pe.setPe_fecha(rs.getDate("pe_fecha"));
		pe.setPe_senia(rs.getDouble("pe_senia"));
		pe.setPe_total(rs.getDouble("pe_total"));
		pe.setPe_adomicilio(rs.getBoolean("pe_adomicilio"));
		pe.setPe_domicilio(rs.getString("pe_domicilio"));
		pe.setPe_fecha_envio(rs.getDate("pe_fecha_envio"));
		pe.setPe_observaciones(rs.getString("pe_observaciones"));
		if (getItems) {
			PedidoItemDAO piDAO=new PedidoItemDAO();
			pe.setItems(piDAO.getPedidoItemsDePedido(pe));
			}
		}
		}catch (SQLException e)
		{
			Sistema.logger.error("Error trying to query to the database: " + e.getMessage() + 
					"select: " + select );
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
		
		return pe;
		}
    
    public List<Pedido> getPedidos(Cliente cliente,Date fechaDesde, Date fechaHasta, String libro) {
		Pedido pe;
		Sistema.logger.debug("consultando pedidos");
		Connection c = null;
		String select="SELECT distinct pe_pedido.pe_pedido_k,pe_pedido.pe_cliente_cl,pe_pedido.pe_fecha," +
				"pe_pedido.pe_senia,pe_pedido.pe_total,pe_pedido.pe_adomicilio,pe_pedido.pe_domicilio," +
				"pe_pedido.pe_fecha_envio,pe_pedido.pe_observaciones FROM pe_pedido,pi_pedido_item " +
				"WHERE pe_pedido_k=pi_pedido_pe";
		
		List<Pedido> pedidos = new ArrayList<Pedido>();
		String where="";
		String orderby="ORDER BY pe_pedido.pe_pedido_k";
		String sql = null;
		
		if (cliente !=null) 
		{where+= " pe_pedido.pe_cliente_cl=" + cliente.getCl_cliente_k() + " AND"; }
		
		if (fechaDesde!=null) 
		{where+= " DATE_FORMAT(pe_pedido.pe_fecha,'%Y-%m-%d')>='" + fechaDesde.toString() + "' AND" ; }
		
		if (fechaHasta!=null) 
		{where+= " DATE_FORMAT(pe_pedido.pe_fecha,'%Y-%m-%d')<='" + fechaHasta.toString() + "' AND" ; }
		
		if (libro!="") 
		{where+= " pi_nombre_libro like '%" + libro + "%' AND" ; }
		
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
				pe=new Pedido();
				pe.setPe_pedido_k(rs.getInt("pe_pedido_k"));
				ClienteDAO clDAO=new ClienteDAO();
				pe.setCliente(clDAO.getCliente(rs.getInt("pe_cliente_cl")));
				pe.setPe_fecha(rs.getDate("pe_fecha"));
				pe.setPe_senia(rs.getDouble("pe_senia"));
				pe.setPe_total(rs.getDouble("pe_total"));
				pe.setPe_adomicilio(rs.getBoolean("pe_adomicilio"));
				pe.setPe_domicilio(rs.getString("pe_domicilio"));
				pe.setPe_fecha_envio(rs.getDate("pe_fecha_envio"));
				pe.setPe_observaciones(rs.getString("pe_observaciones"));
				PedidoItemDAO piDAO=new PedidoItemDAO();
				pe.setItems(piDAO.getPedidoItemsDePedido(pe));
				pedidos.add(pe);
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
		Sistema.logger.debug("retornando pedidos:" + pedidos.size());
		return pedidos;
	}
        
    
    public Pedido insert(Pedido pedido) throws SQLException {
    	Sistema.logger.debug("insertando pedido");
        Connection connection = null;      
        
        
        connection = Sistema.getConnection();
        connection.setAutoCommit(false);
        
        /*String insert= "INSERT INTO librosmario.pe_pedido ( pe_cliente_cl,pe_fecha," +
        "pe_senia,pe_total,pe_adomicilio,pe_domicilio,pe_fecha_envio,pe_observaciones)"
          + " values (" + pedido.getCliente().getCl_cliente_k() + ",now(),"    
          + pedido.getPe_senia() + "," 
          + pedido.getPe_total() + "," 
          + pedido.isPe_adomicilio() + ",'" 
          + pedido.getPe_domicilio() + "'," 
          + "STR_TO_DATE('" + pedido.getFechaToString() + "','%m/%d/%Y'),'" 
          +  pedido.getPe_observaciones()+"');";
        Sistema.logger.debug(insert);*/
        try {
        	PreparedStatement ps = connection.prepareStatement("INSERT INTO librosmario.pe_pedido ( pe_cliente_cl,pe_fecha," +
        	        "pe_senia,pe_total,pe_adomicilio,pe_domicilio,pe_fecha_envio,pe_observaciones)"
        	          + " values (? ,now(),? ,?,?,?,?,?);");
        	
    		ps.setInt(1, pedido.getCliente().getCl_cliente_k() );
    		ps.setDouble(2, pedido.getPe_senia());
    		ps.setDouble(3, pedido.getPe_total());
    		ps.setBoolean(4, pedido.isPe_adomicilio());
    		ps.setString(5, pedido.getPe_domicilio());
    		ps.setDate(6, pedido.getPe_fecha_envio());
    		ps.setString(7, pedido.getPe_observaciones());
    		ps.executeUpdate();
            Statement s = connection.createStatement();
            String sql="select last_insert_id() pedido_k from librosmario.pe_pedido;";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                pedido.setPe_pedido_k(rs.getInt("pedido_k"));
            }
            Sistema.logger.debug("cantidad de items " + pedido.cantidadItems());
            for (PedidoItem pi:pedido.getItems()) {
                Sistema.logger.debug("insertando pedido " + pi.getPi_nombre_libro() );
                PedidoItemDAO piDAO=new PedidoItemDAO(); 
                piDAO.insert(pedido,pi,false,connection);
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
            Sistema.logger.error("Error insertando pedido: " + e.getMessage() );
        }
        finally {
    		connection.close();
    	}
        return pedido;
    }
}
