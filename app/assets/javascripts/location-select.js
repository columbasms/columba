$('#town-select').select2({
    data: []
});
sendProvince(true);
$('#province-select').on('change', function() {
    sendProvince(false);
});
function sendProvince(first) {
    $.ajax({
        url: '/town-by-province',
        data: {
            province_id: $('#province-select').val(),
            first: first
        },
        success: function(data) {
            $('#town-select').select2({
                data: data.data
            }).select2('val', data.selected);
        }
    })
}