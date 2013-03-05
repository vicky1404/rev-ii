package cl.bicevida.revelaciones.ejb.service;


import static ch.lambdaj.Lambda.index;
import static ch.lambdaj.Lambda.on;

import static cl.bicevida.revelaciones.ejb.cross.Constantes.PERSISTENCE_UNIT_NAME;
import cl.bicevida.revelaciones.ejb.entity.CaratulaFecu;
import cl.bicevida.revelaciones.ejb.entity.DatoCaratulaFecu;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.service.local.CaratulaFecuServiceLocal;

import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class CaratulaFecuServiceBean implements CaratulaFecuServiceLocal {

    @Resource
    SessionContext sessionContext;

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;


    /**
     * @param periodo
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public CaratulaFecu findByPeriodo(final Periodo periodo) throws Exception {
        return (CaratulaFecu)em.createNamedQuery(CaratulaFecu.FIND_ALL_BY_PERIODO)
                               .setParameter("idPeriodo", periodo.getIdPeriodo())
                               .getSingleResult();
    }

    /**
     * @param periodo
     * @return
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, DatoCaratulaFecu> getDatoCaratulaFecuMap(final Periodo periodo) throws Exception {
        Map<String, DatoCaratulaFecu> datoCaratulaFecuMap = new LinkedHashMap<String, DatoCaratulaFecu>();
        datoCaratulaFecuMap = index(this.findByPeriodo(periodo).getDatoCaratulaFecuList(), on(DatoCaratulaFecu.class).getNombreDatoCaratulaFecu().getIdNombreDato());
        return datoCaratulaFecuMap;
    }

    /**
     * @param datoCaratulaFecuList
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void mergeDatosCaratula(final List<DatoCaratulaFecu> datoCaratulaFecuList) throws Exception {
        for (DatoCaratulaFecu datoCaratulaFecu : datoCaratulaFecuList) {
            em.merge(datoCaratulaFecu);
        }
    }
}
