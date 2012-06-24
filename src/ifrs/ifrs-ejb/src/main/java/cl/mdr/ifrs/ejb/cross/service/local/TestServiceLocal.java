package cl.mdr.ifrs.ejb.cross.service.local;

import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Test;

@Local
public interface TestServiceLocal {
	
	List<Test> findAll();

}
