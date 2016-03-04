//= require active_admin/base
//= require active_admin/select2
//= require tinymce

$(function() {
    tinyMCE.init({
        mode: 'textareas',
        theme: 'modern',
        plugins: ['uploadimage', 'hr', 'code', 'link'],
        relative_urls: false,
        toolbar: "bold,italic,underline,|,bullist,numlist,outdent,indent,|,undo,redo,|,pastetext,pasteword,selectall,|,link,|,uploadimage",
        remove_script_host: false,
        menu: {
            file: {title: 'File', items: 'newdocument'},
            edit: {title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall'},
            insert: {title: 'Insert', items: 'link uploadimage | template hr'},
            view: {title: 'View', items: 'visualaid'},
            format: {title: 'Format', items: 'bold italic underline strikethrough superscript subscript | formats | removeformat'},
            table: {title: 'Table', items: 'inserttable tableprops deletetable | cell row column'},
            tools: {title: 'Tools', items: 'spellchecker code'}
        }
    });
});
