const url = 'https://magic-database.herokuapp.com/game';
var created = false;



function createLocalGameStorage(){
    localStorage.setItem('gameSize', document.getElementById('gameSize').value);
    localStorage.setItem('baseLife', document.getElementById('baseLife').value);
    localStorage.setItem('gameName', document.getElementById('gameName').value);
    localStorage.setItem('gamePass', document.getElementById('gamePass').value);
    console.log('Game_Size of ' + localStorage.getItem('gameSize') + ' locally.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
}
function checkLocalLog(){
    console.log('Game_Size of ' + localStorage.getItem('gameSize') + ' stored locally.');
    console.log('Base_Life_Total of ' + localStorage.getItem('baseLife') + ' stored locally');
    console.log('Game_Name of ' + localStorage.getItem('gameName') + ' stored locally.');
    console.log('Game_Pass of ' + localStorage.getItem('gamePass') + ' stored locally');
}

function dbCreateGame(){
    const Data={
        gameId : document.getElementById('gameName').value,
        gamePassword : document.getElementById('gamePass').value,
        startingLife : document.getElementById('baseLife').value
    }
    console.log(JSON.stringify(Data));
    const otherParam={
        method: "POST",
        body: JSON.stringify(Data),
        headers:{
            "content-type": "application/json; charset=UTF-8"
        }
    };

    fetch(url, otherParam)
    .then(res=>{
        console.log(res); 
        if(res.status == 200) { 
            window.location.href= 'playerCreation.html';
        }
        else {
            console.log('sorry about that'); //TODO: More detailed error messages
        }
    })
    .catch(error=>console.log('pls')) //Not sure if this is ever called
}

function insertLink(){
    if (document.getElementById('dbInsert').childElementCount == 0){
        var entrance = document.createElement('div');
        entrance.setAttribute('class','divButton');
        var entranceLink = document.createElement('a');
        entranceLink.setAttribute('href','playerCreation.html');
        entranceLink.text = 'Create User';
        // entranceLink.appendChild(document.createTextNode('Create User'));
        entrance.appendChild(entranceLink);
        dbInsert.appendChild(entrance);
        document.getElementById('cG').style.display = 'none';
    }
}

function createGame(){
    if(document.getElementById('gameSize').value < 9 && document.getElementById('gameSize').value > 1 && document.getElementById('baseLife').value > 0 && document.getElementById('gameName').value) {
        createLocalGameStorage();
        dbCreateGame();
    }
    else {
        console.log('missing some fields') //TODO: More detailed error messages
    }
}

function getInputContent(){
    console.log(document.getElementById('gameName').value)
    console.log(document.getElementById('gamePass').value)
    console.log(document.getElementById('baseLife').value)
}

function handleErrors(response) {
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}