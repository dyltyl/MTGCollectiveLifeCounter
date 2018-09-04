//TODO: Store Partner Commanders locally
const jGURL = 'https://magic-database.herokuapp.com/joinGame';
const pCURL = 'https://magic-database.herokuapp.com/player';

function createInputField(){
    var root = document.getElementById('root'); //Root located within HTML body
    if(root.childElementCount <1){
        var input = document.createElement('input');
        input.setAttribute('id','partnerName');
        input.appendChild(document.createTextNode('Partner Name'));
        root.appendChild(input); 
    } else{
        root.removeChild(document.getElementById('partnerName'));
    }
}

/**
 * create the localStorage for a player
 */
function createEntranceStorage(){
    localStorage.setItem('playerName', document.getElementById('playerName').value);
    localStorage.setItem('commanderName', document.getElementById('commanderName').value);
    localStorage.setItem('playerEmail',  document.getElementById('playerEmail').value);
    localStorage.setItem('playerPass',  document.getElementById('playerPass').value);
    console.log('Player ' + localStorage.getItem('playerName') + ' locally stored.')
    console.log('Commander ' + localStorage.getItem('commanderName') + ' locally stored.');
}
function checkLocalLog(){
    console.log('Player ' + localStorage.getItem('playerName') + ' locally stored.')
    console.log('Commander ' + localStorage.getItem('commanderName') + ' locally stored.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
    console.log('Game_Name of ' + localStorage.getItem('gameName') + ' stored locally.');
}


function createPlayer(){
    const playerObject ={
        name : document.getElementById('playerName').value,
        email : document.getElementById('playerEmail').value,
        password : document.getElementById('playerPass').value,
    }
    console.log(JSON.stringify(playerObject));
    const requestBody={
        method: 'POST',
        body: JSON.stringify(playerObject),
        headers:{
            "content-type": "application/json; charset=UTF-8"
        }
    };
    fetch(pCURL, requestBody)
    .then(function(response){
        console.log(response.text())
    })
    .then(res=>{console.log(res)})
    .catch(error=>console.log(error))
}

function joinGame(){
}