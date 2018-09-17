function addInput(button){
    let root = document.getElementById('root'); //Root located within HTML body
    console.log(root.childElementCount);
    if (root.childElementCount < 1){
        root.textContent = 'Game_ID';
        let input = document.createElement('input');
        let searchButton = document.createElement('button');
        searchButton.textContent = 'Search!';
        searchButton.setAttribute('style.text-align','center'); //This does nothing??
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
