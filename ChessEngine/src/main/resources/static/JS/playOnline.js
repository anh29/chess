const contain = document.querySelector(".contain");

async function init() {
    contain.innerHTML=await AJAX('/play')
}

var countdownValue = 0;
var countdownInterval;

function updateCountdown() {
    var durationSelect = document.getElementById("duration");
    var customInput = document.getElementById("customInput");

    if (durationSelect.value === "custom") {
        customInput.style.display = "inline-block";
    } else {
        customInput.style.display = "none";
    }
}

function startCountdown() {
    var durationSelect = document.getElementById("duration");
    var customInput = document.getElementById("customInput");
    var countdownDisplay = document.getElementById("countdown");

    if (durationSelect.value === "custom") {
        // Parse the custom input value (mm:ss) to seconds
        var customTime = customInput.value.split(":");
        countdownValue = parseInt(customTime[0]) * 60 + parseInt(customTime[1]);
    } else {
        countdownValue = parseInt(durationSelect.value);
    }

    // Display initial countdown value
    displayCountdown();

    // Update countdown every second
    countdownInterval = setInterval(function () {
        countdownValue--;

        if (countdownValue >= 0) {
            displayCountdown();
        } else {
            clearInterval(countdownInterval);
            alert("Countdown completed!");
        }
    }, 1000);
}

function displayCountdown() {
    var minutes = Math.floor(countdownValue / 60);
    var seconds = countdownValue % 60;

    document.getElementById("countdown").innerHTML =
        minutes.toString().padStart(2, "0") +
        ":" +
        seconds.toString().padStart(2, "0");
}

async function AJAX(fragment, json = false) {
    // var token = sessionStorage.getItem('jwtToken');
    // if (token) {
    //     $.ajaxSetup({
    //         headers: {
    //             Authorization: 'Bearer ' + token,
    //         },
    //     });
    // }
    // console.log('Token: ', token);

    try {
        const response = await fetch(fragment, {
            method: 'GET',
            headers: {
                'request-source': 'JS',
            },
        });

        const data = json ? await response.json() : await response.text();

        if (!response.ok) throw new Error('Error response');
        return data;
    } catch (e) {
        console.error(e.message);
    }
}