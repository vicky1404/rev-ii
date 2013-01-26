package xbrlcore.taxonomy;

public class Namespace_
{
    public static final org.jdom.Namespace toJDOM(xbrlcore.taxonomy.Namespace namespace) {
        if (namespace.jdomNS == null) {
            namespace.jdomNS = org.jdom.Namespace.getNamespace(namespace.getPrefix(), namespace.getURI());
        }

        return (org.jdom.Namespace)namespace.jdomNS;
    }
}
