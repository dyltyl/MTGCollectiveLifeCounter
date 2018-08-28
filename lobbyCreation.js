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

function createEmptySlot(){
    var root = document.getElementById('root');
    var emptySlot = document.createElement('div');
    emptySlot.class = 'emptySlot';
    var txtNode = document.createTextNode('...Waiting for player...');
    emptySlot.appendChild(txtNode);

    // root.appendChild(emptySlot);
return emptySlot;
}
function createPlayerSlot(name, commanderOne, commanderTwo){
    var root = document.getElementById('root');
    var playerSlot = document.createElement('div');
    playerSlot.setAttribute('class','playerSlot');
    var playerName = document.createElement('p');
        playerName.innerHTML = name;

    var c1 = document.createElement('p');
        c1.innerHTML = commanderOne;

    if (commanderTwo != undefined){
        var c2 = document.createElement('p');
            c2.innerHTML = commanderTwo;
            playerSlot.appendChild(c2);
    }

    playerSlot.appendChild(playerName);
    playerSlot.appendChild(c1);
    // root.appendChild(playerSlot);
    return playerSlot;
}

function createPlayerSlots(){
    var root = document.getElementById('root');
    var availSlots = [gameSize];
    var test = createEmptySlot();

    root.appendChild(test);

    /*
    for(var i = 0; i<gameSize; i++){
        root.appendChild(root.appendChild(createEmptySlot()));
    }*/
}

createPlayerSlots();