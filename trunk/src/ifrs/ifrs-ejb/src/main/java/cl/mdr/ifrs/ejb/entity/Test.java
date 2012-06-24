package cl.mdr.ifrs.ejb.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TEST database table.
 * 
 */
@Entity
public class Test implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String column1;

	private String column2;

    public Test() {
    }

	public String getColumn1() {
		return this.column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return this.column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

}