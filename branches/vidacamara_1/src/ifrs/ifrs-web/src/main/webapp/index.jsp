<?xml version='1.0' encoding='UTF-8'?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.1">
    <jsp:output omit-xml-declaration="true" doctype-root-element="HTML"
                doctype-system="http://www.w3.org/TR/html4/loose.dtd"
                doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>
    <jsp:directive.page contentType="text/html;charset=UTF-8"/>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <title></title>
        </head>
        <body>
            <jsp:scriptlet>
                response.sendRedirect(request.getContextPath().concat("/login.jsf"));       
            </jsp:scriptlet>             
        </body>
    </html>
</jsp:root>