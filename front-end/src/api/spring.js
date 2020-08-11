export default {
  // URL: 'http://joinmeeting.tk/videoconference/api',
  URL: 'http://localhost:8080/videoconference/api',
  ROUTES: {
    
    getProfile: '/account/', // 뒤에 사용자 ID, // 결과값은 ID, 프로필사진, 이름
    
    
    joinConference: '/conference/', // 뒤에 방번호(그룹번호 ID) 붙이기
    getConferenceList: '/conference/conferences',  // 결과값은 현재 내가 속한 그룹들 중 회의중인 그룹들의 그룹 이름과 호스트?
    
    getNoteList: '/note/notes/',  // 결과 값은 내가 만든(저장한) 노트들의 목록(이름 + 생성날짜?)
    getNoteInfo: '/note/', // 뒤에 노트ID, 결과 값은 해당 노트의 내용(마크다운형식?)
    
    // 이 아래로는 진짜로 만든거
    
    getGroupList: '/group/get/me/', // 리턴 값은 내가 속한 그룹들의 정보
    createGroup: '/group/add/',  // 그룹 만들기 ==> 사람들 정보가 나와야 한다.
    
    reToken: '/jwt/refresh',

    signup: '/register',
    checkEmail: '/register/duplicateId',
    login: '/login',
    logout: '/logout',
    getGroupInfo: '/group/get/all', // 뒤에 ID  ==> 결과 값은 해당 ID가 속한 그룹들의 정보(그룹 번호, 그룹이름, 호스트, 그룹소개)
    invite: '/groupmember/add',
    myProfile: '/user/findUserById',
  }
};