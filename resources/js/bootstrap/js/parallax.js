$(function() {
  var $window = $(window);

  // Parallax background effect
  $('section[data-type="background"]').each(function() {
    var $bgobj = $(this); // assigning the object

    $window.scroll(function () {
      var yPos = -($window.scrollTop() / $bgobj.data('speed'));
      console.log(yPos);

      var coords = '50% ' + yPos + 'px';

      // Move the background
      $bgobj.css({backgroundPosition: coords});
    });
  });
});