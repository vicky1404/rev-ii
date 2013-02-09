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
	private String type;
	
	public ConceptTreeNode(RoleType roleType, String label) {
		this.roleType = roleType;
		this.label = label;
		this.codigo = "";
		this.id = roleType.getId();
		this.type = "Role";
	}
	
	public ConceptTreeNode(Concept concept, String label) {
		this.concept = concept;
		this.label = label;
		this.codigo = concept.getAttrib("codigo");
		this.id = concept.getId();
		this.type = concept.getTypeString();
	}
	
	public String getCodigo(){
		return codigo;
	}
	
	public String getId(){
		return id;
	}
	

	public String getType(){
		return type;
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
