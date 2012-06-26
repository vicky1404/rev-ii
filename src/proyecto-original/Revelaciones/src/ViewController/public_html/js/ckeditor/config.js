/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
         config.toolbar = 'Basic';
	 config.language = 'es';	
         config.uiColor = '#C4CEE0';  
         config.pasteFromWordRemoveFontStyles = false;
         config.removePlugins = 'elementspath';
         config.toolbarCanCollapse = false;
         config.height = '300px';
         config.toolbar_Basic =[
            ['NewPage', '-', 
             'Cut','Copy','Paste','PasteText','PasteFromWord', '-',
             'Bold', 'Italic', 'Underline','Strike','Subscript','Superscript', '-', 
             'NumberedList', 'BulletedList', '-', 
             'JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-',
             'Find','Replace']
        ];
        
};
