package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.librosmario.Catalogo;
import model.librosmario.Sistema;


public class CatalogoDAO {
	//static Logger logger = Logger.getLogger(CatalogoDAO.class);

	public Catalogo getCatalogo(int cg_catalogo_k) {
		Catalogo cg=new Catalogo();
		Connection c = null;
		String select="SELECT * FROM cg_catalogo where cg_catalogo_k=" + cg_catalogo_k;
		try {
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery(select);
		
		
		while (rs.next()) {
		cg.setCg_catalogo_k(rs.getInt("cg_catalogo_k"));
		cg.setCg_codigo_luongo(rs.getInt("cg_codigo_luongo"));
		cg.setCg_autor(rs.getString("cg_autor"));
		cg.setCg_descripcion(rs.getString("cg_descripcion"));
		cg.setCg_precio(rs.getDouble("cg_precio"));
		cg.setCg_pedido(rs.getInt("cg_pedido")); 
		cg.setCg_vigente(rs.getBoolean("cg_vigente"));
		cg.setCg_editorial(rs.getString("cg_editorial"));
		cg.setCg_tema(rs.getString("cg_tema"));
		cg.setCg_isbn(rs.getString("cg_isbn"));
		cg.setCg_pst(rs.getInt("cg_pst"));
		cg.setCg_observaciones(rs.getString("cg_observaciones"));
		cg.setCg_creador(rs.getString("cg_creador"));
		cg.setCg_inputdate(rs.getDate("cg_inputdate"));
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
		
		return cg;
		}
		

	
	public List<Catalogo> getCatalogos(String autor, String descripcion, String editorial, String tema, String isbn,boolean buscaComienzo) throws SQLException {
		List<Catalogo> list = new ArrayList<Catalogo>();
		Connection c = null;
		try {
		String select="SELECT * FROM cg_catalogo";
		String where="";
		String orderby="ORDER BY cg_descripcion";
		String comienzo="";
		//DOMConfigurator.configure("log4j.xml");
		
		if (!buscaComienzo) 
		   {comienzo="%";}
		
		String sql="";
		if (autor!="") 
			{where+= this.generaLike(autor, "cg_autor", buscaComienzo); }
		
		if (descripcion!="") 
			{where+= this.generaLike(descripcion, "cg_descripcion", buscaComienzo);}	
			
		if (editorial!="") 
			{where+=this.generaLike(editorial, "cg_editorial", buscaComienzo);};				
		
		if (tema!="") 
			{where+=this.generaLike(tema, "cg_tema", buscaComienzo); }

		if (isbn!="") 
			{where+=this.generaLike(isbn, "cg_isbn", buscaComienzo);}			

		Sistema.logger.debug("where:" + where);	
		//le saco el ultimo OR
		if 	(where!="") 
				{where=where.substring(0, where.length()-2-1);
				 where = "WHERE " + where;
				}
		
		
		
			
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select + " " + where +" "+ orderby;
		Sistema.logger.debug("buscando catalogos:" + sql);	
		ResultSet rs = s.executeQuery(sql);
		Sistema.logger.debug("fin ejecucion query en la base de datos:" + sql);	
		
		while (rs.next()) {
		Catalogo catalogo = new Catalogo();
		catalogo.setCg_catalogo_k(rs.getInt("cg_catalogo_k"));
		catalogo.setCg_codigo_luongo(rs.getInt("cg_codigo_luongo"));
		catalogo.setCg_autor(rs.getString("cg_autor"));
		String descr=rs.getString("cg_descripcion");
		if (descr.startsWith(".")) {
			descr=descr.substring(1);
		}
		descr=descr.replace('§', '°');
		descr=descr.replace('¥', 'Ñ');
		catalogo.setCg_descripcion(descr);
		
		catalogo.setCg_precio(rs.getDouble("cg_precio"));
		catalogo.setCg_pedido(rs.getInt("cg_pedido")); 
		catalogo.setCg_vigente(rs.getBoolean("cg_vigente"));
		catalogo.setCg_editorial(rs.getString("cg_editorial"));
		catalogo.setCg_tema(rs.getString("cg_tema"));
		catalogo.setCg_isbn(rs.getString("cg_isbn"));
		catalogo.setCg_pst(rs.getInt("cg_pst"));
		catalogo.setCg_observaciones(rs.getString("cg_observaciones"));
		catalogo.setCg_creador(rs.getString("cg_creador"));
		catalogo.setCg_inputdate(rs.getDate("cg_inputdate"));
		list.add(catalogo);
		}
		} finally {
		c.close();
		}
		Sistema.logger.debug("fin de retorno de lista");	
		return list;
		}

		public String update(Catalogo catalogo) throws SQLException {
		Connection connection = null;
		String result="";
		try {
		connection = Sistema.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement ps = connection.prepareStatement("UPDATE cg_catalogo SET cg_autor=?, cg_descripcion=?, " +
				"cg_precio=?,cg_pedido=?,cg_editorial=?,cg_tema=?,cg_isbn=?, " +			
				"cg_pst=?,cg_observaciones=?,cg_creador=?,cg_inputdate=now() " +				
				"WHERE cg_catalogo_k=?");
		ps.setString(1, catalogo.getCg_autor() );
		ps.setString(2, catalogo.getCg_descripcion());
		ps.setDouble(3, catalogo.getCg_precio());
		ps.setInt(4, catalogo.getCg_pedido());
		ps.setString(5, catalogo.getCg_editorial());
		ps.setString(6, catalogo.getCg_tema());
		ps.setString(7, catalogo.getCg_isbn());
		ps.setInt(8, catalogo.getCg_pst());
		ps.setString(9, catalogo.getCg_observaciones());
		ps.setString(10, catalogo.getCg_creador());
		ps.setInt(11, catalogo.getCg_catalogo_k());
		ps.executeUpdate();			
		connection.commit();				
		Sistema.logger.debug(ps.toString());
		result="ok";
		}
		catch (SQLException e) {
			try {
				connection.rollback();
				result="Error trying to update to the database: " + e.getMessage();
				Sistema.logger.error("Error trying to update to the database: " + e.getMessage() );
			} catch (SQLException e1) {					
				Sistema.logger.error(e1.getMessage());
			}					
		} finally {
		connection.close();
		}
		return result;
		}
		
		public String insert(Catalogo catalogo) throws SQLException {
			Connection connection = null;
			Statement ps;
			String result="";
			
			connection = Sistema.getConnection();
			connection.setAutoCommit(false);
			Sistema.logger.debug("insertando");
			String insert= "INSERT INTO librosmario.cg_catalogo ( cg_autor, cg_descripcion, " +
		      "cg_precio, cg_pedido, cg_editorial, cg_tema, cg_isbn, cg_pst, " +
		      "cg_observaciones, cg_creador,cg_inputdate) " +
		      " values ('" + catalogo.getCg_autor() + "','" + catalogo.getCg_descripcion() + "'," + catalogo.getCg_precio() + "," 
		      + 0 + ",'"  + catalogo.getCg_editorial() + "','" + catalogo.getCg_tema() + "','" + catalogo.getCg_isbn() + "'," + 0 + ",'"
		      + catalogo.getCg_observaciones() + "','librosmario',now());";
			Sistema.logger.debug(insert);
			try {
				ps = connection.createStatement();
				ps.executeUpdate(insert);
				connection.commit();	
				Sistema.logger.debug("inserté:" + insert);
				result="ok";
			}
			catch (SQLException e) {
			try {
					Sistema.logger.error("Error trying to insert to the database: " + e.getMessage() + 
					"insert: " + insert);					
					connection.rollback();
					result= "Error trying to insert to the database: " + e.getMessage() + 
					"insert: " + insert;
				} catch (SQLException e1) {					
					Sistema.logger.error(e1.getMessage());
				}
								
			}
			finally {
				connection.close();
				}
			return result;
		}
		
		public String generaLike(String busqueda,String campo,boolean buscaComienzo ) {
			String like="";			
			try {
			if (buscaComienzo) 
			   {like= " " + campo + " like '" + busqueda + "%' OR"; }
			else
				{
				String []split=busqueda.split(" "); 
				int i=0;
				for (i=0;i<split.length;i++) {
					String term=split[i].trim();
					Sistema.logger.debug("termino:" + term);
					if (term.equalsIgnoreCase("la") ||
						term.equalsIgnoreCase("el") ||
						term.equalsIgnoreCase("las") ||
						term.equalsIgnoreCase("los") ||
						term.equalsIgnoreCase("de") ||
						term.equalsIgnoreCase("del") || 
						(term.length()==1 && !term.equalsIgnoreCase("0")
										&& !term.equalsIgnoreCase("1")
										&& !term.equalsIgnoreCase("2")
										&& !term.equalsIgnoreCase("3")
										&& !term.equalsIgnoreCase("4")
										&& !term.equalsIgnoreCase("5")
										&& !term.equalsIgnoreCase("6")
										&& !term.equalsIgnoreCase("7")
										&& !term.equalsIgnoreCase("8")
										&& !term.equalsIgnoreCase("9")
						) 
						) {
						Sistema.logger.debug("articulo omitido en la busqueda:" + term);
					} else {
						//busco si tiene el signo +
						Sistema.logger.debug("busco si tiene el signo +");
						//TODO buscar una mejor solucion,tengo que sistituir el + porque split no funciona con +
						term=term.replace('+','¬');
						String []split_mas=term.split("¬");
						Sistema.logger.debug("encontré " + split_mas.length + " terminos");
						int i1=0;
						String condicion_and="(";
						for (i1=0;i1<split_mas.length;i1++) {
							String term_mas=split_mas[i1].trim();
							condicion_and=condicion_and + " " + campo + " like '%" + term_mas + "%' AND";
							Sistema.logger.debug("condicion_and:" + condicion_and);
						}
						//le saco el ultimo and y le agrego parentesis )
						if 	(condicion_and!="") 
						{condicion_and=condicion_and.substring(0, condicion_and.length()-3-1);
						condicion_and+=")" ;
						}
						like+= condicion_and + " OR";
						Sistema.logger.debug(like);
					}
				};
			}
			Sistema.logger.debug("retorno genera like:" + like);			
			
			} catch (Exception e) {
				Sistema.logger.error(e.getMessage());				
			}
			return like;
		}
	
}
