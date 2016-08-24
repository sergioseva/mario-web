package model.librosmario;

import java.util.ArrayList;
import java.util.Date;


public class PedidoADistribuidora {
	
	 Integer pd_pedido_a_distribuidora_k;
	 Date pd_fecha;
	 Editorial pd_distribuidora_ed;
	 boolean pd_pedido_realizado;
	 private ArrayList<PedidoItem> items; 
	 
	 
	 public boolean getPd_pedido_realizado() {
		return pd_pedido_realizado;
	}
	public void setPd_pedido_realizado(boolean pd_pedido_realizado) {
		this.pd_pedido_realizado = pd_pedido_realizado;
	}
	
	public Integer getpd_pedido_a_distribuidora_k() {
		return pd_pedido_a_distribuidora_k;
	}
	public void setpd_pedido_a_distribuidora_k(Integer pd_pedido_a_distribuidora_k) {
		this.pd_pedido_a_distribuidora_k = pd_pedido_a_distribuidora_k;
	}
	public Date getPd_fecha() {
		return pd_fecha;
	}
	public void setPd_fecha(Date pd_fecha) {
		this.pd_fecha = pd_fecha;
	}
	public Editorial getpd_distribuidora_ed() {
		return pd_distribuidora_ed;
	}
	public void setpd_distribuidora_ed(Editorial pd_distribuidora_ed) {
		this.pd_distribuidora_ed = pd_distribuidora_ed;
	}
	public ArrayList<PedidoItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<PedidoItem> list) {
		this.items = list;
	}
	 
	 
}
