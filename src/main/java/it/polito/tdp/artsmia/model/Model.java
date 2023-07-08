package it.polito.tdp.artsmia.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private List<ArtObject> listaOggetti;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> mappaOggetti;
	
//	public Model() {
//		
//		this.grafo = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
//		dao = new ArtsmiaDAO();
////		listaOggetti = new ArrayList<>();
//		mappaOggetti = new HashMap<>();
//	}
//	
//	private void loadNodes() {
//		if(this.listaOggetti.isEmpty())
//			this.listaOggetti = dao.listObjects();
//		
//		if(this.mappaOggetti.isEmpty()) {
//			for(ArtObject a : listaOggetti)
//				this.mappaOggetti.put(a.getId(), a);
//		}
//	}
//	
	public void creaGrafo() {
//		loadNodes();
		this.grafo = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		dao = new ArtsmiaDAO();
		listaOggetti = dao.listObjects();
		
//		Ricordati di inizializzare la mappa!!!!!!!!!!!!!!!!
		mappaOggetti = new HashMap<>();
		for(ArtObject a : listaOggetti) {
			mappaOggetti.put(a.getId(), a);
		}
	    
		Graphs.addAllVertices(grafo, listaOggetti);
		
		List<edgeModel> allEdges = dao.getAllWeight(mappaOggetti);
		for(edgeModel e : allEdges) {
			Graphs.addEdgeWithVertices(this.grafo, e.getSource(), e.getTarget(), e.getPeso());
		}
//		for(ArtObject a1 : this.listaOggetti) {
//			for (ArtObject a2 : this.listaOggetti) {
//				
//				int peso = this.dao.getWeight(a1.getId(), a2.getId());
//				Graphs.addEdgeWithVertices(grafo, a1, a2, peso);
//			}
//		}
		
		System.out.println("Il numero di vertici del grafo: " + grafo.vertexSet().size() + " vertici e " + grafo.edgeSet().size() + " archi");
		
	}
	
	public boolean isIdInGraph(int objID) {
		if(this.mappaOggetti.get(objID) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public Integer componenteConnessa (Integer objID) {
		
//		COMPONENTE CONNESSA: sottoparte del grafo, in quanto non è detto che tutti i nodi siano collegati tra di loro
//		cioè 1 può essere collegato a 2, ma non a 3, un grafo è composto da più componenti connesse, 
//		ciascuna delle quali è costituita da elementi completamente connessi tra di loro.
//		METODO 1
//		Crea un iteratore che esplora il grafo in profondità (mentre BreadthFirstIterator esplora in ampiezza)
		DepthFirstIterator <ArtObject, DefaultWeightedEdge> iterator = new DepthFirstIterator<>(this.grafo, this.mappaOggetti.get(objID));
//	    ci dà una lista di un set di nodi
		List<ArtObject> compConnessa = new ArrayList<>();
		while(iterator.hasNext()) { //mi dice se l'iteratore ha un nodo successivo, finchè ne ha uno successivo itero
			compConnessa.add(iterator.next());
		}
		return compConnessa.size();
		
//		METODO 2: equivalenti
//		Sfruttando tale classe, vengono creati degli oggetti che danno informazioni sulla connessione dei grafi 
//		che è di fatto come un'iteratore che vuole il grafo, poi questo ha un metodo che vuole la sorgente... mi prende un nodo e mi dà la dimensione della componente connessa
//		ConnectivityInspector <ArtObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<> (this.grafo);
//		Set<ArtObject> setConnesso = inspector.connectedSetOf(this.mappaOggetti.get(objID));
//		prende un nodo e dà tutti quelli che possono essere in qualche modo collegati ad esso
//		return setConnesso.size();
		
	}
}
