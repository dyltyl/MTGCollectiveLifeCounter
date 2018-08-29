//Apply PArtner Commanders when applicable 

var lSPlayerName = localStorage.getItem('playerName');
var lSCommanderName = localStorage.getItem('commanderName');
var gameSize = localStorage.getItem('gameSize');
var baseLife = localStorage.getItem('baseLife');
var root = document.getElementById('root');

function checkLog(){
    console.log('Creator Name: ' + lSPlayerName);
    console.log('Creator Commander: ' + lSCommanderName);
    console.log('GameSize: ' + gameSize);
    console.log('BaseLife: ' + baseLife );
}
/**
 * Creates and emptySlot element within JS, intended to be replaced by a 
 * createPlayerSlot element
 */
function createEmptySlot(){ 
    var root = document.getElementById('root');
    var emptySlot = document.createElement('div');
    emptySlot.setAttribute('class','playerSlot');
    var txtNode = document.createTextNode('...Waiting for player...');
    emptySlot.appendChild(txtNode);

    //root.appendChild(emptySlot);
    return emptySlot;
}

function createPlayerSlot(name, commanderOne, commanderTwo){
    var root = document.getElementById('root');
    var playerSlot = document.createElement('div');
    playerSlot.setAttribute('class','playerSlot');
    var playerText = document.createTextNode(lSPlayerName + ' || ' + lSCommanderName);
        
/*
    if (commanderTwo != undefined){
        playerText += " || "  +
    }
*/

    playerSlot.appendChild(playerText);
    // playerSlot.appendChild(c1);
    // root.appendChild(playerSlot);
    return playerSlot;
}

function createPlayerSlots(){
    for(i=0; i<gameSize-1; i++){
        root.appendChild(createEmptySlot());
    }
}

root.appendChild(createPlayerSlot(lSPlayerName,lSCommanderName));
createPlayerSlots();