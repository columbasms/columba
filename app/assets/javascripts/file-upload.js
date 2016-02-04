//= require jquery.jcrop
//= require papercrop

$('#avatar-upload').click(function() {
    $('#avatar-input').click();
});

$('#cover-upload').click(function() {
    $('#cover-input').click();
});

function readURL(input, type) {

    if (input.files && input.files[0]) {
        var file = input.files[0];

        if ((/\.(png|jpeg|jpg|gif)$/i).test(file.name)) {
            var formData = new FormData();
            formData.append('organization[' + type + ']', file);
            formData.append('organization[type]', type);
            formData.append('touch', Modernizr.touch);

            $.ajax({
                url: ('/organizations/' + organization_id + '/upload'),
                type: 'POST',
                xhr: function() {  // Custom XMLHttpRequest
                    var myXhr = $.ajaxSettings.xhr();
                    if(myXhr.upload){ // Check if upload property exists
                        myXhr.upload.addEventListener('progress', function(e) {
                            if(e.lengthComputable){
                                var percent = parseInt((e.loaded / e.total) * 100);
                                updateProgress(percent);
                            }
                        }, false); // For handling the progress of the upload
                    }
                    return myXhr;
                },

                success: function(e) {
                    if (Modernizr.touch) {
                        if (type == 'avatar') {
                            $('#avatar-preview').attr('src', e.avatar);
                        } else {
                            $('#cover-preview').css('background-image', 'url("' + e.cover + '")');
                        }
                        updateProgress(0);
                        $('.progress-panel').hide();
                        $('#modal-crop').modal('toggle');
                    } else {
                        $('#hint-text').html('Processing...');
                        $('#modal-crop .panel-body .progress-panel').after(e).ready(function() {
                            var $imgs = $('#modal-crop img');
                            var count = $imgs.length;
                            var load = 0;
                            $imgs.on('load', function() {
                                load++;
                                if (load == count) {
                                    $('.progress-panel').hide();
                                    updateProgress(0);
                                    $('#crop-form').show();
                                }
                            }).each(function() {
                                if(this.complete) $(this).trigger("load");
                            });
                        });
                    }
                },

                error: function(e) {
                    console.log(e);
                },
                // Form data
                data: formData,

                cache: false,
                contentType: false,
                processData: false
            });
        }
    }
}

$("#avatar-input").change(function(){
    readURL(this, 'avatar');
    $('#hint-text').html('Uploading... <span id="upload-percentage">0%</span>');
    $('#modal-crop').modal();
    $('.progress-panel').show();
});

$('#cover-input').change(function() {
    readURL(this, 'cover');
    $('#hint-text').html('Uploading... <span id="upload-percentage">0%</span>');
    $('#modal-crop').modal();
    $('.progress-panel').show();
});

function updateProgress(progress) {
    $('#upload-progress').css('width', progress + '%');
    $('#upload-percentage').text(progress + '%');
}