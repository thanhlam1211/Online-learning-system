/**
 *
 */

$('document').ready(function () {

    $('.table .btn').on('click', function (event) {
        event.preventDefault();

        var href = $(this).attr('href');

        //dang co van de


        $('#editModal').modal();

    });
});