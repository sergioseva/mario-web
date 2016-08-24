package model.librosmario;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.linuxense.javadbf.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;


public class CatalogoConverter {
	//static Logger logger = Logger.getLogger(CatalogoConverter.class);
	
	public String importarLuongo ()  {		
		final int CODIGO=0;
		final int AUTOR=1;
		final int DESCRIPCION=2;
		final int EDITORIAL=3;
		final int PRECIO=4;
		final int ISBN=5;
		final int PEDIDO=6;
		final int TEMA=7;
		final int OBSERVACIONES=8;
		final int PSTOCK=9;
		int cantRegistrosOk=0;
		int cantRegistrosSalteados=0;
		int codigo;
		String autor;
		String descripcion;
		Double precio;
		int pedido;		
		String editorial;
		String tema;
		long isbn;
		int pstock;
		String observaciones;
		Connection connection = null;
		Statement ps;
		//DOMConfigurator.configure("log4j.xml");
		Sistema.logger.debug("importar luongo excel");
	
		try{
			try	{
			connection=Sistema.getConnection();			
			connection.setAutoCommit(false);
			}
			catch( SQLException e) {
				Sistema.logger.error("Error trying to connecto to the database: " + e.getMessage());
			}
		
		InputStream inputStream = new FileInputStream("C:\\luongo\\ART.xlsx"); // take xls file as program argument
		
        //POIFSFileSystem fs = new POIFSFileSystem( inputStream );
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet  sheet = wb.getSheetAt(0);
        Row row;
        
		
		
		// Now, lets us start reading the rows
		//
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		Sistema.logger.debug(dateFormat.format(date));
		
		
							
			connection.setAutoCommit(false);		
			ps=connection.createStatement();
			ps.execute(" SET SQL_SAFE_UPDATES=0;");
			ps.execute("delete from librosmario.cg_catalogo " +
					"where cg_creador='luongo';");
			
		
			Sistema.logger.debug("Cantidad de registros: " + sheet.getLastRowNum());
		int id;
		boolean skip = false;
		int i=0;
		while (!skip) {
		    i++;	
			
		    
			if   (skip) 
				{break;};
			//Sistema.logger.debug("siguiente registro");
			row = sheet.getRow(i);
			try {
			id =  (int) row.getCell(CODIGO).getNumericCellValue();}
			catch (NullPointerException e) {
				id=0;
				skip=true;
			}
		    if (id!=0) {
			precio=null;
			pedido=0;
			pstock=0;
			isbn=0;
			//Sistema.logger.debug("importando codigo");
			codigo=id;			
			//System.out.println( codigo);
			Sistema.logger.debug("codigo:" + codigo);
			try{
			autor=(row.getCell(AUTOR).getStringCellValue()).replace("'","''");}
			catch (NullPointerException e) {
				autor="";				
			}
			//Sistema.logger.debug("autor:" + autor);
			try {
			descripcion=(row.getCell(DESCRIPCION).getStringCellValue()).replace("'","''");}
			catch (NullPointerException e) {
				descripcion="";				
			}
			//Sistema.logger.debug("descripcion:" + descripcion);
			try{
			precio=row.getCell(PRECIO).getNumericCellValue();}
			catch (NullPointerException e) {
				precio=0.0;				
			}
			//Sistema.logger.debug("precio:" + precio);
			
			
			//Sistema.logger.debug("importando PEDIDO");
			
			pedido=	 (int) row.getCell(PEDIDO).getNumericCellValue();			
			//Sistema.logger.debug( pedido);
			
			//System.out.println( vigente);
			//Sistema.logger.debug("importando Editorial");
			try {
			editorial=(String) row.getCell(EDITORIAL).getStringCellValue().replace("'","''");}
			catch (NullPointerException e) {
				editorial="";				
			}
			//Sistema.logger.debug("editorial:" + editorial);
			//Sistema.logger.debug("importando tema");
			try{
			tema= row.getCell(TEMA).getStringCellValue();}
			catch (NullPointerException e) {
				tema="";				
			}
			tema=tema.replace("'","''");
			//Sistema.logger.debug("tema:" + tema);
			//Sistema.logger.debug("importando IBN");
			try{
		   isbn=(long) row.getCell(ISBN).getNumericCellValue();}
			catch (NullPointerException e) {
				isbn=0;				
			}
			
			
			//Sistema.logger.debug("importando PTOCK");
			
				pstock=(int) row.getCell(PSTOCK).getNumericCellValue();			
				//System.out.println( pstock);
			
			
			//Sistema.logger.debug("importando Observaciones");
			try {
			observaciones=row.getCell(OBSERVACIONES).getStringCellValue();}
			catch (NullPointerException e) {
				observaciones="";				
			}
			observaciones=observaciones.replace("'","''");
			//System.out.println( observaciones);
			
			//si descripcion empieza con . se lo saco
			if (descripcion.startsWith(".")) {
				descripcion=descripcion.substring(1);
			}
			descripcion=descripcion.replace('§', '°');
			descripcion=descripcion.replace('¥', 'Ñ');
			autor=autor.replace('¥', 'Ñ');
			editorial=editorial.replace('¥', 'Ñ');
			tema=tema.replace('¥', 'Ñ');
			String insert= "INSERT INTO librosmario.cg_catalogo (cg_codigo_luongo, cg_autor, cg_descripcion, " +
		      "cg_precio, cg_pedido, cg_editorial, cg_tema, cg_isbn, cg_pst, " +
		      "cg_observaciones, cg_creador,cg_inputdate) " +
		      " values ("+ codigo +",'" + autor + "','" + descripcion + "'," + precio + "," 
		      + pedido + ",'"  + editorial + "','" + tema + "','" + isbn + "'," + pstock + ",'"
		      + observaciones + "','luongo',now());";
			try {
				//ps = connection.createStatement();
				ps.executeUpdate(insert);				
				cantRegistrosOk++;
			}
			catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {					
					Sistema.logger.error(e1.getMessage());
				}
				Sistema.logger.error("Error trying to insert to the database: " + e.getMessage() + 
						"insert: " + insert);				
			}
			   
			}//if
		else {
			
			Sistema.logger.debug("registro salteado:" );
			cantRegistrosSalteados++;
		}
			/*try {
			//skip=((rowObjects = reader.nextRecord()) == null);
			} catch( DBFException e) {
				Sistema.logger.error("Registro salteado:" + e.getMessage());
				skip=false;
				cantRegistrosSalteados++;
				//rowObjects[CODIGO]=null;
				if (cantRegistrosSalteados>10000) {
					connection.rollback();
					skip=true;
					return "la cantidad de registros salteados sobrepasa los 10000, importacion abortada";					
				}
				continue;
			}*/
		}//while
		
		// By now, we have iterated through all of the rows
		inputStream.close();
		date = new Date();
		connection.commit();
		Sistema.logger.debug("Importacion finalizada:" + dateFormat.format(date) + " se importaron " + cantRegistrosOk + " registros. Cantidad de registros salteados:" + cantRegistrosSalteados);
  	    return "Importacion finalizada:" + dateFormat.format(date) + " se importaron " + cantRegistrosOk + " registros. Cantidad de registros salteados:" + cantRegistrosSalteados;
		}
		catch( SQLException e) {
			Sistema.logger.error("Error deleting catalogo: " + e.getMessage());
				
		}
		catch( DBFException e) {
			Sistema.logger.error("DBFException:" + e.getMessage());
			return "Error, comuniquese con el soporte";
		}
		catch( FileNotFoundException e) {
			Sistema.logger.error("FileNotFoundException:" + e.getMessage());
			return "Archivo no encontrado:" + e.getMessage();
		}	
		catch( IOException e) {
			Sistema.logger.error("IOException:" + e.getMessage());
			return "Error, comuniquese con el soporte";
		}	
		catch( Exception e) {
			Sistema.logger.error("Exception:" + e.toString() );
			return "Error, comuniquese con el soporte";
		}
		finally { 
			   try {
			      if (null != connection) {
			         connection.close();
			      }
			   } catch (SQLException e) {
				   Sistema.logger.error("Error trying to close the connection:" + e.getMessage());
		}		
	}	
	return "ok";	

		
	
 }
	
	
	public String importarLuongoDBF ()  {		
		final int CODIGO=0;
		final int AUTOR=1;
		final int DESCRIPCION=2;
		final int EDITORIAL=3;
		final int PRECIO=4;
		final int ISBN=5;
		final int PEDIDO=6;
		final int TEMA=7;
		final int OBSERVACIONES=8;
		final int PSTOCK=9;
		final int VIGENTE=12;
		int cantRegistrosOk=0;
		int cantRegistrosSalteados=0;
		int codigo;
		String autor;
		String descripcion;
		Double precio;
		int pedido;		
		String editorial;
		String tema;
		long isbn;
		int pstock;
		String observaciones;
		Connection connection = null;
		Statement ps;
		//DOMConfigurator.configure("log4j.xml");
		Sistema.logger.debug("importar luongo");
	
		try{
			try	{
			connection=Sistema.getConnection();			
			connection.setAutoCommit(false);
			}
			catch( SQLException e) {
				Sistema.logger.error("Error trying to connecto to the database: " + e.getMessage());
			}
		
		InputStream inputStream = new FileInputStream("C:\\luongo\\ART.DBF"); // take dbf file as program argument
		
		DBFReader reader = new DBFReader( inputStream);
		// get the field count if you want for some reasons like the following
		//
		
		int numberOfFields = reader.getFieldCount();
		// use this count to fetch all field information
		// if required
		//
		for( int i=0; i<numberOfFields; i++) {
		DBFField field = reader.getField( i);
		// do something with it if you want
		// refer the JavaDoc API reference for more details
		//
		Sistema.logger.debug( field.getName());
		}
		// Now, lets us start reading the rows
		//
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		Sistema.logger.debug(dateFormat.format(date));
		
		
							
			connection.setAutoCommit(false);		
			ps=connection.createStatement();
			ps.execute(" SET SQL_SAFE_UPDATES=0;");
			ps.execute("delete from librosmario.cg_catalogo " +
					"where cg_creador='luongo';");
			
		
			Sistema.logger.debug("Cantidad de registros: " + reader.getRecordCount());
		
		Object []rowObjects;
		boolean skip=false;
		skip=((rowObjects = reader.nextRecord()) == null);
		while( !skip) {
			Sistema.logger.debug("siguiente registro");
		if (rowObjects[CODIGO]!=null) {
			precio=null;
			pedido=0;
			pstock=0;
			isbn=0;
			Sistema.logger.debug("importando codigo");
			codigo=	 ((Double) rowObjects[CODIGO]).intValue();			
			//System.out.println( codigo);
			Sistema.logger.debug("codigo:" + codigo);
			autor=((String) rowObjects[AUTOR]).replace("'","''");
			
			//Sistema.logger.debug("autor:" + autor);
			descripcion=((String) rowObjects[DESCRIPCION]).replace("'","''").substring(0,36);
			//Sistema.logger.debug("descripcion:" + descripcion);
			if (rowObjects[PRECIO]!=null) {
				precio=(Double) rowObjects[PRECIO];
				//Sistema.logger.debug("precio:" + precio);
			}
			
			//Sistema.logger.debug("importando PEDIDO");
			if (rowObjects[PEDIDO]!=null) {
				pedido=	 ((Double) rowObjects[PEDIDO]).intValue();			
				//System.out.println( pedido);
			}
			//System.out.println( vigente);
			//Sistema.logger.debug("importando Editorial");
			editorial=((String) rowObjects[EDITORIAL]).replace("'","''");;
			//System.out.println( editorial);
			//Sistema.logger.debug("importando tema");
			tema=((String) rowObjects[TEMA]).replace("'","''");;
			//System.out.println( tema);
			//Sistema.logger.debug("importando IBN");
			if (rowObjects[ISBN]!=null) {
				isbn=((Double) rowObjects[ISBN]).longValue();
				//System.out.println( isbn);
			}
			//Sistema.logger.debug("importando PTOCK");
			if (rowObjects[PSTOCK]!=null) {
				pstock=	 ((Double) rowObjects[PSTOCK]).intValue();			
				//System.out.println( pstock);
			} 
			
			//Sistema.logger.debug("importando Observaciones");
			observaciones=((String) rowObjects[OBSERVACIONES]).replace("'","''");;
			//System.out.println( observaciones);
			
			//si descripcion empieza con . se lo saco
			if (descripcion.startsWith(".")) {
				descripcion=descripcion.substring(1);
			}
			descripcion=descripcion.replace('§', '°');
			descripcion=descripcion.replace('¥', 'Ñ');
			autor=autor.replace('¥', 'Ñ');
			editorial=editorial.replace('¥', 'Ñ');
			tema=tema.replace('¥', 'Ñ');
			String insert= "INSERT INTO librosmario.cg_catalogo (cg_codigo_luongo, cg_autor, cg_descripcion, " +
		      "cg_precio, cg_pedido, cg_editorial, cg_tema, cg_isbn, cg_pst, " +
		      "cg_observaciones, cg_creador,cg_inputdate) " +
		      " values ("+ codigo +",'" + autor + "','" + descripcion + "'," + precio + "," 
		      + pedido + ",'"  + editorial + "','" + tema + "','" + isbn + "'," + pstock + ",'"
		      + observaciones + "','luongo',now());";
			try {
				//ps = connection.createStatement();
				ps.executeUpdate(insert);				
				cantRegistrosOk++;
			}
			catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {					
					Sistema.logger.error(e1.getMessage());
				}
				Sistema.logger.error("Error trying to insert to the database: " + e.getMessage() + 
						"insert: " + insert);				
			}
			   
			}//if
		else {
			
			Sistema.logger.debug("registro salteado:" + ((String) rowObjects[DESCRIPCION]));
			cantRegistrosSalteados++;
		}
			try {
			skip=((rowObjects = reader.nextRecord()) == null);
			} catch( DBFException e) {
				Sistema.logger.error("Registro salteado:" + e.getMessage());
				skip=false;
				cantRegistrosSalteados++;
				rowObjects[CODIGO]=null;
				if (cantRegistrosSalteados>10000) {
					connection.rollback();
					skip=true;
					return "la cantidad de registros salteados sobrepasa los 10000, importacion abortada";					
				}
				continue;
			}
		}//while
		
		// By now, we have iterated through all of the rows
		inputStream.close();
		date = new Date();
		connection.commit();
		Sistema.logger.debug("Importacion finalizada:" + dateFormat.format(date) + " se importaron " + cantRegistrosOk + " registros. Cantidad de registros salteados:" + cantRegistrosSalteados);
  	    return "Importacion finalizada:" + dateFormat.format(date) + " se importaron " + cantRegistrosOk + " registros. Cantidad de registros salteados:" + cantRegistrosSalteados;
		}
		catch( SQLException e) {
			Sistema.logger.error("Error deleting catalogo: " + e.getMessage());
				
		}
		catch( DBFException e) {
			Sistema.logger.error("DBFException:" + e.getMessage());
			return "Error, comuniquese con el soporte";
		}
		catch( FileNotFoundException e) {
			Sistema.logger.error("FileNotFoundException:" + e.getMessage());
			return "Archivo no encontrado:" + e.getMessage();
		}	
		catch( IOException e) {
			Sistema.logger.error("IOException:" + e.getMessage());
			return "Error, comuniquese con el soporte";
		}	
		catch( Exception e) {
			Sistema.logger.error("Exception:" + e.toString() );
			return "Error, comuniquese con el soporte";
		}
		finally { 
			   try {
			      if (null != connection) {
			         connection.close();
			      }
			   } catch (SQLException e) {
				   Sistema.logger.error("Error trying to close the connection:" + e.getMessage());
		}		
	}	
	return "ok";	

		
	
 }

}