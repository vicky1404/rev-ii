package cl.mdr.exfida.modules.xbrl.model;

import java.util.UUID;

import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Version;

public class VersionEstructuraTreeNode {
	
	private Version version;
	private Estructura estructura;
	private boolean parent;
	private String id = UUID.randomUUID().toString();
	
	public VersionEstructuraTreeNode(Version version) {
		this.version = version;
		this.parent = true;
	}
	
	public VersionEstructuraTreeNode(Estructura estructura) {
		this.parent = false;
		this.estructura = estructura;
	}

	public Estructura getEstructura() {
		return estructura;
	}

	public void setEstructura(Estructura estructura) {
		this.estructura = estructura;
	}

	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionEstructuraTreeNode other = (VersionEstructuraTreeNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
