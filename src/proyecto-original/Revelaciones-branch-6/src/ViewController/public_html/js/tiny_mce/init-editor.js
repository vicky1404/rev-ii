
function loadEditor(){        
    tinyMCE.init({
        language : 'es',
        //mode : "exact",
        //elements : "editorObj",
        mode : "textareas",
        theme : "advanced",
        // Skin options
        skin : "o2k7",
        skin_variant : "silver",
        plugins : "lists,pagebreak,style,layer,table,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,inlinepopups,autosave",

        // Theme options
        theme_advanced_buttons1 : "fullscreen,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,formatselect,fontselect,fontsizeselect,|",
        theme_advanced_buttons2 : "selectall,cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,cleanup,code,|,insertdate,inserttime,preview,|,forecolor,backcolor,|",
        theme_advanced_buttons3 : "fullscreen,",        
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_resizing : true		
    });    
}

function initEditor() {    
    var mostrar = jQuery('#render').val();
    try{
        if (mostrar ==  'true'){              
            loadEditor();               
        }
    }catch(err){}
    
}

function limpiarEditor(){    
    tinyMCE.get('editorObj').setContent('');
}
