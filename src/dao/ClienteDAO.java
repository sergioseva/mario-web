package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.librosmario.Cliente;
import model.librosmario.Sistema;


import exceptions.EliminarClienteTienePedidosException;

public class ClienteDAO {
	//static Logger logger = Logger.getLogger(ClienteDAO.class);

	public Cliente getCliente(int cl_cliente_k) {
		Cliente cliente=new Cliente();
		Connection c = null;
		String select="SELECT cl_cliente_k,cl_nombre,cl_direccion,cl_telefono_fijo,cl_telefono_movil,cl_telefono_laboral,cl_telefono_otro,cl_telefono_otro_descr,cl_email FROM cl_cliente where cl_cliente_k=" + cl_cliente_k;
		try {
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		Sistema.logger.debug("Select getCliente:" + select);
		ResultSet rs = s.executeQuery(select);
		while (rs.next()) {
			Sistema.logger.debug(rs.getString("cl_cliente_k"));
			cliente.setCl_cliente_k(rs.getInt("cl_cliente_k"));
			Sistema.logger.debug("cl_direccion :" + rs.getString("cl_direccion"));
			cliente.setCl_direccion(rs.getString("cl_direccion"));
			Sistema.logger.debug("cl_email:" + rs.getString("cl_email"));
			//cliente.setCl_email(rs.getString(rs.getString("cl_email")));
			Sistema.logger.debug("cl_nombre:" + rs.getString("cl_nombre"));
			cliente.setCl_nombre(rs.getString("cl_nombre"));
			Sistema.logger.debug("cl_telefono_fijo" + rs.getString("cl_telefono_fijo"));
			cliente.setCl_telefono_fijo(rs.getString("cl_telefono_fijo"));
			Sistema.logger.debug("cl_telefono_laboral" + rs.getString("cl_telefono_laboral"));
			cliente.setCl_telefono_laboral(rs.getString("cl_telefono_laboral"));
			Sistema.logger.debug("cl_telefono_movil" + rs.getString("cl_telefono_movil"));
			cliente.setCl_telefono_movil(rs.getString("cl_telefono_movil"));
			Sistema.logger.debug("cl_telefono_otro" + rs.getString("cl_telefono_otro"));
			cliente.setCl_telefono_otro(rs.getString("cl_telefono_otro"));
			Sistema.logger.debug("cl_telefono_otro_descr" + rs.getString("cl_telefono_otro_descr"));
			cliente.setCl_telefono_otro_descr(rs.getString("cl_telefono_otro_descr"));
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
		
		return cliente;
	}
	public List<Cliente> getClientes() {
		List<Cliente> list = new ArrayList<Cliente>();
		Connection c = null;
		String sql="";
		String select="SELECT cl_cliente_k,cl_direccion,cl_email,cl_nombre,cl_telefono_fijo,cl_telefono_laboral,cl_telefono_movil,cl_telefono_otro,cl_telefono_otro_descr FROM librosmario.cl_cliente";		
		String orderby="ORDER BY cl_nombre;";
		//DOMConfigurator.configure("log4j.xml");
		
		try {
		Sistema.logger.debug("comenzando getClientes");	
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + " " + orderby;
		ResultSet rs = s.executeQuery(sql);
		
		while (rs.next()) {
			Cliente cliente=new Cliente();		
			//Sistema.logger.debug(rs.getString("cl_cliente_k"));
			cliente.setCl_cliente_k(rs.getInt("cl_cliente_k"));
			//Sistema.logger.debug("cl_direccion :" + rs.getString("cl_direccion"));
			cliente.setCl_direccion(rs.getString("cl_direccion"));
			//Sistema.logger.debug("cl_email:" + rs.getString("cl_email"));
			//cliente.setCl_email(rs.getString(rs.getString("cl_email")));
			//Sistema.logger.debug("cl_nombre:" + rs.getString("cl_nombre"));
			cliente.setCl_nombre(rs.getString("cl_nombre"));
			//Sistema.logger.debug(rs.getString("cl_telefono_fijo"));
			cliente.setCl_telefono_fijo(rs.getString("cl_telefono_fijo"));
			//Sistema.logger.debug(rs.getString("cl_telefono_laboral"));
			cliente.setCl_telefono_laboral(rs.getString("cl_telefono_laboral"));
			//Sistema.logger.debug(rs.getString("cl_telefono_movil"));
			cliente.setCl_telefono_movil(rs.getString("cl_telefono_movil"));
			//Sistema.logger.debug(rs.getString("cl_telefono_otro"));
			cliente.setCl_telefono_otro(rs.getString("cl_telefono_otro"));
			//Sistema.logger.debug(rs.getString("cl_telefono_otro_descr"));
			cliente.setCl_telefono_otro_descr(rs.getString("cl_telefono_otro_descr"));
			list.add(cliente);
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
			Sistema.logger.debug("fin getClientes");	
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}
		}
		return list;
	}
	
	public String update(Cliente cliente) throws SQLException {
		Connection connection = null;
		String result="";
		try {
		connection = Sistema.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement ps = connection.prepareStatement("UPDATE librosmario.cl_cliente " +
				"SET cl_nombre = ?, cl_direccion = ?,cl_telefono_fijo = ?," +
				"cl_telefono_movil = ?,cl_telefono_laboral = ?," +
				"cl_telefono_otro = ?, cl_telefono_otro_descr = ?,cl_email = ? " +
				"WHERE cl_cliente_k = ?;");
		ps.setString(1, cliente.getCl_nombre() );
		ps.setString(2, cliente.getCl_direccion());
		ps.setString(3, cliente.getCl_telefono_fijo());
		ps.setString(4, cliente.getCl_telefono_movil());
		ps.setString(5, cliente.getCl_telefono_laboral());
		ps.setString(6, cliente.getCl_telefono_otro());
		ps.setString(7, cliente.getCl_telefono_otro_descr());
		ps.setString(8, cliente.getCl_email());
		ps.setInt(9, cliente.getCl_cliente_k());
		
		ps.executeUpdate();			
		connection.commit();				
		Sistema.logger.debug(ps.toString());
		result="ok";
		}
		catch (SQLException e) {
			try {
				connection.rollback();
				result="Error trying to update cliente to the database: " + e.getMessage();
				Sistema.logger.error("Error trying to update cliente to the database: " + e.getMessage() );
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
	
	public Cliente insert(Cliente cliente) throws SQLException {
		Connection connection = null;
		Statement ps;		
		
		connection = Sistema.getConnection();
		connection.setAutoCommit(false);
		Sistema.logger.debug("insertando cliente:" + cliente.getCl_nombre());
		String insert= "INSERT INTO librosmario.cl_cliente " +
				"(cl_nombre," +
				"cl_direccion," +
				"cl_telefono_fijo," +
				"cl_telefono_movil," +
				"cl_telefono_laboral," +
				"cl_telefono_otro," +
				"cl_telefono_otro_descr," +
				"cl_email) " +
				"VALUES " +
				"('" + cliente.getCl_nombre() + 
				"','" + cliente.getCl_direccion() + 
				"','" + cliente.getCl_telefono_fijo() + 
				"','" + cliente.getCl_telefono_movil() + 
				"','" + cliente.getCl_telefono_laboral() + 
				"','" + cliente.getCl_telefono_otro() + 
				"','" + cliente.getCl_telefono_otro_descr() + 
				"','" + cliente.getCl_email() + "');";
		Sistema.logger.debug(insert);
		
		try {
			ps = connection.createStatement();
			ps.executeUpdate(insert);
			connection.commit();	
			Statement s = connection.createStatement();
			Sistema.logger.debug("inserté:" + insert);
			String sql="select last_insert_id() cliente_k from librosmario.cl_cliente;";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                cliente.setCl_cliente_k(rs.getInt("cliente_k"));
            }
			return cliente;
		}
		catch (SQLException e) {
		try {
				Sistema.logger.error("Error trying to insert cliente to the database: " + e.getMessage() + 
				"insert: " + insert);					
				connection.rollback();				
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}
			throw e;
							
		}
		finally {
		connection.close();
		}
		
	}
	
	public void delete_cliente(Cliente cliente) throws SQLException, EliminarClienteTienePedidosException {
		Connection connection = null;
		Statement ps;		
		
		connection = Sistema.getConnection();
		connection.setAutoCommit(false);
		Sistema.logger.debug("eliminando cliente:" + cliente.getCl_cliente_k() + "-" + cliente.getCl_nombre());
		String sql= "delete from librosmario.cl_cliente " +
				"where cl_cliente_k=" + cliente.getCl_cliente_k();
		Sistema.logger.debug(sql);
		
		try {
			Statement s=connection.createStatement();
			Sistema.logger.debug("verificando si el cliente tiene pedidos");
			String sql_validation="select * from librosmario.pe_pedido" +
					" where pe_cliente_cl=" + cliente.getCl_cliente_k();
			Sistema.logger.debug(sql_validation);
            ResultSet rs = s.executeQuery(sql_validation);
            if (rs.next()) {
            	Sistema.logger.debug("El cliente tiene pedidos");
                EliminarClienteTienePedidosException e2=new EliminarClienteTienePedidosException("El cliente tiene pedidos hechos, no se puede eliminar");
                throw e2;
            }
            
			ps = connection.createStatement();
			ps.executeUpdate(sql);
			connection.commit();	
			Sistema.logger.debug("eliminè:" + sql);
			}
		catch (SQLException e) {
		try {
				Sistema.logger.error("Error trying to delete cliente into the database: " + e.getMessage() + 
				"insert: " + sql);					
				connection.rollback();				
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}
			throw e;
							
		}
		finally {
		connection.close();
		}
		
	}
	
}
