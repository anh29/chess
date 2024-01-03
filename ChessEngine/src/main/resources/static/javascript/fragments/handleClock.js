const idTime = document.querySelector("#idTime").innerHTML;

let timeControls = parseTimeControls(idTime);

let timeWhite = timeControls.initialTime * 60; // Convert minutes to seconds
let timeBlack = timeControls.initialTime * 60;

let isWhitePaused = false; // Track pause state for white clock
let isBlackPaused = false; // Track pause state for black clock
let clockWhiteInterval; // Timer for white clock
let clockBlackInterval; // Timer for black clock

const interval = 1000;

function updateClock(time, clockElement) {
    var minutes = Math.floor(time / 60);
    var seconds = time % 60;

    clockElement.innerHTML =
        minutes.toString().padStart(2, "0") +
        ":" +
        seconds.toString().padStart(2, "0");
}

function stop(clock) {
    if (clock === 'white') {
        isWhitePaused = true;
        clearInterval(clockWhiteInterval);
    } else if (clock === 'black') {
        isBlackPaused = true;
        clearInterval(clockBlackInterval);
    }
}

function start(clock, time, clockElement) {
    if (clock === 'white') {
        isWhitePaused = false;
        clockWhiteInterval = setInterval(function () {
            if (!isWhitePaused) {
                time--;
                updateClock(time, clockElement);

                if (time <= 0) {
                    clearInterval(clockWhiteInterval);
                    alert("Black win!");
                }
            }
        }, interval);
    } else if (clock === 'black') {
        isBlackPaused = false;
        clockBlackInterval = setInterval(function () {
            if (!isBlackPaused) {
                time--;
                updateClock(time, clockElement);

                if (time <= 0) {
                    clearInterval(clockBlackInterval);
                    alert("White win!");
                }
            }
        }, interval);
    }
}

function parseTimeControls(timeControlString) {
    const [initialTime, increment] = timeControlString.split('_').map(Number);
    return { initialTime, increment };
}

// Initial display
updateClock(timeWhite, document.getElementById('clock-white'));
updateClock(timeBlack, document.getElementById('clock-black'));
