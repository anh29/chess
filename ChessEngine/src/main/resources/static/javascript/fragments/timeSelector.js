const buyBtns = document.querySelectorAll(".js-time-selector");
const modal = document.querySelector(".js-modal");
const modalClose = document.querySelector(".js-modal-btn-close");
const modalContainer = document.querySelector(".js-modal-container");
const loading = document.querySelector(".loading-modal");

console.log(loading)
function showTime() {
    modal.classList.add("open");
    // window.location.href = "/online";
}

function closeTime() {
    modal.classList.remove("open");
}

//Lặp qua từng thẻ button và nghe hành vi click
for (const buyBtn of buyBtns) {
    buyBtn.addEventListener("click", showTime);
}

//Nghe hàm vi click vài button close
modalClose.addEventListener("click", closeTime);

modal.addEventListener("click", closeTime);

modalContainer.addEventListener("click", function (event) {
    event.stopPropagation();
});

// Select all elements with the class "pro"
const listPro = document.querySelectorAll(".pro");

for (const pro of listPro) {
    pro.addEventListener("click", function(event) { // Add the 'event' parameter
        // Show the loading modal
        loading.style.display = 'flex';

        // Simulate some asynchronous task (e.g., fetching data)
        setTimeout(function() {
            loading.style.display = 'none';
        }, 3000); // Replace 3000 with the actual time your task takes

        getTime(event); // Pass the 'event' object to getTime
    });
}


async function getTime(event) {
    // Lấy ID của thẻ được click
    const clickedId = event.currentTarget.id;
    let matchId = null;
    // Chuyển trang đến /online và truyền ID
    // window.location.href = "/online?id=" + clickedId;
    // window.location.href = "/online";

    const response = await fetch('/api/chess/idMatchType', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            idMatchType: clickedId,
        })
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    const data = await response.json();
    if (data) {
        matchId = data.idMatch;
        window.location.href = "/online/" + clickedId + "/" + matchId;
    } else {
        console.error('No data received');
    }
}
