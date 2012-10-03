package cl.bicevida.revelaciones.ejb.test;

import cl.bicevida.revelaciones.ejb.service.local.EstructuraServiceLocal;

import cl.bicevida.xbrl.ejb.service.local.XbrlInstanceGeneratorServiceLocal;

import java.util.Hashtable;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Test;

public class XbrlInstanceGeneratorTest {
    
    private static XbrlInstanceGeneratorServiceLocal xbrlInstanceGeneratorServiceLocal;
    
    public XbrlInstanceGeneratorTest() {
        super();
    }
    
    @BeforeClass
    public static void lookup(){        
        try {
            final Context context = getInitialContext();
            setXbrlInstanceGeneratorServiceLocal((XbrlInstanceGeneratorServiceLocal)context.lookup("AppRevelaciones-branch-6-Model-XbrlInstanceGeneratorService#cl.bicevida.xbrl.ejb.service.local.XbrlInstanceGeneratorServiceLocal"));
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
        getXbrlInstanceGeneratorServiceLocal().generarInstancia();
    }
    
    
    
    private static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();
        // WebLogic Server 10.x connection details
        env.put( Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory" );
        env.put(Context.PROVIDER_URL, "t3://127.0.0.1:7101");
        return new InitialContext( env );
    }

    public static void setXbrlInstanceGeneratorServiceLocal(XbrlInstanceGeneratorServiceLocal xbrlInstanceGeneratorServiceLocal) {
        XbrlInstanceGeneratorTest.xbrlInstanceGeneratorServiceLocal = xbrlInstanceGeneratorServiceLocal;
    }

    public static XbrlInstanceGeneratorServiceLocal getXbrlInstanceGeneratorServiceLocal() {
        return xbrlInstanceGeneratorServiceLocal;
    }
}
