let moves = [];
let currentEvent = null;
let targetEvent = null;
let draggedPieceColor = null;
let pieceTypeCode = null;
let sendMove = null;
let isPromoting = false;

let flagPuzzle;
let fen;
let firstMove;
let puzzleID = null;

let puzzleData = null;
$.ajax({
    url: "/api/puzzle/random",
    async: false,
    success: function(data) {
        puzzleData = data;
    }
});
// Use the received puzzle data
console.log(puzzleData);

fen = puzzleData.fen;
flagPuzzle = puzzleData.flag;
firstMove = puzzleData.firstMove;
puzzleID = puzzleData.id;
setCookie("idPuzzle", puzzleID, 1);

// Define the FEN string
// fen = "q3k1nr/1pp1nQpp/3p4/1P2p3/4P3/B1PP1b2/B5PP/5K2 b k - 0 17";
// Convert the FEN string into an array of ranks
const smallFen = fen.split(" ");
const ranks = smallFen[0].split('/');
// Loop through each rank
for (let rankIndex = 0; rankIndex < ranks.length; rankIndex++) {
    const rank = ranks[rankIndex];
    // Split the rank into individual squares
    const squares = rank.split('');
    // Loop through each square
    let fileIndex = 0;
    for (let indexOfSquare = 0; indexOfSquare < squares.length; indexOfSquare++) {
        const square = squares[indexOfSquare];
        // Get the corresponding div element
        const div = document.querySelector('[data-rank="' + rankIndex + '"][data-file="' + fileIndex + '"]');
        // Update the div element based on the square value
        switch (square) {
            case 'p':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/bp.png" alt="">';
                break;
            case 'n':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/bn.png" alt="">';
                break;
            case 'b':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/bb.png" alt="">';
                break;
            case 'r':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/br.png" alt="">';
                break;
            case 'q':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/bq.png" alt="">';
                break;
            case 'k':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/bk.png" alt="">';
                break;
            case 'P':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/wp.png" alt="">';
                break;
            case 'N':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/wn.png" alt="">';
                break;
            case 'B':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/wb.png" alt="">';
                break;
            case 'R':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/wr.png" alt="">';
                break;
            case 'Q':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/wq.png" alt="">';
                break;
            case 'K':
                fileIndex += 1;
                div.innerHTML = '<img class="img-cb" src="/IMAGE/wk.png" alt="">';
                break;
            default:
                fileIndex += parseInt(square);
                div.innerHTML = ''; // Empty square
                break;
        }
    }
}

setTimeout(() => {}, 5000);
slideImage(convertMoveFormat(firstMove));

window.addEventListener('load', () => {
    let draggedImg = null;
    let startSquare = null;

    function getSquareCoords(square) {
        const rankIndex = [...square.parentNode.children].indexOf(square);
        const fileIndex = [...square.parentNode.parentNode.children].indexOf(square.parentNode);
        return {fileIndex, rankIndex};
    }

    function onDragStart(event) {
        if (isPromoting) {
            event.preventDefault();
            return;
        }
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
            event.target.style.background = 'rgba(0,0,0,0.2)';
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
        event.target.style.opacity = '';
    }

    async function onDrop(event) {
        event.preventDefault(); // Prevent default behavior
        const ID = getCookie("idPuzzle");
        if (draggedImg && event.currentTarget.classList.contains('square') && event.currentTarget !== startSquare) {
            const pieceMatch = draggedImg.src.match(/\/([wb])([prnbkq])\.png$/);
            if (pieceMatch) {
                draggedPieceColor = pieceMatch[1] === 'w' ? 'white' : 'black';
                pieceTypeCode = pieceMatch[2];
                let pieceType;
                switch (pieceTypeCode) {
                    case 'p':
                        pieceType = 'pawn';
                        break;
                    case 'r':
                        pieceType = 'rook';
                        break;
                    case 'n':
                        pieceType = 'knight';
                        break;
                    case 'b':
                        pieceType = 'bishop';
                        break;
                    case 'q':
                        pieceType = 'queen';
                        break;
                    case 'k':
                        pieceType = 'king';
                        break;
                }
            }
            const sourceCoords = getSquareCoords(startSquare);
            const targetCoords = getSquareCoords(event.currentTarget);
            console.log(`Move from (${sourceCoords.rankIndex}, ${sourceCoords.fileIndex}) to (${targetCoords.rankIndex}, ${targetCoords.fileIndex})`);

            currentEvent = event.currentTarget;
            targetEvent = event.target;

            sendMove = `${sourceCoords.rankIndex}${sourceCoords.fileIndex}${targetCoords.rankIndex}${targetCoords.fileIndex}`;
            if (sourceCoords.rankIndex === 1 && targetCoords.rankIndex === 0 && pieceTypeCode === 'p' && draggedPieceColor === 'white') {
                sendMove = `${sourceCoords.fileIndex}${targetCoords.fileIndex}QP`;
            }
            if (sourceCoords.rankIndex === 6 && targetCoords.rankIndex === 7 && pieceTypeCode === 'p' && draggedPieceColor === 'black') {
                sendMove = `${sourceCoords.fileIndex}${targetCoords.fileIndex}qP`;
            }
            if (sourceCoords.rankIndex === 3 && pieceTypeCode === 'p' && draggedPieceColor === 'white'
                && targetCoords.rankIndex === 2 && Math.abs(sourceCoords.fileIndex - targetCoords.fileIndex) === 1
                && !currentEvent.querySelector('img')) {
                sendMove = `${sourceCoords.fileIndex}${targetCoords.fileIndex}WE`;
            }
            if (sourceCoords.rankIndex === 4 && pieceTypeCode === 'p' && draggedPieceColor === 'black'
                && targetCoords.rankIndex === 5 && Math.abs(sourceCoords.fileIndex - targetCoords.fileIndex) === 1
                && !currentEvent.querySelector('img')) {
                sendMove = `${sourceCoords.fileIndex}${targetCoords.fileIndex}BE`;
            }

            console.log("send move: ", sendMove);
            const puzzleResponse = await movePuzzle({
                move: sendMove,
                id: ID,
            });
            if (puzzleResponse.right) {
                console.log("firstMove: ", puzzleResponse.firstMove);
                if (puzzleResponse.firstMove) {
                    slideImage(convertMoveFormat(puzzleResponse.firstMove));
                }
                currentEvent.style.background = '';
                let targetImg = currentEvent.querySelector('img');
                if (draggedImg.classList.contains('img-cb') && draggedPieceColor === 'white' && targetCoords.rankIndex === 0 && draggedImg.src.includes('wp.png') && sourceCoords.rankIndex === 1) {
                    showPromotionOverlay(draggedPieceColor, currentEvent);
                } else if (draggedImg.classList.contains('img-cb') && draggedPieceColor === 'black' && targetCoords.rankIndex === 7 && draggedImg.src.includes('bp.png') && sourceCoords.rankIndex === 6) {
                    showPromotionOverlay(draggedPieceColor, currentEvent);
                }

                if (targetImg) {
                    const targetPieceColor = targetImg.src.includes('/IMAGE/w') ? 'white' : 'black';

                    if (draggedPieceColor === targetPieceColor) {
                        draggedImg.style.opacity = '';
                        draggedImg = null;
                        startSquare = null;
                        return;
                    }

                    currentEvent.removeChild(targetImg);
                }
                if (pieceTypeCode === 'p') {
                    const rankDiff = targetCoords.rankIndex - sourceCoords.rankIndex;
                    const fileDiff = targetCoords.fileIndex - sourceCoords.fileIndex;

                    // En passant conditions for white pawn
                    if (draggedPieceColor === 'white' && rankDiff === -1 && Math.abs(fileDiff) === 1 && targetCoords.rankIndex === 2) {
                        const captureSquare = document.querySelector(`.square[data-rank="${targetCoords.rankIndex + 1}"][data-file="${targetCoords.fileIndex}"]`);
                        const capturedPawnImg = captureSquare ? captureSquare.querySelector('img') : null;
                        if (capturedPawnImg && capturedPawnImg.src.includes('bp.png')) {
                            captureSquare.removeChild(capturedPawnImg);
                        }
                    }

                    // En passant conditions for black pawn
                    if (draggedPieceColor === 'black' && rankDiff === 1 && Math.abs(fileDiff) === 1 && targetCoords.rankIndex === 5) {
                        const captureSquare = document.querySelector(`.square[data-rank="${targetCoords.rankIndex - 1}"][data-file="${targetCoords.fileIndex}"]`);
                        const capturedPawnImg = captureSquare ? captureSquare.querySelector('img') : null;
                        if (capturedPawnImg && capturedPawnImg.src.includes('wp.png')) {
                            captureSquare.removeChild(capturedPawnImg);
                        }
                    }
                }

                if (pieceTypeCode === 'k') {
                    if (draggedPieceColor === 'white' && sourceCoords.rankIndex === 7 && sourceCoords.fileIndex === 4) {
                        if (targetCoords.rankIndex === 7 && targetCoords.fileIndex === 6) {
                            const rookSquare = document.querySelector(`.square[data-rank="7"][data-file="7"]`);
                            const checkImg = rookSquare ? rookSquare.querySelector('img') : null;
                            if (checkImg && checkImg.src.includes('wr.png')) {
                                rookSquare.removeChild(checkImg);
                            }
                            const addRookSquare = document.querySelector(`.square[data-rank="7"][data-file="5"]`);
                            if (addRookSquare) {
                                addRookSquare.appendChild(checkImg);
                            }
                        } else if (targetCoords.rankIndex === 7 && targetCoords.fileIndex === 2) {
                            const rookSquare = document.querySelector(`.square[data-rank="7"][data-file="0"]`);
                            const checkImg = rookSquare ? rookSquare.querySelector('img') : null;
                            if (checkImg && checkImg.src.includes('wr.png')) {
                                rookSquare.removeChild(checkImg);
                            }
                            const addRookSquare = document.querySelector(`.square[data-rank="7"][data-file="3"]`);
                            if (addRookSquare) {
                                addRookSquare.appendChild(checkImg);
                            }
                        }
                    } else if (draggedPieceColor === 'black' && sourceCoords.rankIndex === 0 && sourceCoords.fileIndex === 4) {
                        if (targetCoords.rankIndex === 0 && targetCoords.fileIndex === 6) {
                            const rookSquare = document.querySelector(`.square[data-rank="0"][data-file="7"]`);
                            const checkImg = rookSquare ? rookSquare.querySelector('img') : null;
                            if (checkImg && checkImg.src.includes('wr.png')) {
                                rookSquare.removeChild(checkImg);
                            }
                            const addRookSquare = document.querySelector(`.square[data-rank="0"][data-file="5"]`);
                            if (addRookSquare) {
                                addRookSquare.appendChild(checkImg);
                            }
                        } else if (targetCoords.rankIndex === 0 && targetCoords.fileIndex === 2) {
                            const rookSquare = document.querySelector(`.square[data-rank="0"][data-file="0"]`);
                            const checkImg = rookSquare ? rookSquare.querySelector('img') : null;
                            if (checkImg && checkImg.src.includes('wr.png')) {
                                rookSquare.removeChild(checkImg);
                            }
                            const addRookSquare = document.querySelector(`.square[data-rank="0"][data-file="3"]`);
                            if (addRookSquare) {
                                addRookSquare.appendChild(checkImg);
                            }
                        }
                    }
                }

                currentEvent.appendChild(draggedImg);
                draggedImg.style.opacity = '';
                draggedImg = null;
                startSquare = null;
                console.log(sendMove, puzzleID);
            } else {
                targetEvent.style.background = '';
                targetEvent.style.opacity = '';
            }

            if (puzzleResponse.done) {
                location.reload();
            }
        }
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
});

async function movePuzzle(moveProcessRequest) {
    try {
        const response = await fetch('/api/puzzle/puzzleProcessing', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(moveProcessRequest)
        });
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
    }
}

function slideImage(move) {
    const sourceRank = move.substring(0, 1);
    const sourceFile = move.substring(1, 2);
    const targetRank = move.substring(2, 3);
    const targetFile = move.substring(3, 4);

    console.log(sourceRank, sourceFile, targetRank, targetFile);

    const sourceElement = document.querySelector(`.square[data-rank="${sourceRank}"][data-file="${sourceFile}"]`);
    const targetElement = document.querySelector(`.square[data-rank="${targetRank}"][data-file="${targetFile}"]`);
    const sourceImageElement = sourceElement.querySelector('.img-cb');
    const targetImageElement = targetElement.querySelector('.img-cb');
    console.log("SourceImageElement: ", sourceImageElement);
    console.log("TargetImageElement: ", targetImageElement);
    console.log("TargetElement: ", targetElement);
    if (targetElement.hasChildNodes()) {
        targetElement.innerHTML = '';
    }

    const sourceRect = sourceElement.getBoundingClientRect();
    const targetRect = targetElement.getBoundingClientRect();
    console.log(sourceRect.left, sourceRect.top, targetRect.left, targetRect.top);
    const offsetX = targetRect.left - sourceRect.left;
    const offsetY = targetRect.top - sourceRect.top;
    sourceImageElement.style.transitionDuration = `1s`;
    sourceImageElement.style.transform = `translate(${offsetX}px, ${offsetY}px)`;
    setTimeout(() => {
        sourceElement.removeChild(sourceImageElement);
    // sourceElement.appendChild(sourceImageElement);
        sourceImageElement.removeAttribute('style');
        // if (targetElement.querySelector('.img-cb')) {
        //     targetElement.removeChild(targetImageElement);
        // }
        if (targetElement.hasChildNodes()) {
            targetElement.innerHTML = '';
        }
        targetElement.appendChild(sourceImageElement);
    }, 1500);
}

function convertMoveFormat(move) {
    const sourceFile = move.charAt(0);
    const sourceRank = move.charAt(1);
    const targetFile = move.charAt(2);
    const targetRank = move.charAt(3);
    const sourceFileNumber = sourceFile.charCodeAt(0) - 'a'.charCodeAt(0);
    const sourceRankNumber = '8'.charCodeAt(0) - sourceRank.charCodeAt(0);
    const targetFileNumber = targetFile.charCodeAt(0) - 'a'.charCodeAt(0);
    const targetRankNumber = '8'.charCodeAt(0) - targetRank.charCodeAt(0);
    return "" + sourceRankNumber + sourceFileNumber + targetRankNumber + targetFileNumber;
}


function setCookie(name, value, days) {
    let expires = "";
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + value + expires + "; path=/";
}

function getCookie(name) {
    const cookieName = name + "=";
    const cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(cookieName) === 0) {
            return cookie.substring(cookieName.length, cookie.length);
        }
    }
    return null;
}
