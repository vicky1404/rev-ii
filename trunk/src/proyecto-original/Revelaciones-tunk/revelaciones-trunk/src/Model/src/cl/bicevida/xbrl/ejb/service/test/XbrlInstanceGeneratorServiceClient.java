package cl.bicevida.xbrl.ejb.service.test;

import cl.bicevida.xbrl.ejb.service.local.XbrlInstanceGeneratorService;

import cl.bicevida.xbrl.ejb.service.local.XbrlInstanceGeneratorServiceLocal;

import java.util.Hashtable;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Test;

import xbrlcore.instance.Instance;

public class XbrlInstanceGeneratorServiceClient {
    
    private static XbrlInstanceGeneratorService xbrlInstanceGeneratorService;
    private static Instance instance;
    
    @BeforeClass
    public static void lookup(){        
        try {            
            final Context context = getInitialContext();
             xbrlInstanceGeneratorService = (XbrlInstanceGeneratorService)context.lookup("AppRevelaciones-branch-6-Model-XbrlInstanceGeneratorService#cl.bicevida.xbrl.ejb.service.local.XbrlInstanceGeneratorService");            
        } catch (CommunicationException ex) {
            System.out.println(ex.getClass().getName());
            System.out.println(ex.getRootCause().getLocalizedMessage());
            System.out.println("\n*** A CommunicationException was raised.  This typically\n*** occurs when the target WebLogic server is not running.\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    
    
    @SuppressWarnings("unused")
    @Test 
    public void testEjb(){
        getXbrlInstanceGeneratorService().generarInstancia();
    }
    
    
    private static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        // WebLogic Server 10.x connection details
        env.put( Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory" );
        env.put(Context.PROVIDER_URL, "t3://127.0.0.1:7101");
        return new InitialContext( env );
    }


    public static void setXbrlInstanceGeneratorService(XbrlInstanceGeneratorService xbrlInstanceGeneratorService) {
        XbrlInstanceGeneratorServiceClient.xbrlInstanceGeneratorService = xbrlInstanceGeneratorService;
    }

    public static XbrlInstanceGeneratorService getXbrlInstanceGeneratorService() {
        return xbrlInstanceGeneratorService;
    }
}
