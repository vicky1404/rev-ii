function selectAll(){       
    $('form input:checkbox').each(function() {
        this.checked = true;                        
    });    
}

function unSelectAll(){                
    $('form input:checkbox').each(function() {
        this.checked = false;                        
    });   
}

function getWidth(){ 
	return $('#l_center').width();
}

function setEditorWidth(){
	$('.ui-editor').css("width", "100%");
	$('.ui-editor').find('iframe').css("width", "100%");
}
