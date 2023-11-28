let moves = [];
let currentEvent = null;
let targetEvent = null;
let draggedPieceColor = null;
let pieceTypeCode = null;
let selectedType = null;
let sendMove = null;
let isPromoting = false;
window.addEventListener('load', () => {
    let draggedImg = null; // This will store the dragged image
    let startSquare = null; // This will store the starting square
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
        if (draggedImg && event.currentTarget.classList.contains('square') && event.currentTarget !== startSquare) {
            const pieceMatch = draggedImg.src.match(/\/([wb])([prnbkq])\.png$/);
            if (pieceMatch) {
                draggedPieceColor = pieceMatch[1] === 'w' ? 'white' : 'black';
                pieceTypeCode = pieceMatch[2];
                let pieceType;
                switch (pieceTypeCode) {
                    case 'p': pieceType = 'pawn'; break;
                    case 'r': pieceType = 'rook'; break;
                    case 'n': pieceType = 'knight'; break;
                    case 'b': pieceType = 'bishop'; break;
                    case 'q': pieceType = 'queen'; break;
                    case 'k': pieceType = 'king'; break;
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
            const moveRequest = {
                move: sendMove
            };

            // Send the move to the backend
            const data = await makeMove(moveRequest);
            // fetch('/api/chess/move', {
            //     method: 'POST',
            //     headers: {
            //         'Content-Type': 'application/json'
            //     },
            //     body: JSON.stringify(moveRequest)
            // })
            //     .then(response => response.json())
            //     .then(data => {
                    if (data && data.validMove) {
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
                        if (sendMove[3] !== 'P') {
                            moves.push(sendMove);
                            console.log("moves: ", moves);
                            const moveResponse = await moveProcessing({
                                move: sendMove
                            });
                            console.log("Move response: ", moveResponse);
                        }
                    } else {
                        console.log("vixinh");
                        targetEvent.style.background = '';
                        targetEvent.style.opacity = '';
                    }
                // })
                // .catch(error => {
                //     console.error('Error:', error);
                // });
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

function showPromotionOverlay(color, square) {
    isPromoting = true;
    // Update promotion piece images based on color
    const promotionOverlay = document.getElementById('promotion-overlay');
    const promotionPieces = promotionOverlay.querySelectorAll('.promotion-piece');

    promotionPieces.forEach(img => {
        img.src = img.src.replace(/\/[wb]/, `/${color[0]}`);
        img.onclick = () => selectPromotionPiece(img, square);
    });

    // Show overlay
    promotionOverlay.style.display = 'block';
}

async function selectPromotionPiece(img, square) {
    const promotionType = img.getAttribute('data-piece');
    const promotionOverlay = document.getElementById('promotion-overlay');
    // Hide overlay once a piece is selected
    promotionOverlay.style.display = 'none';

    const srcImg = square.querySelector('img');
    const firstPartPng = srcImg.src.includes('wp.png') ? 'wp' : 'bp';
    square.querySelector('img').src = srcImg.src.replace(`${firstPartPng}.png`, `${firstPartPng[0]}` + promotionType + '.png');
    selectedType = promotionType;
    if (firstPartPng[0] === 'w') {
        sendMove = sendMove.replace(`Q`, selectedType.toUpperCase());
    } else {
        sendMove = sendMove.replace(`q`, selectedType.toLowerCase());
    }
    moves.push(sendMove);
    console.log("moves: ", moves);
    const moveResponse = await moveProcessing({
        move: sendMove
    });
    console.log("Move response: ", moveResponse);
    isPromoting = false;
}

async function makeMove(moveRequest) {
    try {
        const response = await fetch('/api/chess/move', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(moveRequest)
        });
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
    }
}

async function moveProcessing(moveProcessRequest) {
    try {
        const response = await fetch('/api/chess/process', {
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