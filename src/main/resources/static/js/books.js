const bookContainer = document.getElementById('book-container');
const loadingSpinner = document.getElementById('loading');
const errorBox = document.getElementById('error');
const successBox = document.getElementById('add-success');

function fetchBooks() {
    loadingSpinner.style.display = 'block';
    bookContainer.innerHTML = '';
    bookContainer.style.display = 'none';
    errorBox.style.display = 'none';

    fetch('/books')
        .then(resp => {
            if (!resp.ok) throw new Error("Failed to load");
            return resp.json();
        })
        .then(books => {
            loadingSpinner.style.display = 'none';
            bookContainer.style.display = 'flex';
            books.forEach(book => {
                const card = document.createElement('div');
                card.className = 'col';
                card.innerHTML = `
                        <div class="card h-100 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">${book.name}</h5>
                                <p class="card-text text-muted">by ${book.author}</p>
                            </div>
                        </div>
                    `;
                bookContainer.appendChild(card);
            });
        })
        .catch(err => {
            console.error(err);
            loadingSpinner.style.display = 'none';
            errorBox.style.display = 'block';
        });
}

document.getElementById('add-book-form').addEventListener('submit', function (e) {
    e.preventDefault();
    const name = document.getElementById('book-name').value.trim();
    const author = document.getElementById('book-author').value.trim();

    fetch('/books', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({name, author})
    })
        .then(resp => {
            if (resp.status === 201) return resp.json();
            else throw new Error("Invalid input");
        })
        .then(newBook => {
            document.getElementById('book-name').value = '';
            document.getElementById('book-author').value = '';
            successBox.style.display = 'block';
            setTimeout(() => successBox.style.display = 'none', 2000);
            fetchBooks();
        })
        .catch(err => {
            console.error(err);
            alert("Failed to add book: " + err.message);
        });
});

// Refresh books when Available Books tab is shown
document.getElementById('list-tab').addEventListener('shown.bs.tab', fetchBooks);

// Initial load
fetchBooks();

