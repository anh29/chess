const buyBtns = document.querySelectorAll(".js-time-selector");
const modal = document.querySelector(".js-modal");
const modalClose = document.querySelector(".js-modal-btn-close");
const modalContainer = document.querySelector(".js-modal-container");

function showTime() {
    modal.classList.add("open");
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

// Loop through each element and add a click event listener
for (const pro of listPro) {
    pro.addEventListener("click", getTime);
}

function getTime(event) {
    // Lấy ID của thẻ được click
    const clickedId = event.currentTarget.id;

    // Chuyển trang đến /online và truyền ID
    window.location.href = "/online?id=" + clickedId;
}
