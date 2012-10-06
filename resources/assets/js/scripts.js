$(document).ready(function() {
    $('#subsform').submit(function(event) {
        event.preventDefault();
        var formData = $(this).serialize();
        this.reset();
        $.post('http://localhost:5000/subscribe',
               formData,
               function(data, textStatus, jqXHR) {
                   $('#error').html(data.message);
               });
    });
});
