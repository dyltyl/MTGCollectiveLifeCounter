function checkLocalLog(){
    console.log('Game_Size: ' + localStorage.getItem('gameSize') + ' || Base_Life: ' + localStorage.getItem('baseLife'));
    console.log('Game_Name: ' + localStorage.getItem('gameName') + ' || Game_Pass: ' + localStorage.getItem('gamePass'));
    console.log('Player_Name: ' + localStorage.getItem('playerName') + ' || Player_Email: ' + localStorage.getItem('playerEmail'));
    console.log('Commander: ' + localStorage.getItem('commanderName') + ' || Partner_Name: ' + localStorage.getItem('partnerName'));
}
function addInput(button){
    var root = document.getElementById('root'); //Root located within HTML body
    
    console.log(root.childElementCount);
    if (root.childElementCount < 1){
        var input = document.createElement('input');
        var textNode = document.createTextNode('Game_ID ');

        var searchButton = document.createElement('button');
        var buttonText = document.createTextNode('Search!'); //onClick for this button will query the database for the respective game
        searchButton.setAttribute('style.text-align','center'); //This does nothing??

        searchButton.appendChild(buttonText);

    if(button.id === 'returnGame'){
        input.setAttribute('placeHolder','PreviousGameID');
        input.setAttribute('id','returnInput');
    } else{
        input.setAttribute('placeHolder','WaitingLobbyID');
        input.setAttribute('id','searchInput');
    }

    root.appendChild(textNode);
    root.appendChild(input);
    root.appendChild(searchButton);
    } else{
        var input = document.createElement('input');
        input.appendChild(document.createTextNode('Game_ID'));
        var searchButton = document.createElement('button');
        var buttonText = document.createTextNode('Search!'); //onClick for this button will query the database for the respective game
        searchButton.appendChild(buttonText);
        
        var returnInput = document.getElementById('returnInput');
        var searchInput = document.getElementById('searchInput');
        console.log("Return Input after second Press: " + returnInput);
        console.log("Search Input after second Press: " + searchInput);
        console.log('button name: ' + button.id);
        // console.log('Return Input ID: ' + returnInput.id);

        if (returnInput != null && button.id == 'searchGame'){
            var previousElement = document.getElementById('returnInput');
            input.setAttribute('placeHolder','WaitingLobbyID');
            input.setAttribute('id', 'searchInput');
            previousElement.parentNode.replaceChild(input,previousElement);
        }

        
        if (searchInput != null && button.id == 'returnGame'){
            var previousElement = document.getElementById('searchInput');
            input.setAttribute('placeHolder','PreviousGameID');
            input.setAttribute('id','returnInput');
            previousElement.parentNode.replaceChild(input,previousElement);
        }
    }
}