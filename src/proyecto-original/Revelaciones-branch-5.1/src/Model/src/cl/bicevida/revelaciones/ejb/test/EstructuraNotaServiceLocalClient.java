package cl.bicevida.revelaciones.ejb.test;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.service.local.EstructuraServiceLocal;
import cl.bicevida.revelaciones.vo.FilaVO;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class EstructuraNotaServiceLocalClient {
    private static EstructuraServiceLocal estructuraNotaServiceLocal;
    
    private static EstructuraServiceLocal lookup(){
        
        try {
            final Context context = getInitialContext();
            setEstructuraNotaServiceLocal((EstructuraServiceLocal)context.lookup("AppRevelaciones-Model-EstructuraNotaService#cl.bicevida.revelaciones.ejb.service.local.EstructuraNotaServiceLocal"));
        } catch (CommunicationException ex) {
            System.out.println(ex.getClass().getName());
            System.out.println(ex.getRootCause().getLocalizedMessage());
            System.out.println("\n*** A CommunicationException was raised.  This typically\n*** occurs when the target WebLogic server is not running.\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return estructuraNotaServiceLocal;
    }
    /*
    public static void main(String[] args) {
        try {
            lookup();
            StringBuffer row = new StringBuffer();
            Grilla grilla = getEstructuraNotaServiceLocal().findGrilla(1L);
            //System.out.print(grilla.getColumnaNotaList());
            final List<Celda> celdas = getEstructuraNotaServiceLocal().getCeldasFromGrilla(grilla);
            final Set<Long> filas = getEstructuraNotaServiceLocal().getTotalFilasFromGrilla(celdas);
            List<FilaVO> filaList = getEstructuraNotaServiceLocal().buildGrilla(grilla);
            System.out.print(filaList);
            
            /*
            List<CeldaNota> celdasByFila = new ArrayList<CeldaNota>();
            List<FilaDTO> filasDTO = new ArrayList<FilaDTO>();
            FilaDTO filaDTO = null;
            
            for (Long fila : filas) {
                filaDTO = new FilaDTO();
                filaDTO.setNumeroFila(fila);
                for (CeldaNota celda : celdas) {                                    
                    row = new StringBuffer(); 
                    if (celda.getIdFila().equals(fila)) {                        
                        celdasByFila.add(celda);
                    }                    
                }
                filaDTO.setCeldas(celdasByFila);           
            }
            filasDTO.add(filaDTO);    

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    private static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        // WebLogic Server 10.x connection details
        env.put( Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory" );
        env.put(Context.PROVIDER_URL, "t3://127.0.0.1:7101");
        return new InitialContext( env );
    }


    public static void setEstructuraNotaServiceLocal(EstructuraServiceLocal estructuraNotaServiceLocal) {
        EstructuraNotaServiceLocalClient.estructuraNotaServiceLocal = estructuraNotaServiceLocal;
    }

    public static EstructuraServiceLocal getEstructuraNotaServiceLocal() {
        return estructuraNotaServiceLocal;
    }
}
