function checkLocalLog(){
    console.log('Game_Size: ' + localStorage.getItem('gameSize') + ' || Base_Life: ' + localStorage.getItem('baseLife'));
    console.log('Game_Name: ' + localStorage.getItem('gameName') + ' || Game_Pass: ' + localStorage.getItem('gamePass'));
    console.log('Player_Name: ' + localStorage.getItem('playerName') + ' || Player_Email: ' + localStorage.getItem('playerEmail') + ' || Host_Toggle: ' + localStorage.getItem('hostToggle'));
    console.log('Commander: ' + localStorage.getItem('commanderName') + ' || Partner_Name: ' + localStorage.getItem('partnerName'));
}
const getAllUrl = 'https://magic-database.herokuapp.com/players';
function getAllPlayers(){ //Gets all players and returns a promise
    const requestBody={
        method: 'GET',
        headers:{
            "content-type": "application/json; charset=UTF-8",
            gameId: localStorage.getItem('gameName')
        }
    };
    return fetch(getAllUrl,requestBody)
    .then(response => {return response;});
}