package model.librosmario;

import java.util.ArrayList;
import java.sql.Date;


public class Pedido {
    private int pe_pedido_k;
    private Cliente cliente;
    private Date pe_fecha;
    private Double pe_senia;
    private Double pe_total;
    private boolean  pe_adomicilio;
    private String pe_domicilio;
    private Date pe_fecha_envio;
    private String pe_observaciones;
    private ArrayList<PedidoItem> items;
    
    public void setItems(ArrayList<PedidoItem> items) {
        this.items = items;
    }

    public ArrayList<PedidoItem> getItems() {
        return items;
    }
    
    public String getFechaToString () {
        if (this.getPe_fecha_envio()==null) {
            return "null";
        } else {
            return this.getPe_fecha_envio().toString() ;
        }
    }
    
    public int cantidadItems() {        
        return items.size();
    }
    
    public Date getPe_fecha_envio() {
        return pe_fecha_envio;
    }

    public void setPe_fecha_envio(Date pe_fecha_envio) {
        this.pe_fecha_envio = pe_fecha_envio;
    }

    public String getPe_observaciones() {
        return pe_observaciones;
    }

    public void setPe_observaciones(String pe_observaciones) {
        this.pe_observaciones = pe_observaciones;
    }
    
    
    public Pedido() {
        items=new ArrayList<PedidoItem>() ;
    }
    
    public int getPe_pedido_k() {
        return pe_pedido_k;
    }
    public void setPe_pedido_k(int pe_pedido_k) {
        this.pe_pedido_k = pe_pedido_k;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Date getPe_fecha() {
        return pe_fecha;
    }
    public void setPe_fecha(Date pe_fecha) {
        this.pe_fecha = pe_fecha;
    }
    public Double getPe_senia() {
        return pe_senia;
    }
    public void setPe_senia(Double pe_senia) {
        this.pe_senia = pe_senia;
    }
    public Double getPe_total() {
        return pe_total;
    }
    public void setPe_total(Double pe_total) {
        this.pe_total = pe_total;
    }
    public boolean isPe_adomicilio() {
        return pe_adomicilio;
    }
    public void setPe_adomicilio(boolean pe_adomicilio) {
        this.pe_adomicilio = pe_adomicilio;
    }
    public String getPe_domicilio() {
        return pe_domicilio;
    }
    public void setPe_domicilio(String pe_domicilio) {
        this.pe_domicilio = pe_domicilio;
    }
    public void addPedido(PedidoItem pi) {
        items.add(pi);
    }
    public void removePedido(PedidoItem pi) {
        items.remove(pi);
    }


    
}
