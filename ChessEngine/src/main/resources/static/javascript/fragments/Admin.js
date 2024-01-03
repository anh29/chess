document.addEventListener("DOMContentLoaded", function () {
    // Back
    var btnBack = document.getElementById("btnBack");
    btnBack.addEventListener("click", function (event) {
        event.preventDefault();
        window.location.href = "/public/login";
    });
    // Block user
    document.querySelectorAll(".btnBlock").forEach(function (btnBlock) {
        btnBlock.addEventListener("click", async function (event) {
            event.preventDefault();
            var emailToBlock = this.getAttribute('data-gmail1');
            var payload1 = {
                email: emailToBlock,
                status: 0
            };
            try {
                const response = await fetch('/public/block-user', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8'
                    },
                    body: JSON.stringify(payload1),
                });

                if (response.ok) {
                    alert("User blocked successfully");
                    window.location.reload();
                } else {
                    alert("Failed to block user");
                }
            } catch (error) {
                console.error('Error blocking user:', error);
            }
        });
    });
//Unblock
    document.querySelectorAll(".btnUnblock").forEach(function (btnUnblock) {
        btnUnblock.addEventListener("click", async function (event) {
            event.preventDefault();
            var emailToUnblock = this.getAttribute('data-gmail2');
            var payload2 = {
                email: emailToUnblock,
                status: 1
            };
            try {
                const response = await fetch('/public/unblock-user', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8'
                    },
                    body: JSON.stringify(payload2),
                });

                if (response.ok) {
                    alert("User unblocked successfully");
                    window.location.reload();
                } else {
                    alert("Failed to unblock user");
                }
            } catch (error) {
                console.error('Error unblocking user:', error);
            }
        });
    });
});
