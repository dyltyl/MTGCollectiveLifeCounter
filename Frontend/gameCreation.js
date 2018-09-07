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

function createGame(){
    if(document.getElementById('gameSize').value < 9 && document.getElementById('gameSize').value > 1 && document.getElementById('baseLife').value > 0 && document.getElementById('gameName').value) {
        createLocalGameStorage();
        //Create Game
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
            if(res.status == 200) { //Game created successfully
                window.location.href= 'playerCreation.html';
            }
            else {
                console.log('sorry about that'); //TODO: More detailed error messages
            }
        })
        .catch(error=>console.log('pls')) //Not sure if this is ever called
    }
    else {
        console.log('missing some fields') //TODO: More detailed error messages
    }
}

function handleErrors(response) { //What this?
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}