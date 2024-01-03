document.addEventListener("DOMContentLoaded", function () {
    // Load image
    var btnChange = document.getElementById("btnChange");
    var fileInput = document.getElementById("fileInput");

    btnChange.addEventListener("click", function () {
        fileInput.click();
    });

    fileInput.addEventListener("change", function () {
        var selectedFile = fileInput.files[0];
        if (selectedFile) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var imageDataUrl = e.target.result;
                document.getElementById("img-user").src = imageDataUrl;
                saveImageToDatabase(imageDataUrl);
            };
            reader.readAsDataURL(selectedFile);
        }
    });
    function saveImageToDatabase(imageDataUrl) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/public/save-image", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        var imageData = { imagePath: imageDataUrl };
        xhr.onload = function () {
            if (xhr.status == 200) {
                console.log("Image path saved successfully");
            } else {
                console.error("Failed to save image path");
            }
        };
        xhr.send(JSON.stringify(imageData));
    }
    // Save Information
    btnSave.addEventListener("click", async function (event) {
        event.preventDefault();
        var error_message = document.getElementById("error_message");
        var genderInputs = document.querySelectorAll('input[name="rdoGender"]');
        var genderValue = genderInputs[0].checked;
        var userData = {
            email: document.getElementById("txtEmail").value,
            name: document.getElementById("txtName").value,
            dateBirth: document.getElementById("dateBirth").value,
            gender: genderValue,
        };
        try {
            const response = await fetch('/public/update-user', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                },
                body: JSON.stringify(userData),
            });
            if (response.status === 200) {
                error_message.textContent = "User updated successfully";
                console.log("User updated successfully");
            } else {
                response.text().then(function (text) {
                    error_message.textContent = "Failed to update user: " + text;
                });
            }
        } catch (error) {
            error_message.textContent = "Error: " + error;
        }
    });
    // Back
    var btnback = document.getElementById("btnBack");
    btnBack.addEventListener("click", function (event) {
        event.preventDefault();
        window.location.href = "http://localhost:8080/public/login";
    });


});
