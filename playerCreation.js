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

function createEntranceStorage(){
    localStorage.setItem('playerName', document.getElementById('playerName').value);
    localStorage.setItem('commanderName', document.getElementById('commanderName').value);
    console.log('Player ' + localStorage.getItem('playerName') + ' locally stored.')
    console.log('Commander ' + localStorage.getItem('commanderName') + ' locally stored.');
}
function checkLocalLog(){
    console.log('Player ' + localStorage.getItem('playerName') + ' locally stored.')
    console.log('Commander ' + localStorage.getItem('commanderName') + ' locally stored.');
}