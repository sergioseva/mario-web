package model.librosmario;

import java.util.ArrayList;
import java.util.List;


public class PedidoItem implements  Cloneable, Comparable<PedidoItem>{
	  private int pi_pedido_item_k;
	  private Pedido pi_pedido_pe;
	  private Catalogo pi_catalogo_cg;
	  private int pi_cantidad;
	  private String pi_nombre_libro;
	  private String pi_autor;
	  private String pi_editorial;
	  private String pi_isbn;
	  private Double pi_precio;
	  private Editorial pi_editorial_ed;
	  private boolean pi_pendiente;
	  private List<PedidoADistribuidora> pedidosADistribuidoras;
	
	public PedidoItem () {
		this.setPedidosADistribuidoras(new ArrayList<PedidoADistribuidora>());
	}
	
	public Object clone()
	  {
	    Object clone = null;
	    try
	    {
	      clone = super.clone();
	    }
	    catch(CloneNotSupportedException e)
	    {
	      // No debería ocurrir
	    }
	    // Implementacion de la clonación profunda, solo implemento lo que me interesa
	    ((PedidoItem)clone).setPi_cantidad(new Integer(getPi_cantidad()));
	    //((PedidoItem)clone).setPi_autor(pi_autor);
	   
	    return clone;
	  }
	public boolean getPi_pendiente() {
		return pi_pendiente;
	}

	public void setPi_pendiente(boolean pi_pendiente) {
		this.pi_pendiente = pi_pendiente;
	}

	public List<PedidoADistribuidora> getPedidosADistribuidoras() {
		return pedidosADistribuidoras;
	}

	public void setPedidosADistribuidoras(
			List<PedidoADistribuidora> list) {
		this.pedidosADistribuidoras = list;
	}
	
	public void addPedidoAdistribuidora(PedidoADistribuidora p) {
		this.getPedidosADistribuidoras().add(p);
	}

		public int getCg_catalogo_k(){
		if (this.getPi_catalogo_cg()==null) {
			return 0;
		}else
		{
			return this.getPi_catalogo_cg().getCg_catalogo_k();
		}
		
	}
	
	public String getPi_editorial() {
		return pi_editorial;
	}

	public void setPi_editorial(String pi_editorial) {
		this.pi_editorial = pi_editorial;
	}
	
	public String getPi_autor() {
		return pi_autor;
	}
	public void setPi_autor(String pi_autor) {
		this.pi_autor = pi_autor;
	}
	
	public int getPi_pedido_item_k() {
		return pi_pedido_item_k;
	}
	public void setPi_pedido_item_k(int pi_pedido_item_k) {
		this.pi_pedido_item_k = pi_pedido_item_k;
	}
	public Pedido getPi_pedido_pe() {
		return pi_pedido_pe;
	}
	public void setPi_pedido_pe(Pedido pi_pedido_pe) {
		this.pi_pedido_pe = pi_pedido_pe;
	}
	public Catalogo getPi_catalogo_cg() {
		return pi_catalogo_cg;
	}
	public void setPi_catalogo_cg(Catalogo pi_catalogo_cg) {
		this.pi_catalogo_cg = pi_catalogo_cg;
	}
	public int getPi_cantidad() {
		return pi_cantidad;
	}
	public void setPi_cantidad(int pi_cantidad) {
		this.pi_cantidad = pi_cantidad;
	}
	public String getPi_nombre_libro() {
		return pi_nombre_libro;
	}
	public void setPi_nombre_libro(String pi_nombre_libro) {
		this.pi_nombre_libro = pi_nombre_libro;
	}
	public String getPi_isbn() {
		return pi_isbn;
	}
	public void setPi_isbn(String pi_isbn) {
		this.pi_isbn = pi_isbn;
	}
	public Double getPi_precio() {
		return pi_precio;
	}
	public void setPi_precio(Double pi_precio) {
		this.pi_precio = pi_precio;
	}
	public Editorial getPi_editorial_ed() {
		return pi_editorial_ed;
	}
	public void setPi_editorial_ed(Editorial pi_editorial_ed) {
		this.pi_editorial_ed = pi_editorial_ed;
	}

	public int compareTo(PedidoItem o) {
		// TODO Auto-generated method stub
	    if(this.getPi_nombre_libro().compareToIgnoreCase(o.getPi_nombre_libro()) == 0) {            
            if(this.getPi_autor().compareToIgnoreCase(o.getPi_autor()) == 0) { 
                return this.getPi_editorial().compareTo(o.getPi_editorial()); 
            } else { 
                return this.getPi_autor().compareToIgnoreCase(o.getPi_autor()); 
            } 
        } else { 
            return this.getPi_nombre_libro().compareToIgnoreCase(o.getPi_nombre_libro()); 
        }    
		
	}

}
