package cl.mdr.exfida.modules.xbrl.model;

import java.util.ArrayList;
import java.util.List;

import xbrlcore.taxonomy.Concept;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;

public class EEFFConceptMapping {
	
	private EstadoFinanciero estadoFinanciero;
	private List<Concept> concepts;
	
	public EstadoFinanciero getEstadoFinanciero() {
		return estadoFinanciero;
	}
	
	public String getFecuFormat(){
		return this.estadoFinanciero.getFecuFormat();
	}
	
	public void setEstadoFinanciero(EstadoFinanciero estadoFinanciero) {
		this.estadoFinanciero = estadoFinanciero;
	}
	public List<Concept> getConcepts() {
		return concepts;
	}
	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}
	
	public List<String> getConceptsValue(){
		List<String> list = new ArrayList<String>();
		for (Concept concept : concepts) {
			if(!concept.getTypeString().equals("nonnum:domainItemType")){
				list.add(concept.getId());	
			}
			
		}
		return list;
	}
	
	public List<String> getConceptsMember(){
		List<String> list = new ArrayList<String>();
		for (Concept concept : concepts) {
			if(concept.getTypeString().equals("nonnum:domainItemType")){
				list.add(concept.getId());	
			}
			
		}
		return list;
	}
	
	public String getKey(){
		return this.estadoFinanciero.getIdFecu() + "" + this.estadoFinanciero.getIdVersionEeff()  + "" + this.concepts.hashCode(); 
				
	}



}
