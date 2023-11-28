const idTime = document.querySelector("#idTime").innerHTML;

let timeControls = parseTimeControls(idTime);

let timeWhite = timeControls.initialTime * 60; // Chuyển đổi phút thành giây
let timeBlack = timeControls.initialTime * 60;

const interval = 1000;

function updateClock() {
    const clockWhite = document.getElementById('clock-white');
    const clockBlack = document.getElementById('clock-black');

    displayCountdown(timeWhite, clockWhite);
    displayCountdown(timeBlack, clockBlack);

    if (timeWhite <= 0 || timeBlack <= 0) {
        clearInterval(clockInterval);
        alert("Game Over!");
    }
}

const clockInterval = setInterval(function () {
    timeWhite--;
    timeBlack--;
    updateClock();
}, interval);

function startCountdown(secondsTime, clockElement) {
    countdownValue = parseInt(secondsTime);

    // Display initial countdown value
    displayCountdown(countdownValue, clockElement);

    // Update countdown every second
    countdownInterval = setInterval(function () {
        countdownValue--;

        if (countdownValue >= 0) {
            displayCountdown(countdownValue, clockElement);
        } else {
            clearInterval(countdownInterval);

            alert("Game over")
        }
    }, 1000);
}

function displayCountdown(countdownValue, clockElement) {
    var minutes = Math.floor(countdownValue / 60);
    var seconds = countdownValue % 60;

    clockElement.innerHTML =
        minutes.toString().padStart(2, "0") +
        ":" +
        seconds.toString().padStart(2, "0");
}

function parseTimeControls(timeControlString) {
    const [initialTime, increment] = timeControlString.split('_').map(Number);
    return { initialTime, increment };
}
