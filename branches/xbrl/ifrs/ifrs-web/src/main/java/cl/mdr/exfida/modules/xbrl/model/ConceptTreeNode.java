package cl.mdr.exfida.modules.xbrl.model;

import java.io.Serializable;

import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.RoleType;

public class ConceptTreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Concept concept;
	private String label;
	private RoleType roleType;
	private String codigo;
	private String id;
	
	public ConceptTreeNode(RoleType roleType, String label) {
		this.roleType = roleType;
		this.label = label;
		this.codigo = "";
		this.id = roleType.getId();
	}
	
	public ConceptTreeNode(Concept concept, String label) {
		this.concept = concept;
		this.label = label;
		this.codigo = concept.getAttrib("codigo");
		this.id = concept.getId();
	}
	
	public String getCodigo(){
		return codigo;
	}
	
	public String getId(){
		return id;
	}


	public Concept getConcept() {
		return concept;
	}


	public void setConcept(Concept concept) {
		this.concept = concept;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

}
