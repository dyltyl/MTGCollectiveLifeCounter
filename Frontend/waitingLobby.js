
let lSPlayerName = localStorage.getItem('playerName');
let lSCommanderName = localStorage.getItem('commanderName');
let lSCommanderTwo = localStorage.getItem('partnerName');
let gameSize = localStorage.getItem('gameSize');
let baseLife = localStorage.getItem('baseLife');
let players = [];

/**
 * Creates and emptySlot element within JS, intended to be replaced by a 
 * createPlayerSlot element
 */
function createEmptySlot(){ 
    var root = document.getElementById('root');
    var emptySlot = document.createElement('div');
    emptySlot.setAttribute('class','waitingSlot');
    emptySlot.setAttribute('email', 'empty');
    emptySlot.textContent = '...Waiting for player...';
    insertDeleteButton(emptySlot);
    return emptySlot;
}
/**
 * Inserts a delete button into the waiting slot
 * @param {HTMLDivElement} emptySlot The waiting slot
 */
function insertDeleteButton(emptySlot){
    var delButt = document.createElement('button');
    delButt.setAttribute('class','deletePlayer');
    delButt.setAttribute('onclick','deletePlayer()');
    emptySlot.appendChild(delButt);
}
/**
 * Decriments the game size
 */
function deletePlayer(){ //Rename to decreaseGameSize? TODO: Have this affect the db?
    console.log(localStorage.getItem('gameSize'));
    var currentGameSize = localStorage.getItem('gameSize');
    localStorage.setItem('gameSize', (currentGameSize-1));
}
/**
 * Increments the game size
 */
function increaseGameSize(){ //TODO: Have this affect the db?
    console.log(localStorage.getItem('gameSize'));
    var currentGameSize = localStorage.getItem('gameSize');
    localStorage.setItem('gameSize', (currentGameSize*1+1));
}
/**
 * Displays the player on the screen
 * @param {string} name The name of the player
 * @param {string} commanderOne The first commander
 * @param {string} commanderTwo The second (optional) commander
 */
function createPlayerSlot(name, commanderOne, commanderTwo){
    var root = document.getElementById('root');
    var playerSlot = document.createElement('div');
    playerSlot.setAttribute('class','playerSlot');

    if (lSCommanderTwo === undefined){
        var playerText = document.createTextNode(name + ' || ' + commanderOne);
    }else{
       var playerText = document.createTextNode(name + ' || ' + commanderOne + " || "  + commanderTwo);
    }
    playerSlot.appendChild(playerText);
    // playerSlot.appendChild(c1);
    // root.appendChild(playerSlot);
    return playerSlot;
}

function createNameSlot(name){
    var nameSlot = document.createElement('div');
    nameSlot.setAttribute('class','playerSlot');
    var nameText = document.createTextNode(name);
    nameSlot.appendChild(nameText);
    return nameSlot;
}

function checkWaitingSlots(){ //TODO: Should load in player slots already in game before adding waiting slots
    console.log('Current gameSize: ' + localStorage.getItem('gameSize'));
    var rootChildArr = root.children;
    for(var i = 0; i<rootChildArr.length; i++){
        console.log(rootChildArr[i]);
    }
    while(root.childElementCount < localStorage.getItem('gameSize')){
        console.log('..Appending new slot..');
        root.appendChild(createEmptySlot());
    }
    if(root.childElementCount>localStorage.getItem('gameSize')){
        let fEmptyIndex = findEmptySlot();
        root.removeChild(rootChildArr[fEmptyIndex]);
    }
}
/**
 * Checks if the game has started
 */
function hasGameStarted() {
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName')
        }
    };
    return fetch(getUrl('hasGameStarted'), requestBody)
    .then(response => {
        if(response.status === 200) {
            response.text().then(result => {
                if(result == 'true') {
                    return true;
                }
                else
                    return false;
            });
        }
        else {
            response.text().then(error => {alert(error);});
            return false;
        }
    });
}
/**
 * fetches getAllPlayers() and for each inserts them into the empty waiting lobby slots
 */
function playerRefresh(){
    hasGameStarted()
    .then(result => {
        //If the game has started, join
        if(result) {
            window.location.href = 'gameState.html';
        }
        //Update the list of players
        else {
            getAllPlayers()
            .then(function(response){ 
                if(response.status !== 200) {
                    response.text().then(error => console.log(error));
                }
                else
                    return response.json();
            })
            .then(function(data){
                compareArrays(addPlayer, null, removePlayer, players, data);
            });
        }
    })
    .then(_=>setTimeout(playerRefresh, 5000));
    checkWaitingSlots();
}
/**
 * Adds the Player to the lobby
 * @param {Player} player The Player being added
 */
function addPlayer(player) {
    let index = findEmptySlot();
    if(index === -1) {
        console.log('no more room :('); //TODO: Actual error message
    }
    else { //Add in player
        document.getElementById('root').children[index].textContent = player.name;
        document.getElementById('root').children[index].setAttribute('email', player.email);
        document.getElementById('root').children[index].id = player.email;
        players.push(player);
        insertKickButton(player.email);
    }
}
/**
 * Removes the Player from the lobby
 * @param {Player} player The Player being removed
 */
function removePlayer(player) {
    let slot = document.getElementById(player.email);
    if(!slot) {
        console.log('uhhhh.....that\'s not supposed to happen');
    }
    else { //Remove player
        slot.textContent = '...Waiting for player...';
        slot.setAttribute('email', 'empty');
        slot.removeAttribute('id');
    }
}
/**
 * Finds the next empty slot by root
 */
function findEmptySlot() {
    let slots = document.getElementById('root').children;
    for(let i = 0; i < slots.length; i++) {
        if(slots[i].getAttribute('email') === 'empty') {
            return i;
        }
    }
    return -1;
}
/**
 * Sends the request to the database to start the game for all players
 */
function startGame() {
    if(localStorage.getItem('hostToggle') === 'true') {
        const requestBody={
            method: 'GET',
            headers:{
                "content-type": "application/json; charset=UTF-8",
                gameId: localStorage.getItem('gameName')
            }
        };
        fetch(getUrl('startGame'), requestBody)
        .then(response => {
            if(response.status === 200) {
                console.log('starting game');
                window.location.href = 'gameState.html';
            }
            else {
                response.text().then(error => { console.log(error); });
            }
        });
    }
    else {
        alert('You must be the host to start the game');
    }
}
/**
 * Removes the player from the game
 * @param {string} playersEmail 
 */
function kickPlayer(playersEmail){
    const requestBody={
        method: 'DELETE',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            email: playersEmail,
            gameId: localStorage.getItem('gameName')
        }
    };
    fetch(getUrl('leaveGame'),requestBody)
    .then(function(response){
        if(response.status !== 200) {
            response.text().then(res => {console.log(res)});
        }
    })
    .catch(error=>console.log(error));
}
/**
 * Adds the button next the player to kick them
 * @param {number} i The index of the slot the player is in
 */
function insertKickButton(i){
    let slots = document.getElementById('root').children;
    let kickButton = document.createElement('button');
    kickButton.setAttribute('class','kickButton');
    kickButton.setAttribute('onclick','kickPlayer("'+i+'")' );
    if(i !== localStorage.getItem('playerEmail') && (document.getElementById(i))){
        document.getElementById(i).appendChild(kickButton);
    }
}

function createHostControls(){

}
/**
 * Adds the header for the game
 */
function addHeader(){
    let headerTxt = document.createElement('div');
    headerTxt.setAttribute('class','tooltip');
    headerTxt.textContent = 'Welcome to: ' + localStorage.getItem('gameName');
    let tooltipTxt = document.createElement('span');
    tooltipTxt.textContent = 'This game name is the ID your friends need to join you!';
    tooltipTxt.setAttribute('class', 'tooltiptext');
    headerTxt.appendChild(tooltipTxt);
    headerRoot.appendChild(headerTxt);

}
// root.appendChild(createPlayerSlot(lSPlayerName,lSCommanderName,lSCommanderTwo));
checkWaitingSlots();
playerRefresh();
addHeader();