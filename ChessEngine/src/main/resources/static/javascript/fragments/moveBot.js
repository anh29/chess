let movesBot = [];
let currentEventBot = null;
let targetEventBot = null;
let draggedPieceColorBot = null;
let pieceTypeCodeBot = null;
let selectedTypeBot = null;
let sendMoveBot = null;
let isPromotingBot = false;
let flagBot = "white";

function getAllPathVariables2() {
    return window.location.pathname.split('/').filter(Boolean);
}


const hello2 = getAllPathVariables2();
// const urlParams = new URLSearchParams(window.location.search);
// const idVal = urlParams.get('id');
const idMatchTypeVal2 = hello2[1];
const idMatchVal2 = hello2[2];


window.addEventListener('load', () => {
    let draggedImg = null; // This will store the dragged image
    let startSquare = null; // This will store the starting square

    function getSquareCoords(square) {
        const rankIndex = [...square.parentNode.children].indexOf(square);
        const fileIndex = [...square.parentNode.parentNode.children].indexOf(square.parentNode);
        return {fileIndex, rankIndex};
    }

    function onDragStart(event) {
        if (isPromotingBot) {
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
                draggedPieceColorBot = pieceMatch[1] === 'w' ? 'white' : 'black';
                pieceTypeCodeBot = pieceMatch[2];
                let pieceType;
                switch (pieceTypeCodeBot) {
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

            currentEventBot = event.currentTarget;
            targetEventBot = event.target;

            sendMoveBot = `${sourceCoords.rankIndex}${sourceCoords.fileIndex}${targetCoords.rankIndex}${targetCoords.fileIndex}`;
            if (sourceCoords.rankIndex === 1 && targetCoords.rankIndex === 0 && pieceTypeCodeBot === 'p' && draggedPieceColorBot === 'white') {
                sendMoveBot = `${sourceCoords.fileIndex}${targetCoords.fileIndex}QP`;
            }
            if (sourceCoords.rankIndex === 6 && targetCoords.rankIndex === 7 && pieceTypeCodeBot === 'p' && draggedPieceColorBot === 'black') {
                sendMoveBot = `${sourceCoords.fileIndex}${targetCoords.fileIndex}qP`;
            }
            if (sourceCoords.rankIndex === 3 && pieceTypeCodeBot === 'p' && draggedPieceColorBot === 'white'
                && targetCoords.rankIndex === 2 && Math.abs(sourceCoords.fileIndex - targetCoords.fileIndex) === 1
                && !currentEventBot.querySelector('img')) {
                sendMoveBot = `${sourceCoords.fileIndex}${targetCoords.fileIndex}WE`;
            }
            if (sourceCoords.rankIndex === 4 && pieceTypeCodeBot === 'p' && draggedPieceColorBot === 'black'
                && targetCoords.rankIndex === 5 && Math.abs(sourceCoords.fileIndex - targetCoords.fileIndex) === 1
                && !currentEventBot.querySelector('img')) {
                sendMoveBot = `${sourceCoords.fileIndex}${targetCoords.fileIndex}BE`;
            }

            console.log("send move: ", sendMoveBot);
            const moveRequest = {
                move: sendMoveBot,
                flag: flagBot
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
                currentEventBot.style.background = '';
                let targetImg = currentEventBot.querySelector('img');
                if (draggedImg.classList.contains('img-cb') && draggedPieceColorBot === 'white' && targetCoords.rankIndex === 0 && draggedImg.src.includes('wp.png') && sourceCoords.rankIndex === 1) {
                    showPromotionOverlay(draggedPieceColorBot, currentEventBot);
                } else if (draggedImg.classList.contains('img-cb') && draggedPieceColorBot === 'black' && targetCoords.rankIndex === 7 && draggedImg.src.includes('bp.png') && sourceCoords.rankIndex === 6) {
                    showPromotionOverlay(draggedPieceColorBot, currentEventBot);
                }

                if (targetImg) {
                    const targetPieceColor = targetImg.src.includes('/IMAGE/w') ? 'white' : 'black';

                    if (draggedPieceColorBot === targetPieceColor) {
                        draggedImg.style.opacity = '';
                        draggedImg = null;
                        startSquare = null;
                        return;
                    }

                    currentEventBot.removeChild(targetImg);
                }
                if (pieceTypeCodeBot === 'p') {
                    const rankDiff = targetCoords.rankIndex - sourceCoords.rankIndex;
                    const fileDiff = targetCoords.fileIndex - sourceCoords.fileIndex;

                    // En passant conditions for white pawn
                    if (draggedPieceColorBot === 'white' && rankDiff === -1 && Math.abs(fileDiff) === 1 && targetCoords.rankIndex === 2) {
                        const captureSquare = document.querySelector(`.square[data-rank="${targetCoords.rankIndex + 1}"][data-file="${targetCoords.fileIndex}"]`);
                        const capturedPawnImg = captureSquare ? captureSquare.querySelector('img') : null;
                        if (capturedPawnImg && capturedPawnImg.src.includes('bp.png')) {
                            captureSquare.removeChild(capturedPawnImg);
                        }
                    }

                    // En passant conditions for black pawn
                    if (draggedPieceColorBot === 'black' && rankDiff === 1 && Math.abs(fileDiff) === 1 && targetCoords.rankIndex === 5) {
                        const captureSquare = document.querySelector(`.square[data-rank="${targetCoords.rankIndex - 1}"][data-file="${targetCoords.fileIndex}"]`);
                        const capturedPawnImg = captureSquare ? captureSquare.querySelector('img') : null;
                        if (capturedPawnImg && capturedPawnImg.src.includes('wp.png')) {
                            captureSquare.removeChild(capturedPawnImg);
                        }
                    }
                }

                if (pieceTypeCodeBot === 'k') {
                    if (draggedPieceColorBot === 'white' && sourceCoords.rankIndex === 7 && sourceCoords.fileIndex === 4) {
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
                    } else if (draggedPieceColorBot === 'black' && sourceCoords.rankIndex === 0 && sourceCoords.fileIndex === 4) {
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

                currentEventBot.appendChild(draggedImg);
                draggedImg.style.opacity = '';
                draggedImg = null;
                startSquare = null;
                if (sendMoveBot[3] !== 'P') {
                    movesBot.push(sendMoveBot);
                    console.log("movesBot: ", movesBot);
                    const moveResponse = await moveProcessing({
                        move: sendMoveBot
                    });
                    flagBot = "white";
                    console.log("Move response: ", moveResponse.moveBot);
                    if (moveResponse.moveBot) {
                        movesBot.push(moveResponse.moveBot);
                        console.log("movesBot: ", movesBot);
                        if (moveResponse.moveBot[3] === 'E') {
                            if (moveResponse.isWhite) {
                                const source = document.querySelector(`.square[data-rank="3"][data-file="${moveResponse.moveBot[0]}"]`);
                                const target = document.querySelector(`.square[data-rank="2"][data-file="${moveResponse.moveBot[1]}"]`);
                                const captureBlackPawn = document.querySelector(`.square[data-rank="3"][data-file="${moveResponse.moveBot[1]}"]`);

                                const sourceImg = source.querySelector('img');
                                const captureBlackPawnImg = captureBlackPawn.querySelector('img');

                                if (sourceImg && captureBlackPawnImg) {
                                    source.removeChild(sourceImg);
                                    target.appendChild(sourceImg);
                                    captureBlackPawn.removeChild(captureBlackPawnImg);
                                }
                            } else {
                                const source = document.querySelector(`.square[data-rank="4"][data-file="${moveResponse.moveBot[0]}"]`);
                                const target = document.querySelector(`.square[data-rank="5"][data-file="${moveResponse.moveBot[1]}"]`);
                                const captureWhitePawn = document.querySelector(`.square[data-rank="4"][data-file="${moveResponse.moveBot[1]}"]`);

                                const sourceImg = source.querySelector('img');
                                const captureWhitePawnImg = captureWhitePawn.querySelector('img');

                                if (sourceImg && captureWhitePawnImg) {
                                    source.removeChild(sourceImg);
                                    target.appendChild(sourceImg);
                                    captureWhitePawn.removeChild(captureWhitePawnImg);
                                }
                            }
                        } else if (moveResponse.moveBot[3] === 'P') {
                            if (moveResponse.isWhite) {
                                const source = document.querySelector(`.square[data-rank="1"][data-file="${moveResponse.moveBot[0]}"]`);
                                const target = document.querySelector(`.square[data-rank="0"][data-file="${moveResponse.moveBot[1]}"]`);

                                const sourceImg = source.querySelector('img');
                                const targetImg = target.querySelector('img');

                                if (sourceImg) {
                                    source.removeChild(sourceImg);
                                    if (targetImg) {
                                        target.removeChild(targetImg);
                                    }
                                    target.appendChild(sourceImg);
                                    target.querySelector('img').src = target.querySelector('img').src.replace('wp.png', 'w' + moveResponse.moveBot[2].toLowerCase() + '.png');
                                }
                            } else {
                                const source = document.querySelector(`.square[data-rank="6"][data-file="${moveResponse.moveBot[0]}"]`);
                                const target = document.querySelector(`.square[data-rank="7"][data-file="${moveResponse.moveBot[1]}"]`);

                                const sourceImg = source.querySelector('img');
                                const targetImg = target.querySelector('img');

                                if (sourceImg) {
                                    source.removeChild(sourceImg);
                                    if (targetImg) {
                                        target.removeChild(targetImg);
                                    }
                                    target.appendChild(sourceImg);
                                    target.querySelector('img').src = target.querySelector('img').src.replace('bp.png', 'b' + moveResponse.moveBot[2].toLowerCase() + '.png');
                                }
                            }

                        } else if (moveResponse.moveBot === "0402" || moveResponse.moveBot === "0406") {
                            const sourceKing = document.querySelector(`.square[data-rank="0"][data-file="${moveResponse.moveBot[1]}"]`);
                            const targetKing = document.querySelector(`.square[data-rank="0"][data-file="${moveResponse.moveBot[3]}"]`);

                            let sourceRook = document.querySelector(`.square[data-rank="0"][data-file="0"]`);
                            let targetRook = document.querySelector(`.square[data-rank="0"][data-file="3"]`);
                            if (moveResponse.moveBot === "0406") {
                                sourceRook = document.querySelector(`.square[data-rank="0"][data-file="7"]`);
                                targetRook = document.querySelector(`.square[data-rank="0"][data-file="5"]`);
                            }

                            const sourceKingImg = sourceKing.querySelector('img');
                            const targetKingImg = targetKing.querySelector('img');

                            const sourceRookImg = sourceRook.querySelector('img');
                            const targetRookImg = targetRook.querySelector('img');

                            if (sourceKingImg) {
                                sourceKing.removeChild(sourceKingImg);
                                if (targetKingImg) {
                                    targetKing.removeChild(targetKingImg);
                                }
                                targetKing.appendChild(sourceKingImg);
                            }
                            if (sourceRookImg) {
                                sourceRook.removeChild(sourceRookImg);
                                if (targetRookImg) {
                                    targetRook.removeChild(targetRookImg);
                                }
                                targetRook.appendChild(sourceRookImg);
                            }
                        } else {
                            // console.log(moveResponse.moveBot);
                            const source = document.querySelector(`.square[data-rank="${moveResponse.moveBot[0]}"][data-file="${moveResponse.moveBot[1]}"]`);
                            const target = document.querySelector(`.square[data-rank="${moveResponse.moveBot[2]}"][data-file="${moveResponse.moveBot[3]}"]`);

                            const sourceImg = source.querySelector('img');
                            const targetImg = target.querySelector('img');
                            if (sourceImg) {
                                source.removeChild(sourceImg);
                                if (targetImg) {
                                    target.removeChild(targetImg);
                                }
                                target.appendChild(sourceImg);
                                // target.querySelector('img').src = target.querySelector('img').src.replace('bn.png', 'bq.png');
                                // source.removeChild(sourceImg);
                                // target.appendChild(sourceImg);
                            }
                        }
                    }
                }
            } else {
                targetEventBot.style.background = '';
                targetEventBot.style.opacity = '';
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
    isPromotingBot = true;
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
    selectedTypeBot = promotionType;
    if (firstPartPng[0] === 'w') {
        sendMoveBot = sendMoveBot.replace(`Q`, selectedTypeBot.toUpperCase());
    } else {
        sendMoveBot = sendMoveBot.replace(`q`, selectedTypeBot.toLowerCase());
    }
    movesBot.push(sendMoveBot);
    console.log("movesBot: ", movesBot);
    const moveResponse = await moveProcessing({
        move: sendMoveBot
    });
    flagBot = "white";
    console.log("Move response: ", moveResponse.moveBot);
    isPromotingBot = false;
}

async function makeMove(moveRequest) {
    try {
        const response = await fetch(`/api/chess/move/${idMatchTypeVal2}/${idMatchVal2}`, {
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
        const response = await fetch(`/api/chess/processBot/${idMatchTypeVal2}/${idMatchVal2}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(moveProcessRequest)
        });
        flagBot = "black";
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
    }
}
