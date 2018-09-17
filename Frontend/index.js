function addInput(button){
    let root = document.getElementById('root'); //Root located within HTML body
    if (root.childElementCount < 1){
        root.textContent = 'Game_ID';
        let input = document.createElement('input');
        let searchButton = document.createElement('button');
        searchButton.textContent = 'Search!';
        searchButton.setAttribute('style.text-align','center'); //This does nothing??
        searchButton.setAttribute('onclick', 'search()');
        input.setAttribute('id','searchInput');
        if(button.id === 'returnGame'){
            input.setAttribute('placeHolder','PreviousGameID');
        } 
        else{ //searchGame
            input.setAttribute('placeHolder','WaitingLobbyID');
        }
        root.appendChild(input);
        root.appendChild(searchButton);
    } 
    else{
        let searchInput = document.getElementById('searchInput');
        if (searchInput.getAttribute('placeHolder') === 'PreviousGameID' && button.id === 'searchGame'){
            searchInput.setAttribute('placeHolder','WaitingLobbyID');
        }        
        else if(searchInput.getAttribute('placeHolder') === 'WaitingLobbyID' && button.id === 'returnGame') {
            searchInput.setAttribute('placeHolder','PreviousGameID');
        }
    }
}
function search() {
    if(document.getElementById('searchInput').value.trim().length < 1) {
        alert('Please enter a Game ID to search for!');
        return;
    }
    let query = document.getElementById('searchInput').value;
    console.log(query);
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            "gameId": query
        }
    };
    fetch(getUrl('game'), requestBody)
    .then(response => {
        if(response.status !== 200) {
            response.text().then(error => alert(error));
        }
        else {
            response.json().then(gameArr => {
                console.log(gameArr);
                if(gameArr.length > 0) {
                    clearGames();
                    for(let i = 0; i < gameArr.length; i++) {
                        addGame(gameArr[i]);
                    }
                }
                else {
                    alert('Your search returned no results');
                }
            });
        }
    });
}
function clearGames() {
    let root = document.getElementById('games');
    while(root.firstChild)
        root.removeChild(root.firstChild);
}
function addGame(game) {
    console.log(game);
    let gameDiv = document.createElement('div');
    gameDiv.setAttribute('id', game.gameId);
    let gameId = document.createElement('h3');
    gameId.setAttribute('style','display: inline-block; padding-right: 10px;');
    gameId.textContent = game.gameId;
    let gameHost = document.createElement('h3');
    gameHost.setAttribute('style','display: inline-block; padding-right: 10px;');
    gameHost.textContent = game.host;
    let gameSize = document.createElement('h3');
    gameSize.setAttribute('style','display: inline-block; padding-right: 10px;');
    gameSize.textContent = game.currentSize + '/' + game.maxSize;
    let button = document.createElement('button');

    let searchInput = document.getElementById('searchInput');
    if (searchInput.getAttribute('placeHolder') === 'WaitingLobbyID'){
        button.textContent = 'Join Game';
        button.setAttribute('onclick','joinGame("'+game.gameId+'");');
    }        
    else {
        button.textContent = 'Re-Join Game';
    }
    
    gameDiv.appendChild(gameId);
    gameDiv.appendChild(gameHost);
    gameDiv.appendChild(gameSize);
    gameDiv.appendChild(button);
    document.getElementById('games').appendChild(gameDiv);
}
/**
 * Prompts the user for the password to the game and begins the process of joining it
 * @param {string} gameId The id of the game to join
 */
function joinGame(gameId) {
    console.log('joining: '+gameId);
    let password = prompt("Please enter the password for the game");
    console.log(password);
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            "gameId": gameId,
            "gamePassword": password
        }
    };
    fetch(getUrl('verifyGame'), requestBody)
    .then(response => {
        if(response.status !== 200) {
            response.text().then(error => alert(error));
        }
        else {
            response.json().then(result => {
                console.log(result);
                if(result === true) {
                    localStorage.setItem('gameName', gameId);
                    localStorage.setItem('gamePass', password);
                    window.location.href= 'playerCreation.html';
                }
                else {
                    alert('Incorrect password');
                }
            });
        }
    });
}