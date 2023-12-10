const botTitle = document.querySelector(".play-with-bot");

botTitle.addEventListener("click", initMatch);

async function initMatch() {
    const botMatch = "bot";
    let matchId = null;

    const response = await fetch('/api/chess/idMatchType', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            idMatchType: botMatch,
        })
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    const data = await response.json();
    if (data) {
        matchId = data.idMatch;
        window.location.href = "/computer/" + botMatch + "/" + matchId;
    } else {
        console.error('No data received');
    }

}