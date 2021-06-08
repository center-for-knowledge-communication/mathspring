window.ui = {
  state: 'loading',
  readyToCollect: false,
  nExamples: 0,
  nTrainings: 0,

  setContent: function(key, value) {
    // Set an element's content based on the data-content key.
    $('[data-content="' + key + '"]').html(value);
  },

  showInfo: function(text, dontFlash) {
    // Show info and beep / flash.
    this.setContent('info', text);
    if (!dontFlash) {
      $('#info').addClass('flash');
      new Audio('resources/hint.mp3').play();
      setTimeout(function() {
        $('#info').removeClass('flash');
      }, 1000);
    }
  },
};
