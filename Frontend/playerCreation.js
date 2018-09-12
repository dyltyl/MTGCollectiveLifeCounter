const jGURL = 'https://magic-database.herokuapp.com/joinGame';
const pCURL = 'https://magic-database.herokuapp.com/player';

function togglePartner(){
    var root = document.getElementById('root'); //Root located within HTML body
    if(root.childElementCount <1){
        var input = document.createElement('input');
        input.setAttribute('id','partnerName');
        input.appendChild(document.createTextNode('Partner Name'));
        if (localStorage.getItem('partnerName') != null){
            input.value = localStorage.getItem('partnerName');
        }
        root.appendChild(input); 
    } else{
        root.removeChild(document.getElementById('partnerName'));
    }
}

/**
 * create the localStorage for a player
 */
function createLocalLog(){
    var root = document.getElementById('root'); //Root located within HTML body
    localStorage.setItem('playerName', document.getElementById('playerName').value);
    localStorage.setItem('commanderName', document.getElementById('commanderName').value);
    localStorage.setItem('playerEmail',  document.getElementById('playerEmail').value);
    localStorage.setItem('playerPass',  document.getElementById('playerPass').value);
    if(root.childElementCount >0){
        var root = document.getElementById('root'); //Root located within HTML body
        localStorage.setItem('partnerName', document.getElementById('partnerName').value);
    }
}


function createPlayer(){
    if(!document.getElementById('playerName').value) {
        alert('Player name must be set');
        return;
    }
    if(!document.getElementById('playerEmail').value) {
        alert('Player email must be set');
        return;
    }
    createLocalLog();
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
    .then(res=>{
        if(res.status == 200) {
            joinGame();
        }
        else {
            res.text().then(error => alert(error));
        }
    })
    .catch(error=>alert(error))
}

function joinGame(){
    console.log('join game called');
    let failed = false;
    if(root.childElementCount > 0 && document.getElementById('partnerName').value === document.getElementById('commanderName').value) {
        alert('Dylan stop making players with partner commanders with the same name');
        failed = true;
    }
    else {
        var cmdrArr;
        if(root.childElementCount <1){
            cmdrArr = [document.getElementById('commanderName').value];
        }
        else{
            cmdrArr = [document.getElementById('commanderName').value, document.getElementById('partnerName').value ]
        }
        const requestBody={
            method: 'POST',
            body: JSON.stringify(cmdrArr),
            headers:{
                "content-type": "application/json; charset=UTF-8",
                gameId: localStorage.getItem('gameName'),
                gamePassword: localStorage.getItem('gamePass'),
                email: localStorage.getItem('playerEmail'),
                password: localStorage.getItem('playerPass')
            }
        };

        console.log('joinGame Called');
        console.log(JSON.stringify(requestBody));
        fetch(jGURL, requestBody)
        .then(function(response){
            if(response.status === 200) {
                setHost(localStorage.getItem('playerEmail')).then(_ => {
                    window.location.href = 'waitingLobby.html';
                });
            }
            else {
                res.text().then(error => {
                    alert(error);
                    failed = true;
                });
            }
        })
        .catch(error=>{
            alert(error);
            failed = true;
        })
    }
    if(failed) {
        const requestBody={
            method: 'DELETE',
            headers:{
                "content-type": "application/json; charset=UTF-8",
                gameId: localStorage.getItem('gameName'),
                email: localStorage.getItem('playerEmail'),
            }
        };
        fetch(pCURL, requestBody)
        .then(res => {
            if(res.status === 200) {
                console.log('deleted player');
            }
            else {
                res.text().then(error => {
                    alert(error);
                });
            }
        })
    }
}

function loadLocal_Storage(){
        if(localStorage.getItem('playerEmail')!= null){
            document.getElementById('playerName').value = localStorage.getItem('playerName');
            document.getElementById('commanderName').value = localStorage.getItem('commanderName');
            document.getElementById('playerEmail').value = localStorage.getItem('playerEmail');
            document.getElementById('playerPass').value = localStorage.getItem('playerPass');
        }
}

