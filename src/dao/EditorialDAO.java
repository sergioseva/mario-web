package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.librosmario.Editorial;
import model.librosmario.Sistema;

public class EditorialDAO {
	
	//static Logger logger = Sistema.logger.getLogger(EditorialDAO.class);
	private static EditorialDAO instance=new EditorialDAO();
	
	public static EditorialDAO getInstance() {
		return instance;
	}

	public Editorial getEditorial(int ed_editorial_k) {
		Editorial ed=new Editorial();
		Connection c = null;
		String select="SELECT * FROM ed_editorial where ed_editorial_k=" + ed_editorial_k;
		try {
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery(select);
		
		
		while (rs.next()) {
		
		ed.setEd_editorial_k(rs.getInt("ed_editorial_k"));
		ed.setEd_descripcion(rs.getString("ed_descripcion"));
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
		
		return ed;
		}
	
	public List<Editorial> getEditoriales()  {
		List<Editorial> list = new ArrayList<Editorial>();
		Connection c = null;
		try {
		String select="SELECT * FROM ed_editorial";
		
		//DOMConfigurator.configure("log4j.xml");
		
		
		Sistema.logger.debug("EditorialDAO");
		String sql="";
			
		c = Sistema.getConnection();
		Statement s = c.createStatement();
		sql=select;
		ResultSet rs = s.executeQuery(sql);
		
		
		while (rs.next()) {
		Editorial editorial= new Editorial();
		//Sistema.logger.debug(rs.getInt("ed_editorial_k"));
		editorial.setEd_editorial_k(rs.getInt("ed_editorial_k"));
		//Sistema.logger.debug(rs.getString("ed_descripcion"));
		editorial.setEd_descripcion(rs.getString("ed_descripcion"));		
		list.add(editorial);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return list;
		}

}
