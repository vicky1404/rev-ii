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
