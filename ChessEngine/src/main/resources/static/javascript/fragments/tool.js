let currentEvent1 = null;
let targetEvent1 = null;
let fenStringTool = null;

const listIcon = document.querySelectorAll(".icon");

listIcon.forEach(function (icon) {
   icon.addEventListener("click", selectIcon);
});
function selectIcon(event) {
    const selectedIcon = event.target.src;
    // Store the selected icon for later use
    // console.log(selectedIcon);
    localStorage.setItem('selectedIcon', selectedIcon);
    if (!selectedIcon.includes('hand')) {
        const cursorSrc = selectedIcon.replace('/IMAGE/', '/IMAGE/cursors/').replace('.png', '.cur');
        document.body.style.cursor = `url('${cursorSrc}'), auto`;
    } else {
        document.body.style.cursor = 'pointer';
    }
}
const squares = document.getElementsByClassName('square');
for (let i = 0; i < squares.length; i++) {
    squares[i].addEventListener('click', addIconToChessboard);
}
function addIconToChessboard(event) {
    const selectedIcon = localStorage.getItem('selectedIcon');
    const square = event.target;
    if (selectedIcon.includes('trash')) {
        if (square.src) {
            square.parentNode.removeChild(square);
        }
    } else if (selectedIcon.includes('hand')) {
        let draggedImg = null;
        let startSquare = null;

        function onDragStart(event) {
            if (event.target.tagName === 'IMG') {
                draggedImg = event.target;
                startSquare = event.target.closest('.square');
                event.dataTransfer.setData('text', ''); // Necessary for Firefox
                event.target.style.opacity = 0.5; // Make the image transparent when dragging
            } else {
                event.preventDefault(); // Prevent dragging if it's not a piece
            }
        }

        function onDragOver(event) {
            // Allow the drop by preventing the default behavior
            event.preventDefault();
        }

        function onDragEnter(event) {
            if (event.target.classList.contains('square') && event.target !== startSquare) {
                // Visual feedback for the drop target
            }
        }

        function onDragLeave(event) {
            if (event.target.classList.contains('square')) {
                // Remove background color when dragging out of a square
                event.target.style.background = '';
            }
        }

        function onDragEnd(event) {
            // Reset opacity to full when dragging ends
            event.target.style.background = '';
            event.target.style.opacity = '';
        }

        function onDrop(event) {
            event.preventDefault(); // Prevent default behavior
            if (draggedImg && event.currentTarget.classList.contains('square')) {
                currentEvent1 = event.currentTarget;
                targetEvent1 = event.target;
                let targetImg = currentEvent1.querySelector('img');
                if (targetImg) {
                    currentEvent1.removeChild(targetImg);
                }

                currentEvent1.appendChild(draggedImg);
                draggedImg.style.opacity = '';
                draggedImg.style.background = '';
                draggedImg = null;
                startSquare = null;
            } else {
                targetEvent1.style.background = '';
                targetEvent1.style.opacity = '';
            }
            updateFEN();
        }

        // Add event listeners for all squares
        const squares = document.querySelectorAll('.square');
        squares.forEach(square => {
            square.addEventListener('dragover', onDragOver);
            square.addEventListener('dragenter', onDragEnter);
            square.addEventListener('dragleave', onDragLeave);
            square.addEventListener('drop', onDrop);
        });
        // Add event listeners for all images inside squares
        const images = document.querySelectorAll('.square img');
        images.forEach(img => {
            img.addEventListener('dragstart', onDragStart);
            img.addEventListener('dragend', onDragEnd);
        });
    } else {
        // Check if the square already has an image

        if (square.src) {
            square.src = selectedIcon; // Replace the src attribute of the existing image
            return;
        }
        // If there is no existing image, create a new image element and replace the square's contents
        const newImage = document.createElement('img');
        newImage.classList.add('img-cb');
        newImage.src = selectedIcon;
        newImage.alt = '';
        square.innerHTML = ''; // Remove the existing contents of the square
        square.appendChild(newImage); // Add the new image element to the square
    }

    updateFEN();
}

const pieceMapping = {
    'bp': 'p',
    'bn': 'n',
    'bb': 'b',
    'br': 'r',
    'bq': 'q',
    'bk': 'k',
    'wp': 'P',
    'wn': 'N',
    'wb': 'B',
    'wr': 'R',
    'wq': 'Q',
    'wk': 'K'
};

function updateFEN() {
    const board = Array.from(document.querySelectorAll('.square')).reduce(function (acc, square) {
        const rank = parseInt(square.getAttribute('data-rank'));
        const file = parseInt(square.getAttribute('data-file'));
        const imgSrc = square.querySelector('img')?.getAttribute('src');
        if (imgSrc) {
            const fileName = imgSrc.substring(imgSrc.lastIndexOf('/') + 1);
            acc[rank][file] = pieceMapping[fileName.substring(0, 2)];
        } else {
            acc[rank][file] = '';
        }
        return acc;
    }, Array.from({length: 8}, () => Array.from({length: 8}, () => '')));
    let fenString = board.map(function (row) {
        let emptyCount = 0;
        let fenRow = row.map(function (piece) {
            if (piece === '') {
                emptyCount++;
            } else {
                const result = (emptyCount > 0 ? emptyCount : '') + piece;
                emptyCount = 0;
                return result;
            }
        }).join('');
        if (emptyCount > 0) {
            fenRow += emptyCount;
        }
        return fenRow;
    }).join('/');

    const sideToMove = document.querySelector('.color select').value;
    fenString += ' ' + (sideToMove === 'white' ? 'w' : 'b');

    // Get the castling rights
    const whiteShortCastling = document.querySelector('#white-short-castling').checked;
    const whiteLongCastling = document.querySelector('#white-long-castling').checked;
    const blackShortCastling = document.querySelector('#black-short-castling').checked;
    const blackLongCastling = document.querySelector('#black-long-castling').checked;
    let castlingOptions = '';
    if (whiteShortCastling) castlingOptions += 'K';
    if (whiteLongCastling) castlingOptions += 'Q';
    if (blackShortCastling) castlingOptions += 'k';
    if (blackLongCastling) castlingOptions += 'q';
    fenString += ' ' + (castlingOptions || '-');

    document.querySelector('.copyable.positions').value = fenString;
    fenStringTool = fenString.split(' ')[0];
}

const colorSelect = document.querySelector('.color select');
colorSelect.addEventListener('change', updateFEN);
// Event listeners for changes in the castling checkboxes
const castlingCheckboxes = document.querySelectorAll('.castling input[type="checkbox"]');
castlingCheckboxes.forEach(function(checkbox) {
    checkbox.addEventListener('change', updateFEN);
});

function clearBoard() {
    document.querySelectorAll('.files .square img').forEach(img => img.remove());
    updateFEN();
}
// Function to reset the board to its starting position
function startingPosition() {
    const startingPositionMap = {
        "00": "br", "01": "bn", "02": "bb", "03": "bq",
        "04": "bk", "05": "bb", "06": "bn", "07": "br",
        "10": "bp", "11": "bp", "12": "bp", "13": "bp",
        "14": "bp", "15": "bp", "16": "bp", "17": "bp",
        // Rows 20 to 50 should be empty in a standard chess setup
        "60": "wp", "61": "wp", "62": "wp", "63": "wp",
        "64": "wp", "65": "wp", "66": "wp", "67": "wp",
        "70": "wr", "71": "wn", "72": "wb", "73": "wq",
        "74": "wk", "75": "wb", "76": "wn", "77": "wr"
    };
    document.querySelectorAll('.files .square').forEach(square => {
        const rank = square.dataset.rank;
        const file = square.dataset.file;
        const positionKey = `${rank}${file}`;
        const piece = startingPositionMap[positionKey];
        if (piece) {
            let img = square.querySelector('img');
            if (!img) {
                img = document.createElement('img');
                img.className = 'img-cb';
                square.appendChild(img);
            }
            img.setAttribute('src', `/IMAGE/${piece}.png`); // Set the correct path to your image
        } else {
            const img = square.querySelector('img');
            if (img) {
                img.remove();
            }
        }
    });
    updateFEN();
}

document.getElementById('clear-board').addEventListener('click', clearBoard);
document.getElementById('starting-position').addEventListener('click', startingPosition);

const closeButton = document.querySelector('.close-button');
closeButton.addEventListener('click', function () {
    choiceBox.classList.toggle('visible');
});

const continueButton = document.getElementById('continueButton');
const choiceBox = document.getElementById('choiceBox');
continueButton.addEventListener('click', function() {
    choiceBox.classList.toggle('visible');
});
const playWithComputerButton = document.getElementById('playWithComputer');
playWithComputerButton.addEventListener('click', function() {

});
const playWithFriendButton = document.getElementById('playWithFriend');
playWithFriendButton.addEventListener('click', async function () {
    let idMatchType = "10_0";
    let matchId = null;
    const response = await fetch('/api/chess/idMatchType', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            idMatchType: idMatchType,
        })
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }

    const data = await response.json();
    if (data) {
        matchId = data.idMatch;
        window.location.href = "/online/" + idMatchType + "/" + matchId;
    } else {
        console.error('No data received');
    }
});

let f =
    s=>[...s].map(c=>++n%9?+c?n+=--c:a[i='pP/KkQqRrBbNn'.search(c),i&=i>4&a[i]>(i>6)||i]=-~a[i]:x+=c=='/',a=[x=n=0])&&!([p,P,s,k,K]=a,n-71|x-7|s|k*K-1|p>8|P>8)
