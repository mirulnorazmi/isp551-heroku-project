// Define the data for food and items.
var foodData = [
    { 'name': 'Carrots', 'Category': '#101 : Wet ingredients', 'Quantity': '17' },
    { 'name': 'Onions', 'Category': '#201 : Dry ingredients', 'Quantity': '13' },
    { 'name': 'Chicken', 'Category': '#102 : Wet ingredients', 'Quantity': '17' },
    { 'name': 'Fish', 'Category': '#103 : Wet ingredients', 'Quantity': '40' },
    { 'name': 'Soy Sauce', 'Category': '#202 : Dry ingredients', 'Quantity': '17' },
];

var itemData = [
    { 'name': 'Detergent', 'Category': '#301 : Stuff', 'Quantity': '17' },
    { 'name': 'Pampers', 'Category': '#302 : Stuff', 'Quantity': '30' },
];

// Combine all data for the initial table population
var currentData = [...foodData, ...itemData]; 

// Get reference to the table body
var tableBody = document.getElementById('myTable');

// Determine if current page is viewStaff.html
var isViewStaffPage = document.body.id === 'viewStaffPage';

// Store a reference to the previous button clicked
let previousButton = null;

// Function to change button color on click
function changeButtonColor(button) {
    if (previousButton !== null) {
        previousButton.classList.remove('active');
        previousButton.style.backgroundColor = '#ffffff';
        previousButton.style.color = 'black';
    }

    button.classList.add('active');
    button.style.backgroundColor = '#87A2FD';
    button.style.color = 'white';

    previousButton = button;

    // Check which button was clicked and populate the table accordingly
    if (button.innerText === 'Food') {
        currentData = [...foodData]; // Update current data
        populateTable(foodData); // Use food-related dummy data
    } else if (button.innerText === 'Items') {
        currentData = [...itemData]; // Update current data
        populateTable(itemData); // Use item-related dummy data
    } else {
        currentData = [...foodData, ...itemData]; // Update current data
        populateTable(currentData); // Use all the data
    }
}

// Function to populate the table with data
function populateTable(data) {
    // Clear existing table data
    tableBody.innerHTML = '';

    // Populate the table with the new data
    for (var i = 0; i < data.length; i++) {
        var rowHTML = '<th scope="row">' + (i + 1) + '</th>' +
            '<td>' + data[i].name + '</td>' +
            '<td>' + data[i].Category + '</td>' +
            '<td>' + data[i].Quantity + '</td>' +
            '<td>' +
            '<ul class="list-inline m-0">' +
            '<li class="list-inline-item">';

        // Add delete button if not on viewStaff.html
        if (!isViewStaffPage) {
            rowHTML += '<button class="btn btn-lg rounded-0" type="button" data-bs-toggle="modal" data-bs-target="#deleteModal" data-toggle="tooltip" data-placement="top" title="Delete">' +
                '<i class="bi bi-trash"></i>' +
                '</button>';
        }

        rowHTML += '</li>' +
            '<li class="list-inline-item">' +
            '<button class="btn btn-lg rounded-0" type="button" onclick="location.href=\'edittask.html\'" data-toggle="tooltip" data-placement="top" title="Edit">' +
            '<i class="bi bi-pen"></i>' +
            '</button>' +
            '</li>' +
            '</ul>' +
            '</td>';

        var row = document.createElement('tr');
        row.innerHTML = rowHTML;
        tableBody.appendChild(row);

        // Add event listener to delete button if it exists
        if (!isViewStaffPage) {
            row.querySelector('.bi-trash').parentElement.addEventListener('click', function() {
                document.getElementById('confirmDelete').addEventListener('click', function() {
                    data.splice(i, 1); // Delete the item from the array
                    populateTable(data); // Repopulate the table
                }, { once: true }); // Ensure the event listener only fires once
            });
        }
    }
}

// Initial population of the table with default data
populateTable(currentData); 
