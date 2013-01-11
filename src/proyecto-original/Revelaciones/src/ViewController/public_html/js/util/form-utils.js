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

function clearAllText(){     
    $('form input:text').val('');
}

function selectMenuPadre(evt){    
    
}



