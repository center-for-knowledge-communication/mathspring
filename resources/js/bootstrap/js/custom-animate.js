$(function () {
    $('#scrollBtn').click(function () {
        console.log('hello');
        $('html, body').animate({
            scrollTop: $('#stat').offset().top
        }, 1000);
    });
});