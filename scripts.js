function createLineBreak(container){
    container.appendChild(document.createElement("br"));
}

function addFields(){
    // Number of inputs to create
    var number = document.getElementById("player").value;
    // Container <div> where dynamic content will be placed
    var container = document.getElementById("container");
    // Clear previous contents of the container
    while (container.hasChildNodes()) {
        container.removeChild(container.lastChild);
    }
    if(number>8){
        container.appendChild(document.createTextNode(
            'Eight Player Maximum'));
            createLineBreak(container);
        number = 8;
    } else if(number<2 && number>-1){
        number = 0;  
        container.appendChild(document.createTextNode(
            'Additional Friends Required'));
    } else if(number<0){
        number = 0;
        container.appendChild(document.createTextNode(
            'It is beyond the scope of this app to create "negative" friends. Only Magic can do that.'
        ));
    }
    for (i=0;i<number;i++){
        // Append a node with a random text
        container.appendChild(document.createTextNode("Player " + (i+1)));
        // Create an <input> element, set its type and name attributes
        var inputName = document.createElement("input");
        inputName.type = "text";
        inputName.name = "player" + i;

        var inputLife = document.createElement('input');
        inputLife.type = 'number';
        inputLife.name = 'HP';
        inputLife.value = document.getElementById('lifeTotal').value;

        container.appendChild(inputName);
        container.appendChild(inputLife);
        // Append a line break 
        createLineBreak(container);
    }
}