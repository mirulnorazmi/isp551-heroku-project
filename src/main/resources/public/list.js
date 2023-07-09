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
        
        rowHTML += '</li>' +
            '<li class="list-inline-item">' +
            '<button class="btn btn-lg rounded-0" type="button" onclick="Quantity(-1)" data-toggle="tooltip" data-placement="top" title="Edit">' +
            '<i class="bi bi-plus-circle"></i>' +
            '</button>' +
            '</li>' +
            '</ul>' +
            '</td>';

        var row = document.createElement('tr');
        row.innerHTML = rowHTML;
        tableBody.appendChild(row);
        
    }
}

// function click Decrement
function Quantity(click){
    const sum = document.getElementById('myTable');
    const sumvalue = parseInt(Quantity.innerText);
    console.log(sumvalue + click);
    Quantity.innerText = sumvalue;

// avoid negative
if(sumvalue < 0){
    Quantity.innerText = 0;
}
//reset value

}


// Initial population of the table with default data
populateTable(currentData); 