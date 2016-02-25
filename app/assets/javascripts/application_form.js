// This is a manifest file that'll be compiled into application.js, which will include all the files
// listed below.
//
// Any JavaScript/Coffee file within this directory, lib/assets/javascripts, vendor/assets/javascripts,
// or any plugin's vendor/assets/javascripts directory can be referenced here using a relative path.
//
// It's not advisable to add code directly here, but if you do, it'll appear at the bottom of the
// compiled file.
//
// Read Sprockets README (https://github.com/rails/sprockets#sprockets-directives) for details
// about supported directives.
//
//= require jquery
//= require jquery_ujs
//= require turbolinks
//= require plugins/modernizr.custom
//= require plugins/jquery-ui/jquery-ui.min
//= require plugins/boostrapv3/js/bootstrap.min
//= require plugins/bootstrap-select2/select2.min
//= require plugins/jquery-validation/js/jquery.validate.min
//= require plugins/jquery-scrollbar/jquery.scrollbar.min
//= require plugins/bootstrap-datepicker/js/bootstrap-datepicker
//= require plugins/bootstrap3-wysihtml5/bootstrap3-wysihtml5.all.min
//= require pages
//= require cocoon

$(function() {
    $('#datepicker').datepicker({
        format: 'dd/mm/yyyy'
    });
    $('.wysiwyg').wysihtml5();
});