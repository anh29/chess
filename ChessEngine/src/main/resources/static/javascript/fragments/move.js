window.addEventListener('load', () => {
    let draggedImg = null; // This will store the dragged image
    let startSquare = null; // This will store the starting square

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

    function getSquareCoords(square) {
        const rankIndex = [...square.parentNode.children].indexOf(square);
        const fileIndex = [...square.parentNode.parentNode.children].indexOf(square.parentNode);
        return { fileIndex, rankIndex };
    }
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
    function onDrop(event) {
        event.preventDefault(); // Prevent default behavior
        if (draggedImg && event.currentTarget.classList.contains('square') && event.currentTarget !== startSquare) {
            const sourceCoords = getSquareCoords(startSquare);
            const targetCoords = getSquareCoords(event.currentTarget);
            console.log(`Move from (${sourceCoords.rankIndex}, ${sourceCoords.fileIndex}) to (${targetCoords.rankIndex}, ${targetCoords.fileIndex})`);

            event.currentTarget.style.background = '';
            let targetImg = event.currentTarget.querySelector('img');
            if (targetImg) {
                event.currentTarget.removeChild(targetImg);
            }

            event.currentTarget.appendChild(draggedImg);
            draggedImg.style.opacity = '';
            draggedImg = null;
            startSquare = null;
        }
    }
});