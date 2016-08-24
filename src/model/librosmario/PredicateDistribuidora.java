package model.librosmario;

import org.apache.commons.collections.Predicate;


public class PredicateDistribuidora implements Predicate {
	private Editorial ed;

	public PredicateDistribuidora(Editorial editorial) {
		this.ed=editorial;
	}
	public boolean evaluate(Object o) {
		// TODO Auto-generated method stub
		return ed.getEd_editorial_k()==((PedidoItem) o).getPi_editorial_ed().getEd_editorial_k();
	}

}
