let currentEvent1 = null;
let targetEvent1 = null;

const listIcon = document.querySelectorAll(".icon");

listIcon.forEach(function (icon) {
   icon.addEventListener("click", selectIcon);
});
function selectIcon(event) {
    const selectedIcon = event.target.src;
    // Store the selected icon for later use
    // console.log(selectedIcon);
    console.log("event target: ", event.target);
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
    console.log("Selected icon: ", selectedIcon);
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
        console.log(square);

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
    console.log("fenString: ", fenString);
}

const colorSelect = document.querySelector('.color select');
colorSelect.addEventListener('change', updateFEN);
// Event listeners for changes in the castling checkboxes
const castlingCheckboxes = document.querySelectorAll('.castling input[type="checkbox"]');
castlingCheckboxes.forEach(function(checkbox) {
    checkbox.addEventListener('change', updateFEN);
});
