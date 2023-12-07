// This function will map the filename to the FEN piece notation
function getFENCharFromFileName(filename) {
    const fileToPieceMap = {
        'br.png': 'r', 'bn.png': 'n', 'bb.png': 'b', 'bq.png': 'q', 'bk.png': 'k', 'bp.png': 'p',
        'wr.png': 'R', 'wn.png': 'N', 'wb.png': 'B', 'wq.png': 'Q', 'wk.png': 'K', 'wp.png': 'P'
    };
    return fileToPieceMap[filename] || '';
}
let fen = "";
let emptyCount = 0;
// Iterate over each rank starting from 8 to 1
for (let rank = 0; rank <= 7; rank++) {
    for (let file = 0; file <= 7; file++) {
        // Find the square in rank 'rank' and file 'file'
        const square = document.querySelector(`.square[data-rank="${rank}"][data-file="${file}"]`);
        const img = square.querySelector('img');
        if (img) {
            if (emptyCount > 0) {
                // If there were empty squares before this piece, add the count to the FEN string
                fen += emptyCount;
                emptyCount = 0; // Reset empty square counter
            }
            // Get the filename from the 'th:src' attribute of the image
            const src = img.getAttribute('src');
            const filename = src.split('/').pop(); // Assumes the filename comes after the last '/'
            const fenChar = getFENCharFromFileName(filename);
            fen += fenChar;
        } else {
            emptyCount++;
        }
    }
    // At the end of each file, if there are empty squares, add the count to the FEN string
    if (emptyCount > 0) {
        fen += emptyCount;
        emptyCount = 0;
    }
    // At the end of each rank, add a slash to separate ranks, except for the last rank
    if (rank < 7) fen += '/';
}
// Adding default values for active color, castling availability, en passant, half-move clock and full-move number.
// You will need to adjust these based on the actual game state.
fen += ' w KQkq - 0 1';
console.log(fen);
const FENRequest = {
    fenRequest: fen
};

function getAllPathVariables() {
    return window.location.pathname.split('/').filter(Boolean);
}


const pathVariables = getAllPathVariables();
// const urlParams = new URLSearchParams(window.location.search);
// const idVal = urlParams.get('id');
const idMatchTypeVal1 = pathVariables[1];
const idMatchVal1 = pathVariables[2];

fetch(`/api/chess/fen/${idMatchVal1}`, {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(FENRequest)
})
.then(response => response.json())
.then(data => {
    console.log(data);})
.catch(error => {
    console.error('Error: ', error);
})