const container = document.querySelector(".container")
const countStart = document.querySelector("#countStart");

document.addEventListener("DOMContentLoaded", function () {
    let seconds = 2;

    function updateCountdown() {
        countStart.innerHTML = "Start clock in " + seconds + " seconds";
    }

    function hideCountdown() {
        countStart.style.display = "none";
    }

    function countdown() {
        updateCountdown();
        if (seconds === 0) {
            hideCountdown();

            init()
        } else {
            seconds--;
            setTimeout(countdown, 1000);
        }
    }

    // Start the countdown
    setTimeout(countdown, 1000);
});

async function init() {
    const path = '/javascript/fragments/handleClock.js';
    const script = document.createElement('script');
    const text = document.createTextNode(await AJAX(path));
    script.appendChild(text);
    container.append(script);

    loadedScript.add(path);
}

async function AJAX(fragment, json = false) {
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