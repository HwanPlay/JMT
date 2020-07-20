# WebRTC Text Chat

document.getElementById('open-room').onclick = function() {
    this.disabled = true;
    connection.open(document.getElementById('room-id').value);
};
document.getElementById('join-room').onclick = function() {
    this.disabled = true;
    connection.join(document.getElementById('room-id').value);
};

connection.open ('room-id')
	=> connection.open을 통해 room-id값에 해당하는 채팅방에 오픈


connection.open ('room-id')
	=> connection.join을 통해 room-id값에 해당하는 채팅방에 입장


document.getElementById('input-text-chat').onkeyup = function(e) {
    if(e.keyCode != 13) return;
    
    // removing trailing/leading whitespace
    this.value = this.value.replace(/^\s+|\s+$/g, '');
    // .replace(/^\s+|\s+$/g,'') : 앞뒤 공백 제거
 
 
    
    connection.send(this.value);
    appendDIV(this.value);
    this.value =  '';
};

onkeyup 
=>input-text-chat에 글자를 쓰고 엔터를 누르면 함수 실행
this.value.replace(/^\s+|\s+$/g,‘’);
=>replace 정규식을 통해 앞뒤 공백 제거
connection.send(this.value)
=>connection.send를 통해 공백이 제거된 글자를 전송

appendDIV(this.value)
=>appendDIV 함수를 실행

function appendDIV(event) {
    var div = document.createElement('div');
    div.innerHTML = event.data || event;
    chatContainer.insertBefore(div, chatContainer.firstChild);
    div.tabIndex = 0; div.focus();
    
    document.getElementById('input-text-chat').focus();
}

funtion appendDIV(event)
var div = document.createElement(‘div’);
	div element 생성

div.focus();
해당 요소에 포커스를 부여하여
텍스트 창의 경우, 커서를 위치시켜 바로 입력이 가능합니다.
버튼의 경우, 엔터 키를 눌렀을때 클릭 효과를 냅니다.
