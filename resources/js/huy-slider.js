function toggleVolumeControl() {
    if (! window.volumeControlOpen) {
        $('.slider-wrapper').css('display', 'block');
        window.volumeControlOpen = true;
    } else {
        $('.slider-wrapper').css('display', 'none');
        window.volumeControlOpen = false;
    }
}

function closeVolumeControl(e) {
    var speakerEl = $('#volume_control');
    var volumeEl = $('.slider-wrapper');
    if ((!speakerEl.is(e.target) && speakerEl.has(e.target).length === 0) &&
        (!volumeEl.is(e.target) && volumeEl.has(e.target).length === 0)
    ) {
        volumeEl.hide();
        window.volumeControlOpen = false;
    }
}

function adjustVolume() {
    $('#backgroundmusic').get(0).volume = 0.5;
}

function playBackground() {
    var playButtonEl = $('.play-button span');
    playButtonEl.toggleClass('fa-pause fa-play');
    if (playButtonEl.hasClass('fa-pause')) {
        $('#backgroundmusic').get(0).play();
    } else {
        $('#backgroundmusic').get(0).pause();
    }
}