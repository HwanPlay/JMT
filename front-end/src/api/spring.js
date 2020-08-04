export default {
  URL: 'http://localhost:8000',
  ROUTES: {
    signup: '/rest-auth/signup/',
    login: '/rest-auth/login/',
    logout: '/rest-auth/logout/',

    isEmailOverlap: '/account/overlap/', // +뒤에 이메일, 결과값은 true or false
    emailVerify: '/account/verity/', // 뒤에 인증코드? 딸려보내기 // 결과값은 true or false
    getProfile: '/account/', // 뒤에 사용자 ID, // 결과값은 ID, 프로필사진, 이름

    createGroup: '/group/new/',  // 그룹 만들기 ==> 사람들 정보가 나와야 한다.
    getGroupList: '/group/groups/', // 리턴 값은 내가 속한 그룹들의 이름
    getGroupInfo: '/group/', // 뒤에 그룹 ID번호 붙이기  ==> 결과 값은 해당 그룹의 정보들(그룹이름, 호스트, 소속인원, 그룹소개)
  
    joinConference: '/conference/', // 뒤에 방번호(그룹번호 ID) 붙이기
    getConferenceList: '/conference/conferences',  // 결과값은 현재 내가 속한 그룹들 중 회의중인 그룹들의 그룹 이름과 호스트?
    
    getNoteList: '/note/notes/',  // 결과 값은 내가 만든(저장한) 노트들의 목록(이름 + 생성날짜?)
    getNoteInfo: '/note/', // 뒤에 노트ID, 결과 값은 해당 노트의 내용(마크다운형식?)
  }
};