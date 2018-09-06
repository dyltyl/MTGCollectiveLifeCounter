const url = 'https://magic-database.herokuapp.com/game';



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

    console.log('dbCreateGame() called');
    fetch(url, otherParam)
    .then(function(response){
        console.log(response);
        console.log(response.text());
    })
    .then(res=>{console.log(res)})
    .catch(error=>console.log(error))
}

function insertLink(){
    if (document.getElementById('dbInsert').childElementCount == 0){
        var entrance = document.createElement('button');
        var entranceLink = document.createElement('a');
        entranceLink.setAttribute('href','playerCreation.html');
        entranceLink.appendChild(document.createTextNode('Create User'));
        entrance.appendChild(entranceLink);
        dbInsert.appendChild(entrance);
        document.getElementById('cG').style.display = 'none';
    }
}

function createGame(){
    createLocalGameStorage();
    dbCreateGame();
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