package model.librosmario;

import java.util.ArrayList;
import java.util.Date;

public class Remito {
	
	 Integer re_remito_k;
	 Date re_fecha;
	 Editorial re_distribuidora_ed;	 
	 String re_observaciones;
	 public String getRe_observaciones() {
		return re_observaciones;
	}
	public void setRe_observaciones(String re_observaciones) {
		this.re_observaciones = re_observaciones;
	}
	private ArrayList<RemitoItem> items;
	 
	public ArrayList<RemitoItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<RemitoItem> items) {
		this.items = items;
	}
	public Integer getRe_remito_k() {
		return re_remito_k;
	}
	public void setRe_remito_k(Integer re_remito_k) {
		this.re_remito_k = re_remito_k;
	}
	public Date getRe_fecha() {
		return re_fecha;
	}
	public void setRe_fecha(Date re_fecha) {
		this.re_fecha = re_fecha;
	}
	public Editorial getRe_distribuidora_ed() {
		return re_distribuidora_ed;
	}
	public void setRe_distribuidora_ed(Editorial re_distribuidora_ed) {
		this.re_distribuidora_ed = re_distribuidora_ed;
	} 

}
