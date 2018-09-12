const root = document.getElementById('root');
let gameStartedUrl = 'https://magic-database.herokuapp.com/hasGameStarted';
const leaveGameURL = 'https://magic-database.herokuapp.com/leaveGame';
const headerRoot = document.getElementById('headerRoot');

var lSPlayerName = localStorage.getItem('playerName');
var lSCommanderName = localStorage.getItem('commanderName');
var lSCommanderTwo = localStorage.getItem('partnerName');
var gameSize = localStorage.getItem('gameSize');
var baseLife = localStorage.getItem('baseLife');
var players = [];

/**
 * Creates and emptySlot element within JS, intended to be replaced by a 
 * createPlayerSlot element
 */
function createEmptySlot(){ 
    var root = document.getElementById('root');
    var emptySlot = document.createElement('div');
    emptySlot.setAttribute('class','waitingSlot');
    emptySlot.setAttribute('email', 'empty');
    var txtNode = document.createTextNode('...Waiting for player...');
    emptySlot.appendChild(txtNode);
    insertDeleteButton(emptySlot);
    //root.appendChild(emptySlot);
    return emptySlot;
}

function insertDeleteButton(emptySlot){
    var delButt = document.createElement('button');
    delButt.setAttribute('class','deletePlayer');
    delButt.setAttribute('onclick','deletePlayer()');
    emptySlot.appendChild(delButt);
}

function deletePlayer(){
    console.log(localStorage.getItem('gameSize'));
    var currentGameSize = localStorage.getItem('gameSize');
    localStorage.setItem('gameSize', (currentGameSize-1));
}

function increaseGameSize(){
    console.log(localStorage.getItem('gameSize'));
    var currentGameSize = localStorage.getItem('gameSize');
    localStorage.setItem('gameSize', (currentGameSize*1+1));
}

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
 * fetches getAllPlayers() and for each inserts them into the empty waiting lobby slots
 */
function playerRefresh(){
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName')
        }
    };
    fetch(gameStartedUrl, requestBody)
    .then(response => {
        if(response.status === 200) {
            response.text().then(result => {
                //If the game has started, join
                if(result == 'true') {
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
                        //Determine if the arrays are equal
                        let equal = true;
                        if(data.length !== players.length)
                            equal = false;
                        for(let i = 0; i < data.length && equal; i++) {
                            if(data[i].email !== players[i].email) {
                                equal = false;
                            }
                        }   
                        //If not equal
                        if(!equal) {
                            for(let i = 0; i < data.length; i++) {
                                let found = false;
                                for(let j = 0; j < players.length; j++) {
                                    if(data[i].email === players[j].email) {
                                        found = true;
                                        break;
                                    }
                                }
                                //player in data but not players
                                if(!found) {
                                    let index = findEmptySlot();
                                    if(index === -1) {
                                        console.log('no more room :('); //TODO: Actual error message
                                    }
                                    else { //Add in player
                                        document.getElementById('root').children[index].textContent = data[i].name;
                                        document.getElementById('root').children[index].setAttribute('email', data[i].email);
                                        document.getElementById('root').children[index].id = data[i].email;
                                        players.push(data[i]);
                                        insertKickButton(data[i].email);
                                    }
                
                                }
                            }
                
                            for(let i = 0; i < players.length; i++) {
                                let found = false;
                                for(let j = 0; j < data.length; j++) {
                                    if(data[j].email === players[i].email) {
                                        found = true;
                                        break;
                                    }
                                }
                                //player in players but not data
                                if(!found) {
                                    let index = findSlot(players[i]);
                                    if(index === -1) {
                                        console.log('uhhhh.....that\'s not supposed to happen');
                                    }
                                    else { //Remove player
                                        document.getElementById('root').children[index].textContent = '...Waiting for player...';
                                        document.getElementById('root').children[index].setAttribute('email', 'empty');
                                        players.splice(i, 1);
                                        i--;
                                    }
                                }
                            }
                            
                        }
                    })
                }
            })
        }
    })
    .then(_=>setTimeout(playerRefresh, 5000));
    checkWaitingSlots();
}
/**
 * Finds the next empty slot by root
 */
function findEmptySlot() {
    var slots = document.getElementById('root').children;
    for(let i = 0; i < slots.length; i++) {
        if(slots[i].getAttribute('email') === 'empty') {
            return i;
        }
    }
    return -1;
}
function findSlot(player) {
    var slots = document.getElementById('root').children;
    for(let i = 0; i < slots.length; i++) {
        if(slots[i].getAttribute('email') === player.email) {
            return i;
        }
    }
    return -1;
}
function startGame() {
    if(localStorage.getItem('hostToggle') === 'true') {
        const requestBody={
            method: 'GET',
            headers:{
                "content-type": "application/json; charset=UTF-8",
                gameId: localStorage.getItem('gameName')
            }
        };
        fetch('https://magic-database.herokuapp.com/startGame', requestBody)
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

function kickPlayer(playersEmail){
    const requestBody={
        method: 'DELETE',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            email: playersEmail,
            gameId: localStorage.getItem('gameName')
        }
    };
    fetch(leaveGameURL,requestBody)
    .then(function(response){
        if(response.status !== 200) {
            response.text().then(res => {console.log(res)});
        }
    })
    .catch(error=>console.log(error));
}

function insertKickButton(i){
    var slots = document.getElementById('root').children;
    var kickButton = document.createElement('button');
    kickButton.setAttribute('class','kickButton');
    kickButton.setAttribute('onclick','kickPlayer("'+i+'")' );
    // var buttonImage = document.createElement('img');
    // buttonImage.setAttribute('src','https://pastorhobbins.files.wordpress.com/2011/07/kickedout1.gif');
    // kickButton.appendChild(buttonImage);
    console.log('hmmm?')
    if(i !== localStorage.getItem('playerEmail') && (document.getElementById(i))){
        console.log('ah');
        document.getElementById(i).appendChild(kickButton);
    }
}

function createHostControls(){

}
function addHeader(){
    var headerTxt = document.createElement('div');
    headerTxt.setAttribute('class','tooltip');
    headerTxt.textContent = 'Welcome to: ' + localStorage.getItem('gameName');
    var tooltipTxt = document.createElement('span');
    tooltipTxt.textContent = 'This game name is the ID your friends need to join you!';
    tooltipTxt.setAttribute('class', 'tooltiptext');
    headerTxt.appendChild(tooltipTxt);
    headerRoot.appendChild(headerTxt);

}
// root.appendChild(createPlayerSlot(lSPlayerName,lSCommanderName,lSCommanderTwo));
checkWaitingSlots();
playerRefresh();
addHeader();