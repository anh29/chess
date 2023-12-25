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
    btnSave.addEventListener("click", function (event) {
        event.preventDefault(); // Prevent the default form submission
        var genderInputs = document.querySelectorAll('input[name="rdoGender"]');
        var genderValue;
        for (var i = 0; i < genderInputs.length; i++) {
            if (genderInputs[i].checked) {
                genderValue = genderInputs[i].value;
                break;
            }
        }
        var userData = {
            email: document.getElementById("txtEmail").value,
            name: document.getElementById("txtName").value,
            dateBirth: document.getElementById("dateBirth").value,
            gender: genderValue,
        };
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/public/update-user", true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.onload = function () {
            if (xhr.status == 200) {
                console.log("User updated successfully");
            } else {
                console.error("Failed to update user");
            }
        };
        xhr.send(JSON.stringify(userData));
    });

    // Back
    var btnback = document.getElementById("btnBack");
    btnBack.addEventListener("click", function (event) {
        event.preventDefault();
        history.back();
    });


});
