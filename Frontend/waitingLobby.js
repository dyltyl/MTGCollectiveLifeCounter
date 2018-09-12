const gameStartedUrl = 'https://magic-database.herokuapp.com/hasGameStarted';
const leaveGameURL = 'https://magic-database.herokuapp.com/leaveGame';

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
    let emptySlot = document.createElement('div');
    emptySlot.setAttribute('class','playerSlot');
    emptySlot.setAttribute('email', 'empty');
    emptySlot.textContent = '...Waiting for player...';
    return emptySlot;
}

function createWaitingSlots(){ //TODO: Should load in player slots already in game before adding waiting slots
    for(let i = 0; i < gameSize; i++){
        root.appendChild(createEmptySlot());
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
                                    let slot = document.getElementById(players[i].email);
                                    if(!slot) {
                                        console.log('uhhhh.....that\'s not supposed to happen');
                                    }
                                    else { //Remove player
                                        slot.textContent = '...Waiting for player...';
                                        slot.setAttribute('email', 'empty');
                                        slot.removeAttribute('id');
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
}
function findEmptySlot() {
    let slots = document.getElementById('root').children;
    for(let i = 0; i < slots.length; i++) {
        if(slots[i].getAttribute('email') === 'empty') {
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
    let slots = document.getElementById('root').children;
    let kickButton = document.createElement('button');
    kickButton.setAttribute('class','kickButton');
    kickButton.setAttribute('onclick','kickPlayer("'+i+'")' );
    if(i !== localStorage.getItem('playerEmail') && (document.getElementById(i))){
        document.getElementById(i).appendChild(kickButton);
    }
}

function deletePlayer(){

}
function createHostControls(){

}
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
createWaitingSlots();
playerRefresh();
addHeader();